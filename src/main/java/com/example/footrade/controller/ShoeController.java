package com.example.footrade.controller;

import com.example.footrade.service.ShoeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shoes")
@RequiredArgsConstructor
public class ShoeController {
    private final ShoeService shoeService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getShoeById(@PathVariable String id) {
        return ResponseEntity.ok(shoeService.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<?> getAllShoesByModelContaining(
            @RequestParam() String query
    ) {
        return ResponseEntity.ok(shoeService.getAllByQuery(query));
    }

    @GetMapping("/suggestions")
    public ResponseEntity<?> getSuggestions(
            @RequestParam() String query
    ) {
        return ResponseEntity.ok(shoeService.getSuggestions(query));
    }

    @GetMapping("/preferences")
    public ResponseEntity<?> getAllShoesByUserPreference(
            @RequestParam() Integer page,
            @RequestParam() Integer pageSize
    ) {
        return ResponseEntity.ok(shoeService.getAllByUserPreference(page, pageSize));
    }

    @GetMapping("/favorites")
    public ResponseEntity<?> getAllShoesByFavorite(
            @RequestParam() Integer page,
            @RequestParam() Integer pageSize
    ) {
        return ResponseEntity.ok(shoeService.getAllByUserFavorite(page, pageSize));
    }
}
