package br.org.flem.sav.bo; 

import br.org.flem.fwe.bo.BaseBOAb;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.sav.dao.ItemItinerarioDAO;
import br.org.flem.sav.negocio.ItemItinerario;
import br.org.flem.sav.negocio.Itinerario;
import java.util.Collection;

/**
 *
 * @author mccosta
 */
public class ItemItinerarioBO extends BaseBOAb<ItemItinerario> {

    public ItemItinerarioBO() throws AplicacaoException{
        super(new ItemItinerarioDAO());
    }

    public Collection<ItemItinerario> obterPorItinerario(Itinerario itinerario) {
        return ((ItemItinerarioDAO) dao).obterPorItinerario(itinerario);
    }
}