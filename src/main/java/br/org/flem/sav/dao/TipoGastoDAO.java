package br.org.flem.sav.dao;

import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.hibernate.dao.base.BaseDAOAb;
import br.org.flem.sav.negocio.TipoGasto;
import java.util.Collection;


/**
 *
 * @author ILFernandes
 */

public class TipoGastoDAO extends BaseDAOAb<TipoGasto>{

    public TipoGastoDAO() throws AcessoDadosException{
    }

    @Override
    protected Class<TipoGasto> getClasseDto() {
        return TipoGasto.class;
    }

    public Collection<TipoGasto> obterTodosAtivos() {
        return session.createQuery("from TipoGasto where ativo = true").list();
    }
}
