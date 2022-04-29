package br.org.flem.sav.dao; 

import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.hibernate.dao.base.BaseDAOAb;
import br.org.flem.sav.negocio.Itinerario;

/**
 *
 * @author mccosta
 */
public class ItinerarioDAO extends BaseDAOAb <Itinerario>{

    public ItinerarioDAO() throws AcessoDadosException{
    }

    @Override
    protected Class<Itinerario> getClasseDto() {
        return Itinerario.class;
    }
}
