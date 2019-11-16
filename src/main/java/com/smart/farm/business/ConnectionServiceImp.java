package com.smart.farm.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.smart.farm.models.SystemURL;
import com.smart.farm.repositories.SystemUrlRepository;

@Service
public class ConnectionServiceImp implements IConnectionService{
	
	
	@Autowired 
	private SystemUrlRepository urlRepository;
	
	// Returns the current url as an Json.
	public SystemURL getCurrentSystemUrl (String systemId) {
		SystemURL url=	urlRepository.findTopBySystemIdOrderByUpdateTimeDesc(systemId);
		return url; 
	}
}
