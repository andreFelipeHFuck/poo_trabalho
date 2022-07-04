package apresentacao;

import dados.Usuario;
import dados.Email;
import exception.*;
import jdk.jfr.Enabled;
import negocio.Sistema;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class TesteUnitario {
    private Sistema sistema;

    public TesteUnitario(){
        try{
            sistema = new Sistema();
        }catch (ClassNotFoundException e){
            System.err.println(e.getMessage());
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }catch (SelectException e){
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        TesteUnitario main = new TesteUnitario();

        // Criando usuarios
        Usuario usuario1 = new Usuario();
        usuario1.setNome("Pedro");
        usuario1.setEmail("pedro@email.com");
        usuario1.setSenha("pedro123");

        Usuario usuario2 = new Usuario();
        usuario2.setNome("Maria");
        usuario2.setEmail("maria@email.com");
        usuario2.setSenha("maria321");

        try{
            main.sistema.cadastrarUsuario(usuario1);
            main.sistema.cadastrarUsuario(usuario2);
        }catch (InsertException e){
            System.err.println(e.getMessage());
        }catch (SelectException e){
            System.err.println(e.getMessage());
        }

        // Email
        Email email1 = new Email();
        email1.setRemetente(usuario1);
        email1.setDestinatario(usuario2);
        email1.setTitulo("Ola Maria");
        email1.setCorpo("Oi Maria, como vai vc?");

        try{
            main.sistema.escreverEmail(email1);
        }catch (UpdateException e){
            System.err.println(e.getMessage());
        }

        // Resposta
        Email email2 = new Email();
        email2.setRemetente(usuario2);
        email2.setDestinatario(usuario1);
        email2.setTitulo("Resposta a Pedro");
        email2.setCorpo("Oi Pedro, esta tudo bem por aqui");
        email2.setEmailRespondido(true);
        email2.setIdEmailRespondido(email1.getId());


        main.sistema.responderEmail(email2);


        try {
            main.sistema.excluirEmail(email2.getId());
        }catch (DeleteException e){
            System.err.println(e.getMessage());
        }


    }
}
