package br.com.notask.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.notask.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	boolean existsByEmail(String email);
	
	boolean existsByUsername(String username);
	
	public Optional<Usuario> findByEmailAndSenha(String email, String senha);
	
	public Usuario findBySenha(String email);
}
