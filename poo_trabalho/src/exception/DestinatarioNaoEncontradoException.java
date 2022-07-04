package exception;

public class DestinatarioNaoEncontradoException extends Exception{
    public DestinatarioNaoEncontradoException(){
        super("Endereço de email não encontrado, por favor tente de novo");
    }
}
