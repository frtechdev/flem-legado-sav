package br.org.flem.sav.dao; 

import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.hibernate.dao.base.BaseDAOAb;
import br.org.flem.sav.negocio.Itinerario;
import br.org.flem.sav.negocio.util.Cidade;
import br.org.flem.sav.negocio.util.Estado;
import br.org.flem.sav.negocio.util.Pais;
import java.util.Collection;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author mccosta
 */
public class CidadeDAO extends BaseDAOAb <Cidade>{

    public CidadeDAO() throws AcessoDadosException{
    }

    @Override
    protected Class<Cidade> getClasseDto() {
        return Cidade.class;
    }

    public Cidade obterPorNome(String nome, Estado estado, Pais pais) {
        //return (Cidade) session.createQuery("FROM Cidade c WHERE c.nome= :nome").setString("nome", nome).uniqueResult();
        
        Criteria c = session.createCriteria(Cidade.class);

        if (estado != null) {
            c.createAlias("estado", "estado");
            c.add(Restrictions.eq("estado.id", estado.getId()));
            //System.out.println("estado not null - " + estado.getId());
        }
        if (pais != null) {
            c.createAlias("pais", "pais");
            c.add(Restrictions.eq("pais.id", pais.getId()));
            //System.out.println("pais not null - " + pais.getId());
        }
        c.add(Restrictions.eq("nome", nome));
        
        return (Cidade) c.uniqueResult();
    }

    public Collection<Cidade> obterBahia() {
        return (Collection<Cidade>) session.createQuery("FROM Cidade c WHERE c.estado.id = '7' ORDER BY c.nome").list();
    }

    public Collection<Cidade> obterBrasil() {
        //return (Collection<Cidade>) session.createQuery("FROM Cidade c WHERE c.pais.id = '3' AND c.estado.id <> '7'  ORDER BY c.nome").list();
        return (Collection<Cidade>) session.createQuery("FROM Cidade c WHERE c.pais.id = '3' ORDER BY c.nome").list();
    }

    public Collection<Cidade> obterExterior() {
        /*return (Collection<Cidade>) session.createQuery("FROM Cidade c WHERE c.pais.id <> '3'  ORDER BY c.nome").list();*/
        return (Collection<Cidade>) session.createQuery("FROM Cidade c ORDER BY c.nome").list();
    }

    @Override
    public void adicionarAgregacoesCriteria(Criteria c, Cidade objeto) {
        if (objeto.getEstado() != null && objeto.getEstado().getId() != null && objeto.getEstado().getId() > 0) {
            c.add(Expression.eq("estado", objeto.getEstado()));
        }
        if (objeto.getPais() != null && objeto.getPais().getId() != null && objeto.getPais().getId() > 0) {
            c.add(Expression.eq("pais", objeto.getPais()));
        }
    }
    
    public Collection<Cidade> obterListaDestinoPorItemItinerario(Itinerario itinerario) {
        return (Collection<Cidade>) session.createQuery("SELECT c FROM ItemItinerario i, Cidade c WHERE i.destino=c.id AND i.itinerario = :itinerario").setEntity("itinerario", itinerario).list();
    }


}
