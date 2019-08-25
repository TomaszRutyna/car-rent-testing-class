package pl.sda.carrent.domain;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import pl.sda.carrent.domain.model.Car;
import pl.sda.carrent.domain.repository.CarRepository;
import pl.sda.carrent.infrastructure.dto.CarDto;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CarFinderTest {

    private CarRepository carRepository = Mockito.mock(CarRepository.class);

    private CarFinder carFinder = new CarFinder(carRepository);

    @Test
    public void shouldReturnAllCars() {
        //given
        List<Car> carEntities = Arrays.asList(
            Car.builder().id(1L).brand("VW").model("Golf").vin("123321").build(),
            Car.builder().id(2L).brand("Opel").model("Astra").vin("312415").build(),
            Car.builder().id(3L).brand("Audi").model("A3").vin("1231241").build()
        );
        Mockito.when(carRepository.findAll()).thenReturn(carEntities);
        //when
        List<CarDto> cars = carFinder.findAll();
        //then
        assertEquals(3, cars.size());
        CarDto secondCar = cars.get(1);
        assertEquals("Opel", secondCar.getBrand());
        assertEquals("Astra", secondCar.getModel());
        assertEquals("312415", secondCar.getVin());
    }

}
