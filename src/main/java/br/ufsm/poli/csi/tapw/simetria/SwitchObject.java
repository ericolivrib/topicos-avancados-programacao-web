package br.ufsm.poli.csi.tapw.simetria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKey;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SwitchObject implements Serializable {

    private byte[] cipherText;
    private String nomeArquivo;
    private SecretKey chave;

}
