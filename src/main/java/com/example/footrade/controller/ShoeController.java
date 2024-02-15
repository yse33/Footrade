package com.example.footrade.controller;

import com.example.footrade.service.ShoeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getAllShoesByUserPreference(
            @PathVariable String username,
            @RequestParam() Integer page,
            @RequestParam() Integer pageSize
    ) {
        return ResponseEntity.ok(shoeService.getAllByUserPreference(username, page, pageSize));
    }

    @GetMapping("/favorite/{username}")
    public ResponseEntity<?> getAllShoesByFavorite(
            @PathVariable String username,
            @RequestParam() Integer page,
            @RequestParam() Integer pageSize
    ) {
        return ResponseEntity.ok(shoeService.getAllByUserFavorite(username, page, pageSize));
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
