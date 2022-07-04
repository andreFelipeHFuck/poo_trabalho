package dados;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Email {
    private int id;
    private String titulo;
    private String corpo;
    private String data;

    private String hora;
    private Usuario remetente;
    private Usuario destinatario;

    private int idEmailRespondido = 0;

    private boolean emailRespondido = false;


    public Email(){
        Date d = new Date();

        SimpleDateFormat dia = new SimpleDateFormat("d");
        data = dia.format(d) + "/";

        SimpleDateFormat mes = new SimpleDateFormat("M");
        data += mes.format(d) + "/";

        SimpleDateFormat ano = new SimpleDateFormat("y");
        data += ano.format(d);

        SimpleDateFormat h = new SimpleDateFormat("H");
        hora = h.format(d) + ":";

        SimpleDateFormat m = new SimpleDateFormat("m");
        hora += m.format(d);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Usuario getRemetente() {
        return remetente;
    }

    public void setRemetente(Usuario remetente) {
        this.remetente = remetente;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    public void setEmailRespondido(boolean emailRespondido) {
        this.emailRespondido = emailRespondido;
    }

    public boolean isEmailRespondido() {
        return emailRespondido;
    }

    public int getIdEmailRespondido() {
        return idEmailRespondido;
    }

    public void setIdEmailRespondido(int idEmailRespondido) {
        this.idEmailRespondido = idEmailRespondido;
    }

    public String toString(){
        return "- " + remetente.getNome() + " | " +  titulo + " | " + data + " " + hora;
    }
}
