package br.org.flem.sav.dao;

import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.hibernate.dao.base.BaseDAOAb;
import br.org.flem.sav.negocio.util.Estado;
import br.org.flem.sav.negocio.util.Pais;
import java.util.Collection;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

/**
 *
 * @author ILFernandes
 */

public class EstadoDAO extends BaseDAOAb<Estado>{

    public EstadoDAO() throws AcessoDadosException{
    }

    @Override
    protected Class<Estado> getClasseDto() {
        return Estado.class;
    }

    public Collection<Estado> obterPorPais(Pais pais) {
        Criteria criteria = session.createCriteria(this.getClasseDto());
        if (pais != null && pais.getId() != null) {
            criteria.add(Expression.eq("pais", pais));
        }
        return criteria.list();
    }
    
    public Estado obterPorSigla(String sigla) {
        return (Estado) session.createQuery("FROM Estado e WHERE e.sigla IS NOT NULL AND e.sigla = :sigla").setString("sigla", sigla).uniqueResult();
    }

    /*public static void main(String[] args) throws AcessoDadosException {
        Pais pais = new Pais();
        pais.setId(Long.valueOf(3));
        System.out.println(new EstadoDAO());
    }*/
}
