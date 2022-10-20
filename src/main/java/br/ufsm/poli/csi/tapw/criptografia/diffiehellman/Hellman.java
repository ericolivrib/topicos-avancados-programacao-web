package br.ufsm.poli.csi.tapw.criptografia.diffiehellman;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class Hellman {

    private static final Random RANDOM = new SecureRandom();

    public BigInteger criarChaveCompartilhada(BigInteger q, BigInteger a, BigInteger yDiffie) {
        BigInteger x = Diffie.escolherChavePrivada(q, RANDOM);
        BigInteger y = a.modPow(x, q);

        // Gera a chave compartilhada
        BigInteger k = yDiffie.modPow(x, q);
        byte[] kArr = k.toByteArray();

        String secretKey = Base64.getEncoder().encodeToString(kArr);
        System.out.println("Hellman KEY: " + secretKey);

        return y;
    }

}
