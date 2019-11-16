package com.smart.farm.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.smart.farm.models.FarmUser;

public interface FarmUserRepository extends MongoRepository<FarmUser,String> {
	public FarmUser findByUsername(String username);
}
