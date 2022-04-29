package br.org.flem.sav.bo; 

import br.org.flem.fwe.bo.BaseBOAb;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.sav.dao.EstadoDAO;
import br.org.flem.sav.negocio.util.Estado;

/**
 *
 * @author mccosta
 */
public class EstadoBO extends BaseBOAb<Estado> {
    
    public EstadoBO() throws AplicacaoException{
        super(new EstadoDAO());
    }

    public Estado obterPorSigla(String sigla) {
        return ((EstadoDAO) dao).obterPorSigla(sigla);
    }
}
