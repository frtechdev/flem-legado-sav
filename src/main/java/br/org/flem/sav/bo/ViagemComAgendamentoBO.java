package br.org.flem.sav.bo; 

import br.org.flem.fwe.bo.BaseBOAb;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fw.service.IFuncionario;
import br.org.flem.sav.dao.ViagemComAgendamentoDAO;
import br.org.flem.sav.negocio.ViagemComAgendamento;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author mccosta
 */
public class ViagemComAgendamentoBO extends BaseBOAb<ViagemComAgendamento> {

    public ViagemComAgendamentoBO() throws AplicacaoException {
        super(new ViagemComAgendamentoDAO());
    }

    public Collection<ViagemComAgendamento> obterTodosNaoRecebido(){
        return ((ViagemComAgendamentoDAO) dao).obterTodosNaoRecebido();
    }
  
    public Collection<ViagemComAgendamento> obterTodosFuncionario(IFuncionario f) {
        return ((ViagemComAgendamentoDAO) dao).obterTodosFuncionario(f);
    }

    public Collection<ViagemComAgendamento> obterTodosBySatusAberto() {
        return ((ViagemComAgendamentoDAO) dao).obterTodosBySatusAberto();
    }

    public Map<Integer, ViagemComAgendamento> obterTodosFuncionarioMap(IFuncionario f) {
        return ((ViagemComAgendamentoDAO) dao).obterTodosFuncionarioMap(f);
    }

    public Map<Integer, ViagemComAgendamento> obterTodosEmMap() {
        return ((ViagemComAgendamentoDAO) dao).obterTodosEmMap();
    }

    public Collection<ViagemComAgendamento> obterTodosConsultor() {
        return ((ViagemComAgendamentoDAO) dao).obterTodosConsultor();
    }

    public Collection<ViagemComAgendamento> obterTodosByStatusAgendamento(Integer status) {
        return ((ViagemComAgendamentoDAO) dao).obterTodosByStatusAgendamento(status);
    }

    public Collection<ViagemComAgendamento> obterTodosByStatusAgendamentoAberto() {
        return ((ViagemComAgendamentoDAO) dao).obterTodosByStatusAgendamentoAberto();
    }

    public Collection<ViagemComAgendamento> obterTodosByStatusAgendamentoAberto(IFuncionario f) {
        return ((ViagemComAgendamentoDAO) dao).obterTodosByStatusAgendamentoAberto(f);
    }

    public Collection<ViagemComAgendamento> obterTodosByStatusPrestacaoConta(Integer status) {
        return ((ViagemComAgendamentoDAO) dao).obterTodosByStatusPrestacaoConta(status);
    }

    public Collection<ViagemComAgendamento> obterTodosByStatusPrestacaoContaEmAberto() {
        return ((ViagemComAgendamentoDAO) dao).obterTodosByStatusPrestacaoContaEmAberto();
    }

    public Collection<ViagemComAgendamento> obterTodosByStatusAgendamentoAbertoReprovado(IFuncionario f) {
        return ((ViagemComAgendamentoDAO) dao).obterTodosByStatusAgendamentoAbertoReprovado(f);
    }
}
