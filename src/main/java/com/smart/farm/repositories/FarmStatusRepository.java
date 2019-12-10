package com.smart.farm.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.smart.farm.models.FarmStatus;

public interface FarmStatusRepository extends MongoRepository<FarmStatus, String>{
	
public Page <FarmStatus> findBySystemIdAndDateInsertionAfter(String systemId,Date dateInsertion,Pageable pageable);

public Page <FarmStatus> findBySystemIdAndTypeAndDateInsertionAfter(String systemId,int type,Date dateInsertion,Pageable pageable);

public List<FarmStatus> findBySystemIdAndTypeAndDateInsertionAfter(String systemId,int type,Date dateInsertion);
}
