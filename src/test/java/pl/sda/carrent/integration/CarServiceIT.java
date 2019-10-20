package pl.sda.carrent.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sda.carrent.domain.CarService;
import pl.sda.carrent.domain.model.Car;
import pl.sda.carrent.domain.repository.CarRepository;
import pl.sda.carrent.infrastructure.dto.CarDto;
import pl.sda.carrent.infrastructure.exception.CarAlreadyExistsException;
import pl.sda.carrent.infrastructure.exception.CarAlreadyReservedException;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarServiceIT {

    @Autowired
    private CarService carService;

    @Autowired
    private CarRepository carRepository;

    @Before
    public void deleteAllCars() {
        carRepository.deleteAll();
    }

    @Test
    public void shouldCreateCar() {
        //given
        CarDto dto = CarDto.builder()
                .vin("1234")
                .model("Astra")
                .brand("Opel")
                .build();
        //when
        carService.create(dto);
        //then
        List<Car> all = carRepository.findAll();
        assertEquals(all.size(), 1);
        Car car = all.get(0);
        assertEquals("Opel", car.getBrand());
        assertEquals("Astra", car.getModel());
        assertEquals("1234", car.getVin() );
    }

    @Test(expected = CarAlreadyExistsException.class)
    public void shouldNotCreateCarWhenVinAlreadyExists() {
        //given
        Car existingCar = Car.builder()
                .vin("1234")
                .model("Opel")
                .brand("Astra")
                .build();
        carRepository.save(existingCar);

        CarDto dto = CarDto.builder()
                .vin("1234")
                .model("Volkswagen")
                .brand("Golf")
                .build();
        //when
        carService.create(dto);
        //then
        List<Car> all = carRepository.findAll();
        assertEquals(1, all.size());

        assertEquals("Opel", all.get(0).getBrand());
    }

}
