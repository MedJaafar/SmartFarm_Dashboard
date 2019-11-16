package com.smart.farm.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.smart.farm.models.SystemURL;

public interface SystemUrlRepository extends MongoRepository<SystemURL, String> {
	// Returns last url by date search
	SystemURL findTopByOrderByUpdateTimeDesc();
	SystemURL findTopBySystemIdOrderByUpdateTimeDesc(String systemId);
	
}
