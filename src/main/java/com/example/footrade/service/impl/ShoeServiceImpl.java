package com.example.footrade.service.impl;

import com.example.footrade.DTO.ShoeDetailDTO;
import com.example.footrade.DTO.ShoeListingDTO;
import com.example.footrade.DTO.ShoeSearchDTO;
import com.example.footrade.entity.Preference;
import com.example.footrade.entity.Shoe;
import com.example.footrade.entity.User;
import com.example.footrade.repository.ShoeRepository;
import com.example.footrade.service.AuthenticationService;
import com.example.footrade.service.ShoeService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.example.footrade.mapper.ShoeMapper.SHOE_MAPPER;

@Service
@RequiredArgsConstructor
public class ShoeServiceImpl implements ShoeService {
    private final ShoeRepository shoeRepository;
    private final AuthenticationService authenticationService;

    @Override
    public ShoeDetailDTO getById(String id) {
        return SHOE_MAPPER.toShoeDetailDTO(shoeRepository.findById(id).orElse(null));
    }

    private List<ShoeListingDTO> setListingDTOsFavorite(User user, List<ShoeListingDTO> shoes) {
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

    private List<ShoeSearchDTO> setSearchDTOsFavorite(User user, List<ShoeSearchDTO> shoes) {
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
    public List<ShoeListingDTO> getAllByUserPreference(Integer page, Integer pageSize) {
        User user = authenticationService.getCurrentUser();
        Preference preference = user.getPreference();

        List<ShoeListingDTO> shoesForUser = SHOE_MAPPER.toShoeListingDTOs(
                shoeRepository.findAllByBrandInAndAvailableSizesContainingAndOnSaleIsTrue(
                        preference.getBrands(),
                        preference.getSizes(),
                        PageRequest.of(page, pageSize)
                )
        );

        return setListingDTOsFavorite(user, shoesForUser);
    }

    @Override
    public List<ShoeListingDTO> getAllByUserFavorite(Integer page, Integer pageSize) {
        User user = authenticationService.getCurrentUser();

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
    public List<ShoeSearchDTO> getAllByQuery(String query) {
        User user = authenticationService.getCurrentUser();

        List<ShoeSearchDTO> shoes = SHOE_MAPPER.toShoeSearchDTOs(
                shoeRepository.findAllByModelContainingIgnoreCase(query)
        );

        return setSearchDTOsFavorite(user, shoes);
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
    public List<ObjectId> getShoeIdsForNotification(Date lastUpdated) {
        return shoeRepository.findAllByOnSaleIsTrueAndLastUpdatedIsAfter(lastUpdated)
                .stream()
                .map(Shoe::getId)
                .toList();
    }
}
