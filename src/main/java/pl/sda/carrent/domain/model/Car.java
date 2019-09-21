package pl.sda.carrent.domain.model;

import lombok.*;
import pl.sda.carrent.infrastructure.dto.CarDto;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "cars")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String brand;

    private String model;

    private String vin;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id")
    private Set<CarReservation> reservations;

    public void update(CarDto dto) {
        this.brand = dto.getBrand();
        this.model = dto.getModel();
        this.vin = dto.getVin();
    }

}
