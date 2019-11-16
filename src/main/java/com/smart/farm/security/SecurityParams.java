package com.smart.farm.security;

public interface SecurityParams {
	
	public static final String JWT_HEADER_NAME = "Authorization";
	public static final String SECRET = "ja3@gmail.net";
	public static final long EXPIRATION = 2*3600*1000;  //Expiration of token in mS
	public static final String HEADER_PREFIX = "Bearer ";
}
