package br.ufsm.poli.csi.tapw.criptografia.diffiehellman;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class Diffie {

    public static final int NUM_BITS = 128;
    private static final Random RANDOM = new SecureRandom();

    public static void main(String[] args) {
        Diffie diffie = new Diffie();
        Hellman hellman = new Hellman();
        diffie.criarChaveCompartilhada(hellman);
    }

    private void criarChaveCompartilhada(Hellman hellman) {
        BigInteger[] qa = Util.geraQA(NUM_BITS);
        BigInteger q = qa[0], a = qa[1];

        BigInteger x = escolherChavePrivada(q, RANDOM);
        BigInteger y = a.modPow(x, q);

        BigInteger yHellman = hellman.criarChaveCompartilhada(q, a, y);
        BigInteger k = yHellman.modPow(x, q);

        byte[] kArr = k.toByteArray();

        String secretKey = Base64.getEncoder().encodeToString(kArr);

        System.out.println("Diffie KEY: " + secretKey);
    }

    public static BigInteger escolherChavePrivada(BigInteger q, Random rnd) {
        BigInteger n = new BigInteger(NUM_BITS, rnd);

        while (n.compareTo(q) >= 0) {
            n = new BigInteger(NUM_BITS, rnd);
        }

        return n;
    }

}
