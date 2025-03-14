package br.com.notask.model;

import java.time.LocalDate;

import br.com.notask.utils.Prioridade;
import br.com.notask.utils.Tag;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Tarefa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private LocalDate dataPrazo;
	
	@Enumerated(EnumType.STRING)
	private Tag tag;
	
	@Enumerated(EnumType.STRING) 
    private Prioridade prioridade;
	
	private String descricao;
	
	@ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

}
