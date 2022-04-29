package br.org.flem.sav.bo; 

import br.org.flem.fwe.bo.BaseBOAb;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.sav.negocio.Companhia;
import br.org.flem.sav.dao.CompanhiaDAO;

/**
 *
 * @author mccosta
 */
public class CompanhiaBO extends BaseBOAb<Companhia> {
    
    public CompanhiaBO() throws AplicacaoException{
        super(new CompanhiaDAO());
    }
}
