package br.org.flem.sav.dao; 

import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.hibernate.dao.base.BaseDAOAb;
import br.org.flem.sav.negocio.Trecho;
import br.org.flem.sav.negocio.Viagem;
import java.util.Collection;

/**
 *
 * @author mccosta
 */
public class TrechoDAO extends BaseDAOAb <Trecho>{

    public TrechoDAO() throws AcessoDadosException{
    }

    @Override
    protected Class<Trecho> getClasseDto() {
        return Trecho.class;
    }

    public Collection<Trecho> obterPorViagem(Viagem viagem) {
        return (Collection<Trecho>) session.createQuery("FROM Trecho t WHERE t.viagem = :viagem ORDER BY t.dataInicio ASC").setEntity("viagem", viagem).list();
    }
}