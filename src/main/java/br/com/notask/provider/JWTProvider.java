package br.com.notask.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class JWTProvider {
	
	//Secret para autenticação
	@Value("${security.token.secret}")
	private String secretkey;
	
	public DecodedJWT validateToken(String token) {
		
		token = token.replace("Bearer", "");
		Algorithm algorithm = Algorithm.HMAC256(secretkey);
		
		try {
			return JWT.require(algorithm).build().verify(token);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
