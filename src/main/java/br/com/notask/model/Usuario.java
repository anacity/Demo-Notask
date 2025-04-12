package br.com.notask.model;

import br.com.notask.util.HashUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	
	@Column(unique = true)
	private String username;
	
	@Column(unique = true)
	private String email;
	
	@Column(nullable = false)
	private String senha;
	
	public void setSenha(String senha) {
		this.senha = HashUtil.hash(senha);
	}
	
	public void setSenhaComHash(String hash) {
		this.senha = hash; 
	}

}
