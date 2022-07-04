package negocio;

import dados.Email;
import dados.Usuario;

import persistencia.UsuarioDAO;
import persistencia.EmailDAO;

import exception.DestinatarioNaoEncontradoException;
import exception.LoginErroException;
import java.sql.SQLException;
import exception.SelectException;
import exception.InsertException;
import exception.UpdateException;
import exception.DeleteException;

import java.util.ArrayList;
import java.util.List;


public class Sistema {
    private UsuarioDAO usuarioDAO;
    private EmailDAO emailDAO;

    public Sistema() throws ClassNotFoundException, SQLException, SelectException{
        usuarioDAO = UsuarioDAO.getInstance();
        emailDAO = EmailDAO.getInstance();
    }

    public void login(Usuario usuario) throws SelectException, LoginErroException{
        int id_usuario = usuarioDAO.selectLogin(usuario.getEmail(), usuario.getSenha());
        Usuario u;

        if(id_usuario == 0){
            throw new LoginErroException();
        }else{
            u = usuarioDAO.select(id_usuario);
            usuario.setId(u.getId());
            usuario.setNome(u.getNome());
            usuario.setEmail(u.getEmail());
        }
    }

    public Email buscarEmail(int id_email) throws SelectException{
        Email email = emailDAO.select(id_email);
        return email;
    }

    public void buscaDestinatario(Usuario destinatario) throws DestinatarioNaoEncontradoException{
        try{
            int id = usuarioDAO.selectEmail(destinatario.getEmail());

            if(id == 0){
                throw new DestinatarioNaoEncontradoException();
            }else{
                destinatario.setId(id);
            }
        }catch (SelectException e){
            System.err.println(e.getMessage());
        }
    }

    public void cadastrarUsuario(Usuario usuario) throws  InsertException, SelectException{
        usuarioDAO.insert(usuario);
    }

    public void escreverEmail(Email email) throws UpdateException{
        int id =  emailDAO.insert(email);
        email.setId(id);
        int chave = chaveDeEncriptar(email);
        email.setCorpo(encriptar(email.getCorpo(), chave));
        emailDAO.update(email);
    }

    public void excluirEmail(int id_email) throws DeleteException{
       emailDAO.delete(id_email);
    }

    public List<Email> emailsUsuario(int id_usuario) throws SelectException{
        List<Email> emails = emailDAO.selectDestinatario(id_usuario);
        return emails;
    }

    public List<Email> emailsExcluir(int id_usuario) throws SelectException{
        List<Email> emails = emailDAO.selectRemetente(id_usuario);
        return emails;
    }

    public void responderEmail(Email emailResposta){
        emailDAO.insert(emailResposta);
    }

    public int chaveDeEncriptar(Email email){
        int dia = Integer.parseInt(email.getData().split("/")[0]);
        int id = ((email.getId()%100)%10);
        return dia + id;
    }

    public String encriptar(String corpo, int chave){
        StringBuilder textoCifrado = new StringBuilder();
        int tamanhoTexto = corpo.length();

        for(int i = 0; i <tamanhoTexto; i++){
            int letraASCII = ((int) corpo.charAt(i) + (chave - 1));

            while (letraASCII > 126){
                letraASCII -= 94;
            }

            textoCifrado.append((char) letraASCII);
        }
        return textoCifrado.toString();
    }

    public String decriptar(String corpo, int chave){
        StringBuilder texto = new StringBuilder();
        int tamanhoTexto = corpo.length();

        for(int i = 0; i < tamanhoTexto; i++){
            int letraASCII = ((int) corpo.charAt(i) - (chave - 1));

            while (letraASCII < 32){
                letraASCII += 94;
            }

            texto.append((char) letraASCII);
        }

        return texto.toString();
    }
}
