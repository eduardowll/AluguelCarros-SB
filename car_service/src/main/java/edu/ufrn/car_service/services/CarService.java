package edu.ufrn.car_service.services;

import edu.ufrn.car_service.models.Car;
import edu.ufrn.car_service.models.StatusCarro;
import edu.ufrn.car_service.repositories.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car salvar(Car car) {
        if (car.getStatus() == null) {
            car.setStatus(StatusCarro.DISPONIVEL);
        }
        return carRepository.save(car);
    }

    public List<Car> listarTodos() {
        return carRepository.findAll();
    }

    public Car buscarPorId(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carro não encontrado"));
    }

    public List<Car> listarDisponiveis() {
        return carRepository.findByStatus(StatusCarro.DISPONIVEL);
    }

    public Car atualizarStatus(Long id, StatusCarro novoStatus) {
        Car car = buscarPorId(id);
        car.setStatus(novoStatus);
        return carRepository.save(car);
    }

    public void deletar(Long id) {
        carRepository.deleteById(id);
    }

    public List<Car> salvarVarios(List<Car> carros) {
        carros.forEach(car -> {
            if (car.getStatus() == null) {
                car.setStatus(StatusCarro.DISPONIVEL);
            }
        });
        return carRepository.saveAll(carros);
    }
}
