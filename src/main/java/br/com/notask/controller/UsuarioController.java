package br.com.notask.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.notask.model.Usuario;
import br.com.notask.model.dto.LoginDTO;
import br.com.notask.repository.UsuarioRepository;
import br.com.notask.util.HashUtil;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository userRep;
	
	@GetMapping("/")
	public ResponseEntity<?> buscarUsuarios(){
		try {
			List<Usuario> usuarios = userRep.findAll();
			
			if (usuarios.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT)
						.body("Nenhum usuário encontrado.");
			} 
			
			return ResponseEntity.ok(usuarios);
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro interno no servidor.");
		}
	}
	
	@PostMapping("/")
	public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario){
		try {
			
			
			if (usuario.getNome() == null || usuario.getNome().isEmpty()) {
				return ResponseEntity.badRequest()
						.body("O nome do usuário não pode ser vazio");
			}
			
			ResponseEntity<String> emailValidation = verificarEmail(usuario);
			if(emailValidation.getStatusCode() == HttpStatus.BAD_REQUEST) {
				return emailValidation;
			}
			
			String senhaHash = HashUtil.hash(usuario.getSenha());
			usuario.setSenha(senhaHash);
			
			Usuario newU = userRep.save(usuario);
			
			return ResponseEntity.ok(newU);
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro interno no servidor.");
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizarUsuario(@PathVariable("id") Long id, @RequestBody Usuario usuarioAtualizado){
		try {
			
			Optional<Usuario> verificaExiste = userRep.findById(id);
			
			if (verificaExiste.isPresent()) {
				Usuario u = verificaExiste.get();
				
				if (!u.getEmail().equals(usuarioAtualizado.getEmail())) {
					ResponseEntity<String> emailValidation = verificarEmail(usuarioAtualizado);
					if (emailValidation.getStatusCode() == HttpStatus.BAD_REQUEST) {
						
					}
				}
				
				if (!u.getUsername().equals(usuarioAtualizado.getUsername())) {
                    ResponseEntity<String> usernameValidation = verificarUsername(usuarioAtualizado);
                    if (usernameValidation.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        return usernameValidation; 
                    }
                }
				
				u.setNome(u.getNome());
				u.setEmail(u.getEmail());
				u.setUsername(u.getUsername());
				
				if(u.getSenha().equals(HashUtil.hash(""))) {
					String hash = userRep.findById(u.getId()).get().getSenha();
					u.setSenhaComHash(hash);
				}
				
				userRep.save(u);
				
				return ResponseEntity.ok(u);
				
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Usuário não encontrado");
			}
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro interno no servidor.");
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
	    try {
	        // Gera o hash da senha recebida (igual ao cadastro)
	        String senhaHash = HashUtil.hash(loginDTO.getSenha());
	        
	        // Busca no banco com email e hash da senha
	        Usuario u = userRep.findByEmailAndSenha(loginDTO.getEmail(), senhaHash);
	        
	        if (u == null) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                    .body("Email ou senha incorretos");
	        } else {
	            return ResponseEntity.ok("Login realizado com sucesso.");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Ocorreu um erro ao realizar login");
	    }
	}
	
	public ResponseEntity<String> verificarEmail(Usuario usuario) {
		
		if (userRep.existsByEmail(usuario.getEmail())) {
			return ResponseEntity.badRequest()
					.body("O email inserido já está em uso.");
		} else {
			return ResponseEntity.ok("Email validado!");
		}
		
	}
	
	public ResponseEntity<String> verificarUsername(Usuario usuario){
		if (userRep.existsByUsername(usuario.getUsername())) {
			return ResponseEntity.badRequest()
					.body("O username inserido já está em uso.");
		} else {
			return ResponseEntity.ok("Username validado!");
		}
	}

}
