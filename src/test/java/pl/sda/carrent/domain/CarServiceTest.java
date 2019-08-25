package pl.sda.carrent.domain;

import org.junit.Test;
import org.mockito.Mockito;
import pl.sda.carrent.domain.model.Car;
import pl.sda.carrent.domain.repository.CarRepository;
import pl.sda.carrent.infrastructure.dto.CarDto;
import pl.sda.carrent.infrastructure.exception.CarAlreadyExistsException;

import java.util.Optional;

public class CarServiceTest {

    private CarRepository carRepository = Mockito.mock(CarRepository.class);

    private CarService carService = new CarService(carRepository);

    @Test
    public void shouldSaveNewCar() {
        //given
        CarDto carDto = CarDto.builder().brand("Volvo").model("S60")
                .vin("1234567").build();
        Mockito.when(carRepository.findByVin("1234567")).thenReturn(Optional.empty());

        Car expectedCar = Car.builder().brand("Volvo").model("S60")
                .vin("1234567").build();
        //when
        carService.create(carDto);
        //then
        Mockito.verify(carRepository, Mockito.times(1)).save(expectedCar);
    }

    @Test(expected = CarAlreadyExistsException.class)
    public void shouldNotSaveBecauseVinAlreadyExist() {
        //given
        CarDto carDto = CarDto.builder().brand("Volvo").model("S60")
                        .vin("1234567").build();
        Mockito.when(carRepository.findByVin("1234567")).thenReturn(
                Optional.of(Car.builder().brand("Opel").model("Astra")
                        .vin("1234567").build()));
        //when
        carService.create(carDto);
        //then
        Mockito.verify(carRepository, Mockito.times(0)).save(Mockito.any(Car.class));
    }
}
