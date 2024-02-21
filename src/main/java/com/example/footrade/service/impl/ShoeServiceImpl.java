package com.example.footrade.service.impl;

import com.example.footrade.DTO.ShoeDetailDTO;
import com.example.footrade.DTO.ShoeListingDTO;
import com.example.footrade.DTO.ShoeSearchDTO;
import com.example.footrade.entity.Preference;
import com.example.footrade.entity.Shoe;
import com.example.footrade.entity.User;
import com.example.footrade.repository.ShoeRepository;
import com.example.footrade.repository.UserRepository;
import com.example.footrade.service.ShoeService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.example.footrade.mapper.ShoeMapper.SHOE_MAPPER;

@Service
@RequiredArgsConstructor
public class ShoeServiceImpl implements ShoeService {
    private final ShoeRepository shoeRepository;
    private final UserRepository userRepository;

    @Override
    public List<ShoeDetailDTO> getAll() {
        return SHOE_MAPPER.toShoeDetailDTOs(shoeRepository.findAll());
    }

    @Override
    public ShoeDetailDTO getById(String id) {
        return SHOE_MAPPER.toShoeDetailDTO(shoeRepository.findById(id).orElse(null));
    }

    @Override
    public ShoeDetailDTO save(ShoeDetailDTO shoe) {
        return SHOE_MAPPER.toShoeDetailDTO(shoeRepository.save(SHOE_MAPPER.fromShoeDetailDTO(shoe)));
    }

    @Override
    public ShoeDetailDTO update(ShoeDetailDTO shoe, String id) {
        return SHOE_MAPPER.toShoeDetailDTO(shoeRepository.save(SHOE_MAPPER.fromShoeDetailDTO(shoe)));
    }

    @Override
    public void delete(String id) {
        shoeRepository.deleteById(id);
    }

    private List<ShoeListingDTO> setListingDTOsAsFavorite(User user, List<ShoeListingDTO> shoes) {
        List<ObjectId> shoeIds = shoes.stream().map(ShoeListingDTO::getId).toList();

        SHOE_MAPPER.toShoeListingDTOs(
                shoeRepository.findAllByIdIn(user.getFavorites()))
                .forEach(
                        shoe -> {
                            if (shoeIds.contains(shoe.getId())) {
                                shoes.get(shoeIds.indexOf(shoe.getId())).setIsFavorite(true);
                            }
                        }
                );

        return shoes;
    }

    private List<ShoeSearchDTO> setSearchDTOsAsFavorite(User user, List<ShoeSearchDTO> shoes) {
        List<ObjectId> shoeIds = shoes.stream().map(ShoeSearchDTO::getId).toList();

        SHOE_MAPPER.toShoeSearchDTOs(
                        shoeRepository.findAllByIdIn(user.getFavorites()))
                .forEach(
                        shoe -> {
                            if (shoeIds.contains(shoe.getId())) {
                                shoes.get(shoeIds.indexOf(shoe.getId())).setIsFavorite(true);
                            }
                        }
                );

        return shoes;
    }

    @Override
    public List<ShoeListingDTO> getAllByUserPreference(String username, Integer page, Integer pageSize) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
        Preference preference = user.getPreference();

        List<ShoeListingDTO> shoesForUser = SHOE_MAPPER.toShoeListingDTOs(
                shoeRepository.findAllByBrandInAndAvailableSizesContainingAndOnSaleIsTrue(
                        preference.getBrands(),
                        preference.getSizes(),
                        PageRequest.of(page, pageSize)
                )
        );

        return setListingDTOsAsFavorite(user, shoesForUser);
    }

    @Override
    public List<ShoeListingDTO> getAllByUserFavorite(String username, Integer page, Integer pageSize) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        List<ShoeListingDTO> favoriteShoes = SHOE_MAPPER.toShoeListingDTOs(
                shoeRepository.findAllByIdIn(
                        user.getFavorites(),
                        PageRequest.of(page, pageSize)
                )
        );
        favoriteShoes.forEach(shoe -> shoe.setIsFavorite(true));

        return favoriteShoes;
    }

    @Override
    public List<ShoeSearchDTO> getAllByQuery(String query, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        List<ShoeSearchDTO> shoes = SHOE_MAPPER.toShoeSearchDTOs(shoeRepository.findAllByModelContainingIgnoreCase(query));

        return setSearchDTOsAsFavorite(user, shoes);
    }

    @Override
    public List<String> getSuggestions(String query) {
        return SHOE_MAPPER.toShoeSearchDTOs(shoeRepository.findAllByModelContainingIgnoreCase(query))
                .stream()
                .map(ShoeSearchDTO::getModel)
                .limit(5)
                .toList();
    }

    @Override
    public List<ObjectId> getUpdatedShoeIdsAfter(Date lastUpdated) {
        return shoeRepository.findAllByLastUpdatedIsAfter(lastUpdated)
                .stream()
                .map(Shoe::getId)
                .toList();
    }

    @Override
    public List<ShoeDetailDTO> getAllByBrand(String brand) {
        return SHOE_MAPPER.toShoeDetailDTOs(shoeRepository.findAllByBrand(brand));
    }

    @Override
    public List<ShoeDetailDTO> getAllByProvider(String provider) {
        return SHOE_MAPPER.toShoeDetailDTOs(shoeRepository.findAllByProvider(provider));
    }

    @Override
    public List<ShoeDetailDTO> getAllByNewPriceLessThan(BigDecimal price) {
        return SHOE_MAPPER.toShoeDetailDTOs(shoeRepository.findAllByNewPriceLessThan(price));
    }

    @Override
    public List<ShoeDetailDTO> getAllByBrandAndOnSale(String brand) {
        return SHOE_MAPPER.toShoeDetailDTOs(shoeRepository.findAllByBrandAndOnSale(brand, true));
    }
}
