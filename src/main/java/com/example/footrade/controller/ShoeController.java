package com.example.footrade.controller;

import com.example.footrade.service.ShoeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/shoes")
@RequiredArgsConstructor
public class ShoeController {
    private final ShoeService shoeService;

    @GetMapping
    public ResponseEntity<?> getAllShoes() {
        return ResponseEntity.ok(shoeService.getAll());
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<?> getAllShoesByBrand(@PathVariable String brand) {
        return ResponseEntity.ok(shoeService.getAllByBrandAndOnSale(brand));
    }

    @GetMapping("/provider/{provider}")
    public ResponseEntity<?> getAllShoesByProvider(@PathVariable String provider) {
        return ResponseEntity.ok(shoeService.getAllByProvider(provider));
    }

    @GetMapping("/below_price/{price}")
    public ResponseEntity<?> getAllShoesByNewPriceLessThan(@PathVariable BigDecimal price) {
        return ResponseEntity.ok(shoeService.getAllByNewPriceLessThan(price));
    }
}
