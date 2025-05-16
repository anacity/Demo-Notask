package br.com.notask.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.notask.model.Tarefa;
import br.com.notask.model.Usuario;
import br.com.notask.repository.TarefaRepository;
import br.com.notask.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@RestController
@CrossOrigin
@RequestMapping("/tarefas")
public class TarefaController {
	
	@Autowired
	private TarefaRepository repTarefa;
	
	@Autowired
    private UsuarioRepository usuarioRepository;
	
	@GetMapping("/geral")
	public ResponseEntity<?> buscarTarefas() {
		try {
			List<Tarefa> tarefas = repTarefa.findAll();
			
			if (tarefas.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT)
						.body("Nenhuma Tarefa encontrada.");
			}
			
			return ResponseEntity.ok(tarefas);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro interno no servidor.");
		}
	}
	
	@GetMapping("/")
    public ResponseEntity<?> listarTarefasDoUsuario(HttpServletRequest request) {
		try {
			var id_usuario = request.getAttribute("user_id");
			
			
	        List<Tarefa> tarefas = repTarefa.findByUsuarioId(Long.parseLong((String) id_usuario));
	        
	        if(tarefas.isEmpty()) {
	        	return ResponseEntity.status(HttpStatus.NO_CONTENT)
						.body("Nenhuma Tarefa encontrada.");
	        }
	        
	        return ResponseEntity.ok(tarefas);
	        
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro interno no servidor.");
		}
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTarefaPorId(@PathVariable("id") Long id, HttpServletRequest request) {
    	try {
    		
    		var id_usuario = request.getAttribute("user_id");
    		
    		Optional<Tarefa> verificaExiste = repTarefa.findByIdAndUsuarioId(id, Long.parseLong((String) id_usuario));
    		
    		if(verificaExiste.isPresent()) {
    			return ResponseEntity.ok(verificaExiste);
    		}
    		
    		return ResponseEntity.status(HttpStatus.NO_CONTENT)
    				.body("Não existe tarefa com esse id.");
    		
    		
    	} catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    				.body("Ocorreu um erro interno no servidor");
    	}

    }

	@PostMapping("/")
	public ResponseEntity<?> criarTarefa(@RequestBody Tarefa tarefa, HttpServletRequest request) {
		var id_user = request.getAttribute("user_id");
		Optional<Usuario> usuario = usuarioRepository.findById(Long.parseLong((String) id_user));
		tarefa.setUsuario(usuario.get());
		
		try {
			if (tarefa.getNome() == null || tarefa.getNome().isEmpty()) {
				return ResponseEntity.badRequest().body("O nome da tarefa não pode ser vazio.");
			}
			
			Tarefa newT = repTarefa.save(tarefa);
			
			return ResponseEntity.ok(newT);
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro interno no servidor.");
		}
	}
	
	@Transactional
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarTarefa(@PathVariable("id") Long id, HttpServletRequest request) {
		
		try {
			var id_usuario = request.getAttribute("user_id");
    		
    		Optional<Tarefa> verificaExiste = repTarefa.findByIdAndUsuarioId(id, Long.parseLong((String) id_usuario));
			
			if (verificaExiste.isPresent()) {
				repTarefa.deleteById(id);
				
				return ResponseEntity.ok("Tarefa deletada com sucesso");
			}else {
				return ResponseEntity.status(HttpStatus.NO_CONTENT)
						.body("Ocorreu um erro interno no servidor.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro interno no servidor.");
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizarTarefa(@PathVariable("id") Long id, @RequestBody Tarefa tarefa, HttpServletRequest request) {
		try {
			
			var id_usuario = request.getAttribute("user_id");
    		
    		
    		Optional<Tarefa> verificaExiste = repTarefa.findByIdAndUsuarioId(id, Long.parseLong((String) id_usuario));
			
			if (verificaExiste.isPresent()) {
				Tarefa t = verificaExiste.get();
				t.setNome(tarefa.getNome());
				t.setDataPrazo(tarefa.getDataPrazo());
				t.setTag(tarefa.getTag());
				t.setPrioridade(tarefa.getPrioridade());
				t.setDescricao(tarefa.getDescricao());
				
				repTarefa.save(t);
				
				return ResponseEntity.ok(t);
			}else {
				return ResponseEntity.status(HttpStatus.NO_CONTENT)
						.body("Tarefa não encontrada");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ocorreu um erro interno no servidor.");
		}
	}
}


























