package br.ufsm.poli.csi.tapw.criptografia.criptomoeda.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Creator {

    private final String id;
    private final byte[] key;

}
