package br.org.flem.sav.bo; 

import br.org.flem.fwe.bo.BaseBOAb;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.sav.dao.TrechoDAO;
import br.org.flem.sav.negocio.Trecho;
import br.org.flem.sav.negocio.Viagem;
import java.util.Collection;

/**
 *
 * @author mccosta
 */
public class TrechoBO extends BaseBOAb<Trecho>{

    public TrechoBO() throws AplicacaoException{
        super (new TrechoDAO());
    }

    public Collection<Trecho> obterPorViagem(Viagem viagem) {
        return ((TrechoDAO) dao).obterPorViagem(viagem);
    }
}
