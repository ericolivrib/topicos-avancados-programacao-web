package br.ufsm.poli.csi.tapw.assimetria;

import javax.crypto.Cipher;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PublicKey;

public class Alice {

    public static void main(String[] args) throws Exception {
        JFileChooser fileChooser = new JFileChooser("");
        System.out.println("Selecionando arquivo...");

        if (fileChooser.showDialog(new JFrame(), "OK") == JFileChooser.APPROVE_OPTION) {
            /* Seleciona um arquivo */
            File arquivo = fileChooser.getSelectedFile();

            try (FileInputStream fin = new FileInputStream(arquivo)) {
                byte[] byteArray = new byte[(int) fin.getChannel().size()];

                // System.out.println(fin.read(byteArray));
                System.out.println("Arquivo selecionado e lido...");

                Cipher rsa = Cipher.getInstance("RSA");
                // SecretKey chave = KeyGenerator.getInstance("RSA").generateKey();

                /* Abre uma conexão */
                Socket socket = new Socket("localhost", 5555);

                /* Recebe a chave pública do Bob */
                ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream oin = new ObjectInputStream(socket.getInputStream());

                PublicKey chavePublica = (PublicKey) oin.readObject();

                /* Criptografa a chave do Bob */
                rsa.init(Cipher.ENCRYPT_MODE, chavePublica);

                byte[] cifrado = rsa.doFinal(byteArray);

                SwitchObject objetoTroca = SwitchObject.builder()
                    // .chaveSecreta(chave)
                    // .chavePublica(chavePublica)
                    .nomeArquivo(arquivo.getName())
                    .textoCifrado(cifrado)
                    .build();
                oout.writeObject(objetoTroca);

                socket.close();
                oout.close();
            }


        }
    }

}
