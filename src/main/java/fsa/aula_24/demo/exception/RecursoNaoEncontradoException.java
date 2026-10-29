package fsa.aula_24.demo.exception;

// 404 - registro inexistente
public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
