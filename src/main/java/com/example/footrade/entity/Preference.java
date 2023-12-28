package com.example.footrade.entity;

import com.example.footrade.enums.Brand;
import com.example.footrade.enums.Gender;
import lombok.Data;

import java.util.List;

@Data
public class Preference {
    private List<Brand> brands;
    private List<String> sizes;
    private List<Gender> genders;
}
