package br.org.flem.sav.dao;

import br.org.flem.fw.service.IFuncionario;
import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.hibernate.dao.base.BaseDAOAb;
import br.org.flem.fwe.util.Data;
import br.org.flem.sav.dto.RelatorioDiariaSalarioColaboradoresDTO;
import br.org.flem.sav.negocio.StatusPrestacaoContas;
import br.org.flem.sav.negocio.StatusViagem;
import br.org.flem.sav.negocio.Viagem;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author mccosta
 */
public class ViagemDAO extends BaseDAOAb<Viagem>  {

    public ViagemDAO() throws AcessoDadosException {
    }

    @Override
    protected Class<Viagem> getClasseDto() {
        return Viagem.class;
    }

    public Collection<Viagem> obterTodosFuncionario(IFuncionario f) {
        return (Collection<Viagem>) session.createQuery("FROM Viagem V WHERE V.codigoDominioUsuarioViajante = :matV").setInteger("matV", f.getCodigoDominio()).list();
    }

    public Collection<Integer> obterFuncMatriculaSemPrestacaoConta() {
        return (Collection<Integer>) session.createQuery("SELECT V.codigoDominioUsuarioViajante FROM Viagem V WHERE V.prestacaoContas is null").list();
    }

    public Collection<Viagem> obterTodosConsultor() {
        return (Collection<Viagem>) session.createQuery("FROM Viagem V WHERE V.codigoConsultorViajante LIKE 'P%' ").list();
    }

    public Collection<Viagem> obterTodosConsultor(String codigo_consultor) {
        return (Collection<Viagem>) session.createQuery("FROM Viagem V WHERE V.codigoConsultorViajante LIKE :codigo_consultor ").setString("codigo_consultor", codigo_consultor).list();
    }

    public Collection<Viagem> obterTodosFuncionario() {
        return (Collection<Viagem>) session.createQuery("FROM Viagem V WHERE V.codigoDominioUsuarioViajante > 0 ").list();
    }

    public Collection<Viagem> obterTodosByStatusViagem(Integer status) {
        return (Collection<Viagem>) session.createQuery("FROM Viagem V WHERE V.statusViagem = :status").setInteger("status", status).list();
    }

    public Collection<Viagem> obterTodosByStatusViagemAbertoRecebido() {
        return (Collection<Viagem>) session.createQuery("FROM Viagem V WHERE V.statusViagem = 0 OR V.statusViagem = 1").list();
    }

    public Collection<Viagem> obterTodosByStatusViagemAbertoRecebidoSemPrestacao() {
        return (Collection<Viagem>) session.createQuery("FROM Viagem V WHERE (V.statusViagem = 0 OR V.statusViagem = 1) AND "
                + "V.prestacaoContas = null AND V.codigoCentroCusto is not null AND V.codigoCentroResponsabilidade is not null AND "
                + "V.id not in (SELECT VCA.id FROM ViagemComAgendamento VCA WHERE VCA.statusAgendamento = 0 OR VCA.statusAgendamento = 2)").list();
    }

    public Collection<Viagem> obterTodosByStatusViagemRecebidoSemPrestacao() {
        return (Collection<Viagem>) session.createQuery("FROM Viagem V WHERE V.statusViagem = 1 AND "
                + "V.prestacaoContas = null AND V.id not in (SELECT VCA.id FROM ViagemComAgendamento VCA "
                + "WHERE VCA.statusAgendamento = 0 OR VCA.statusAgendamento = 2)").list();
    }

    public Collection<Viagem> obterTodosByStatusPrestacaoConta(Integer status) {
        return (Collection<Viagem>) session.createQuery("FROM Viagem V WHERE V.prestacaoContas.statusPrestacaoContas = :status").setInteger("status", status).list();
    }

    public Collection<Viagem> obterTodosByStatusPrestacaoContaEmAberto() {
        return (Collection<Viagem>) session.createQuery("FROM Viagem V WHERE V.prestacaoContas.statusPrestacaoContas = :status").setInteger("status", StatusPrestacaoContas.PRESTACAO_INFORMADA.ordinal()).list();
    }

    public Collection<Viagem> obterTodosComPrestacaoConta() {
        return (Collection<Viagem>) session.createQuery("FROM Viagem V WHERE V.prestacaoContas is not null AND V.statusViagem <> 3").list();
    }

    public Collection<Viagem> obterTodosByStatusPrestacaoContaRecebidoFinalizado() {
        return (Collection<Viagem>) session.createQuery("FROM Viagem V WHERE V.prestacaoContas.statusPrestacaoContas = :status1 "
                + "OR V.prestacaoContas.statusPrestacaoContas = :status2").setInteger("status1", StatusPrestacaoContas.PRESTACAO_RECEBIDA.ordinal()).setInteger("status2", StatusPrestacaoContas.PRESTACAO_FINALIZADA.ordinal()).list();
    }

    public Collection<Viagem> obterSemPrestacaoContaPorViajante(Integer codigoDominio) { 
        return (Collection<Viagem>) session.createQuery("FROM Viagem V WHERE V.prestacaoContas IS NULL "
                + "AND V.codigoDominioUsuarioViajante = :codigoDominio AND V.statusViagem <> 2").setInteger("codigoDominio", codigoDominio).list();
    }

    public Collection<Viagem> obterPrestacaoContaAbertaPorViajante(Integer codigoDominio) { 
        return (Collection<Viagem>) session.createQuery("FROM Viagem V WHERE V.prestacaoContas.statusPrestacaoContas = 0 "
                + "AND V.codigoDominioUsuarioViajante = :codigoDominio AND V.statusViagem <> 2").setInteger("codigoDominio", codigoDominio).list();
    }
    
    public Collection<Viagem> obterCancelamentoRecebimentoPrestacaoContaPorViajante(Integer codigoDominio) {
        return (Collection<Viagem>) session.createQuery("FROM Viagem V WHERE V.statusViagem = 2 "
                + "AND V.prestacaoContas.statusPrestacaoContas = 0 "
                + "AND V.codigoDominioUsuarioViajante = :codigoDominio ").setInteger("codigoDominio", codigoDominio).list();
    }

    public Collection<Viagem> obterSemPrestacaoContaPorConsultor(String codigo) {
        return (Collection<Viagem>) session.createQuery("FROM Viagem V WHERE V.prestacaoContas IS NULL "
                + "AND V.codigoConsultorViajante =:codigo AND V.statusViagem <> 2").setString("codigo", codigo).list();
    }

    public Collection<Viagem> obterPrestacaoContaAbertaPorConsultor(String codigo) {
        return (Collection<Viagem>) session.createQuery("FROM Viagem V WHERE V.prestacaoContas.statusPrestacaoContas = 0 "
                + "AND V.codigoConsultorViajante =:codigo AND V.statusViagem <> 2").setString("codigo", codigo).list();
    }
    
    public Collection<Viagem> obterCancelamentoRecebimentoPrestacaoContaPorConsultor(String codigo) {
        return (Collection<Viagem>) session.createQuery("FROM Viagem V WHERE V.statusViagem = 2 "
                + "AND V.prestacaoContas.statusPrestacaoContas = 0 "
                + "AND V.codigoConsultorViajante =:codigo ").setString("codigo", codigo).list();
    }

    public Collection<Viagem> obterTodasPorMesDoFuncionario(IFuncionario funcionario, int mes, int ano) {
        Query query = session.createQuery("FROM Viagem V "
                + "WHERE V.codigoDominioUsuarioViajante = :matV AND "
                + "month(V.dataSaidaPrevista) = :mes AND "
                + "month(V.dataRetornoPrevista) = :mes AND "
                + "year(V.dataSaidaPrevista) = :ano AND "
                + "year(V.dataRetornoPrevista) = :ano AND "
                + "V.statusViagem <> :viagemCancelada ");

        query.setParameter("matV", funcionario.getCodigoDominio());
        query.setParameter("mes", mes);
        query.setParameter("ano", ano);
        query.setInteger("viagemCancelada", StatusViagem.VIAGEM_CANCELADA.ordinal());
        return query.list();
    }

    public Collection<Viagem> obterTodasPorMesAnoComDiarias(int mes, int ano, Collection<String> centroCustos) {
        Criteria criteria = session.createCriteria(Viagem.class);
        criteria.add(Expression.ne("statusViagem", StatusViagem.VIAGEM_CANCELADA));
        criteria.add(Expression.isNotNull("codigoDominioUsuarioViajante"));
        criteria.add(Expression.gt("diaria", 0.0));
        if (centroCustos != null) {
            criteria.add(Expression.in("codigoCentroCusto", centroCustos));
        }
        criteria.add(Restrictions.sqlRestriction("year(dataSaidaPrevista) = ?", ano, Hibernate.INTEGER));
        criteria.add(Restrictions.sqlRestriction("year(dataRetornoPrevista) = ?", ano, Hibernate.INTEGER));
        criteria.add(Restrictions.sqlRestriction("month(dataSaidaPrevista) = ?", mes, Hibernate.INTEGER));
        criteria.add(Restrictions.sqlRestriction("month(dataRetornoPrevista) = ?", mes, Hibernate.INTEGER));

        return criteria.list();
    }

    public Collection<Viagem> obterTodasDoFuncionarioPorPeriodoMesAnoComDiarias(Integer codigoDominio, Date dataSaida, Date dataRetorno) {
        Criteria criteria = session.createCriteria(Viagem.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Expression.ne("statusViagem", StatusViagem.VIAGEM_CANCELADA));
        criteria.add(Expression.eq("codigoDominioUsuarioViajante", codigoDominio));
        criteria.add(Expression.between("dataSaidaPrevista", dataSaida, dataRetorno));
        criteria.add(Expression.between("dataRetornoPrevista", dataSaida, dataRetorno));
        criteria.add(Expression.gt("diaria", 0.0));
        return criteria.list();
    }

    public Collection<Viagem> obterTodosOrdenadoDataSolicitacao() {
        return (Collection<Viagem>) session.createQuery("FROM Viagem V order by V.dataSolicitacao desc").list();
    }

    public Collection<Viagem> obterPorFiltros(Viagem viagem, Collection<Integer> codigosDominio) {
        Criteria criteria = session.createCriteria(Viagem.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (codigosDominio != null && codigosDominio.size() > 0) {
            criteria.add(Expression.in("codigoDominioUsuarioViajante", codigosDominio));
        }
        if (viagem != null) {
            if (viagem.getId() != null && viagem.getId() > 0) {
                criteria.add(Expression.eq("id", viagem.getId()));
            }
            if (viagem.getCodigoCentroCusto() != null && !viagem.getCodigoCentroCusto().isEmpty()) {
                criteria.add(Expression.eq("codigoCentroCusto", viagem.getCodigoCentroCusto()));
            }
            if (viagem.getCodigoCentroResponsabilidade() != null && !viagem.getCodigoCentroResponsabilidade().isEmpty()) {
                criteria.add(Expression.eq("codigoCentroResponsabilidade", viagem.getCodigoCentroResponsabilidade()));
            }
            if (viagem.getCodigoFonteRecurso() != null && !viagem.getCodigoFonteRecurso().isEmpty()) {
                criteria.add(Expression.eq("codigoFonteRecurso", viagem.getCodigoFonteRecurso()));
            }
            if (viagem.getDestino() != null) {
                criteria.add(Expression.eq("destino", viagem.getDestino()));
            }
            if (viagem.getDataSaidaPrevista() != null) {
                criteria.add(Expression.ge("dataSaidaPrevista", viagem.getDataSaidaPrevista()));
            }
            if (viagem.getDataRetornoPrevista() != null) {
                criteria.add(Expression.le("dataRetornoPrevista", viagem.getDataRetornoPrevista()));
            }
        }

        criteria.addOrder(Order.desc("dataSolicitacao"));

        return criteria.list();

    }

    public Collection<Viagem> obterPorFiltros(Viagem viagemFiltro, Collection<Integer> codigosDominio, Collection<String> consultores) {
        Criteria criteria = session.createCriteria(Viagem.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        if (codigosDominio != null && !codigosDominio.isEmpty() && consultores != null && consultores.isEmpty()) {

            criteria.add(Expression.in("codigoDominioUsuarioViajante", codigosDominio));

        } else if (codigosDominio != null && codigosDominio.isEmpty() && consultores != null && !consultores.isEmpty()) {

            criteria.add(Expression.in("codigoConsultorViajante", consultores));

        } else if (codigosDominio != null && !codigosDominio.isEmpty() && consultores != null && !consultores.isEmpty()) {

            criteria.add(Expression.or(Expression.in("codigoDominioUsuarioViajante", codigosDominio), Expression.in("codigoConsultorViajante", consultores)));

        }
        if (viagemFiltro != null) {
            if (viagemFiltro.getId() != null && viagemFiltro.getId() > 0) {
                criteria.add(Expression.eq("id", viagemFiltro.getId()));
            } else {
                if (viagemFiltro.getCodigoCentroCusto() != null && !viagemFiltro.getCodigoCentroCusto().isEmpty()) {
                    criteria.add(Expression.eq("codigoCentroCusto", viagemFiltro.getCodigoCentroCusto()));
                }
                if (viagemFiltro.getCodigoCentroResponsabilidade() != null && !viagemFiltro.getCodigoCentroResponsabilidade().isEmpty()) {
                    criteria.add(Expression.eq("codigoCentroResponsabilidade", viagemFiltro.getCodigoCentroResponsabilidade()));
                }
                if (viagemFiltro.getCodigoFonteRecurso() != null && !viagemFiltro.getCodigoFonteRecurso().isEmpty()) {
                    criteria.add(Expression.eq("codigoFonteRecurso", viagemFiltro.getCodigoFonteRecurso()));
                }
                if (viagemFiltro.getStatusViagem() != null) {
                    criteria.add(Expression.eq("statusViagem", viagemFiltro.getStatusViagem()));
                }
                if (viagemFiltro.getDestino() != null) {
                    criteria.add(Expression.eq("destino", viagemFiltro.getDestino()));
                }
                if (viagemFiltro.getDataSaidaPrevista() != null) {
                    criteria.add(Expression.ge("dataSaidaPrevista", viagemFiltro.getDataSaidaPrevista()));
                }
                if (viagemFiltro.getDataRetornoPrevista() != null) {
                    criteria.add(Expression.le("dataRetornoPrevista", viagemFiltro.getDataRetornoPrevista()));
                }
            }
        }
        criteria.addOrder(Order.desc("dataSolicitacao"));
        return criteria.list();
    }

    public Map<Integer, Integer> obterMatriculaQtViagensPrestacaoAberta(Collection<String> codigosCentroCusto) {
        Criteria c = session.createCriteria(Viagem.class);
        //c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        c.add(Expression.ne("statusViagem", StatusViagem.VIAGEM_CANCELADA)).add(Expression.ne("statusViagem", StatusViagem.VIAGEM_ABERTA)).add(Expression.isNotNull("codigoDominioUsuarioViajante")).add(Expression.isNull("prestacaoContas")).add(Expression.lt("dataRetornoPrevista", new Date()));
        if (codigosCentroCusto != null && !codigosCentroCusto.isEmpty()) {
            c.add(Expression.in("codigoCentroCusto", codigosCentroCusto));
        } else {
            c.add(Expression.isNotNull("codigoCentroCusto"));
        }
        c.setProjection(Projections.projectionList().add(Projections.groupProperty("codigoDominioUsuarioViajante").as("Matricula_viajante")).add(Projections.rowCount(), "prestao_em_aberto")).addOrder(Order.asc("codigoDominioUsuarioViajante"));

        Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        for (Object[] objects : (Collection<Object[]>) c.list()) {
            map.put(Integer.valueOf(objects[0].toString()), Integer.valueOf(objects[1].toString()));
        }

        return map;
    }

    public Map<String, Integer> obterCodConsultorQtViagensPrestacaoAberta(Collection<String> codigosCentroCusto) {
        Criteria c = session.createCriteria(Viagem.class);
        //  c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        c.add(Expression.ne("statusViagem", StatusViagem.VIAGEM_CANCELADA)).add(Expression.ne("statusViagem", StatusViagem.VIAGEM_ABERTA)).add(Expression.isNotNull("codigoConsultorViajante")).add(Expression.isNull("prestacaoContas")).add(Expression.lt("dataRetornoPrevista", new Date()));
        if (codigosCentroCusto != null && !codigosCentroCusto.isEmpty()) {
            c.add(Expression.in("codigoCentroCusto", codigosCentroCusto));
        } else {
            c.add(Expression.isNotNull("codigoCentroCusto"));
        }
        c.setProjection(Projections.projectionList().add(Projections.groupProperty("codigoConsultorViajante").as("Codigo_consultor")).add(Projections.rowCount(), "prestao_em_aberto")).addOrder(Order.asc("codigoConsultorViajante"));


        Map<String, Integer> map = new HashMap<String, Integer>();

        for (Object[] objects : (Collection<Object[]>) c.list()) {
            map.put(objects[0].toString(), Integer.valueOf(objects[1].toString()));
        }

        return map;
    }

    /**
     * <b>OBS.:</b> No relatorio SAV015 a dataRetornoPrevista é entendida como a data final do período porém na querie o valor da data retorno
     * é passado para o banco como sendo a da de saida. data saida >= dataInicial e data saida <= dataFinal
     * @param viagemFiltro
     * @param codigosCentroCusto
     * @return Coleção de viagens
     *
     **/
    public Collection<Viagem> obterPorFiltroFaixaCentroCusto(Viagem viagemFiltro, Collection<String> codigosCentroCusto) {
        Criteria c = session.createCriteria(Viagem.class);
        c.add(Expression.in("codigoCentroCusto", codigosCentroCusto));
        if (viagemFiltro != null) {
            //Data inicial do período.
            if (viagemFiltro.getDataSaidaPrevista() != null) {
                c.add(Expression.ge("dataSaidaPrevista", viagemFiltro.getDataSaidaPrevista()));
            }
            // Data final do período.
            if (viagemFiltro.getDataRetornoPrevista() != null) {
                c.add(Expression.le("dataSaidaPrevista", viagemFiltro.getDataRetornoPrevista()));
            }
            if (viagemFiltro.getPrestacaoContas() != null
                    && viagemFiltro.getPrestacaoContas().getStatusPrestacaoContas() != null) {
                c.createAlias("prestacaoContas", "prestacao");
                c.add(Expression.eq("prestacao.statusPrestacaoContas", viagemFiltro.getPrestacaoContas().getStatusPrestacaoContas()));
            }
            if (viagemFiltro.getStatusViagem() != null) {
                c.add(Expression.eq("statusViagem", viagemFiltro.getStatusViagem()));
            }
            if (viagemFiltro.getCodigoDominioUsuarioViajante() != null && viagemFiltro.getCodigoDominioUsuarioViajante() > 0) {
                c.add(Expression.eq("codigoDominioUsuarioViajante", viagemFiltro.getCodigoDominioUsuarioViajante()));
            } else if (viagemFiltro.getCodigoConsultorViajante() != null && !viagemFiltro.getCodigoConsultorViajante().isEmpty()) {
                c.add(Expression.eq("codigoConsultorViajante", viagemFiltro.getCodigoConsultorViajante()));
            }
        }
        c.add(Expression.ne("statusViagem", StatusViagem.VIAGEM_CANCELADA));

        return c.list();
    }
    
    /**
     * <b>OBS.:</b> No relatorio SAV015 a dataRetornoPrevista é entendida como a data final do período porém na querie o valor da data retorno
     * é passado para o banco como sendo a da de saida. data saida >= dataInicial e data saida <= dataFinal
     * @param viagemFiltro
     * @param codigosCentroCusto
     * @return Coleção de viagens
     *
     **/
    public Collection<Viagem> obterTodasPorFiltroFaixaCentroCusto(Viagem viagemFiltro, Collection<String> codigosCentroCusto) {
        Criteria c = session.createCriteria(Viagem.class);
        c.add(Expression.in("codigoCentroCusto", codigosCentroCusto));
        if (viagemFiltro != null) {
            //Data inicial do período.
            if (viagemFiltro.getDataSaidaPrevista() != null) {
                c.add(Expression.ge("dataSaidaPrevista", viagemFiltro.getDataSaidaPrevista()));
            }
            // Data final do período.
            if (viagemFiltro.getDataRetornoPrevista() != null) {
                c.add(Expression.le("dataSaidaPrevista", viagemFiltro.getDataRetornoPrevista()));
            }
            if (viagemFiltro.getPrestacaoContas() != null
                    && viagemFiltro.getPrestacaoContas().getStatusPrestacaoContas() != null) {
                c.createAlias("prestacaoContas", "prestacao");
                c.add(Expression.eq("prestacao.statusPrestacaoContas", viagemFiltro.getPrestacaoContas().getStatusPrestacaoContas()));
            }
            if (viagemFiltro.getCodigoDominioUsuarioViajante() != null && viagemFiltro.getCodigoDominioUsuarioViajante() > 0) {
                c.add(Expression.eq("codigoDominioUsuarioViajante", viagemFiltro.getCodigoDominioUsuarioViajante()));
            } else if (viagemFiltro.getCodigoConsultorViajante() != null && !viagemFiltro.getCodigoConsultorViajante().isEmpty()) {
                c.add(Expression.eq("codigoConsultorViajante", viagemFiltro.getCodigoConsultorViajante()));
            }
        }
        c.add(Expression.or(Expression.eq("statusViagem", StatusViagem.VIAGEM_FINALIZADA), Expression.eq("statusViagem", StatusViagem.VIAGEM_CANCELADA)));

        return c.list();
    }

    /**
     * <b>OBS.:</b> Retorna todas as viágens que estão dentro de um período, tendo como base, também, outros parâmetros.
     * @param id
     * @param codigoDominioUsuarioViajante
     * @param codigoConsultorViajante
     * @param dataSaidaPrevista recebe como valor o dataRetornoPrevista
     * @param dataRetornoPrevista recebe como valor o dataSaidaPrevista
     * @return Coleção de viagens
     *
     **/
    public Collection<Viagem> obterTodosPorPeriodo(Viagem viagem) {
        Criteria criteria = session.createCriteria(Viagem.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Expression.ne("statusViagem", StatusViagem.VIAGEM_CANCELADA));
        if (viagem.getId() != null) {
            criteria.add(Expression.ne("id", viagem.getId()));
        }
        if (viagem.getCodigoDominioUsuarioViajante() != null) {
            criteria.add(Expression.eq("codigoDominioUsuarioViajante", viagem.getCodigoDominioUsuarioViajante()));
        }
        if (viagem.getCodigoConsultorViajante() != null && !viagem.getCodigoConsultorViajante().isEmpty()) {
            criteria.add(Expression.eq("codigoConsultorViajante", viagem.getCodigoConsultorViajante()));
        }
        if (viagem.getDataSaidaPrevista() != null && viagem.getDataRetornoPrevista() != null) {
            criteria.add(Expression.le("dataSaidaPrevista", viagem.getDataRetornoPrevista()));
            criteria.add(Expression.ge("dataRetornoPrevista", viagem.getDataSaidaPrevista()));
        }

        criteria.addOrder(Order.desc("dataSolicitacao"));

        return criteria.list();
    }

    /**
     * <b>Data retorno prevista é entendido como data final do periodo. Este periodo refere-se as datas de saida</b>
     * @param viagemFiltro
     * @param codigosCentroCusto
     * @return
     */
    public Collection<Viagem> relatorioAnalíticoPrestacaoPendente(Viagem viagemFiltro, Collection<String> codigosCentroCusto) {
        if(codigosCentroCusto != null && !codigosCentroCusto.isEmpty()){
            Criteria c = session.createCriteria(Viagem.class);
            c.add(Expression.in("codigoCentroCusto", codigosCentroCusto));
            //Data inicial do período.
            if (viagemFiltro.getDataSaidaPrevista() != null) {
                c.add(Expression.ge("dataSaidaPrevista", viagemFiltro.getDataSaidaPrevista()));
            }
            // Data final do período.
            if (viagemFiltro.getDataRetornoPrevista() != null) {
                c.add(Expression.le("dataSaidaPrevista", viagemFiltro.getDataRetornoPrevista()));
            }
            c.add(Expression.lt("dataRetornoPrevista", new Date()));
            c.add(Expression.isNull("prestacaoContas"));
            c.add(Expression.eq("statusViagem", StatusViagem.VIAGEM_RECEBIDA));
            return c.list();
        }else{
            return new ArrayList<Viagem>();
        }
    }

    public Collection<Viagem> relatorioAnalíticoPrestacaoInformada(Viagem viagemFiltro, Collection<String> codigosCentroCusto) {
        Criteria c = session.createCriteria(Viagem.class);
        c.add(Restrictions.in("codigoCentroCusto", codigosCentroCusto));
        //Data inicial do período.
        if (viagemFiltro.getDataSaidaPrevista() != null) {
            c.add(Restrictions.ge("dataSaidaPrevista", viagemFiltro.getDataSaidaPrevista()));
        }
        // Data final do período.
        if (viagemFiltro.getDataRetornoPrevista() != null) {
            c.add(Restrictions.le("dataSaidaPrevista", viagemFiltro.getDataRetornoPrevista()));
        }
        c.add(Restrictions.lt("dataRetornoPrevista", new Date()));
        c.createAlias("prestacaoContas", "prestacao");
        c.add(Restrictions.eq("prestacao.statusPrestacaoContas", StatusPrestacaoContas.PRESTACAO_INFORMADA));
        
        //c.add(Expression.eq("statusViagem", StatusViagem.VIAGEM_RECEBIDA));
        c.add(Restrictions.or(Restrictions.eq("statusViagem", StatusViagem.VIAGEM_RECEBIDA), Restrictions.eq("statusViagem", StatusViagem.VIAGEM_CANCELADA)));
        
        return c.list();
    }

    public Collection<Viagem> obterViagensPorIds(Collection<Integer> ids) {
        if (ids != null && !ids.isEmpty()) {
            Criteria viagem = session.createCriteria(Viagem.class);
            viagem.add(Expression.in("id", ids));

            return viagem.list();
        }
        return new ArrayList<Viagem>();
    }

    public Collection<Viagem> obterViagensPorDataPagamentoViajante(Date date, Integer codigoDominio) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Criteria criteria = session.createCriteria(Viagem.class);
        criteria.add(Expression.eq("codigoDominioUsuarioViajante", codigoDominio));
        criteria.add(Restrictions.sqlRestriction("year({alias}.dataPagamento) = ?", calendar.get(Calendar.YEAR), Hibernate.INTEGER));

        //MONTH +1 pois o mê no java começa por zero.
        criteria.add(Restrictions.sqlRestriction("month({alias}.dataPagamento) = ?", calendar.get(Calendar.MONTH) + 1, Hibernate.INTEGER));

        return criteria.list();
    }

    public Collection<Viagem> obterViagensComPrestacaoContasPorDataPagamentoViajante(Date dataPagamento, Integer codigoDominio) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataPagamento);
        //System.out.println("dataDePagamento 03 ->>> " + dataPagamento);
        Criteria viagem = session.createCriteria(Viagem.class);
        viagem.add(Expression.eq("codigoDominioUsuarioViajante", codigoDominio));
        Criteria prestacaoContas = viagem.createCriteria("prestacaoContas");
        prestacaoContas.add(Restrictions.sqlRestriction("year({alias}.dataPagamento) = ?", calendar.get(Calendar.YEAR), Hibernate.INTEGER));
        //System.out.println("dataDePagamento 03.1 ->>> " + calendar.get(Calendar.YEAR));
        //MONTH +1 pois o mê no java começa por zero.
        prestacaoContas.add(Restrictions.sqlRestriction("month({alias}.dataPagamento) = ?", (calendar.get(Calendar.MONTH) + 1), Hibernate.INTEGER));
        //System.out.println("dataDePagamento 03.2 ->>> " + (calendar.get(Calendar.MONTH) + 1));
        return viagem.list();
    }

    public Collection<Viagem> obterViagensPorDataDePagamentoCentroCusto(Date dataPagamento, Collection<String> codigosCentroCusto) {
        Criteria viagem = session.createCriteria(Viagem.class);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataPagamento);
        viagem.add(Expression.in("codigoCentroCusto", codigosCentroCusto));
        viagem.add(Restrictions.sqlRestriction("year({alias}.dataPagamento) = ?", calendar.get(Calendar.YEAR), Hibernate.INTEGER));
        viagem.add(Restrictions.sqlRestriction("month({alias}.dataPagamento) = ?", calendar.get(Calendar.MONTH) + 1, Hibernate.INTEGER));
        return viagem.list();
    }

    public Collection<Viagem> obterViagensPorDataDePagamentoCentroCusto(String dataSaida, String dataRetorno, Collection<String> codigosCentroCusto) {
        Query query = session.createQuery("FROM Viagem v WHERE v.codigoCentroCusto IN (:codigoCentroCusto) AND "
                + "v.dataPagamento >= :dataSaida AND v.dataPagamento <= :dataRetorno "
                + "ORDER BY v.dataPagamento ASC");
        try {
            query.setDate("dataSaida", Data.formataData(dataSaida) );
            query.setDate("dataRetorno", Data.formataData(dataRetorno));
        } catch (ParseException ex) {
            Logger.getLogger(ViagemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        query.setParameterList("codigoCentroCusto", codigosCentroCusto);
        
        return query.list();
    }

    public Collection<Viagem> obterViagensPorDataDePagamentoUsuarios(Date dataPagamento, Collection<Integer> codigosDominio) {
        if(codigosDominio != null && !codigosDominio.isEmpty()){
            Criteria viagem = session.createCriteria(Viagem.class);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dataPagamento);
            viagem.add(Expression.in("codigoDominioUsuarioViajante", codigosDominio));
            viagem.add(Restrictions.sqlRestriction("year({alias}.dataPagamento) = ?", calendar.get(Calendar.YEAR), Hibernate.INTEGER));
            viagem.add(Restrictions.sqlRestriction("month({alias}.dataPagamento) = ?", calendar.get(Calendar.MONTH) + 1, Hibernate.INTEGER));
            return viagem.list();
        }else{
            return new ArrayList<Viagem>();
        }
    }

    public Collection<Viagem> obterViagensPorDataDePagamentoUsuarios(String dataSaida, String dataRetorno, Collection<Integer> codigosDominio) {
        Query query = session.createQuery("FROM Viagem "
                + "WHERE dataPagamento >= :dataSaida AND "
                + "dataPagamento <= :dataRetorno AND "
                + "codigoDominioUsuarioViajante IN (:codigoDominioUsuarioViajante)"
                + "ORDER BY dataPagamento ASC");

        query.setString("dataSaida", dataSaida);
        query.setString("dataRetorno", dataRetorno);
        query.setParameterList("codigoDominioUsuarioViajante", codigosDominio);
        return query.list();
    }

    public Collection<Viagem> consultaViagem(Viagem viagemFiltro, Collection<Integer> codigosDominio, Collection<String> consultores) {
        Criteria criteria = session.createCriteria(Viagem.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
        if (codigosDominio != null && !codigosDominio.isEmpty() && consultores != null && consultores.isEmpty()) {

            criteria.add(Restrictions.in("codigoDominioUsuarioViajante", codigosDominio));

        } else if (codigosDominio != null && codigosDominio.isEmpty() && consultores != null && !consultores.isEmpty()) {

            criteria.add(Restrictions.in("codigoConsultorViajante", consultores));

        } else if (codigosDominio != null && !codigosDominio.isEmpty() && consultores != null && !consultores.isEmpty()) {

            criteria.add(Restrictions.or(Restrictions.in("codigoDominioUsuarioViajante", codigosDominio), Expression.in("codigoConsultorViajante", consultores)));

        }
        if (viagemFiltro != null) {
            if (viagemFiltro.getCodigoCentroCusto() != null && !viagemFiltro.getCodigoCentroCusto().isEmpty()) {
                criteria.add(Restrictions.eq("codigoCentroCusto", viagemFiltro.getCodigoCentroCusto()));
            }
            if (viagemFiltro.getCodigoCentroResponsabilidade() != null && !viagemFiltro.getCodigoCentroResponsabilidade().isEmpty()) {
                criteria.add(Restrictions.eq("codigoCentroResponsabilidade", viagemFiltro.getCodigoCentroResponsabilidade()));
            }
            if (viagemFiltro.getCodigoFonteRecurso() != null && !viagemFiltro.getCodigoFonteRecurso().isEmpty()) {
                criteria.add(Restrictions.eq("codigoFonteRecurso", viagemFiltro.getCodigoFonteRecurso()));
            }
            if (viagemFiltro.getDestino() != null) {
                criteria.add(Restrictions.eq("destino", viagemFiltro.getDestino()));
            }
            if (viagemFiltro.getDataSaidaPrevista() != null) {
                criteria.add(Restrictions.ge("dataSaidaPrevista", viagemFiltro.getDataSaidaPrevista()));
            }
            if (viagemFiltro.getDataRetornoPrevista() != null) {
                criteria.add(Restrictions.le("dataRetornoPrevista", viagemFiltro.getDataRetornoPrevista()));
            }
            if (viagemFiltro.getCodigoDominioUsuarioViajante() != null)  {
                criteria.add(Restrictions.eq("codigoDominioUsuarioViajante", viagemFiltro.getCodigoDominioUsuarioViajante()));
            }
            if (viagemFiltro.getCodigoConsultorViajante() != null) {
                criteria.add(Restrictions.eq("codigoConsultorViajante", viagemFiltro.getCodigoConsultorViajante()));
            }
        }
        criteria.addOrder(Order.desc("dataSolicitacao"));

        return criteria.list();

    }
    
    
    public List<RelatorioDiariaSalarioColaboradoresDTO> relatorioDiariaSalarioColaboradores (String mes, String ano) throws AcessoDadosException{
        try {
            
            SQLQuery query = null;
            StringBuilder str = new StringBuilder("SELECT e.i_empregados, ").
                append("   CONVERT(VARCHAR(100),e.nome) Nome, ").
                append("   YEAR(v.dataSaidaPrevista) Ano, ").
                append("   MONTH(v.dataSaidaPrevista) Mes, ").
                append("   CONVERT(NUMERIC(9,2), SUM(v.totalDiarias)) 'Vl. Total Diarias', ").
                append("   CASE WHEN SUM(v.totalDiarias) > (MAX(e.salario)/2) THEN SUM(v.totalDiarias) - (MAX(e.salario)/2) ELSE 0.0 END 'Vl. recebido acima 50% sal.', ").
                append("   CONVERT(numeric(9,2), MAX(e.salario)/2) 'Base 50% Salario', ").
                append("   CONVERT(NUMERIC(9,2), ((SUM(v.totalDiarias) ) / MAX(e.salario) )* 100.0 ) 'Percertual diaria/salário' ").
                append(" FROM BDSAV.dbo.viagem v ").
                append(" INNER JOIN SRVDOMINIO.Contabil.bethadba.foempregados e ON e.i_empregados = v.codigoDominioUsuarioViajante AND e.codi_emp = 1 ").
                append(" WHERE YEAR(v.dataSaidaPrevista) = ").append(ano).append("AND MONTH(v.dataSaidaPrevista) IN (").append(mes).append(") AND v.statusViagem != ").append(StatusViagem.VIAGEM_CANCELADA.getId()).append(" ").
                append(" GROUP BY e.i_empregados, v.codigoDominioUsuarioViajante, e.nome, YEAR(v.dataSaidaPrevista), MONTH(v.dataSaidaPrevista) ").
                append(" HAVING SUM(v.totalDiarias) > 0 ").
                append(" ORDER BY e.nome, YEAR(v.dataSaidaPrevista), MONTH(v.dataSaidaPrevista)");

            
            query = session.createSQLQuery(str.toString());
            List<Object[]> lista = query.list();
            List<RelatorioDiariaSalarioColaboradoresDTO> retorno = new ArrayList<RelatorioDiariaSalarioColaboradoresDTO>();
            
            for(Object[] r : lista){
                RelatorioDiariaSalarioColaboradoresDTO rel = new RelatorioDiariaSalarioColaboradoresDTO();
                rel.setMatricula(r[0].toString());
                rel.setNome(r[1].toString());
                rel.setAno(r[2].toString());
                rel.setMes(r[3].toString());
                rel.setVlTotal(r[4].toString());
                rel.setVlRecebido(r[5].toString());
                rel.setbSalario(r[6].toString());
                rel.setPercentualDiariaSalario(r[7].toString());
                retorno.add(rel);
            
            }
            return retorno;
        } catch (HibernateException e) {
            throw new AcessoDadosException(e);
        }

    }

}
