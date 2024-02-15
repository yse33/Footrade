package com.example.footrade.repository;

import com.example.footrade.entity.Shoe;
import com.example.footrade.enums.Brand;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Repository
public interface ShoeRepository extends MongoRepository<Shoe, String> {
    List<Shoe> findAllByIdIn(Collection<ObjectId> id);
    List<Shoe> findAllByIdIn(Collection<ObjectId> id, Pageable pageable);
    List<Shoe> findAllByBrandInAndAvailableSizesContainingAndOnSaleIsTrue(
            Collection<Brand> brand, List<String> availableSizes, Pageable pageable
    );
    List<Shoe> findAllByModelContainingIgnoreCase(String model);
    List<Shoe> findAllByBrand(String brand);
    List<Shoe> findAllByProvider(String provider);
    List<Shoe> findAllByBrandAndOnSale(String brand, Boolean onSale);
    List<Shoe> findAllByNewPriceLessThan(BigDecimal price);
}
