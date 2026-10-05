package fsa.aula_24.demo.controller;

import fsa.aula_24.demo.model.Professor;
import fsa.aula_24.demo.service.ProfessorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping("/professores")
    public List<Professor> listarTodos() {
        return professorService.listarTodos();
    }

    @GetMapping("/professores/nome/{nome}")
    public List<Professor> buscarPorNome(@PathVariable String nome) {
        return professorService.buscarPorNome(nome);
    }

    @GetMapping("/professores/area/{area}")
    public List<Professor> buscarPorArea(@PathVariable String area) {
        return professorService.buscarPorArea(area);
    }

    @PostMapping("/professores")
    public ResponseEntity<Professor> cadastrar(@RequestBody Professor professor) {
        Professor salvo = professorService.cadastrar(professor);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/professores/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable Long id, @RequestBody Professor professor) {
        Professor atualizado = professorService.atualizar(id, professor);
        if (atualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Professor não encontrado");
        }
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/professores/{id}")
    public ResponseEntity<Object> excluir(@PathVariable Long id) {
        boolean excluido = professorService.excluir(id);
        if (!excluido) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Professor não encontrado");
        }
        return ResponseEntity.noContent().build();
    }
}
