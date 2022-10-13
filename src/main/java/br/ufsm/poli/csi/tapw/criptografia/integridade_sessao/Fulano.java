package br.ufsm.poli.csi.tapw.criptografia.integridade_sessao;

import lombok.SneakyThrows;

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

public class Fulano {

    @SneakyThrows
    public static void main(String[] args) {

        System.out.println("Selecionando arquivo...");
        JFileChooser fileChooser = new JFileChooser();

        if (fileChooser.showDialog(new JFrame(), "OK") == JFileChooser.APPROVE_OPTION) {
            File arquivo = fileChooser.getSelectedFile();
            FileInputStream file = new FileInputStream(arquivo);
            byte[] conteudo = new byte[(int) file.getChannel().size()];

            Socket socket = new Socket("localhost", 5555);

            /* Receber a chave pública do Beltrano */
            ObjectInputStream infosRecebidas = new ObjectInputStream(socket.getInputStream());
            PublicKey chavePublica = (PublicKey) infosRecebidas.readObject();

            // TODO: Gerar par de chaves

            /* Gerar chave simétrica */
            Cipher aes = Cipher.getInstance("AES");
            SecretKey chaveSimetrica = KeyGenerator.getInstance("AES").generateKey();

            /* Criptografar conteúdo com a chave simétrica */
            aes.init(Cipher.ENCRYPT_MODE, chaveSimetrica);
            byte[] conteudoCifrado = aes.doFinal(conteudo);

            /* Criptografar a chave simétrica com a pública */
            Cipher rsa = Cipher.getInstance("RSA");
            rsa.init(Cipher.ENCRYPT_MODE, chavePublica);
            byte[] chaveSessao = rsa.doFinal(chaveSimetrica.getEncoded());

            /* Enviar o conteúdo para o Beltrano */
            Confidencial infos = Confidencial.builder()
                    .nomeArquivo(arquivo.getName())
                    .conteudoCifrado(conteudoCifrado)
                    .chaveSessao(chaveSessao)
                    .build();

            ObjectOutputStream infosEnviadas = new ObjectOutputStream(socket.getOutputStream());
            infosEnviadas.writeObject(infos);

            infosEnviadas.close();
            socket.close();
        }

    }

}
