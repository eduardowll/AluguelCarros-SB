package edu.ufrn.rental_service.controllers;

import edu.ufrn.rental_service.models.Rental;
import edu.ufrn.rental_service.services.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    public ResponseEntity<Rental> criar(@RequestBody Rental rental) {
        return ResponseEntity.ok(rentalService.criarAluguel(rental));
    }

    @GetMapping
    public ResponseEntity<List<Rental>> listarTodos() {
        return ResponseEntity.ok(rentalService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rental> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.buscarPorId(id));
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<Rental> finalizar(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.finalizarAluguel(id));
    }
}
