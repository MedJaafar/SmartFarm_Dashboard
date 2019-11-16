package com.smart.farm.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart.farm.jpa.entities.AppRole;
import com.smart.farm.jpa.entities.AppRoleRepository;
import com.smart.farm.jpa.entities.AppUser;
import com.smart.farm.jpa.entities.AppUserRepository;
import com.smart.farm.mdbServices.NextSequenceService;
import com.smart.farm.models.FarmSystem;
import com.smart.farm.models.FarmUser;
import com.smart.farm.repositories.FarmSystemRepository;
import com.smart.farm.repositories.FarmUserRepository;
import com.smart.farm.repositories.SystemUrlRepository;

@Service
@Transactional
public class AccountServiceImp implements AccountService {
	
	@Autowired
	private AppUserRepository appuserrepository;
	@Autowired
	private AppRoleRepository approlerepository;
	@Autowired
	private BCryptPasswordEncoder bcpe;
	@Autowired 
	private FarmUserRepository farmUserRepository;
	@Autowired
	private FarmSystemRepository systemRepository;
	@Autowired
	private NextSequenceService nextsequenceService;
	
	@Override
	public AppUser saveUser(String username, String password, String confirmedPassword, String email,String first_name,String last_name,String systemCode) {
		AppUser user = appuserrepository.findByUsername(username);
		if(user != null) throw new RuntimeException("Utilisateur existant");
		FarmUser farmUser = new FarmUser();
		if(!password.equals(confirmedPassword)) throw new RuntimeException("Confirmez votre mot de passe");
		AppUser appUser = new AppUser();
		appUser.setUsername(username);
		appUser.setFirst_name(first_name);
		appUser.setLast_name(last_name);
		appUser.setPassword(bcpe.encode(password));
		appUser.setEmail(email);
		appUser.setActivated(true);
		appuserrepository.save(appUser);  //This Method is Transactional but, only in the End of the method
		addRoleToUser(username, "ADMIN");
		
		/*  Create the MONGO DB User clone  */
		farmUser.setId(Integer.toString(nextsequenceService.getNextSequence(NextSequenceService.FARMER_USER_SEQ)));
		farmUser.setUsername(username);
		farmUser.setFirst_name(first_name);
		farmUser.setLast_name(last_name);
		farmUser.setEmail(email);
		farmUser.setActivated(true);
		
		Optional<FarmSystem> systemFarm = systemRepository.findBySystemCode(systemCode);
		systemFarm.ifPresent(system -> {
		List<String> systemList = new ArrayList<String>();
		systemList.add(system.getSystemID());
		farmUser.setSystem(systemList);
		});
		
		farmUserRepository.save(farmUser);
		/*  Create the MONGO DB User clone  */
		return appUser;
	}

	@Override
	public AppRole saveRole(AppRole role) {
		return approlerepository.save(role);
	}

	@Override
	public AppUser loadUserByUsername(String userName) {
		return appuserrepository.findByUsername(userName);
	}

	@Override
	public void addRoleToUser(String userName, String roleName) {
		AppUser appuser = appuserrepository.findByUsername(userName);
		AppRole approle = approlerepository.findByRoleName(roleName);
		appuser.getRoles().add(approle);			
	}
}
