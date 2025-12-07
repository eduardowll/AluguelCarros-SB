package edu.ufrn.car_service.controllers;

import edu.ufrn.car_service.models.Car;
import edu.ufrn.car_service.models.StatusCarro;
import edu.ufrn.car_service.services.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping
    public ResponseEntity<Car> criar(@RequestBody Car car) {
        return ResponseEntity.ok(carService.salvar(car));
    }

    @GetMapping
    public ResponseEntity<List<Car>> listarTodos() {
        return ResponseEntity.ok(carService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(carService.buscarPorId(id));
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<Car>> listarDisponiveis() {
        return ResponseEntity.ok(carService.listarDisponiveis());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Car> atualizarStatus(@PathVariable Long id,
                                               @RequestParam StatusCarro status) {
        return ResponseEntity.ok(carService.atualizarStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        carService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Car>> criarVarios(@RequestBody List<Car> carros) {
        return ResponseEntity.ok(carService.salvarVarios(carros));
    }
}
