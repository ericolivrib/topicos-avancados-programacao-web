package br.ufsm.poli.csi.tapw.simetria;

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

            FileInputStream fin = new FileInputStream(arquivo);

            byte[] byteArray = new byte[(int) fin.getChannel().size()];

            fin.read(byteArray);

            System.out.println("Arquivo selecionado e lido...");

            Cipher aes = Cipher.getInstance("AES");

            SecretKey chave = KeyGenerator.getInstance("AES").generateKey();

            aes.init(Cipher.ENCRYPT_MODE, chave);

            byte[] cifrado = aes.doFinal(byteArray);

            Socket socket = new Socket("localhost", 5555);

            SwitchObject objetoTroca = SwitchObject.builder().chave(chave).cipherText(cifrado).build();

            ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
            oout.writeObject(objetoTroca);
            oout.close();
            socket.close();
        }
    }

}
