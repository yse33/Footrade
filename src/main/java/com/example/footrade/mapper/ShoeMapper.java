package com.example.footrade.mapper;

import com.example.footrade.DTO.ShoeDetailDTO;
import com.example.footrade.DTO.ShoeListingDTO;
import com.example.footrade.DTO.ShoeSearchDTO;
import com.example.footrade.entity.Shoe;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = ShoeMapper.class)
public interface ShoeMapper {
    ShoeMapper SHOE_MAPPER = Mappers.getMapper(ShoeMapper.class);
    Shoe fromShoeDetailDTO(ShoeDetailDTO resource);
    ShoeDetailDTO toShoeDetailDTO(Shoe shoe);
    List<ShoeDetailDTO> toShoeDetailDTOs(List<Shoe> shoes);
    ShoeListingDTO toShoeListingDTO(Shoe shoe);
    List<ShoeListingDTO> toShoeListingDTOs(List<Shoe> shoes);
    List<ShoeSearchDTO> toShoeSearchDTOs(List<Shoe> shoes);
}
