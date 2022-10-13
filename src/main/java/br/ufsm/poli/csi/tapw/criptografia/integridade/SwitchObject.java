package br.ufsm.poli.csi.tapw.criptografia.integridade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.security.PublicKey;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SwitchObject implements Serializable {

    private byte[] textoCifrado;
    private String nomeArquivo;
    private PublicKey chavePublica;
    private byte[] assinatura;
    private byte[] textoPlano;

}
