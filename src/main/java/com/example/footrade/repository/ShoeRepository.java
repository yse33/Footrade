package com.example.footrade.repository;

import com.example.footrade.entity.Shoe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ShoeRepository extends MongoRepository<Shoe, String> {
    List<Shoe> findAllByBrand(String brand);
    List<Shoe> findAllByProvider(String provider);
    List<Shoe> findAllByNewPriceLessThan(BigDecimal price);
}
