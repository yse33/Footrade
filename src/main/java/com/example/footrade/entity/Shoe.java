package com.example.footrade.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Document(collection = "shoes")
@Data
public class Shoe {
    @Id
    private ObjectId id;
    private String brand;
    private String model;
    @Field("new_price")
    private BigDecimal newPrice;
    @Field("old_price")
    private BigDecimal oldPrice;
    private List<String> sizes;
    @Field("available_sizes")
    private List<String> availableSizes;
    private String provider;
    private String url;
    private List<String> images;
    @Field("initial_image")
    private String initialImage;
    @Field("on_sale")
    private Boolean onSale;
    @Field("last_updated")
    private Date lastUpdated;
}
