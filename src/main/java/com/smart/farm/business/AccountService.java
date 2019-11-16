package com.smart.farm.business;

import com.smart.farm.jpa.entities.AppRole;
import com.smart.farm.jpa.entities.AppUser;

public interface AccountService {

	public AppUser saveUser(String username, String password,String confirmedPassword, String email,String first_name,String last_name,String systemCode);
	public AppRole saveRole(AppRole role);
	public AppUser loadUserByUsername(String userName);
	public void addRoleToUser(String UserName, String roleName);
}
