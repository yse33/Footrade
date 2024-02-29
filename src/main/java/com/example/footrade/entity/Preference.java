package com.example.footrade.entity;

import com.example.footrade.enums.Brand;
import lombok.Data;

import java.util.List;

@Data
public class Preference {
    private final List<Brand> brands;
    private final List<String> sizes;
}
