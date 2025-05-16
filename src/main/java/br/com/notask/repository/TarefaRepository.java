package br.com.notask.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.notask.model.Tarefa;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long>{
	
	 @Query("SELECT t FROM Tarefa t WHERE t.usuario.email = :emailUsuario")
	    List<Tarefa> findAllByUsuarioEmail(@Param("emailUsuario") String emailUsuario);
	    
    // Busca uma tarefa específica se pertencer ao usuário
    @Query("SELECT t FROM Tarefa t WHERE t.id = :id AND t.usuario.email = :emailUsuario")
	    Optional<Tarefa> findByIdAndUsuarioEmail(
	        @Param("id") Long id, 
	        @Param("emailUsuario") String emailUsuario
	    );
	    
    @Query("SELECT t FROM Tarefa t WHERE t.usuario.id = :id")
    	List<Tarefa> findByUsuarioId(
	        @Param("id") Long id
	    );

    @Query("SELECT t FROM Tarefa t WHERE t.id = :id AND t.usuario.id = :usuarioId")
    Optional<Tarefa> findByIdAndUsuarioId(
        @Param("id") Long id, 
        @Param("usuarioId") Long usuarioId
    );


}
