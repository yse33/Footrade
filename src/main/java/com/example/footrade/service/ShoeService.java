package com.example.footrade.service;

import com.example.footrade.DTO.ShoeDetailDTO;
import com.example.footrade.DTO.ShoeListingDTO;
import com.example.footrade.DTO.ShoeSearchDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ShoeService {
    List<ShoeDetailDTO> getAll();
    ShoeDetailDTO getById(String id);
    ShoeDetailDTO save(ShoeDetailDTO shoe);
    ShoeDetailDTO update(ShoeDetailDTO shoe, String id);
    void delete(String id);
    List<ShoeListingDTO> getAllByUserPreference(String username, Integer page, Integer pageSize);
    List<ShoeListingDTO> getAllByUserFavorite(String username, Integer page, Integer pageSize);
    List<ShoeSearchDTO> getAllByQuery(String query, String username);
    List<ShoeDetailDTO> getAllByBrand(String brand);
    List<ShoeDetailDTO> getAllByProvider(String provider);
    List<ShoeDetailDTO> getAllByNewPriceLessThan(BigDecimal price);
    List<ShoeDetailDTO> getAllByBrandAndOnSale(String brand);
}
