package com.example.footrade.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "shoes")
@Data
public class Shoe {
    @Id
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
}
