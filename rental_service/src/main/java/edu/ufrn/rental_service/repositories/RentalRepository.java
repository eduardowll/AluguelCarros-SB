package edu.ufrn.rental_service.repositories;

import edu.ufrn.rental_service.models.Rental;
import edu.ufrn.rental_service.models.StatusAluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByStatus(StatusAluguel status);
    List<Rental> findByCarId(Long carId);
}
