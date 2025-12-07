package edu.ufrn.rental_service.services;

import edu.ufrn.rental_service.client.CarClient;
import edu.ufrn.rental_service.client.CarResponse;
import edu.ufrn.rental_service.models.Rental;
import edu.ufrn.rental_service.models.StatusAluguel;
import edu.ufrn.rental_service.repositories.RentalRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CarClient carClient;

    public RentalService(RentalRepository rentalRepository, CarClient carClient) {
        this.rentalRepository = rentalRepository;
        this.carClient = carClient;
    }

    public Rental criarAluguel(Rental rental) {
        // 1. Busca o carro no car-service
        CarResponse car = carClient.buscarCarro(rental.getCarId());

        // 2. Verifica se está disponível
        if (!"DISPONIVEL".equals(car.getStatus())) {
            throw new RuntimeException("Carro não está disponível para aluguel");
        }

        // 3. Calcula o valor total
        long dias = ChronoUnit.DAYS.between(rental.getStartDate(), rental.getExpectedEndDate());
        BigDecimal total = rental.getDailyRate().multiply(BigDecimal.valueOf(dias));
        rental.setTotalPrice(total);
        rental.setStatus(StatusAluguel.ATIVO);

        // 4. Salva o aluguel
        Rental saved = rentalRepository.save(rental);

        // 5. Atualiza o status do carro para ALUGADO
        carClient.atualizarStatus(rental.getCarId(), "ALUGADO");

        return saved;
    }

    public Rental finalizarAluguel(Long id) {
        Rental rental = buscarPorId(id);
        rental.setStatus(StatusAluguel.FINALIZADO);
        rental.setActualEndDate(java.time.LocalDate.now());

        // Libera o carro
        carClient.atualizarStatus(rental.getCarId(), "DISPONIVEL");

        return rentalRepository.save(rental);
    }

    public Rental buscarPorId(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado"));
    }

    public List<Rental> listarTodos() {
        return rentalRepository.findAll();
    }
}