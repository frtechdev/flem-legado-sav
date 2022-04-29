package br.org.flem.sav.dao;

import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fwe.hibernate.dao.base.BaseDAOAb;
import br.org.flem.sav.negocio.PercentualDiaria;
import java.util.List;


/**
 *
 * @author MCCosta
 */
public class PercentualDiariaDAO extends BaseDAOAb<PercentualDiaria>{

    public PercentualDiariaDAO() throws AplicacaoException{

    }

    @Override
    protected Class<PercentualDiaria> getClasseDto() {
        return PercentualDiaria.class;
    }

    @Override
    public List<PercentualDiaria> obterTodos(){
        return session.createQuery("FROM PercentualDiaria pd").list();
    }

    public PercentualDiaria obterPorDepartamentoDominio(Integer departamentoDominio){
        return (PercentualDiaria) session.createQuery("FROM PercentualDiaria pd WHERE pd.departamentoDominio  = :departamentoDominio ").setInteger("departamentoDominio", departamentoDominio).uniqueResult();
    }
    
    

}
