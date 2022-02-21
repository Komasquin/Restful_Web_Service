package com.rest.store002.shared;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.rest.store002.ws.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Utils {
	//this class is used to encrypt the userID variable from the 'UserEntity' class
	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
//	private final int ITERATIONS = 10000;
//	private final int KEY_LENGTH = 256;

	public String generateID(int length) {
		return generateRandomString(length);
	}
	
	public String generateRandomString(int length) {
		StringBuilder returnValue = new StringBuilder(length);
		
		for(int i = 0; i < length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		return new String(returnValue);
	}
	
	public static boolean hasTokenExpired(String token) {
		//First: create a return value
		boolean returnValue = false;

		try {
			//Creates a Claim object using the parameter token and our generated token for the project
			Claims claims = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret()).parseClaimsJws(token)
					.getBody();

			//Gets the tokens expiration date
			Date tokenExpirationDate = claims.getExpiration();
			
			//Create and object of type date to compare
			Date todayDate = new Date();

			//compare if the tokens expiration date is before today, and set to true or false
			returnValue = tokenExpirationDate.before(todayDate);
		} catch (ExpiredJwtException ex) {
			//Any exceptions just return false
			returnValue = true;
		}

		//Last: return the returnValue
		return returnValue;
	}
	
    public String generateEmailVerificationToken(String userId) {
    	//Build the token
        String token = Jwts.builder()
        		//start by using the userID 
                .setSubject(userId)
                //Set the exiration date
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                //encrypt the token
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                //seerialize the token to be passed in URL field
                .compact();
        return token;
    }
}
