package dados;

import exception.LoginErroException;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String senha;

    public Usuario(){}
    public Usuario(int id, String nome, String email, String senha){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public boolean equals(Object obj) {
        Usuario u = (Usuario) (obj);

        if(this.getEmail().equals(u.getEmail()) && this.getSenha().equals(u.getSenha())){
            return true;
        }
        return false;
    }

    public String toString(){
        return "Nome: " + nome + ", Email: " + email;
    }
}
