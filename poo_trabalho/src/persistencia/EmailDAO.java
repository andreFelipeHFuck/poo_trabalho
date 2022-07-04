package persistencia;

import exception.InsertException;
import exception.SelectException;
import exception.UpdateException;
import exception.DeleteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import persistencia.UsuarioDAO;

import java.util.ArrayList;
import java.util.List;

import dados.Email;

public class EmailDAO {
    private static EmailDAO instancia = null;
    private PreparedStatement selectNewId;
    private PreparedStatement select;
    private PreparedStatement selectDestinatario;
    private PreparedStatement selectRemetente;
    private PreparedStatement insert;
    private PreparedStatement delete;
    private PreparedStatement update;

    private UsuarioDAO usuarioDAO;

    public static EmailDAO getInstance() throws ClassNotFoundException, SQLException, SelectException{
        if(instancia == null){
            instancia = new EmailDAO();
        }
        return instancia;
    }

    private EmailDAO() throws ClassNotFoundException, SQLException, SelectException{
        usuarioDAO = UsuarioDAO.getInstance();
        Connection conexao = Conexao.getConexao();
        selectNewId = conexao.prepareStatement("select nextval('id_email')");
        select = conexao.prepareStatement("select * from email where id = ?");
        insert = conexao.prepareStatement("insert into email " +
                                             "(id, id_remetente, id_destinatario, titulo," +
                                             " data, hora, corpo, emailRespondido)" +
                                             "values (?,?,?,?,?,?,?,?)");
        selectDestinatario = conexao.prepareStatement("select * from email where id_destinatario = ?");
        selectRemetente = conexao.prepareStatement("select * from email where id_remetente = ?");
        delete = conexao.prepareStatement("delete from email where id = ?");
        update = conexao.prepareStatement("update email set corpo = ? where id = ?");
    }

    private int selectNewId() throws SelectException{
        try{
            ResultSet rs = selectNewId.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        }catch (SQLException e){
            throw new SelectException("Erro ao gerar um novo id na tabela email");
        }
        return 0;
    }

    public int insert(Email email){
        try{
            int id = selectNewId();
            insert.setInt(1, id);
            insert.setInt(2, email.getRemetente().getId());
            insert.setInt(3, email.getDestinatario().getId());
            insert.setString(4, email.getTitulo());
            insert.setString(5, email.getData());
            insert.setString(6, email.getHora());
            insert.setString(7, email.getCorpo());
            insert.setBoolean(8, email.isEmailRespondido());
            insert.executeUpdate();
            return id;
        }catch (SelectException e){
            System.err.println(e.getMessage());
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
        return 0;
    }

    public Email select(int id_email) throws SelectException{
        try{
            Email email = new Email();
            select.setInt(1, id_email);
            ResultSet rs = select.executeQuery();
            if(rs.next()){
                email.setId(rs.getInt(1));
                email.setTitulo(rs.getString(2));
                email.setData(rs.getString(3));
                email.setEmailRespondido( rs.getBoolean(4));
                email.setIdEmailRespondido(rs.getInt(5));
                email.setRemetente(usuarioDAO.select(rs.getInt(6)));
                email.setDestinatario(usuarioDAO.select(rs.getInt(7)));
                email.setHora(rs.getString(8));
                email.setCorpo(rs.getString(9));
                return email;
            }
        }catch (SQLException e){
            throw new SelectException("Não foi possível buscar o email");
        }
        return null;
    }

    public List<Email> selectDestinatario(int destinatario) throws SelectException{
        try{
            Email email;
            List<Email> emails = new ArrayList<Email>();
            selectDestinatario.setInt(1, destinatario);
            ResultSet rs = selectDestinatario.executeQuery();
            while (rs.next()){
                email = new Email();
                email.setId(rs.getInt(1));
                email.setTitulo(rs.getString(2));
                email.setData(rs.getString(3));
                email.setEmailRespondido( rs.getBoolean(4));
                email.setIdEmailRespondido(rs.getInt(5));
                email.setRemetente(usuarioDAO.select(rs.getInt(6)));
                email.setDestinatario(usuarioDAO.select(rs.getInt(7)));
                email.setHora(rs.getString(8));
                email.setCorpo(rs.getString(9));
                emails.add(email);
            }
            return emails;
        }catch (SQLException e){
            throw new SelectException("Não foi possivel buscar os seus emails");
        }
    }

    public List<Email> selectRemetente(int remetente) throws SelectException {
        try{
            Email email;
            List<Email> emails = new ArrayList<Email>();
            selectRemetente.setInt(1, remetente);
            ResultSet rs = selectRemetente.executeQuery();
            while (rs.next()){
                email = new Email();
                email.setId(rs.getInt(1));
                email.setTitulo(rs.getString(2));
                email.setData(rs.getString(3));
                email.setEmailRespondido( rs.getBoolean(4));
                email.setIdEmailRespondido(rs.getInt(5));
                email.setRemetente(usuarioDAO.select(rs.getInt(6)));
                email.setDestinatario(usuarioDAO.select(rs.getInt(7)));
                email.setHora(rs.getString(8));
                email.setCorpo(rs.getString(9));
                emails.add(email);
            }
            return emails;
        }catch (SQLException e){
            throw new SelectException("Não foi possivel buscar os seus emails");
        }
    }

    public void delete(int id_email) throws DeleteException{
        try{
            delete.setInt(1, id_email);
            delete.executeUpdate();
        }catch (SQLException e){
            throw new DeleteException("Não foi possível deletar o email");
        }
    }

    public void update(Email email) throws UpdateException{
        try{
            update.setString(1, email.getCorpo());
            update.setInt(2, email.getId());
            update.executeUpdate();
        }catch (SQLException e){
            throw new UpdateException("Não foi possivel encriptar o corpo do texto");
        }
    }

}
