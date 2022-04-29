package br.org.flem.sav.bo;

import br.org.flem.fwe.bo.BaseBOAb;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.sav.dao.ContaCorrenteViagemDAO;
import br.org.flem.sav.negocio.ContaCorrenteViagem;
import java.util.List;

/**
 *
 * @author mgsilva
 */
public class ContaCorrenteViagemBO extends BaseBOAb<ContaCorrenteViagem>{

    public ContaCorrenteViagemBO() throws AplicacaoException{
        super(new ContaCorrenteViagemDAO());

    }

     public ContaCorrenteViagem obterPorMatriculaFuncionario(String matricula){
         return ((ContaCorrenteViagemDAO)dao).obterPorMatriculaFuncionario(matricula);

     }
     
     public List<ContaCorrenteViagem> obterPorMatriculaOuNome(String busca){
         return ((ContaCorrenteViagemDAO)dao).obterPorMatriculaOuNome(busca);
     }
    @Override
     public List<ContaCorrenteViagem> obterTodos(){
         return ((ContaCorrenteViagemDAO)dao).obterTodos();

     }

}
