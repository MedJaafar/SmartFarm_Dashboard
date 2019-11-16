package com.smart.farm.repositories;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.smart.farm.models.FarmSystem;

public interface FarmSystemRepository extends MongoRepository<FarmSystem, String> {
	
	/* Optional  Retrieve */
	Optional<FarmSystem> findBySystemCode(String SystemCode);
	FarmSystem findBySystemID ( String systemID);
}
