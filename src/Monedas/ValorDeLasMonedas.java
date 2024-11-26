package Monedas;

import java.util.Map;

public record ValorDeLasMonedas(
        String result,
        String base_code,
        Map<String, Double> conversion_rates
) {}

