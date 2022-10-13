package br.ufsm.poli.csi.tapw.criptografia.integridade_sessao;

import lombok.SneakyThrows;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class Beltrano {

    @SneakyThrows
    public static void main(String[] args) {

        int porta = 5555;
        ServerSocket server = new ServerSocket(porta);

        /* Gerar par de chaves */
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair parChaves = kpg.generateKeyPair();

        System.out.println("Aguardando conexão com a porta " + porta + "...");
        Socket socket = server.accept();

        /* Enviar a chave pública para o Fulano */
        ObjectOutputStream infosEnviadas = new ObjectOutputStream(socket.getOutputStream());
        infosEnviadas.writeObject(parChaves.getPublic());

        /* Recebe o conteúdo cifrado e a assinatura enviados pelo Fulano */
        // TODO: Receber a assinatura
        ObjectInputStream infosRecebidas = new ObjectInputStream(socket.getInputStream());

    }

}
