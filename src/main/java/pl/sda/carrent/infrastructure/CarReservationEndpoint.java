package pl.sda.carrent.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrent.domain.CarReservationFinder;
import pl.sda.carrent.domain.CarReservationService;
import pl.sda.carrent.infrastructure.dto.CarReservationDto;

import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class CarReservationEndpoint {

    private final CarReservationFinder finder;
    private final CarReservationService service;

    @GetMapping("/{carId}")
    List<CarReservationDto> getForCar(@PathVariable Long carId) {
        return finder.findForCar(carId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createReservation(@RequestBody CarReservationDto carReservationDto) {
        service.create(carReservationDto);
    }

    @DeleteMapping("/{carId}/{resId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long resId, @PathVariable Long carId) {
        service.delete(carId, resId);
    }
}
