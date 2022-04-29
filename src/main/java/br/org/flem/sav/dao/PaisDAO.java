package br.org.flem.sav.dao;

import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.hibernate.dao.base.BaseDAOAb;
import br.org.flem.sav.negocio.util.Pais;
import java.util.Collection;
import org.hibernate.criterion.Order;

/**
 *
 * @author ILFernandes
 */
public class PaisDAO extends BaseDAOAb<Pais> {

    public PaisDAO() throws AcessoDadosException {
    }

    @Override
    protected Class<Pais> getClasseDto() {
        return Pais.class;
    }

    public Collection<Pais> obterTodosOrdenado() {
        return session.createCriteria(this.getClasseDto()).addOrder(Order.asc("nome")).list();
    }
    
    public Pais obterPorSigla(String sigla) {
        return (Pais) session.createQuery("FROM Pais p WHERE p.sigla IS NOT NULL AND p.sigla = :sigla").setString("sigla", sigla).uniqueResult();
    }
}
