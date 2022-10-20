package br.ufsm.poli.csi.tapw.criptografia.criptomoeda;

import br.ufsm.poli.csi.tapw.criptografia.criptomoeda.model.BitCoin;
import br.ufsm.poli.csi.tapw.criptografia.criptomoeda.model.Creator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Mining {
    private static final BigInteger DIFICULDADE = new BigInteger(geraF(1000), 16);

    @Getter
    @AllArgsConstructor
    enum Arquivo {
        PUBLIC_KEY(new File("src/main/resources/mineracao/public-key.txt")),
        PRIVATE_KEY(new File("src/main/resources/mineracao/private-key.txt"));
        private final File arquivo;
    }

    private static String geraF(int numF) {
        /*
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numF; i++) {
            sb.append("F");
            System.out.println(sb);
        }
        return sb.toString();
         */

        return "1".repeat(Math.max(0, numF));
    }

    @SneakyThrows
    public static void main(String[] args) {
        new Thread(new Minerador()).start();
    }

    @SneakyThrows
    private static byte[] getKey(Arquivo arquivo) {
        FileReader fileReader = new FileReader(arquivo.getArquivo());
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String keyString = bufferedReader.readLine();

        fileReader.close();
        bufferedReader.close();

        String[] bytes = keyString.split(", ");
        byte[] keyByte = new byte[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            int b = Integer.parseInt(bytes[i]);
            keyByte[i] = (byte) b;
        }

        return keyByte;
    }

    private static class Minerador implements Runnable {

        Creator creator = Creator.builder()
                .id("erico")
                .key(getKey(Arquivo.PUBLIC_KEY))
                .build();

        int numTentativas = 0;
        BigInteger numHash;

        @SneakyThrows
        @Override
        public void run() {
            System.out.println("Minerando criptomoeda...");

            do {
                Random rnd = new SecureRandom();
                BigInteger magicNumber = new BigInteger(128, rnd).abs();

                BitCoin coin = BitCoin.builder()
                        .creationDate(new Date())
                        .creator(creator)
                        .magicNumber(magicNumber)
                        .build();

                String jsonCoin = new ObjectMapper().writeValueAsString(coin);

                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(jsonCoin.getBytes(StandardCharsets.UTF_8));
                numHash = new BigInteger(hash).abs();

                numTentativas++;
            } while (numHash.compareTo(DIFICULDADE) > 0);

            System.out.println("Moeda encontrada em " + numTentativas + " tentativas!");
            System.out.println("Hash: " + numHash);

            salvarCriptoMoeda(numHash);
        }

        private void salvarCriptoMoeda(BigInteger criptoMoeda) {
        }

    }

}
