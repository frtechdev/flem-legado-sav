package br.org.flem.sav.dao; 

import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fwe.hibernate.dao.base.BaseDAOAb;
import br.org.flem.sav.bo.ItinerarioBO;
import br.org.flem.sav.negocio.ItemItinerario;
import br.org.flem.sav.negocio.Itinerario;
import java.util.Collection;

/**
 *
 * @author mccosta
 */
public class ItemItinerarioDAO extends BaseDAOAb <ItemItinerario>{

    public ItemItinerarioDAO() throws AcessoDadosException{
    }

    @Override
    protected Class<ItemItinerario> getClasseDto() {
        return ItemItinerario.class;
    }

    public Collection<ItemItinerario> obterPorItinerario(Itinerario itinerario) {
        return (Collection<ItemItinerario>) session.createQuery("FROM ItemItinerario it WHERE it.itinerario = :itinerario ORDER BY it.data ASC").setEntity("itinerario", itinerario).list();
    }

    public void excluir(Integer itinerario) {
        session.createSQLQuery("delete from item_intinerario where id_itinerario = " + itinerario.toString() );
    }
}