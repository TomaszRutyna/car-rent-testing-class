package pl.sda.carrent.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sda.carrent.domain.model.Car;
import pl.sda.carrent.domain.model.CarReservation;
import pl.sda.carrent.domain.repository.CarRepository;
import pl.sda.carrent.infrastructure.dto.CarReservationDto;
import pl.sda.carrent.infrastructure.exception.CarAlreadyReservedException;
import pl.sda.carrent.infrastructure.exception.CarNotExistException;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarReservationService {

    private final CarRepository carRepository;

    @Transactional
    public void create(CarReservationDto carReservationDto) {
        Optional<Car> car = carRepository.findById(carReservationDto.getCarId());
        if (!car.isPresent()) {
            throw new CarNotExistException();
        }
        Car carInDb = car.get();

        checkIfReserved(carInDb, carReservationDto.getReservedFrom(), carReservationDto.getReservedTo());

        carInDb.getReservations().add(CarReservation.builder()
                .reservedBy(carReservationDto.getReservedBy())
                .reservedFrom(carReservationDto.getReservedFrom())
                .reservedTo(carReservationDto.getReservedTo())
                .build());

        carRepository.save(carInDb);
    }

    @Transactional
    public void delete(Long carId, Long reservationId) {
        Optional<Car> car = carRepository.findById(carId);
        if (!car.isPresent()) {
            throw new CarNotExistException();
        }
        car.get().getReservations().removeIf(res -> Objects.equals(res.getId(), reservationId));
    }

    private void checkIfReserved(Car car, LocalDate from, LocalDate to) {
        boolean hasConflict = car.getReservations().stream()
                .anyMatch(res -> isReservationInRange(res, from, to));

        if (hasConflict) {
            throw new CarAlreadyReservedException();
        }
    }

    private boolean isReservationInRange(CarReservation carReservation, LocalDate from, LocalDate to) {
        return isDateInRange(carReservation.getReservedFrom(), from, to) ||
                isDateInRange(carReservation.getReservedTo(), from, to);
    }

    private boolean isDateInRange(LocalDate date, LocalDate from, LocalDate to) {
        return date.isAfter(from) && date.isBefore(to);
    }
}
