package br.ufsm.poli.csi.tapw.criptografia.integridade_sessao;

import lombok.Builder;
import lombok.Getter;

import java.security.PublicKey;

@Getter
@Builder
public class Confidencial {

    String nomeArquivo;
    byte[] conteudo;
    byte[] conteudoCifrado;
    byte[] assinatura;
    byte[] chaveSessao;
    PublicKey chavePublica;

}
