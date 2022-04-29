package br.org.flem.sav.dao;

import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.hibernate.dao.base.BaseDAOAb;
import br.org.flem.sav.negocio.Gasto;
import br.org.flem.sav.negocio.PrestacaoContas;
import java.util.Collection;

/**
 *
 * @author ILFernandes
 */

public class GastoDAO extends BaseDAOAb<Gasto>{

    public GastoDAO() throws AcessoDadosException{
    }

    @Override
    protected Class<Gasto> getClasseDto() {
        return Gasto.class;
    }

    public Collection<Gasto> obterPorPrestacaoConta(PrestacaoContas prestacaoContas) {
        return session.createQuery("from Gasto g where g.prestacaoContas = :prestacaoContas").setEntity("prestacaoContas", prestacaoContas).list();
    }
}
