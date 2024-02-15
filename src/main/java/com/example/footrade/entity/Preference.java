package com.example.footrade.entity;

import com.example.footrade.enums.Brand;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Preference {
    private final List<Brand> brands;
    private final List<String> sizes;
    // private final List<Gender> genders;
}
