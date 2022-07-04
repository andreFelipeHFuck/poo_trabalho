package exception;

public class LoginErroException extends Exception{
    public LoginErroException(){
        super("Não foi possivel realizar o login, tente novamente");
    }
}
