package br.org.flem.sav.bo;

import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fwe.bo.BaseBOAb;
import br.org.flem.sav.negocio.Gasto;
import br.org.flem.sav.dao.GastoDAO;
import br.org.flem.sav.negocio.PrestacaoContas;
import java.util.Collection;


/**
 *
 * @author ILFernandes
 */

public class GastoBO extends BaseBOAb<Gasto>{

    public GastoBO() throws AplicacaoException{
        super(new GastoDAO());
    }

    public Collection<Gasto> obterPorPrestacaoConta(PrestacaoContas prestacaoContas) {
        return ((GastoDAO)this.dao).obterPorPrestacaoConta(prestacaoContas);
    }
}
