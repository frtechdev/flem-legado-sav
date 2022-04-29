package br.org.flem.sav.bo; 

import br.org.flem.fwe.bo.BaseBOAb;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fw.service.IFuncionario;
import br.org.flem.sav.negocio.LiberacaoViagem;
import br.org.flem.sav.dao.LiberacaoViagemDAO;
import br.org.flem.sav.negocio.TipoLiberacao;
import br.org.flem.sav.negocio.Viagem;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author mccosta
 */
public class LiberacaoViagemBO extends BaseBOAb<LiberacaoViagem> {

    public LiberacaoViagemBO() throws AplicacaoException{
        super(new LiberacaoViagemDAO());
    }

    public LiberacaoViagem obterByViagemTipo(Viagem viagem, TipoLiberacao tipoLiberacao) {
        return ((LiberacaoViagemDAO) dao).obterByViagemTipo(viagem, tipoLiberacao);
    }

    public LiberacaoViagem obterByIFuncionario(IFuncionario iFuncionario) {
        return ((LiberacaoViagemDAO) dao).obterByIFuncionario(iFuncionario);
    }

    public LiberacaoViagem obterLimite50ByIFuncionario(IFuncionario iFuncionario) {
        return ((LiberacaoViagemDAO) dao).obterLimite50ByIFuncionario(iFuncionario);
    }

    public LiberacaoViagem obterByCodigoConsultor(String codigo) {
        return ((LiberacaoViagemDAO) dao).obterByCodigoConsultor(codigo);
    }

    public LiberacaoViagem obterByIFuncionarioTipo(IFuncionario iFuncionario, TipoLiberacao tipoLiberacao) {
        return ((LiberacaoViagemDAO) dao).obterByIFuncionarioTipo(iFuncionario, tipoLiberacao);
    }

    public LiberacaoViagem obterByCodigoConsultorTipo(String codigo, TipoLiberacao tipoLiberacao) {
        return ((LiberacaoViagemDAO) dao).obterByCodigoConsultorTipo(codigo, tipoLiberacao);
    }

    public Map<Integer,LiberacaoViagem> obterTodosValido() {
        return ((LiberacaoViagemDAO) dao).obterTodosValido();
    }

    public Map<Integer,LiberacaoViagem> obterTodosValidoByTipo(TipoLiberacao tipoLiberacao) {
        return ((LiberacaoViagemDAO) dao).obterTodosValidoByTipo(tipoLiberacao);
    }

    public Collection<LiberacaoViagem> obterTodosByTipo(TipoLiberacao tipoLiberacao) {
        return ((LiberacaoViagemDAO) dao).obterTodosByTipo(tipoLiberacao);
    }
}