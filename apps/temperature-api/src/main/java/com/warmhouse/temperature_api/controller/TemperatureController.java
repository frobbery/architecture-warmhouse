package com.warmhouse.temperature_api.controller;

import com.warmhouse.temperature_api.response.TemperatureResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class TemperatureController {

    private final Random random = new Random();

    @GetMapping("temperature/{sensorId}")
    public TemperatureResponse getTemperature(@PathVariable Integer sensorId) {
        return randomTemperature();
    }

    @GetMapping("temperature")
    public TemperatureResponse getTemperature(@RequestParam String location) {
        return randomTemperature();
    }

    private TemperatureResponse randomTemperature() {
        return new TemperatureResponse(random.nextDouble(-50, 50));
    }
}
