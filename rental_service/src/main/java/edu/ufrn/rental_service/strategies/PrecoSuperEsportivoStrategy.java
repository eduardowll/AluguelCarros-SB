package edu.ufrn.rental_service.strategies;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class PrecoSuperEsportivoStrategy implements CalculoPrecoStrategy {
    @Override
    public BigDecimal calcular(long dias) {
        return new BigDecimal("10000.00").multiply(BigDecimal.valueOf(dias));
    }
}
