package br.org.flem.sav.bo;

import br.org.flem.fwe.bo.BaseBOAb;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.sav.dao.PercentualDiariaDAO;
import br.org.flem.sav.negocio.PercentualDiaria;
import java.util.List;

/**
 *
 * @author MCCosta
 */
public class PercentualDiariaBO extends BaseBOAb<PercentualDiaria>{

    public PercentualDiariaBO() throws AplicacaoException{
        super(new PercentualDiariaDAO());

    }
    
    @Override
    public List<PercentualDiaria> obterTodos(){
         return ((PercentualDiariaDAO)dao).obterTodos();
    }
    
    public PercentualDiaria obterPorDepartamentoDominio(Integer departamentoDominio){
         return ((PercentualDiariaDAO)dao).obterPorDepartamentoDominio(departamentoDominio);
    }

}
