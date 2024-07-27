package com.example.gangaram.repository;

import com.example.gangaram.entity.NSC;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface NSCRepo extends MongoRepository<NSC, String> {
    @Query(value = "{ 'name': ?0 }", fields = "{ 'instrument_key': 1, '_id': 0 }")
    List<NSC> findInstrumentKeysByName(String name);
}
