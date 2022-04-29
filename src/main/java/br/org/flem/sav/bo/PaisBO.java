package br.org.flem.sav.bo; 

import br.org.flem.fwe.bo.BaseBOAb;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.sav.dao.PaisDAO;
import br.org.flem.sav.negocio.DestinoViagem;
import br.org.flem.sav.negocio.util.Estado;
import br.org.flem.sav.negocio.util.Pais;
import java.util.Collection;

/**
 *
 * @author mccosta
 */
public class PaisBO extends BaseBOAb<Pais> {
    
    public PaisBO() throws AplicacaoException{
        super(new PaisDAO());
    }

    public Pais obterPorSigla(String sigla) {
        return ((PaisDAO) dao).obterPorSigla(sigla);
    }
}
