package br.ufsm.poli.csi.tapw.criptografia.integridade;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;

public class Alice {

    @SneakyThrows
    public static void main(String[] args) throws Exception {
        JFileChooser fileChooser = new JFileChooser("C:\\Users\\aluno");
        System.out.println("Selecionando arquivo...");

        if (fileChooser.showDialog(new JFrame(), "OK") == JFileChooser.APPROVE_OPTION) {
            /* Seleciona um arquivo */
            File arquivo = fileChooser.getSelectedFile();

            FileInputStream fin = new FileInputStream(arquivo);
            byte[] byteArray = new byte[(int) fin.getChannel().size()];
            fin.read(byteArray);

            /* Gerando par de chaves */
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            /* Gerando hash e assinatura */
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(byteArray);

            /* Criptografa a chave simétrica com a chave pública */
            Cipher rsa = Cipher.getInstance("RSA");
            rsa.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
            byte[] assinatura = rsa.doFinal(hash);

            /* Abre uma conexão */
            System.out.println("Enviando arquivo e assinatura...");
            Socket socket = new Socket("localhost", 5555);

            /* Conteúdo é transferido para o Bob */
            SwitchObject objetoTroca = SwitchObject.builder()
                    .nomeArquivo(arquivo.getName())
                    .textoPlano(byteArray)
                    .assinatura(assinatura)
                    .chavePublica(keyPair.getPublic())
                    .build();

            ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
            oout.writeObject(objetoTroca);

            oout.close();
            socket.close();

            System.out.println("Fim!");
        }
    }

}
