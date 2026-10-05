package fsa.aula_24.demo.service;

import fsa.aula_24.demo.model.Professor;
import fsa.aula_24.demo.repository.ProfessorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public List<Professor> listarTodos() {
        return professorRepository.findAll();
    }

    public List<Professor> buscarPorNome(String nome) {
        return professorRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Professor> buscarPorArea(String area) {
        return professorRepository.findByAreaIgnoreCase(area);
    }

    public Professor cadastrar(Professor professor) {
        return professorRepository.save(professor);
    }

    public Professor atualizar(Long id, Professor dadosAtualizados) {
        Professor professor = professorRepository.findById(id).orElse(null);
        if (professor == null) {
            return null;
        }
        professor.setNome(dadosAtualizados.getNome());
        professor.setEmail(dadosAtualizados.getEmail());
        professor.setArea(dadosAtualizados.getArea());
        professor.setTelefone(dadosAtualizados.getTelefone());
        return professorRepository.save(professor);
    }

    public boolean excluir(Long id) {
        if (!professorRepository.existsById(id)) {
            return false;
        }
        professorRepository.deleteById(id);
        return true;
    }
}
