package com.example.footrade.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShoeDetailDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String brand;
    private String model;
    private BigDecimal newPrice;
    private BigDecimal oldPrice;
    private List<String> sizes;
    private List<String> availableSizes;
    private String provider;
    private String url;
    private List<String> images;
    private String initialImage;
    private Boolean onSale;
}
