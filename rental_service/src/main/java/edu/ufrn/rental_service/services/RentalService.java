package edu.ufrn.rental_service.services;

import edu.ufrn.rental_service.client.CarClient;
import edu.ufrn.rental_service.client.CarResponse;
import edu.ufrn.rental_service.models.Rental;
import edu.ufrn.rental_service.models.StatusAluguel;
import edu.ufrn.rental_service.repositories.RentalRepository;
import edu.ufrn.rental_service.strategies.CalculoPrecoStrategy;
import edu.ufrn.rental_service.strategies.PrecoEsportivoStrategy;
import edu.ufrn.rental_service.strategies.PrecoSuperEsportivoStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CarClient carClient;

    private final PrecoEsportivoStrategy precoEsportivo;
    private final PrecoSuperEsportivoStrategy precoSuperEsportivo;

    public RentalService(RentalRepository rentalRepository,
                         CarClient carClient,
                         PrecoEsportivoStrategy precoEsportivo,
                         PrecoSuperEsportivoStrategy precoSuperEsportivo) {
        this.rentalRepository = rentalRepository;
        this.carClient = carClient;
        this.precoEsportivo = precoEsportivo;
        this.precoSuperEsportivo = precoSuperEsportivo;
    }

    public Rental criarAluguel(Rental rental) {
        CarResponse car = carClient.buscarCarro(rental.getCarId());

        if (!"DISPONIVEL".equals(car.getStatus())) {
            throw new RuntimeException("Este carro de luxo não está disponível no momento.");
        }

        long dias = ChronoUnit.DAYS.between(rental.getStartDate(), rental.getExpectedEndDate());
        if (dias <= 0) dias = 1;

        CalculoPrecoStrategy strategy;
        BigDecimal dailyRate;

        if (ehSuperEsportivo(car.getMarca())) {
            strategy = precoSuperEsportivo;
            dailyRate = new BigDecimal("10000.00");
        } else {
            strategy = precoEsportivo;
            dailyRate = new BigDecimal("2000.00");
        }

        BigDecimal totalPrice = strategy.calcular(dias);

        rental.setDailyRate(dailyRate);
        rental.setTotalPrice(totalPrice);
        rental.setStatus(StatusAluguel.ATIVO);

        rental.setActualEndDate(null);

        carClient.atualizarStatus(rental.getCarId(), "ALUGADO");
        return rentalRepository.save(rental);
    }

    private boolean ehSuperEsportivo(String marca) {
        return marca.equalsIgnoreCase("Ferrari") ||
                marca.equalsIgnoreCase("Lamborghini") ||
                marca.equalsIgnoreCase("Bugatti") ||
                marca.equalsIgnoreCase("Koenigsegg") ||
                marca.equalsIgnoreCase("Pagani") ||
                marca.equalsIgnoreCase("McLaren");
    }


    public Rental finalizarAluguel(Long id) {
        Rental rental = buscarPorId(id);

        rental.setActualEndDate(java.time.LocalDate.now());
        rental.setStatus(StatusAluguel.FINALIZADO);

        if (rental.getActualEndDate().isAfter(rental.getExpectedEndDate())) {
            long diasExtras = ChronoUnit.DAYS.between(rental.getExpectedEndDate(), rental.getActualEndDate());

            BigDecimal valorMulta = rental.getDailyRate()
                    .multiply(BigDecimal.valueOf(diasExtras))
                    .multiply(BigDecimal.valueOf(2)); // Dobro
            BigDecimal novoTotal = rental.getTotalPrice().add(valorMulta);
            rental.setTotalPrice(novoTotal);

            System.out.println("ALERTA: Atraso de " + diasExtras + " dias. Multa aplicada: " + valorMulta);
        }

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