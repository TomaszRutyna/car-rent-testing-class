package pl.sda.carrent.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
public class CarReservationDto {
    private Long carId;
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservedFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservedTo;
    private String reservedBy;
}
