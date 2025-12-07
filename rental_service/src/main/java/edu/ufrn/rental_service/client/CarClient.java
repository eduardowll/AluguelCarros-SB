package edu.ufrn.rental_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CarClient {

    private final RestTemplate restTemplate;
    private final String CAR_SERVICE_URL = "http://localhost:8081/cars";

    public CarClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CarResponse buscarCarro(Long carId) {
        return restTemplate.getForObject(CAR_SERVICE_URL + "/" + carId, CarResponse.class);
    }

    public void atualizarStatus(Long carId, String status) {
        restTemplate.patchForObject(
                CAR_SERVICE_URL + "/" + carId + "/status?status=" + status,
                null,
                CarResponse.class
        );
    }
}
