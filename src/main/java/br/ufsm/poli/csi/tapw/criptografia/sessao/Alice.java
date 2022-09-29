package br.ufsm.poli.csi.tapw.criptografia.sessao;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PublicKey;

public class Alice {

    public static void main(String[] args) throws Exception {
        JFileChooser fileChooser = new JFileChooser(
                "C:\\Users\\aluno\\IdeaProjects\\topicos-avancados-programacao-web");
        System.out.println("Selecionando arquivo...");

        if (fileChooser.showDialog(new JFrame(), "OK") == JFileChooser.APPROVE_OPTION) {
            /* Seleciona um arquivo */
            File arquivo = fileChooser.getSelectedFile();

            try (FileInputStream fin = new FileInputStream(arquivo)) {
                byte[] byteArray = new byte[(int) fin.getChannel().size()];
                fin.read(byteArray);

                System.out.println("Arquivo selecionado e lido...");

                /* Abre uma conexão */
                Socket socket = new Socket("localhost", 5555);

                /* Recebe a chave pública do Bob */
                ObjectInputStream oin = new ObjectInputStream(socket.getInputStream());
                PublicKey chavePublica = (PublicKey) oin.readObject();

                /* Gera a chave simétrica */
                Cipher aes = Cipher.getInstance("AES");
                SecretKey chaveSimetrica = KeyGenerator.getInstance("AES").generateKey();

                /* Criptografa o conteúdo com a chave simétrica */
                aes.init(Cipher.ENCRYPT_MODE, chaveSimetrica);
                byte[] cifrado = aes.doFinal(byteArray);

                /* Criptografa a chave simétrica com a chave pública */
                Cipher rsa = Cipher.getInstance("RSA");
                rsa.init(Cipher.ENCRYPT_MODE, chavePublica);
                byte[] chaveSessao = rsa.doFinal(chaveSimetrica.getEncoded());

                /* Conteúdo é transferido para o Bob */
                SwitchObject objetoTroca = SwitchObject.builder()
                        .nomeArquivo(arquivo.getName())
                        .textoCifrado(cifrado)
                        .chaveSessao(chaveSessao)
                        .build();

                ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
                oout.writeObject(objetoTroca);

                socket.close();
                oout.close();
            }


        }
    }

}
