package com.example.footrade.service;

import com.example.footrade.DTO.ShoeDetailDTO;
import com.example.footrade.DTO.ShoeListingDTO;
import com.example.footrade.DTO.ShoeSearchDTO;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public interface ShoeService {
    ShoeDetailDTO getById(String id);
    List<ShoeListingDTO> getAllByUserPreference(Integer page, Integer pageSize);
    List<ShoeListingDTO> getAllByUserFavorite(Integer page, Integer pageSize);
    List<ShoeSearchDTO> getAllByQuery(String query);
    List<String> getSuggestions(String query);
    List<ObjectId> getUpdatedShoeIdsAfter(Date lastUpdated);
}
