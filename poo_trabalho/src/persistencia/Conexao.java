package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static Connection conexao = null;

    private Conexao(){}

    public static Connection getConexao() throws ClassNotFoundException, SQLException{
        try {
            if(conexao == null){
                String url = "jdbc:postgresql://localhost:5432/poo_trabalho";
                String usuario = "postgres";
                Class.forName("org.postgresql.Driver");
                conexao = DriverManager.getConnection(url, usuario, "root");
            }
        }catch (ClassNotFoundException e){
            System.err.println(e.getMessage());
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
        return conexao;
    }
}
