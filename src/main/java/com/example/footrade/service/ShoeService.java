package com.example.footrade.service;

import com.example.footrade.DTO.ShoeDetailDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ShoeService {
    List<ShoeDetailDTO> getAll();
    ShoeDetailDTO getById(String id);
    ShoeDetailDTO save(ShoeDetailDTO shoe);
    ShoeDetailDTO update(ShoeDetailDTO shoe, String id);
    void delete(String id);
    List<ShoeDetailDTO> getAllByBrand(String brand);
    List<ShoeDetailDTO> getAllByProvider(String provider);
    List<ShoeDetailDTO> getAllByNewPriceLessThan(BigDecimal price);
}
