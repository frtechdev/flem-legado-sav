package br.org.flem.sav.bo; 

import br.org.flem.fwe.bo.BaseBOAb;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.sav.dao.PrestacaoContasDAO;
import br.org.flem.sav.negocio.PrestacaoContas;

public class PrestacaoContasBO extends BaseBOAb<PrestacaoContas>{

    public PrestacaoContasBO() throws AplicacaoException{
        super(new PrestacaoContasDAO());
    }
}
