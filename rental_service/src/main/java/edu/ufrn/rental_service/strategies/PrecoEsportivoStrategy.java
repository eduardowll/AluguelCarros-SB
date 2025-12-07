package edu.ufrn.rental_service.strategies;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class PrecoEsportivoStrategy implements CalculoPrecoStrategy {
    @Override
    public BigDecimal calcular(long dias) {
        return new BigDecimal("2000.00").multiply(BigDecimal.valueOf(dias));
    }
}
