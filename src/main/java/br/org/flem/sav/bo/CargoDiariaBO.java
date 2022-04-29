package br.org.flem.sav.bo; 

import br.org.flem.fwe.bo.BaseBOAb;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.sav.dao.CargoDiariaDAO;
import br.org.flem.sav.negocio.CargoDiaria;
import java.util.Collection;

/**
 *
 * @author mccosta
 */
public class CargoDiariaBO extends BaseBOAb<CargoDiaria> {

    public CargoDiariaBO() throws AplicacaoException {
        super(new CargoDiariaDAO());
    }

    public Collection<CargoDiaria> obterPorDescricao(String descricao) {
        return ((CargoDiariaDAO) dao).obterPorDescricao(descricao);
    }

    public CargoDiaria obterCargoDiariaConsultor() {
        return ((CargoDiariaDAO) dao).obterCargoDiariaConsultor();
    }

    public CargoDiaria obterPorCodigo(Integer codigo) {
        return ((CargoDiariaDAO) dao).obterPorCargoDominio(codigo);
    }

    public Collection<CargoDiaria> obterTodosOrdenado() {
        return ((CargoDiariaDAO)this.dao).obterTodosOrdenado();
    }

}