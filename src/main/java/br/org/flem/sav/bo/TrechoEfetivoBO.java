package br.org.flem.sav.bo; 

import br.org.flem.fwe.bo.BaseBOAb;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.sav.dao.TrechoEfetivoDAO;
import br.org.flem.sav.negocio.PrestacaoContas;
import br.org.flem.sav.negocio.TrechoEfetivo;
import java.util.Collection;

/**
 *
 * @author mccosta
 */
public class TrechoEfetivoBO extends BaseBOAb<TrechoEfetivo>{

    public TrechoEfetivoBO() throws AplicacaoException{
        super (new TrechoEfetivoDAO());
    }

    public Collection<TrechoEfetivo> obterPorPrestacao(PrestacaoContas prestacaoContas) {
        return ((TrechoEfetivoDAO) dao).obterPorPrestacao(prestacaoContas);
    }
}
