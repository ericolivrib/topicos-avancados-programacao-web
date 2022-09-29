package br.ufsm.poli.csi.tapw.assimetria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.security.PublicKey;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SwitchObject implements Serializable {

    private byte[] textoCifrado;
    private String nomeArquivo;
    // private SecretKey chaveSecreta;
    private PublicKey chavePublica;

}
