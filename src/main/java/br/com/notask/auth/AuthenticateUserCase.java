package br.com.notask.auth;

import java.util.Date;
import java.util.Optional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.notask.model.Usuario;
import br.com.notask.repository.UsuarioRepository;

public class AuthenticateUserCase {
	
	private final UsuarioRepository repUser;
	private final String secretKey = "olateste";
	private final long expiration = 86400000;
	
	public AuthenticateUserCase(UsuarioRepository repUser) {
		this.repUser = repUser;
	}
	
	public String execute(String email, String senha) {
		System.out.println(email + "/ /////" + senha);
		
		Optional<Usuario> user = repUser.findByEmailAndSenha(email, senha);
		
		System.out.println("user encontrado");
		System.out.println(user);
		
		
		if(user == null || user.isEmpty()) {
			throw new RuntimeException("Email ou Senha Inconrreto");
		}
		
		return generateToken(user.get());
	}
	
	public String generateToken(Usuario usuario) {
		
		Algorithm algorithm = Algorithm.HMAC256(secretKey);
		
		return JWT.create()
				.withSubject(usuario.getId().toString())
				.withClaim("nome", usuario.getNome())
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + expiration))
				.sign(algorithm);
	}

}
