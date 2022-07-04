package apresentacao;

import dados.Usuario;
import dados.Email;
import exception.*;
import negocio.Sistema;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;


public class Main {
    private Sistema sistema;

    private Usuario usuarioLogado = null;
    private Scanner s = new Scanner(System.in);

    public Main(){
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
        Main m = new Main();
        int opt = -1;

        System.out.println("Sistema de email: ");
        while (opt != 0){
            if(m.estaLogado() == false){
                m.menuInicial();
                opt = Integer.valueOf(m.s.nextLine());

                switch (opt){
                    case 1:
                        m.cadastrarUsuario();
                        break;
                    case 2:
                        m.login();
                        break;
                    case 0:
                        break;
                    default:
                        break;
                }
            }else {
                m.menuUsuario();
                opt = Integer.valueOf(m.s.nextLine());

                switch (opt){
                    case 1:
                        m.verEmails();
                        break;
                    case 2:
                        m.escreverEmail();
                        break;
                    case 3:
                        m.excluirEmail();
                        break;
                    case 0:
                        // Limpar usuario login
                        m.usuarioLogado = null;
                        opt = -1;
                        break;
                    default:
                        break;
                }
            }

        }
    }

    public boolean estaLogado(){
        if(usuarioLogado == null){
            return false;
        }
        return true;
    }
    public void menuInicial(){
        System.out.println("1 - Cadastrar novo usuario");
        System.out.println("2 - Fazer login");
        System.out.println("0 - Sair");
        System.out.print("> ");
    }

    public Usuario formularioUsuario(){
        Usuario u = new Usuario();

        System.out.println("Cadastro de Usuario: ");

        System.out.print("Nome: ");
        String nome = s.nextLine();
        u.setNome(nome);

        System.out.print("Email: ");
        // Criar um sitema de verificação de email
        String email = s.nextLine();
        u.setEmail(email);

        System.out.print("Senha: ");
        String senha = s.nextLine();
        u.setSenha(senha);

        return u;
    }

    public void cadastrarUsuario(){
        Usuario u = formularioUsuario();
        try{
            sistema.cadastrarUsuario(u);
        }catch (InsertException e){
            System.err.println(e.getMessage());
        }catch (SelectException e){
            System.err.println(e.getMessage());
        }
    }
    public Usuario formularioLogin(){
        Usuario u = new Usuario();

        System.out.println("Login: ");

        System.out.print("Email: ");
        String email = s.nextLine();
        u.setEmail(email);

        System.out.print("Senha: ");
        String senha = s.nextLine();
        u.setSenha(senha);

        return u;
    }

    public void login(){
        Usuario u = formularioLogin();

        try {
            sistema.login(u);
            System.out.println("Login realizado com sucesso");
            usuarioLogado = u;
        }catch (SelectException e){
            System.err.println(e.getMessage());
        }catch (LoginErroException e){
            System.out.println(e.getMessage());
        }
    }

    public void menuUsuario(){
        System.out.println("Menu do usuario: ");
        System.out.println("1 - Ver emails");
        System.out.println("2 - Escrever email");
        System.out.println("3 - Excluir email");
        System.out.println("0 - Sair");
        System.out.print("> ");
    }

    public void verEmails(){
        try{
            List<Email> emails = sistema.emailsUsuario(usuarioLogado.getId());
            int cont = 0;
            int opt = -1;

            if(emails.size() == 0){
                System.out.println("Nenhum email encotrado: ");
            }else{
                System.out.println("Emails: ");
                for(Email e: emails){
                    System.out.println(cont + "" + e);
                    cont++;
                }

                System.out.println("Escolha o email que deseja ler (Caso contrario digite -1)");
                System.out.print("> ");
                opt = Integer.valueOf(s.nextLine());
                verEmail(emails.get(opt).getId());
            }
        }catch (SelectException e){
            System.err.println(e.getMessage());
        }

    }

    public void verEmail(int id_email){
        try {
            Email email = sistema.buscarEmail(id_email);
            System.out.println(email.getRemetente().getNome() + " <" + email.getRemetente().getEmail() + ">  Data: " + email.getData() + " " + email.getHora());
            System.out.println(email.getTitulo());
            System.out.println();
            String corpo = sistema.decriptar(email.getCorpo(), sistema.chaveDeEncriptar(email));
            System.out.println(corpo);
            perguntaResponderEmail(email);
        }catch (SelectException e){
            System.err.println(e.getMessage());
        }
    }
    public Email formularioEmail(){
        Email e = new Email();
        Usuario destinatario = new Usuario();

        System.out.println("Novo Email");

        e.setRemetente(usuarioLogado);

        while (true){
            // Destinatario
            System.out.print("Destinatario: ");
            String email = s.nextLine();
            destinatario.setEmail(email);

            try{
                sistema.buscaDestinatario(destinatario);

                e.setDestinatario(destinatario);

                System.out.println("Titulo: ");
                System.out.print("> ");
                String titulo = s.nextLine();
                e.setTitulo(titulo);

                System.out.println("Corpo: ");
                System.out.print("> ");
                String corpo = s.nextLine();
                e.setCorpo(corpo);

                System.out.println("Email enviado");
                break;
            }catch (DestinatarioNaoEncontradoException exc){
                System.out.println(exc.getMessage());
            }
        }

        return e;
    }

    public void escreverEmail(){
        try {
            Email email = formularioEmail();
            sistema.escreverEmail(email);
        }catch (UpdateException e){
            System.err.println(e.getMessage());
        }
    }

    public void perguntaResponderEmail(Email email){
        if(!email.getRemetente().getEmail().equals(email.getDestinatario().getEmail())){
            System.out.println("\nDeseja responder o email? S/ N");
            String resp = s.nextLine().toUpperCase();
            char r = resp.charAt(0);

            if (r == 'S') {
                responderEmail(email);
            }
        }
    }

    public Email formularioResponderEmail(Email email){
        Email emailResposta = new Email();

        emailResposta.setEmailRespondido(true);
        emailResposta.setIdEmailRespondido(email.getId());
        emailResposta.setDestinatario(email.getRemetente());
        emailResposta.setRemetente(email.getDestinatario());
        emailResposta.setTitulo("Respota: " + email.getTitulo());

        System.out.println("Corpo: ");
        System.out.print("> ");
        String corpo = s.nextLine();
        emailResposta.setCorpo(corpo);

        System.out.println("Email enviado");

        return emailResposta;
    }

    public void responderEmail(Email email){
        Email emailResposta = formularioResponderEmail(email);
        sistema.responderEmail(emailResposta);
    }
    public int formularioExcluirEmail(){
        int id = 0;
        int opt = 0;
        int cont = 0;
        try{
            List<Email> emails = sistema.emailsExcluir(usuarioLogado.getId());
            if(emails.size() == 0){
                System.out.println("Nenhume email encontrado");
            }else{
                for(Email e: emails){
                    System.out.println(cont + "" + e);
                    cont++;
                }

                System.out.println("Escolha um dos emails para ser deletado");
                System.out.print("> ");
                opt = Integer.valueOf(s.nextLine());
                return emails.get(opt).getId();

            }
        }catch (SelectException e){
            System.err.println(e.getMessage());
        }
        return opt;
    }

    public void excluirEmail(){
        int id = formularioExcluirEmail();
        if(id != 0){
            try {
                sistema.excluirEmail(id);
            }catch (DeleteException e){
                System.err.println(e.getMessage());
            }
        }
    }
}
