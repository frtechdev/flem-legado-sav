package br.org.flem.sav.bo; 

import br.org.flem.fwe.bo.BaseBOAb;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.sav.dao.ItinerarioDAO;
import br.org.flem.sav.negocio.Itinerario;

/**
 *
 * @author mccosta
 */
public class ItinerarioBO extends BaseBOAb<Itinerario> {

    public ItinerarioBO() throws AplicacaoException{
        super(new ItinerarioDAO());
    }
}
