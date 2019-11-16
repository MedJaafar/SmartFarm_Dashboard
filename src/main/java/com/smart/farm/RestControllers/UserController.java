package com.smart.farm.RestControllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smart.farm.business.AccountService;
import com.smart.farm.jpa.entities.AppUser;
import com.smart.farm.jpa.entities.AppUserRepository;
import com.smart.farm.models.FarmSystem;
import com.smart.farm.models.FarmUser;
import com.smart.farm.repositories.FarmSystemRepository;
import com.smart.farm.repositories.FarmUserRepository;

@RestController
public class UserController {
	
	private static final String REGISTER_PATH = "/register";
	private static final String CONNECTED_USER_PATH = "/getconnecteduser";
	private static final String CONNECTED_USERS_SYSTEMS_PATH = "/getusersystems";
	
	@Autowired
	private AccountService accountSerivce;
	@Autowired
	private FarmUserRepository farmuserrepository;
	@Autowired
	private FarmSystemRepository systemRepository; 
	@Autowired 
	private AppUserRepository appuserrepository;
	
	
	@PostMapping(REGISTER_PATH)
	public AppUser register(@RequestBody UserForm userform) {
		return accountSerivce.saveUser(userform.getUsername(), userform.getPassword(), userform.getConfirmedPassword(), userform.getEmail(), userform.getFirst_name(), userform.getLast_name(),userform.getSystemCode());
	}
	
	@GetMapping(CONNECTED_USER_PATH)
	public AppUser getConnectedUser_Username() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		AppUser user = appuserrepository.findByUsername(auth.getName());
		return user;
	}
	
	// In connection to the system -> Returns the current user list of systems
	@GetMapping(CONNECTED_USERS_SYSTEMS_PATH)
	public List<FarmSystem> getCurrentusersSystemsList(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		FarmUser user = farmuserrepository.findByUsername(auth.getName());
		List<String> systemIdList = user.getSystem();
		List<FarmSystem> systemList = new ArrayList<FarmSystem>();
		
		for(int i=0;i<systemIdList.size();i++) {
		FarmSystem farmSystem = systemRepository.findBySystemID(systemIdList.get(i));
		if(farmSystem != null) {
			systemList.add(farmSystem); }
		}
		return systemList;	
	}
	
}

