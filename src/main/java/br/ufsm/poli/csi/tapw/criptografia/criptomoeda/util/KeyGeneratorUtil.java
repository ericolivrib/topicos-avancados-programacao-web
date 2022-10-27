package br.ufsm.poli.csi.tapw.criptografia.criptomoeda.util;

import lombok.SneakyThrows;

import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Arrays;

public class KeyGeneratorUtil {

    public static final File FILE_ARCHIVE = new File("src/main/resources/mineracao/private-key.txt");

    @Deprecated
    @SneakyThrows
    public static void generateKeys() {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();

        FileReader fileReader = new FileReader(FILE_ARCHIVE);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        if (bufferedReader.readLine() == null) {
            FileOutputStream writer = new FileOutputStream(FILE_ARCHIVE);

            writer.write(kp.getPublic().getEncoded());

            writer.flush();
            writer.close();
           
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();

        System.out.println(Arrays.toString(kp.getPublic().getEncoded()));
        System.out.println(Arrays.toString(kp.getPrivate().getEncoded()));
    }

}
