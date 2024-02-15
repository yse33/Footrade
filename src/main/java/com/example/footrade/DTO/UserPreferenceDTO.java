package com.example.footrade.DTO;

import com.example.footrade.entity.Preference;
import lombok.Data;

@Data
public class UserPreferenceDTO {
    private String username;
    private Preference preference;
}
