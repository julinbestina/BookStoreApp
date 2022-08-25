package com.bl.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bl.Exception.CustomerException;
import com.bl.dto.LoginDTO;
import com.bl.dto.SignUpDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil implements Serializable {

	@Value("${secret}")
	private String secret;

	public String generateJWT(LoginDTO loginDto) {
		Map<String, Object> data = new HashMap<>();
		data.put("email", loginDto.getEmailId());
		data.put("password", loginDto.getPassword());

		return Jwts.builder().setClaims(data).signWith(SignatureAlgorithm.HS512, secret).compact();

	}

	public String verify(String authorization) {
		try {
			 Claims data = Jwts.parser().setSigningKey(secret)
					 .parseClaimsJws(authorization).getBody();
			 return (String) data.get("email");
			
			 
		} catch (Exception ex) {
			throw new CustomerException("Access Denied");
		}
	}
}
