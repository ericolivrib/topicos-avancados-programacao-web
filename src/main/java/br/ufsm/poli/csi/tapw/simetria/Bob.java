package br.ufsm.poli.csi.tapw.simetria;

import lombok.SneakyThrows;

import javax.crypto.*;
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Bob {

    @SneakyThrows
    public static void main(String[] args) {
        try (ServerSocket ss = new ServerSocket(5555);) {
            // while (true) {
                System.out.println("Aguardando conexão na porta 5555...");

                Socket socket = ss.accept();
                System.out.println("Conexão recebida!");

                ObjectInputStream oin = new ObjectInputStream(socket.getInputStream());
                System.out.println("Recebendo arquivo...");

                SwitchObject objetoTroca = (SwitchObject) oin.readObject();
                System.out.println("Arquivo recebido!");

                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, objetoTroca.getChave());
                FileOutputStream fout = new FileOutputStream(objetoTroca.getNomeArquivo());
                FileOutputStream foutCifrado = new FileOutputStream(objetoTroca.getNomeArquivo() + "");
                System.out.println("Decriptando arquivo...");

                byte[] arquivoDecifrado = cipher.doFinal(objetoTroca.getCipherText());
                System.out.println("Salvando arquivo...");

                foutCifrado.write(objetoTroca.getCipherText());
                fout.write(arquivoDecifrado);

                fout.write(arquivoDecifrado);
                fout.close();
                socket.close();

                System.out.println("Fim da conexão.");
            // }
        }
    }

}
