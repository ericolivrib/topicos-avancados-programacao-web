package br.ufsm.poli.csi.tapw.criptografia.sessao;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class Bob {

    @SneakyThrows
    public static void main(String[] args) {
        try (ServerSocket ss = new ServerSocket(5555)) {
            while (true) {
                /* Gera um par de chaves */
                System.out.println("Gerando par de chaves...");
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(4096);
                KeyPair keyPair = keyPairGenerator.generateKeyPair();

                System.out.println("Aguardando conexão na porta 5555...");
                Socket socket = ss.accept();

                System.out.println("Conexão recebida!");

                /* Envia a chave pública */
                ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
                oout.writeObject(keyPair.getPublic());
                oout.flush();

                /* Recebe um arquivo cifrado da Alice */
                ObjectInputStream oin = new ObjectInputStream(socket.getInputStream());

                System.out.println("Recebendo arquivo...");
                SwitchObject objetoTroca = (SwitchObject) oin.readObject();

                System.out.println("Arquivo recebido!");

                Cipher rsa = Cipher.getInstance("RSA");
                rsa.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());

                byte[] chaveDecifrada = rsa.doFinal(objetoTroca.getChaveSessao());
                SecretKey chaveSessao = new SecretKeySpec(chaveDecifrada, "AES");

                Cipher aes = Cipher.getInstance("AES");
                aes.init(Cipher.DECRYPT_MODE, chaveSessao);

                FileOutputStream fout = new FileOutputStream(objetoTroca.getNomeArquivo());
                FileOutputStream foutCifrado = new FileOutputStream(objetoTroca.getNomeArquivo() + ".txt");

                System.out.println("Decriptando arquivo...");
                byte[] arquivoDecifrado = aes.doFinal(objetoTroca.getTextoCifrado());

                System.out.println("Salvando arquivo...");
                foutCifrado.write(objetoTroca.getTextoCifrado());
                fout.write(arquivoDecifrado);

                fout.write(arquivoDecifrado);
                fout.close();
                socket.close();

                System.out.println("Fim da conexão.");
            }
        }
    }

}
