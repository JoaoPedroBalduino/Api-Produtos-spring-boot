package fsa.aula_24.demo.exception;

// 422 - dados válidos, mas que violam uma regra de negócio
public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
