package br.org.flem.sav.dao; 

import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.hibernate.dao.base.BaseDAOAb;
import br.org.flem.sav.negocio.Reserva;

/**
 *
 * @author mccosta
 */
public class ReservaDAO extends BaseDAOAb <Reserva>{

    public ReservaDAO() throws AcessoDadosException{
    }

    @Override
    protected Class<Reserva> getClasseDto() {
        return Reserva.class;
    }
}