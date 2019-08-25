package pl.sda.carrent.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CarReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String reservedBy;

    private LocalDate reservedFrom;

    private LocalDate reservedTo;


}
