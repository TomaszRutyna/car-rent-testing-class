package pl.sda.carrent.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sda.carrent.domain.model.Car;
import pl.sda.carrent.domain.model.CarReservation;
import pl.sda.carrent.domain.repository.CarRepository;
import pl.sda.carrent.infrastructure.dto.CarDto;
import pl.sda.carrent.infrastructure.exception.CarAlreadyExistsException;
import pl.sda.carrent.infrastructure.exception.CarNotExistException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    public void create(CarDto carDto) {
        Optional<Car> carWithSameVin = carRepository.findByVin(carDto.getVin());

        if (carWithSameVin.isPresent()) {
            throw new CarAlreadyExistsException();
        }

        carRepository.save(Car.builder()
                .brand(carDto.getBrand())
                .model(carDto.getModel())
                .vin(carDto.getVin())
                .build());
    }

    @Transactional
    public void update(CarDto carDto) {
        Optional<Car> carWithId = carRepository.findById(carDto.getId());
        if (!carWithId.isPresent()) {
            throw new CarNotExistException();
        }

        carWithId.get().update(carDto);
    }

    public void delete(Long id) {
        carRepository.deleteById(id);
    }
}
