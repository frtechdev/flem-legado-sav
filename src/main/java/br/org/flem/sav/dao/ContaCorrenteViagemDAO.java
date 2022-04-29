package br.org.flem.sav.dao;

import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fwe.hibernate.dao.base.BaseDAOAb;
import br.org.flem.sav.negocio.ContaCorrenteViagem;
import java.util.List;

/**
 *
 * @author mgsilva
 */
public class ContaCorrenteViagemDAO extends BaseDAOAb<ContaCorrenteViagem>{

    public ContaCorrenteViagemDAO() throws AplicacaoException{

    }

    public ContaCorrenteViagem obterPorMatriculaFuncionario(String codigoDominio){
        return (ContaCorrenteViagem) session.createQuery("FROM ContaCorrenteViagem ccv where ccv.codigoDominioFuncionarioViajante = :codigoDominio")
               .setString("codigoDominio", codigoDominio).uniqueResult();
    }
    
    public List<ContaCorrenteViagem> obterPorMatriculaOuNome(String busca){
    
        return  (List<ContaCorrenteViagem>) session.createQuery("FROM ContaCorrenteViagem ccv where ccv.nomeFuncionario like :busca").setString("busca", busca+"%").list();
    }
    


    @Override
    protected Class<ContaCorrenteViagem> getClasseDto() {
        return ContaCorrenteViagem.class;
    }

    @Override
    public List<ContaCorrenteViagem> obterTodos(){
        return session.createQuery("FROM ContaCorrenteViagem ccv").list();
    }

}
