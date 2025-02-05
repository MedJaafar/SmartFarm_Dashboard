package com.smart.farm.security;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.smart.farm.business.AccountService;
import com.smart.farm.jpa.entities.AppUser;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

	@Autowired
	private AccountService accountService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = accountService.loadUserByUsername(username);
		if(appUser == null) throw new UsernameNotFoundException("User does not exist");
		Collection <GrantedAuthority> authorities = new ArrayList<>();
		appUser.getRoles().forEach(r->{
			authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
		});
		return new User(appUser.getUsername(),appUser.getPassword(),authorities);
	}

}
