package br.org.flem.sav.dao;

import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.hibernate.dao.base.BaseDAOAb;
import br.org.flem.fw.service.IFuncionario;
import br.org.flem.sav.negocio.StatusAgendamento;
import br.org.flem.sav.negocio.ViagemComAgendamento;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mccosta
 */
public class ViagemComAgendamentoDAO extends BaseDAOAb <ViagemComAgendamento>{

    public ViagemComAgendamentoDAO() throws AcessoDadosException{
    }

    @Override
    protected Class<ViagemComAgendamento> getClasseDto() {
        return ViagemComAgendamento.class;
    }

    public Collection<ViagemComAgendamento> obterTodosNaoRecebido(){
        return (Collection<ViagemComAgendamento>) session.createQuery("FROM ViagemComAgendamento V WHERE V.statusViagem = 0").list();
    }

    public Collection<ViagemComAgendamento> obterTodosFuncionario(IFuncionario f) {
        return (Collection<ViagemComAgendamento>) session.createQuery("FROM ViagemComAgendamento V WHERE V.codigoDominioUsuarioViajante = :matV ").setInteger("matV", f.getCodigoDominio()).list();
    }

    public Collection<ViagemComAgendamento> obterTodosBySatusAberto() {
        return session.createQuery("FROM ViagemComAgendamento V WHERE  V.statusAgendamento =:status")
               .setInteger("status", StatusAgendamento.AGENDAMENTO_ABERTO.ordinal()).list();
    }

    public Map<Integer,ViagemComAgendamento> obterTodosFuncionarioMap(IFuncionario f) {
        Map<Integer,ViagemComAgendamento> mapVCA = new HashMap<Integer,ViagemComAgendamento>();
        Collection<ViagemComAgendamento> vcaList = (Collection<ViagemComAgendamento>) session.createQuery("FROM ViagemComAgendamento V WHERE V.codigoDominioUsuarioViajante = :matV ").setInteger("matV", f.getCodigoDominio()).list();

        for (ViagemComAgendamento vca : vcaList) {
            mapVCA.put(vca.getId(), vca);
        }

        return mapVCA;
    }

    public Map<Integer,ViagemComAgendamento> obterTodosEmMap() {
        Map<Integer,ViagemComAgendamento> mapVCA = new HashMap<Integer,ViagemComAgendamento>();
        Collection<ViagemComAgendamento> vcaList = (Collection<ViagemComAgendamento>) session.createQuery("FROM ViagemComAgendamento").list();

        for (ViagemComAgendamento vca : vcaList) {
            mapVCA.put(vca.getId(), vca);
        }

        return mapVCA;
    }

    public Collection<ViagemComAgendamento> obterTodosConsultor() {
        return (Collection<ViagemComAgendamento>) session.createSQLQuery("SELECT * FROM ViagemComAgendamento V WHERE V.codigoConsultorViajante LIKE 'P%' ").list();
    }

    public Collection<ViagemComAgendamento> obterTodosByStatusAgendamento(Integer status) {
        return (Collection<ViagemComAgendamento>) session.createQuery("FROM ViagemComAgendamento V WHERE V.statusAgendamento = :status").setInteger("status", status).list();
    }

    public Collection<ViagemComAgendamento> obterTodosByStatusAgendamentoAberto() {
        return (Collection<ViagemComAgendamento>) session.createQuery("FROM ViagemComAgendamento V WHERE V.statusAgendamento = 0").list();
    }

    public Collection<ViagemComAgendamento> obterTodosByStatusAgendamentoAberto(IFuncionario f) {
        return (Collection<ViagemComAgendamento>) session.createQuery("FROM ViagemComAgendamento V WHERE V.statusAgendamento = 0 AND V.codigoDominioUsuarioViajante = :matV ").setInteger("matV", f.getCodigoDominio()).list();
    }

    public Collection<ViagemComAgendamento> obterTodosByStatusPrestacaoConta(Integer status) {
        return (Collection<ViagemComAgendamento>) session.createQuery("FROM ViagemComAgendamento V WHERE V.prestacaoContas.statusPrestacaoContas = :status").setInteger("status", status).list();
    }

    public Collection<ViagemComAgendamento> obterTodosByStatusPrestacaoContaEmAberto() {
        return (Collection<ViagemComAgendamento>) session.createQuery("FROM ViagemComAgendamento V WHERE V.prestacaoContas.statusPrestacaoContas = 0 OR V.prestacaoContas.statusPrestacaoContas = 1 ").list();
    }

    public Collection<ViagemComAgendamento> obterTodosByStatusAgendamentoAbertoReprovado(IFuncionario f) {
         return (Collection<ViagemComAgendamento>) session.createQuery("FROM ViagemComAgendamento V WHERE V.statusAgendamento in (0,2) AND V.codigoDominioUsuarioViajante = :matV ").setInteger("matV", f.getCodigoDominio()).list();
    }
}
