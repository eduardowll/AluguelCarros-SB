package edu.ufrn.rental_service.strategies;

import java.math.BigDecimal;

public interface CalculoPrecoStrategy {
    BigDecimal calcular(long dias);
}
