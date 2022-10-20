package br.ufsm.poli.csi.tapw.criptografia.criptomoeda.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.util.Date;

@Getter
@Builder
public class BitCoin {

    private final Creator creator;
    private final Date creationDate;
    private final byte[] signature;
    private BigInteger magicNumber;

}
