package br.org.flem.sav.dao; 

import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.hibernate.dao.base.BaseDAOAb;
import br.org.flem.sav.negocio.CargoDiaria;
import java.util.Collection;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;

/**
 *
 * @author mccosta
 */
public class CargoDiariaDAO extends BaseDAOAb<CargoDiaria>{

    public CargoDiariaDAO() throws AcessoDadosException {
    }

    @Override
    protected Class<CargoDiaria> getClasseDto() {
       return CargoDiaria.class;
    } 

    public Collection<CargoDiaria> obterPorDescricao(String descricao) {
        Criteria cargo = session.createCriteria(CargoDiaria.class);
        if (descricao != null && !descricao.isEmpty()) {
            cargo.add(Expression.like("descricao", descricao, MatchMode.ANYWHERE));
        }
        cargo.addOrder(Order.asc("descricao"));
        return cargo.list();
    }

    public CargoDiaria obterCargoDiariaConsultor() {
        return (CargoDiaria) session.createQuery("FROM CargoDiaria C WHERE C.cargoNome = 'Consultor'").uniqueResult();
    }

    public CargoDiaria obterPorCargoDominio(Integer iCargos) {
        return (CargoDiaria) session.createQuery("FROM CargoDiaria C WHERE C.cargoDominio = :iCargos").setInteger("iCargos", iCargos).uniqueResult();
    }

    public Collection<CargoDiaria> obterTodosOrdenado() {
        return (Collection<CargoDiaria>) session.createQuery("FROM CargoDiaria C ORDER BY C.cargoNome").list();
    }

}
