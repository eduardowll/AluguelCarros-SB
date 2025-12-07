package edu.ufrn.car_service.repositories;

import edu.ufrn.car_service.models.Car;
import edu.ufrn.car_service.models.StatusCarro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByStatus(StatusCarro status);
}
