package apresentacao;

import negocio.Sistema;

import javax.print.CancelablePrintJob;
import java.util.Scanner;

public class CifraCesar {
    Sistema sistema;
    Scanner s = new Scanner(System.in);

    public static void main(String[] args) {
        CifraCesar m = new CifraCesar();
        System.out.println("Digite um texto: ");
        System.out.print("> ");
        String texto = m.s.nextLine();

        System.out.println("chave: ");
        System.out.print("> ");
        int chave = m.s.nextInt();

        String e = m.sistema.encriptar(texto, chave);

        System.out.println(e);

        System.out.println("Decodificando o texto: ");
        System.out.println(m.sistema.decriptar(e, chave));


    }
}
