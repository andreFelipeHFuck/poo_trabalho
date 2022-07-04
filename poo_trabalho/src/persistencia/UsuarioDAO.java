package persistencia;

import exception.InsertException;
import exception.SelectException;
import exception.UpdateException;
import exception.DeleteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dados.Usuario;

import java.util.List;
import java.util.ArrayList;

public class UsuarioDAO {
    private static UsuarioDAO instancia = null;
    private PreparedStatement selectNewID;
    private PreparedStatement select;
    private PreparedStatement selectLogin;

    private PreparedStatement selectEmail;
    private PreparedStatement insert;
    private PreparedStatement update;

    public static UsuarioDAO getInstance() throws ClassNotFoundException, SQLException, SelectException{
        if(instancia == null){
            instancia = new UsuarioDAO();
        }
        return instancia;
    }

    private UsuarioDAO() throws ClassNotFoundException, SQLException, SelectException{
        Connection conexao = Conexao.getConexao();
        selectNewID = conexao.prepareStatement("select nextval('id_usuario')");
        insert = conexao.prepareStatement("insert into usuario values(?,?,?,?)");
        select = conexao.prepareStatement("select * from usuario where id = ?");
        selectLogin = conexao.prepareStatement("select id from usuario where email = ? and senha = ?");
        selectEmail = conexao.prepareStatement("select * from usuario where email = ?");
        update = conexao.prepareStatement("update usuario set nome = ?, senha = ?, email = ? where id = ?");
    }

    private int selectNewID() throws SelectException{
        try{
            ResultSet rs = selectNewID.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        }catch (SQLException e){
            throw new SelectException("Erro ao gerar id para usuario");
        }
        return 0;
    }

    public void insert(Usuario usuario){
        try{
            insert.setInt(1, selectNewID());
            insert.setString(2, usuario.getNome());
            insert.setString(3, usuario.getSenha());
            insert.setString(4, usuario.getEmail());
            insert.executeUpdate();
        }catch (SelectException e){
            System.err.println(e.getMessage());
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }

    public Usuario select(int usuario) throws SelectException{
        try{
            select.setInt(1, usuario);
            ResultSet rs = select.executeQuery();
            if(rs.next()){
                int id = rs.getInt(1);
                String nome = rs.getString(2);
                String senha = rs.getString(3);
                String email = rs.getString(4);
                return new Usuario(id, nome, email, senha);
            }
        }catch (SQLException e){
            throw new SelectException("Erro na busca do Usuario");
        }
        return null;
    }

    public int selectLogin(String email, String senha) throws SelectException{
        try{
            selectLogin.setString(1, email);
            selectLogin.setString(2, senha);
            ResultSet rs = selectLogin.executeQuery();
            if(rs.next()){
                int id = rs.getInt(1);
                return id;
            }
        }catch (SQLException e){
            throw new SelectException("Erro ao realizar o login");
        }
        return 0;
    }

    public int selectEmail(String email) throws SelectException{
        try{
            selectEmail.setString(1, email);
            ResultSet rs = selectEmail.executeQuery();

            if (rs.next()){
                int id = rs.getInt(1);
                return id;
            }
        }catch (SQLException e){
            throw new SelectException("Erro na busca dos emails");
        }
        return 0;
    }

    public void update(Usuario usuario) throws UpdateException{
        try{
            update.setString(1, usuario.getNome());
            update.setString(2, usuario.getSenha());
            update.setString(3, usuario.getEmail());
            update.setInt(4, usuario.getId());
        }catch (SQLException e){
            throw new UpdateException("Erro ao atualizar o usuario");
        }
    }
}
