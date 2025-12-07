package edu.ufrn.rental_service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "rentals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long carId;
    private String customerName;
    private String customerCpf;

    private LocalDate startDate;
    private LocalDate expectedEndDate;
    private LocalDate actualEndDate;

    private BigDecimal dailyRate;
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private StatusAluguel status;
}
