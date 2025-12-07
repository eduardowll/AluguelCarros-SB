package edu.ufrn.rental_service.client;

import lombok.Data;

@Data
public class CarResponse {
    private Long id;
    private String marca;
    private String modelo;
    private Integer ano;
    private String placa;
    private Double quilometragem;
    private String status;
}