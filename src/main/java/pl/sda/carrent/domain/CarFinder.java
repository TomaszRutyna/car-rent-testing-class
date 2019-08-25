package pl.sda.carrent.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sda.carrent.domain.model.Car;
import pl.sda.carrent.domain.repository.CarRepository;
import pl.sda.carrent.infrastructure.dto.CarDto;
import pl.sda.carrent.infrastructure.exception.CarNotExistException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarFinder {

    private final CarRepository carRepository;

    public List<CarDto> findAll() {
        return carRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public CarDto findOne(Long id) {
        return carRepository.findById(id).map(this::toDto).orElseThrow(CarNotExistException::new);
    }

    private CarDto toDto(Car car) {
        return CarDto.builder()
                .id(car.getId())
                .model(car.getModel())
                .brand(car.getBrand())
                .vin(car.getVin())
                .build();
    }
}

