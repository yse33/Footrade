package com.example.footrade.mapper;

import com.example.footrade.DTO.ShoeDetailDTO;
import com.example.footrade.entity.Shoe;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = ShoeMapper.class)
public interface ShoeMapper {
    ShoeMapper SHOE_MAPPER = Mappers.getMapper(ShoeMapper.class);
    Shoe fromShoeDTO(ShoeDetailDTO resource);
    ShoeDetailDTO toShoeDTO(Shoe shoe);
    List<Shoe> fromShoeDTOs(List<ShoeDetailDTO> dtos);
    List<ShoeDetailDTO> toShoeDTOs(List<Shoe> shoes);
}
