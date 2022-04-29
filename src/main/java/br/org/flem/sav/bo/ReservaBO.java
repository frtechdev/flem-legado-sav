package br.org.flem.sav.bo; 

import br.org.flem.fwe.bo.BaseBOAb;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.sav.dao.ReservaDAO;
import br.org.flem.sav.negocio.Reserva;

/**
 *
 * @author mccosta
 */
public class ReservaBO extends BaseBOAb<Reserva> {

    public ReservaBO() throws AplicacaoException{
        super(new ReservaDAO());
    }
}