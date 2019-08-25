package pl.sda.carrent.integration;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import pl.sda.carrent.domain.model.Car;
import pl.sda.carrent.domain.model.CarReservation;
import pl.sda.carrent.domain.repository.CarRepository;
import pl.sda.carrent.infrastructure.dto.CarReservationDto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarReservationEndpointIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CarRepository carRepository;

    @Test
    public void shouldCreateReservation() {
        //given
        Car car = Car.builder().model("VW").brand("Golf").vin("12314").build();
        Car savedCar = carRepository.save(car);
        CarReservationDto rq = CarReservationDto.builder()
                .reservedTo(LocalDate.parse("2019-09-15"))
                .reservedFrom(LocalDate.parse("2019-09-10"))
                .reservedBy("Janusz")
                .carId(savedCar.getId())
                .build();
        HttpEntity<CarReservationDto> httpEntity = new HttpEntity<>(rq);
        //when
        ResponseEntity<Void> response =
                restTemplate.exchange("/reservation",
                        HttpMethod.POST, httpEntity, Void.class);
        //then
        Assert.assertEquals(201, response.getStatusCodeValue());

        Car reservedCar = carRepository.findById(savedCar.getId()).get();
        Assert.assertEquals(1, reservedCar.getReservations().size());
        CarReservation reservation = reservedCar.getReservations().iterator().next();
        Assert.assertEquals("Janusz", reservation.getReservedBy());
        Assert.assertEquals(LocalDate.parse("2019-09-10"), reservation.getReservedFrom());
        Assert.assertEquals(LocalDate.parse("2019-09-15"), reservation.getReservedTo());
    }

    @Test
    public void shouldNotBeAbleToReserveCarWhenIsAlreadyReserved() {
        //given
        CarReservation carReservation = CarReservation.builder()
                .reservedBy("Janusz")
                .reservedFrom(LocalDate.parse("2019-09-10"))
                .reservedTo(LocalDate.parse("2019-09-15"))
                .build();
        Set<CarReservation> reservations = new HashSet<>();
        reservations.add(carReservation);
        Car car = Car.builder()
                .brand("Opel")
                .model("Astra")
                .vin("1231231")
                .reservations(reservations)
                .build();
        Car savedCar = carRepository.save(car);

        CarReservationDto rq = CarReservationDto.builder()
                .reservedFrom(LocalDate.parse("2019-09-12"))
                .reservedTo(LocalDate.parse("2019-09-18"))
                .reservedBy("Marek")
                .carId(savedCar.getId())
                .build();
        HttpEntity<CarReservationDto> httpEntity = new HttpEntity<>(rq);
        //when
        ResponseEntity<Void> rsp = restTemplate.exchange("/reservation", HttpMethod.POST,
                httpEntity, Void.class);
        //then
        Assert.assertEquals(409, rsp.getStatusCodeValue());

        Car editedCar = carRepository.findById(savedCar.getId()).get();
        Assert.assertEquals(1, editedCar.getReservations().size());
    }

    @Test
    public void shouldNotReserveCarWhenCarNotExists() {
        //given
        CarReservationDto rq = CarReservationDto.builder()
                .reservedFrom(LocalDate.parse("2019-09-12"))
                .reservedTo(LocalDate.parse("2019-09-18"))
                .reservedBy("Marek")
                .carId(100L)
                .build();
        HttpEntity<CarReservationDto> httpEntity = new HttpEntity<>(rq);
        //when
        ResponseEntity<Void> rsp = restTemplate.exchange("/reservation", HttpMethod.POST,
                httpEntity, Void.class);
        //then
        Assert.assertEquals(404, rsp.getStatusCodeValue());
    }

}
