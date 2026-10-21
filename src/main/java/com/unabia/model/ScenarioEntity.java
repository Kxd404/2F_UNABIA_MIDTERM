package com.unabia.model;

/**
 * Luggage record (Java Record, 5 attributes).
 * id is assigned automatically by the database starting from 1.
 */
public record ScenarioEntity(
        int id,
        String passengerName,
        String flightNo,
        double weightKg,
        String status
) {
}
