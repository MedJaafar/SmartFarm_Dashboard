package com.smart.farm.jpa.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
	
	public AppRole findByRoleName(String roleNaime);
}
