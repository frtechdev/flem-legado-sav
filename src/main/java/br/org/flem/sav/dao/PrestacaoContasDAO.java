package br.org.flem.sav.dao; 

import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.hibernate.dao.base.BaseDAOAb;
import br.org.flem.sav.negocio.PrestacaoContas;

/**
 *
 * @author mccosta
 */
public class PrestacaoContasDAO extends BaseDAOAb <PrestacaoContas>{
    
    public PrestacaoContasDAO() throws AcessoDadosException{
    }

    @Override
    protected Class<PrestacaoContas> getClasseDto() {
        return PrestacaoContas.class;
    }
}