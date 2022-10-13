package br.ufsm.poli.csi.tapw.criptografia.integridade;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class Bob {

    @SneakyThrows
    public static void main(String[] args) {
        try (ServerSocket ss = new ServerSocket(5555)) {
            while (true) {
                /* Gera um par de chaves */
                System.out.println("Gerando par de chaves...");
                System.out.println("Aguardando conexão na porta 5555...");
                Socket socket = ss.accept();

                System.out.println("Conexão recebida!");

                /* Recebe um arquivo cifrado da Alice */
                ObjectInputStream oin = new ObjectInputStream(socket.getInputStream());

                System.out.println("Recebendo arquivo...");
                SwitchObject objetoTroca = (SwitchObject) oin.readObject();

                System.out.println("Arquivo recebido!");

                Cipher rsa = Cipher.getInstance("RSA");
                rsa.init(Cipher.DECRYPT_MODE, objetoTroca.getChavePublica());

                MessageDigest md = MessageDigest.getInstance("SHA-256");

                byte[] hashAssinatura = rsa.doFinal(objetoTroca.getAssinatura());
                System.out.println("Hash assinatura: " + Base64.getEncoder().encodeToString(hashAssinatura));

                byte[] hashGerado = md.digest(objetoTroca.getTextoPlano());
                System.out.println("Hash gerado: " + Base64.getEncoder().encodeToString(hashGerado));

                if (!(Arrays.equals(hashAssinatura, hashGerado))) {
                    System.out.println("Assinatura inválida! O arquivo possivelmente não está íntegro.");
                    socket.close();
                    continue;
                }

                /* Escreve o arquivo */
                FileOutputStream fout = new FileOutputStream(objetoTroca.getNomeArquivo());
                System.out.println("Salvando arquivo...");
                fout.write(objetoTroca.getTextoPlano());

                fout.close();
                socket.close();

                System.out.println("Fim da conexão.");
            }
        }
    }

}
