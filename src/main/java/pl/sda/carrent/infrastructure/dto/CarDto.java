package pl.sda.carrent.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CarDto {

    private Long id;
    private String brand;
    private String model;
    private String vin;
}
