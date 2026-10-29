package fsa.aula_24.demo.exception;

// 409 - conflito com o estado atual dos dados (duplicidade, vínculos)
public class ConflitoException extends RuntimeException {
    public ConflitoException(String mensagem) {
        super(mensagem);
    }
}
