package fsa.aula_24.demo.repository;

import fsa.aula_24.demo.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    List<Professor> findByNomeContainingIgnoreCase(String nome);

    List<Professor> findByAreaIgnoreCase(String area);
}
