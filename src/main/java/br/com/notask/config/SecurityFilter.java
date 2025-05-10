package br.com.notask.config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.notask.provider.JWTProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter{
	
	@Autowired
	private JWTProvider jwtProvider;
	
	private static final List<String> PERMIT_LIST = List.of(
			"/usuarios/",
			"/usuarios/login"
		);
		
		// rotas privadas (com auenticacao)
		private static final List<String> AUTH_LIST_STARTS_WITH = List.of(
			"/tarefas/"
		);
		
	
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();
        
        if (header != null) {
            DecodedJWT token = jwtProvider.validateToken(header);
            
            if (token == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            
            request.setAttribute("user_id", token);
            
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(token.getSubject(),null);
        }
        
        filterChain.doFilter(request, response);
    }
    
	
	// validar se é uma rota não protegida
		private boolean isNotProtectedEndpoint(String requestURI) {
			// lista caminhos permitidos, verificar se é iguall ou bate
			
			// return PERMIT_LIST.strem().noneMatch(s -> requestURI.matches(S));
			return PERMIT_LIST.stream().noneMatch(requestURI::matches);
		}
		
		// validar se é uma rota protegida
		private boolean protectedList(String requestURI) {
			return AUTH_LIST_STARTS_WITH.stream().noneMatch(requestURI::startsWith);
		}
}