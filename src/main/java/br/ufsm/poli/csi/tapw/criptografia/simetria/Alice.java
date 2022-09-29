package br.ufsm.poli.csi.tapw.criptografia.simetria;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Alice {

    public static void main(String[] args) throws Exception {
        JFileChooser fileChooser = new JFileChooser("");
        System.out.println("Selecionando arquivo...");

        if (fileChooser.showDialog(new JFrame(), "OK") == JFileChooser.APPROVE_OPTION) {
            File arquivo = fileChooser.getSelectedFile();
            try (FileInputStream fin = new FileInputStream(arquivo)) {
                byte[] byteArray = new byte[(int) fin.getChannel().size()];

                System.out.println(fin.read(byteArray));
                System.out.println("Arquivo selecionado e lido...");

                Cipher aes = Cipher.getInstance("AES");
                SecretKey chave = KeyGenerator.getInstance("AES").generateKey();

                aes.init(Cipher.ENCRYPT_MODE, chave);

                byte[] cifrado = aes.doFinal(byteArray);

                try (Socket socket = new Socket("localhost", 5555); ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream())) {
                    SwitchObject objetoTroca = SwitchObject.builder().chave(chave).nomeArquivo(arquivo.getName()).cipherText(cifrado).build();
                    oout.writeObject(objetoTroca);
                }
            }


        }
    }

}
