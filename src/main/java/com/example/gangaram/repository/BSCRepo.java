package com.example.gangaram.repository;

import com.example.gangaram.entity.BSC;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface BSCRepo extends MongoRepository<BSC, String> {
    @Query(value = "{ 'name': ?0 }", fields = "{ 'instrument_key': 1, '_id': 0 }")
    List<BSC> findInstrumentKeysByName(String name);
}
