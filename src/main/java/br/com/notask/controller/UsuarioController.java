package br.com.notask.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.notask.model.Usuario;
import br.com.notask.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuarios")
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
	
	public ResponseEntity<String> verificarEmail(Usuario usuario) {
		
		if (userRep.existsByEmail(usuario.getEmail())) {
			return ResponseEntity.badRequest()
					.body("O email inserido já está em uso.");
		} else {
			return ResponseEntity.ok("Email validado!");
		}
		
	}

}
