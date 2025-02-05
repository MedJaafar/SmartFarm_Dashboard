package com.smart.farm.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTAuthorisationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterchain)
			throws ServletException, IOException {
		
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, authorization");
		response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, authorization");
		response.addHeader("Access-Control-Allow-Methods","GET,POST,DELETE,PUT,PATCH");
		
		if(request.getMethod().equals("OPTIONS")) {
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else if(request.getRequestURI().equals("/login")) {
			filterchain.doFilter(request, response);
			return;
		}
		else {
		String jwt = request.getHeader(SecurityParams.JWT_HEADER_NAME);
		if(jwt == null || !jwt.startsWith(SecurityParams.HEADER_PREFIX)) {
			filterchain.doFilter(request, response);
			return;
		}
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SecurityParams.SECRET)).build();
		DecodedJWT decodeJWT = verifier.verify(jwt.substring(SecurityParams.HEADER_PREFIX.length()));
		String userName = decodeJWT.getSubject();
		List <String> roles = decodeJWT.getClaims().get("roles").asList(String.class);
		Collection <GrantedAuthority> authorities = new ArrayList<>();
		roles.forEach(role->{
			authorities.add(new SimpleGrantedAuthority(role));
		});
		UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(userName,null, authorities);
		SecurityContextHolder.getContext().setAuthentication(user);
		filterchain.doFilter(request, response);
		}
	}

}
