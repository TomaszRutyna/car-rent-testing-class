package pl.sda.carrent.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sda.carrent.domain.model.Car;
import pl.sda.carrent.domain.model.CarReservation;
import pl.sda.carrent.domain.repository.CarRepository;
import pl.sda.carrent.infrastructure.dto.CarReservationDto;
import pl.sda.carrent.infrastructure.exception.CarNotExistException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CarReservationFinder {
    private final CarRepository carRepository;

    @Transactional(readOnly = true)
    public List<CarReservationDto> findForCar(final Long carId) {
        Optional<Car> car = carRepository.findById(carId);
        if (!car.isPresent()) {
            throw new CarNotExistException();
        }

        return car.map(Car::getReservations)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(res -> toDto(res, carId))
                .collect(Collectors.toList());
    }

    private CarReservationDto toDto(CarReservation reservation, Long carId) {
        return CarReservationDto.builder()
                .carId(carId)
                .id(reservation.getId())
                .reservedBy(reservation.getReservedBy())
                .reservedFrom(reservation.getReservedFrom())
                .reservedTo(reservation.getReservedTo())
                .build();
    }
}
