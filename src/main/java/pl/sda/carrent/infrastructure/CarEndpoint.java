package pl.sda.carrent.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrent.domain.CarFinder;
import pl.sda.carrent.domain.CarService;
import pl.sda.carrent.infrastructure.dto.CarDto;

import java.util.List;

@RestController
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarEndpoint {

    private final CarFinder finder;
    private final CarService service;

    @GetMapping
    List<CarDto> getAll() {
        return finder.findAll();
    }

    @GetMapping("/{id}")
    CarDto getOne(@PathVariable Long id) {
        return finder.findOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void create(@RequestBody CarDto car) {
        service.create(car);
    }

    @PutMapping
    void update(@RequestBody CarDto car) {
        service.update(car);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
