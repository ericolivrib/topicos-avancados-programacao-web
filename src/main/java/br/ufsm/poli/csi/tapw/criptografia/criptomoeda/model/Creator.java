package br.ufsm.poli.csi.tapw.criptografia.criptomoeda.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Creator {

    private String id;
    private byte[] key;

}
