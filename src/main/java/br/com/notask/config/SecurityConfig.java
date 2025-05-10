package br.com.notask.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private SecurityFilter securityFilter;
	
	private static final String[] PERMIT_URLS = {
			"/usuarios/",
	        "/usuarios/login"
	};
	
	
	@Bean
	// Filtro de segurança 
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// csrf -> Crooss-Site Request Forgery
		// csrf.disable() -> que ele não vai usar sessão, é token
		http.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(authorizeRequests -> {
					// rotas liberadas
					authorizeRequests
						.requestMatchers(PERMIT_URLS).permitAll();
					
					// outras rotas, deve estar autenticado
					authorizeRequests.anyRequest().authenticated();
			});
		// retorna a requisição quando sessão desabilitada
		return http.build();
	}

}
