package br.org.flem.sav.dao; 

import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.hibernate.dao.base.BaseDAOAb;
import br.org.flem.sav.negocio.util.Banco;
import java.util.Collection;

/**
 *
 * @author mccosta
 */
public class BancoDAO extends BaseDAOAb <Banco>{

    public BancoDAO() throws AcessoDadosException{
    }

    @Override
    protected Class<Banco> getClasseDto() {
        return Banco.class;
    }

    public Banco obterPorCodigo(String codigo){
        return (Banco) session.createQuery("FROM Banco b WHERE b.codigo = :codigo").setString("codigo", codigo).uniqueResult();
    }

    public Banco obterPorNome(String nome){
        return (Banco) session.createQuery("FROM Banco b WHERE b.nome = :nome").setString("nome", nome).uniqueResult();
    }

    public Collection<Banco> obterTodosOrdenado(){
        return (Collection<Banco>) session.createQuery("FROM Banco b ORDER BY b.nome").list();
    }
}
