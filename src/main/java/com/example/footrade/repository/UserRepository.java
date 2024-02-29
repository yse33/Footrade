package com.example.footrade.repository;

import com.example.footrade.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    List<User> findByFavoritesIn(List<ObjectId> favoritesIds);
}
