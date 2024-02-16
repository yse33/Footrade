package com.example.footrade.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class ShoeSearchDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String brand;
    private String model;
    private String initialImage;
    private Boolean isFavorite = false;
}
