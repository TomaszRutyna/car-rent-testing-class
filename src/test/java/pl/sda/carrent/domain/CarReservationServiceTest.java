package pl.sda.carrent.domain;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import pl.sda.carrent.domain.model.Car;
import pl.sda.carrent.domain.model.CarReservation;
import pl.sda.carrent.domain.repository.CarRepository;
import pl.sda.carrent.infrastructure.dto.CarReservationDto;
import pl.sda.carrent.infrastructure.exception.CarAlreadyReservedException;
import pl.sda.carrent.infrastructure.exception.CarNotExistException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static org.junit.Assert.assertEquals;

public class CarReservationServiceTest {

    private CarRepository carRepository = Mockito.mock(CarRepository.class);
    private CarReservationService carReservationService =
            new CarReservationService(carRepository);
    private ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);

    @Test
    public void shouldCreateReservation() {
        //given
        CarReservationDto reservation =
                new CarReservationDto(100L, null,
                        LocalDate.parse("2019-08-10"),
                        LocalDate.parse("2019-08-15"), "A.Malysz");
        CarReservation exisitingReservation = CarReservation.builder()
                .reservedBy("R.Lewandowski")
                .reservedFrom(LocalDate.parse("2019-08-05"))
                .reservedTo(LocalDate.parse("2019-08-07"))
                .id(121L)
                .build();
        Set<CarReservation> exisitngReservations = new HashSet<>();
        exisitngReservations.add(exisitingReservation);
        Car car = Car.builder()
                .id(100L)
                .brand("Audi")
                .model("a5")
                .vin("123124")
                .reservations(exisitngReservations)
                .build();
        Mockito.when(carRepository.findById(100L)).thenReturn(Optional.of(car));
        //when
        carReservationService.create(reservation);
        //then
        Mockito.verify(carRepository, Mockito.times(1)).save(carCaptor.capture());
        Car capturedCar = carCaptor.getValue();

        Set<CarReservation> reservations = capturedCar.getReservations();
        assertEquals(reservations.size(), 2);

        CarReservation createdReservation = reservations.stream()
                .filter(res -> res.getReservedBy().equals("A.Malysz"))
                .findFirst().orElse(null);
        assertEquals(LocalDate.parse("2019-08-10"), createdReservation.getReservedFrom());
        assertEquals(LocalDate.parse("2019-08-15"), createdReservation.getReservedTo());
    }


    @Test(expected = CarNotExistException.class)
    public void shouldNotCreateReservationBecauseCarNotExist() {
        //given:
        CarReservationDto reservation =
                new CarReservationDto(100L, null, LocalDate.now(), LocalDate.now(), "A.Malysz");
        Mockito.when(carRepository.findById(100L)).thenReturn(Optional.empty());
        //when:
        carReservationService.create(reservation);
        //then:
        Mockito.verify(carRepository, Mockito.never()).save(Mockito.any(Car.class));
    }

    @Test(expected = CarAlreadyReservedException.class)
    public void shouldNotCreateReservationBecauseCarIsAlreadyReserved() {
        //given:
        CarReservationDto reservation =
                new CarReservationDto(100L, null,
                        LocalDate.parse("2019-08-10"),
                        LocalDate.parse("2019-08-15"), "A.Malysz");
        CarReservation exisitingReservation = CarReservation.builder()
                .reservedBy("R.Lewandowski")
                .reservedFrom(LocalDate.parse("2019-08-05"))
                .reservedTo(LocalDate.parse("2019-08-12"))
                .id(121L)
                .build();
        Set<CarReservation> exisitngReservations = new HashSet<>();
        exisitngReservations.add(exisitingReservation);
        Car car = Car.builder()
                .id(100L)
                .brand("Audi")
                .model("a5")
                .vin("123124")
                .reservations(exisitngReservations)
                .build();
        Mockito.when(carRepository.findById(100L)).thenReturn(Optional.of(car));
        //when:
        carReservationService.create(reservation);
        //then:
        Mockito.verify(carRepository, Mockito.never()).save(Mockito.any(Car.class));
    }
}
