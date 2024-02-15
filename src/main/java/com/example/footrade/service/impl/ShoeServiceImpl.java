package com.example.footrade.service.impl;

import com.example.footrade.DTO.ShoeDetailDTO;
import com.example.footrade.entity.Preference;
import com.example.footrade.entity.User;
import com.example.footrade.repository.ShoeRepository;
import com.example.footrade.repository.UserRepository;
import com.example.footrade.service.ShoeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.example.footrade.mapper.ShoeMapper.SHOE_MAPPER;

@Service
@RequiredArgsConstructor
public class ShoeServiceImpl implements ShoeService {
    private final ShoeRepository shoeRepository;
    private final UserRepository userRepository;

    @Override
    public List<ShoeDetailDTO> getAll() {
        return SHOE_MAPPER.toShoeDTOs(shoeRepository.findAll());
    }

    @Override
    public ShoeDetailDTO getById(String id) {
        return SHOE_MAPPER.toShoeDTO(shoeRepository.findById(id).orElse(null));
    }

    @Override
    public ShoeDetailDTO save(ShoeDetailDTO shoe) {
        return SHOE_MAPPER.toShoeDTO(shoeRepository.save(SHOE_MAPPER.fromShoeDTO(shoe)));
    }

    @Override
    public ShoeDetailDTO update(ShoeDetailDTO shoe, String id) {
        return SHOE_MAPPER.toShoeDTO(shoeRepository.save(SHOE_MAPPER.fromShoeDTO(shoe)));
    }

    @Override
    public void delete(String id) {
        shoeRepository.deleteById(id);
    }

    @Override
    public List<ShoeDetailDTO> getAllByUserPreference(String username, Integer page, Integer pageSize) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
        Preference preference = user.getPreference();

        return SHOE_MAPPER.toShoeDTOs(
                shoeRepository.findAllByBrandInAndAvailableSizesContainingAndOnSaleIsTrue(
                        preference.getBrands(),
                        preference.getSizes(),
                        PageRequest.of(page, pageSize)
                )
        );
    }

    @Override
    public List<ShoeDetailDTO> getAllByBrand(String brand) {
        return SHOE_MAPPER.toShoeDTOs(shoeRepository.findAllByBrand(brand));
    }

    @Override
    public List<ShoeDetailDTO> getAllByProvider(String provider) {
        return SHOE_MAPPER.toShoeDTOs(shoeRepository.findAllByProvider(provider));
    }

    @Override
    public List<ShoeDetailDTO> getAllByNewPriceLessThan(BigDecimal price) {
        return SHOE_MAPPER.toShoeDTOs(shoeRepository.findAllByNewPriceLessThan(price));
    }

    @Override
    public List<ShoeDetailDTO> getAllByBrandAndOnSale(String brand) {
        return SHOE_MAPPER.toShoeDTOs(shoeRepository.findAllByBrandAndOnSale(brand, true));
    }
}
