package com.example.footrade.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.bson.types.ObjectId;

import java.math.BigDecimal;

@Data
public class ShoeListingDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String brand;
    private String model;
    private BigDecimal newPrice;
    private BigDecimal oldPrice;
    private String initialImage;
    private Boolean isFavorite;
}
