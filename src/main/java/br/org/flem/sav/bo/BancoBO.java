package br.org.flem.sav.bo; 

import br.org.flem.fwe.bo.BaseBOAb;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.sav.dao.BancoDAO;
import br.org.flem.sav.negocio.util.Banco;
import java.util.Collection;

/**
 *
 * @author mccosta
 */
public class BancoBO extends BaseBOAb<Banco> {

    public BancoBO() throws AplicacaoException{
        super(new BancoDAO());
    }

    public Banco obterPorCodigo(String codigo){
        return ((BancoDAO) dao).obterPorCodigo(codigo);
    }

    public Banco obterPorNome(String nome){
        return ((BancoDAO) dao).obterPorNome(nome);
    }

    public Collection<Banco> obterTodosOrdenado(){
        return ((BancoDAO) dao).obterTodosOrdenado();
    }
}
