package br.com.notask.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.notask.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	boolean existsByEmail(String email);
	
	boolean existsByUsername(String username);
}
