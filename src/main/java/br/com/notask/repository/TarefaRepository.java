package br.com.notask.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.notask.model.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Long>{

}
