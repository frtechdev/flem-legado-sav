package br.org.flem.sav.bo;

import br.org.flem.fw.persistencia.dto.Usuario;
import br.org.flem.fw.service.IFuncionario;
import br.org.flem.fw.service.impl.RHServico;
import br.org.flem.sav.negocio.ContaCorrenteViagem;
import br.org.flem.sav.regras.SolicitarViagemFlemStrategy;
import br.org.flem.sav.regras.SolicitarViagemSeturStrategy;
import br.org.flem.sav.regras.SolicitarViagemTrilhaSecStrategy;
import java.util.ArrayList;

/**
 *
 * @author mccosta
 */
public class SolicitarViagemBO {

    public ArrayList<String> funcionarioPodeViajar(Usuario usuario){
        ArrayList<String> resposta = new ArrayList<String>();
        IFuncionario iFunc = new RHServico().obterFuncionarioPorMatricula(usuario.getCodigoDominio());
        /**
        try{
            ContaCorrenteViagem conta = new ContaCorrenteViagemBO().obterPorMatriculaFuncionario(usuario.getCodigoDominio().toString());
            if(conta == null){
                System.out.println("usuário: " + usuario);
                System.out.println("código domínio: " + usuario.getCodigoDominio());
                System.out.println("conta: " + conta);
                resposta.add("false");
                StringBuilder str = new StringBuilder("O Funcionário ");
                str.append(iFunc.getNome().replace("  ", ""));
                str.append(" não pode solicitar uma viagem porque está com pendência da conta corrente no sistema.");
                resposta.add(str.toString());
                resposta.add("conta");
                return resposta;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        */
        switch (usuario.getTipoSolicitacao()) {
            case FLEM:
                resposta = new SolicitarViagemFlemStrategy().funcionarioPodeViajar(iFunc);
            break;

            case TRILHA_SEC:
                resposta = new SolicitarViagemTrilhaSecStrategy().funcionarioPodeViajar(iFunc);
            break;
                
            case SETUR:
                resposta = new SolicitarViagemSeturStrategy().funcionarioPodeViajar(iFunc);
            break;
        }

        return resposta;
    }
}
