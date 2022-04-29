package br.org.flem.sav.dao; 

import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.hibernate.dao.base.BaseDAOAb;
import br.org.flem.sav.negocio.Companhia;

/**
 *
 * @author mccosta
 */
public class CompanhiaDAO extends BaseDAOAb <Companhia>{

    public CompanhiaDAO() throws AcessoDadosException{
    }

    @Override
    protected Class<Companhia> getClasseDto() {
        return Companhia.class;
    }
}
