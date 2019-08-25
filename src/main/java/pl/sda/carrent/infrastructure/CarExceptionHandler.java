package pl.sda.carrent.infrastructure;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.sda.carrent.infrastructure.exception.CarAlreadyExistsException;
import pl.sda.carrent.infrastructure.exception.CarAlreadyReservedException;
import pl.sda.carrent.infrastructure.exception.CarNotExistException;

@ControllerAdvice
public class CarExceptionHandler {

    @ExceptionHandler({CarNotExistException.class})
    ResponseEntity<String> carNotExist(CarNotExistException ex) {
        return ResponseEntity.status(404).body("Car not exist");
    }

    @ExceptionHandler({CarAlreadyExistsException.class})
    ResponseEntity<String> carAlreadyExist(CarAlreadyExistsException ex) {
        return ResponseEntity.status(409).body("Car already exist");
    }

    @ExceptionHandler({CarAlreadyReservedException.class})
    ResponseEntity<String> carAlreadyReserved(CarAlreadyReservedException ex) {
        return ResponseEntity.status(409).body("Car already reserved");
    }
}
