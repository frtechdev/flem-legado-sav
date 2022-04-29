package br.org.flem.sav.action;

import br.org.flem.fw.arquivo.Layout;
import br.org.flem.fw.arquivo.gem.layout.Parcela;
import br.org.flem.fw.arquivo.gem.layout.RegistroHeader;
import br.org.flem.fw.arquivo.gem.layout.RegistroLinha;
import br.org.flem.fw.persistencia.dao.legado.bdprocesso.ControleProcessoDAO;
import br.org.flem.fw.persistencia.dao.legado.gem.CentroCustoDAO;
import br.org.flem.fw.persistencia.dao.legado.gem.EntidadeDAO;
import br.org.flem.fw.persistencia.dao.legado.gem.FonteRecursoDAO;
import br.org.flem.fw.persistencia.dto.Funcionario;
import br.org.flem.fw.persistencia.dto.Usuario;
import br.org.flem.fw.service.ICargo;
import br.org.flem.fw.service.IColaborador;
import br.org.flem.fw.service.IFonteRecurso;
import br.org.flem.fw.service.IFuncionario;
import br.org.flem.fw.service.IUsuario;
import br.org.flem.fw.service.impl.RHServico;
import br.org.flem.fw.util.Constante;
import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fwe.exception.RelatorioSemDadosException;
import br.org.flem.fwe.hibernate.util.HibernateUtil;
import br.org.flem.fwe.util.Data;
import br.org.flem.fwe.util.Valores;
import br.org.flem.fwe.web.action.SecurityDispatchAction;
import br.org.flem.fwe.web.annotation.Funcionalidade;
import br.org.flem.fwe.web.util.MensagemTagUtil;
import br.org.flem.sav.bo.CidadeBO;
import br.org.flem.sav.bo.CompanhiaBO;
import br.org.flem.sav.bo.GastoBO;
import br.org.flem.sav.bo.ItemItinerarioBO;
import br.org.flem.sav.bo.ItinerarioBO;
import br.org.flem.sav.bo.PercentualDiariaBO;
import br.org.flem.sav.bo.PrestacaoContasBO;
import br.org.flem.sav.bo.TipoGastoBO;
import br.org.flem.sav.bo.TrechoBO;
import br.org.flem.sav.bo.TrechoEfetivoBO;
import br.org.flem.sav.bo.UsuarioBO;
import br.org.flem.sav.bo.ViagemBO;
import br.org.flem.sav.dto.ItemItinerarioDTO;
import br.org.flem.sav.dto.RelatorioViagem;
import br.org.flem.sav.dto.ViagemDTO;
import br.org.flem.sav.negocio.DestinoViagem;
import br.org.flem.sav.negocio.Gasto;
import br.org.flem.sav.negocio.ItemItinerario;
import br.org.flem.sav.negocio.Itinerario;
import br.org.flem.sav.negocio.Natureza;
import br.org.flem.sav.negocio.PercentualDiaria;
import br.org.flem.sav.negocio.PrestacaoContas;
import br.org.flem.sav.negocio.SituacaoDataPagamentoEnum;
import br.org.flem.sav.negocio.StatusPrestacaoContas;
import br.org.flem.sav.negocio.StatusViagem;
import br.org.flem.sav.negocio.TipoDiaria;
import br.org.flem.sav.negocio.TipoGasto;
import br.org.flem.sav.negocio.TrechoEfetivo;
import br.org.flem.sav.negocio.Viagem;
import br.org.flem.sav.negocio.util.Cidade;
import br.org.flem.sav.negocio.util.Diarias;
import br.org.flem.sav.relatorio.SAVCriadorRelatorio;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 *
 * @author mccosta
 */
public class PrestacaoContasAction extends SecurityDispatchAction{

    @Funcionalidade(nomeCurto="solicRecbPrestConta")
    public ActionForward listaRecebimento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Collection<ViagemDTO> viagensL1DTO = new ArrayList<ViagemDTO>();
        Collection<ViagemDTO> viagensL2DTO = new ArrayList<ViagemDTO>();
        Collection<Viagem> viagensComPrestacaoConta = new ViagemBO().obterTodosComPrestacaoConta();
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        RHServico rh = new RHServico();
        IFuncionario iFunc = null;
        Map<Integer, IFuncionario> mapFunc = rh.obterMapTodos();
        Map<Integer, ICargo> mapCargo = rh.obterCargos();
        Map<String, Integer> mapProcesso = new ControleProcessoDAO().obterMapProcesso();
        Map<String, String> mapEnt = new EntidadeDAO().obterMapConsultoresPorNome("");
        
        for (Viagem viagem : viagensComPrestacaoConta) {
            ViagemDTO viagemDTO = new ViagemDTO();

            if (viagem.getProcesso() != null) {
                //vdto.setProcesso(mapProcesso.get(viagem.getProcesso().substring(0, viagem.getProcesso().indexOf("/"))));
                if (mapProcesso.get(viagem.getProcesso()) != null) {
                    viagemDTO.setProcesso(mapProcesso.get(viagem.getProcesso()));
                } else {
                    viagemDTO.setProcesso(0);
                }
            } else {
                viagemDTO.setProcesso(0);
            }

            String nome = "";
            Double porcentagem = null;
            
            if (viagem.getCodigoConsultorViajante() == null) {
                iFunc = mapFunc.get(viagem.getCodigoDominioUsuarioViajante());
                if (iFunc != null) {
                    nome = iFunc.getNome();
                }
            } else {
                nome = mapEnt.get(viagem.getCodigoConsultorViajante());
            }

            switch (viagem.getPrestacaoContas().getStatusPrestacaoContas()) {
                case PRESTACAO_INFORMADA:
                    viagemDTO.setNomeViajante(nome);
                    viagemDTO.setViagem(viagem);
                    viagemDTO.setPorcentagem(porcentagem);
                    viagemDTO.setPeriodo(simpleDate.format(viagem.getPrestacaoContas().getDataSaidaEfetiva()) + " " + simpleDate.format(viagem.getPrestacaoContas().getDataRetornoEfetiva()));
                    viagensL1DTO.add(viagemDTO);
                    break;

                case PRESTACAO_RECEBIDA:
                    viagemDTO.setNomeViajante(nome);
                    viagemDTO.setViagem(viagem);
                    viagemDTO.setPorcentagem(porcentagem);
                    viagemDTO.setPeriodo(simpleDate.format(viagem.getPrestacaoContas().getDataSaidaEfetiva()) + " " + simpleDate.format(viagem.getPrestacaoContas().getDataRetornoEfetiva()));
                    viagensL2DTO.add(viagemDTO);
                    break;

                case PRESTACAO_FINALIZADA:
                    /*viagemDTO.setNomeViajante(nome);
                    viagemDTO.setViagem(viagem);
                    viagemDTO.setPorcentagem(porcentagem);
                    viagemDTO.setPeriodo(simpleDate.format(viagem.getPrestacaoContas().getDataSaidaEfetiva()) + " " + simpleDate.format(viagem.getPrestacaoContas().getDataRetornoEfetiva()));
                    viagensL2DTO.add(viagemDTO);*/
                    break;

            }

        }

        request.setAttribute("lista1", viagensL1DTO);
        request.setAttribute("lista2", viagensL2DTO);

        return mapping.findForward("recebimentoLista");
    }

    public ActionForward novo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        String id = request.getParameter("id");
        ViagemDTO viagemDTO = new ViagemDTO();
        IFuncionario Ifunc = null;
        PercentualDiaria percentualDiaria = null;
        Usuario usr = null;
        

        try {
            PercentualDiariaBO percentualDiariaBO = new PercentualDiariaBO();
            if (GenericValidator.isInt(id)) {
                Viagem viagem = new ViagemBO().obterPorPk(Integer.valueOf(id));
                viagemDTO.setViagem(viagem);

                if (viagem.getCodigoConsultorViajante() == null) {

                    Ifunc = new RHServico().obterFuncionarioPorMatricula(viagem.getCodigoDominioUsuarioViajante());
                    viagemDTO.setNomeViajante(Ifunc.getNome());
                    
                    request.setAttribute("consultor", false);
                    
                    //OBTER O PERCENTUAL DE DIÁRIA DO VIAJANTE COM BASE EM SUA LOTAÇÃO
                    usr = (Usuario) new UsuarioBO().obterPorCodigoDominio(viagem.getCodigoDominioUsuarioViajante());
                    percentualDiaria = percentualDiariaBO.obterPorDepartamentoDominio(usr.getLotacaoDominio());
                } else {
                    viagemDTO.setNomeViajante(new EntidadeDAO().obterConsultorPorCodigo(viagem.getCodigoConsultorViajante()).getNome().toUpperCase());
                    request.setAttribute("consultor", true);
                }
                if(percentualDiaria == null){
                    percentualDiaria = percentualDiariaBO.obterPorDepartamentoDominio(0);//"PADRAO"
                }
                request.setAttribute("percentualDiaria", percentualDiaria);

                request.setAttribute("viagensDTO", viagemDTO);
                request.setAttribute("listaNaturezas", Natureza.values());
                request.setAttribute("listaTiposDiaria", TipoDiaria.toCollection());
                request.setAttribute("listaCentrosCusto", new CentroCustoDAO().obterCentroDeCusto());
                request.setAttribute("listaCentrosResponsabilidade", new CentroCustoDAO().obterCentroDeResponsabilidade());
                request.setAttribute("listaCompanhias", new CompanhiaBO().obterTodos());
                request.setAttribute("itensItinerario", new ItemItinerarioBO().obterPorItinerario(viagem.getItinerario()));
                request.setAttribute("itensTrecho", new TrechoBO().obterPorViagem(viagem));
                request.setAttribute("listaTipoGastos", new TipoGastoBO().obterTodosAtivos());
                request.setAttribute("tipoDiaria", viagem.getTipoDiaria().ordinal());

                dyna.set("viagem", viagem);
                dyna.set("destino", viagem.getDestino().name());
                dyna.set("dataSaidaPrevista", Data.formataData(viagem.getDataSaidaPrevista()));
                dyna.set("horaSaidaPrevista", Data.formataHoraCurta(viagem.getDataSaidaPrevista()));
                dyna.set("dataRetornoPrevista", Data.formataData(viagem.getDataRetornoPrevista()));
                dyna.set("horaRetornoPrevista", Data.formataHoraCurta(viagem.getDataRetornoPrevista()));

                dyna.set("dataSaidaEfetiva", Data.formataData(viagem.getDataSaidaPrevista()));
                dyna.set("horaSaidaEfetiva", Data.formataHoraCurta(viagem.getDataSaidaPrevista()));
                dyna.set("dataRetornoEfetiva", Data.formataData(viagem.getDataRetornoPrevista()));
                dyna.set("horaRetornoEfetiva", Data.formataHoraCurta(viagem.getDataRetornoPrevista()));

                dyna.set("diaria", String.valueOf(viagem.getDiaria()));
                dyna.set("diariaP", String.valueOf(viagem.getDiaria()));
                dyna.set("totalDiariaOriginal", String.valueOf(viagem.getTotalDiarias()));
                dyna.set("descricaoE", viagem.getDescricaoE());
                dyna.set("descricaoEOriginal", viagem.getDescricaoE());
                dyna.set("qtDiaria", String.valueOf(viagem.getQtDiaria()));
                dyna.set("totalDiarias", String.valueOf(viagem.getTotalDiarias()));
                dyna.set("totalAdiantamento", String.valueOf(viagem.getTotalAdiantamento()));
                dyna.set("valorAdiantamento", String.valueOf(viagem.getValorAdiantamento()));

                dyna.set("observacao", viagem.getItinerario().getObservacao());

                //request.setAttribute("listaTiposDiaria", TipoDiaria.toCollection());

            }
            return mapping.findForward("novo");
        } catch (AplicacaoException ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar selecionar a Solicitação.");
        }
        return mapping.findForward("redirect");
    }

    public ActionForward adicionar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);

        try {
            Itinerario itinerario = new Itinerario();
            ItemItinerario itemItinerario = null;
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            PrestacaoContas prestacaoContas = new PrestacaoContas();
            PrestacaoContasBO prestacaoContasBO = new PrestacaoContasBO();
            Double totalSaldo = 0.0;

            Viagem viagemForm = (Viagem) dyna.get("viagem");
            Viagem viagem = new ViagemBO().obterPorPk(viagemForm.getId());
            if(viagem.getStatusViagem().equals(StatusViagem.VIAGEM_RECEBIDA)){
                String tipoDiaria = (String) dyna.get("tipoDiaria");
                String dataSaidaEfetiva = (String) dyna.get("dataSaidaEfetiva");
                String horaSaidaEfetiva = (String) dyna.get("horaSaidaEfetiva");
                String dataRetornoEfetiva = (String) dyna.get("dataRetornoEfetiva");
                String horaRetornoEfetiva = (String) dyna.get("horaRetornoEfetiva");
                //String totalAdiantamento = (String) dyna.get("totalAdiantamento");
                //String totalDiarias = (String) dyna.get("totalDiarias");
                String[] data = (String[]) dyna.get("data");
                String[] cidadeO = (String[]) dyna.get("cidadeO");
                String[] cidadeD = (String[]) dyna.get("cidadeD");
                String[] obs = (String[]) dyna.get("obs");
                String observacao = (String) dyna.get("observacao");
                String qtdeDoc = (String) dyna.get("qtdeDoc");
                String qtDiaria = (String) dyna.get("qtDiaria");
                String diaria = (String) dyna.get("diaria");
                String descricaoE = (String) dyna.get("descricaoE");

                String tot2 = (String) dyna.get("tot2");
                String vd2 = (String) dyna.get("vd2");

                //trecho
                String[] dataI = (String[]) dyna.get("dataI");
                String[] horaI = (String[]) dyna.get("horaI");
                String[] dataF = (String[]) dyna.get("dataF");
                String[] horaF = (String[]) dyna.get("horaF");
                String[] qtDiariaT = (String[]) dyna.get("qtDiariaT");
                String[] valorDiariaT = (String[]) dyna.get("valorDiariaT");
                //String[] totalDiariaT = (String[]) dyna.get("totalDiariaT");
                String[] descT = (String[]) dyna.get("descT");

                //gasto
                String[] idsTipoGastos = (String[]) dyna.get("idGasto");
                String[] vlrGastos = (String[]) dyna.get("vlrGasto");

                //prestacaoContas.setTipoDiaria(viagem.getTipoDiaria());
                prestacaoContas.setTipoDiaria(TipoDiaria.getById(Integer.valueOf(tipoDiaria)));


                prestacaoContas.setDataPrestacao(Calendar.getInstance().getTime());
                prestacaoContas.setCodigoDominioUsuarioPrestacao(usuario.getCodigoDominio());

                prestacaoContas.setDataSaidaEfetiva(simpleDate.parse(dataSaidaEfetiva + " " + horaSaidaEfetiva));
                prestacaoContas.setDataRetornoEfetiva(simpleDate.parse(dataRetornoEfetiva + " " + horaRetornoEfetiva));

                prestacaoContas.setQtDiariaEfetiva(Valores.desformataValor(qtDiaria));

                //prestacaoContas.setTotalAdiantamento(Valores.desformataValor(totalAdiantamento));
                prestacaoContas.setTotalAdiantamento(Valores.desformataValor(tot2));
                //prestacaoContas.setTotalDiarias(Valores.desformataValor(totalDiarias));
                prestacaoContas.setTotalDiarias(Valores.desformataValor(vd2));

                prestacaoContas.setDescricaoE("");
                if(prestacaoContas.getTipoDiaria().equals(TipoDiaria.ESPECIAL)){
                    prestacaoContas.setDescricaoE(descricaoE);
                }

                prestacaoContas.setDiariaEfetiva(Valores.desformataValor(diaria));

                prestacaoContas.setQuantidadeDocumentos(Integer.valueOf(qtdeDoc));

                prestacaoContas.setObservacao(observacao);

                HibernateUtil.beginTransaction();
                itinerario.setObservacao(observacao);
                Object objI = new ItinerarioBO().inserir(itinerario);
                itinerario = new ItinerarioBO().obterPorPk((Integer) objI);
                for (int i = 0; i < obs.length; i++) {
                    Cidade cO = null;
                    if (cidadeO[i].contains(" / ")) {
                        cO = new CidadeBO().obterPorNome(cidadeO[i].substring(0, cidadeO[i].indexOf(" / ")), cidadeO[i].substring(cidadeO[i].indexOf(" / ")+3,cidadeO[i].length()));
                    } else {
                        cO = new CidadeBO().obterPorNome(cidadeO[i]);
                    }
                    Cidade cD = null;
                    if (cidadeD[i].contains(" / ")) {
                        cD = new CidadeBO().obterPorNome(cidadeD[i].substring(0, cidadeD[i].indexOf(" / ")), cidadeD[i].substring(cidadeD[i].indexOf(" / ")+3,cidadeD[i].length()));
                    } else {
                        cD = new CidadeBO().obterPorNome(cidadeD[i]);
                    }
                    itemItinerario = new ItemItinerario();
                    itemItinerario.setData(Data.formataData(data[i]));
                    itemItinerario.setOrigem(cO);
                    itemItinerario.setDestino(cD);
                    itemItinerario.setObservacoes(obs[i]);
                    itemItinerario.setItinerario(itinerario);
                    new ItemItinerarioBO().inserir(itemItinerario);
                }
                prestacaoContas.setItinerario(itinerario);
                prestacaoContas.setStatusPrestacaoContas(StatusPrestacaoContas.PRESTACAO_INFORMADA);

                Object objP = prestacaoContasBO.inserir(prestacaoContas);
                prestacaoContas = prestacaoContasBO.obterPorPk((Integer) objP);

                viagem.setPrestacaoContas(prestacaoContas);

                new ViagemBO().alterar(viagem);

                TrechoEfetivoBO trechoEfetivoBO = new TrechoEfetivoBO();
                for (int i = 0; i < descT.length; i++) {
                    TrechoEfetivo trechoEfetivo = new TrechoEfetivo();
                    trechoEfetivo.setPrestacaoContas(prestacaoContas);
                    trechoEfetivo.setDataInicio(Data.formataData(dataI[i] + " " + horaI[i] + ":00", "dd/MM/yyyy HH:mm:ss"));
                    trechoEfetivo.setDataFim(Data.formataData(dataF[i] + " " + horaF[i] + ":00", "dd/MM/yyyy HH:mm:ss"));
                    trechoEfetivo.setQtDiaria(Double.valueOf(Valores.desformataValor(qtDiariaT[i])));
                    trechoEfetivo.setDiaria(Double.valueOf(Valores.desformataValor(valorDiariaT[i])));
                    trechoEfetivo.setObservacao(descT[i]);

                    trechoEfetivoBO.inserir(trechoEfetivo);
                }

                for (int i = 0; i < idsTipoGastos.length; i++) {
                    TipoGasto tipoGasto = new TipoGasto();
                    tipoGasto.setId(Integer.valueOf(idsTipoGastos[i]));
                    Gasto gasto = new Gasto();
                    gasto.setTipoGasto(tipoGasto);
                    gasto.setValor(Double.valueOf(Valores.desformataValor(vlrGastos[i])));
                    gasto.setPrestacaoContas(prestacaoContas);
                    new GastoBO().inserir(gasto);
                }


                //totalSaldo > 0 -> À DEVOLVER
                //totalSaldo < 0 -> À RECEBER
                totalSaldo = viagem.getTotalAdiantamento() - prestacaoContas.getTotalAdiantamento();


                if (totalSaldo < 0) {
                    /*
                        Programa uma data para a FLEM pagar o viajante. 
                        Essa data será levada em consideração para o cálculo dos 50% das 
                        diárias do mês em que foi feito o pagamento.
                    */

                    //Pega a data de hoje e acrescenta mais 2 dias.
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, +2);
                    /*
                       Trata se a data de vencimento não cai em final de semana, 
                       nesse caso busca o próximo dia útil.
                    */
                    while (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        calendar.add(Calendar.DAY_OF_MONTH, +1);
                    }
                    prestacaoContas.setDataPagamento(calendar.getTime());
                    prestacaoContas.setSituacaoDataPagamento(SituacaoDataPagamentoEnum.PREVISTA);
                }else{
                    /*
                        A data de pagamento será a mesma data do pagamento da solicitação de viagem.
                    */
                    prestacaoContas.setDataPagamento(viagem.getDataPagamento());
                    prestacaoContas.setSituacaoDataPagamento(SituacaoDataPagamentoEnum.PREVISTA);
                }
                prestacaoContasBO.alterar(prestacaoContas);


                HibernateUtil.commitTransaction();

                MensagemTagUtil.adicionarMensagem(request.getSession(), "Prestação de Contas informada com sucesso.");
            }else{ 
                MensagemTagUtil.adicionarMensagem(request.getSession(), "Para gerar a Prestação de Contas é necessário que a viagem esteja com o status de recebida");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao informar a Prestação de Contas.");
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(CargoDiariaAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return mapping.findForward("redirect");
    }

    public ActionForward cancelar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);

        try {
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            PrestacaoContas prestacaoContas = new PrestacaoContas();

            ViagemBO viagemBO = new ViagemBO();

            Viagem viagemForm = (Viagem) dyna.get("viagem");
            Viagem viagem = viagemBO.obterPorPk(viagemForm.getId());

            String dataSaidaEfetiva = (String) dyna.get("dataSaidaEfetiva");
            String horaSaidaEfetiva = (String) dyna.get("horaSaidaEfetiva");
            Date efetiva = simpleDate.parse(dataSaidaEfetiva + " " + horaSaidaEfetiva);

            prestacaoContas.setDataPrestacao(Calendar.getInstance().getTime());
            prestacaoContas.setDataSaidaEfetiva(efetiva);
            prestacaoContas.setDataRetornoEfetiva(efetiva);
            prestacaoContas.setQtDiariaEfetiva(0.0);
            prestacaoContas.setTotalAdiantamento(0.0);
            prestacaoContas.setTotalDiarias(0.0);
            prestacaoContas.setTipoDiaria(viagem.getTipoDiaria());

            prestacaoContas.setQuantidadeDocumentos(0);
            prestacaoContas.setObservacao("Viagem cancelada");

            HibernateUtil.beginTransaction();
            prestacaoContas.setItinerario(null);

            prestacaoContas.setStatusPrestacaoContas(StatusPrestacaoContas.PRESTACAO_INFORMADA);
            prestacaoContas.setCodigoDominioUsuarioPrestacao(usuario.getCodigoDominio());
            prestacaoContas.setDataPrestacao(new Date());

            Object objP = new PrestacaoContasBO().inserir(prestacaoContas);
            prestacaoContas = new PrestacaoContasBO().obterPorPk((Integer) objP);

            viagem.setPrestacaoContas(prestacaoContas);
            viagem.setStatusViagem(StatusViagem.VIAGEM_CANCELADA);

            viagemBO.alterar(viagem);

            if (viagem.getProcesso() != null && viagem.getAnoProcesso() != null) {
                new ControleProcessoDAO().cancelaProcessoNumero(viagem.getProcesso());
            }

            HibernateUtil.commitTransaction();

            MensagemTagUtil.adicionarMensagem(request.getSession(), "Prestação de Contas cancelada com sucesso.");

        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao cancelar a Prestação de Contas.");
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(CargoDiariaAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }


        return mapping.findForward("redirect");
    }

    public ActionForward selecionar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm dyna = (DynaActionForm) form;
        String id = request.getParameter("id");
        ViagemDTO viagemDTO = new ViagemDTO();
        IFuncionario Ifunc = null;
        
        PercentualDiaria percentualDiaria = null;
        Usuario usr = null;
        
        try {
            PercentualDiariaBO percentualDiariaBO = new PercentualDiariaBO();
            
            if (GenericValidator.isInt(id)) {
                Viagem viagem = new ViagemBO().obterPorPk(Integer.valueOf(id));
                viagemDTO.setViagem(viagem);

                if (viagem.getCodigoConsultorViajante() == null) {
                    Ifunc = new RHServico().obterFuncionarioPorMatricula(viagem.getCodigoDominioUsuarioViajante());
                    viagemDTO.setNomeViajante(Ifunc.getNome());
                    request.setAttribute("consultor", false);
                    
                    //OBTER O PERCENTUAL DE DIÁRIA DO VIAJANTE COM BASE EM SUA LOTAÇÃO
                    usr = (Usuario) new UsuarioBO().obterPorCodigoDominio(viagem.getCodigoDominioUsuarioViajante());
                    percentualDiaria = percentualDiariaBO.obterPorDepartamentoDominio(usr.getLotacaoDominio());
                } else {
                    viagemDTO.setNomeViajante(new EntidadeDAO().obterConsultorPorCodigo(viagem.getCodigoConsultorViajante()).getNome().toUpperCase());
                    request.setAttribute("consultor", true);
                }
                
                //OBTER O PERCENTUAL DE DIÁRIA DO VIAJANTE COM BASE EM SUA LOTAÇÃO
                if(percentualDiaria == null){
                    percentualDiaria = percentualDiariaBO.obterPorDepartamentoDominio(0);//"PADRAO"
                }
                request.setAttribute("percentualDiaria", percentualDiaria);

                request.setAttribute("viagensDTO", viagemDTO);
                request.setAttribute("listaNaturezas", Natureza.values());
                request.setAttribute("listaTiposDiaria", TipoDiaria.toCollection());
                request.setAttribute("listaCentrosCusto", new CentroCustoDAO().obterCentroDeCusto());
                request.setAttribute("listaCentrosResponsabilidade", new CentroCustoDAO().obterCentroDeResponsabilidade());
                request.setAttribute("listaCompanhias", new CompanhiaBO().obterTodos());
                request.setAttribute("listaTipoGastos", new TipoGastoBO().obterTodosAtivos());

                request.setAttribute("tipoDiaria", viagem.getPrestacaoContas().getTipoDiaria().ordinal());
                /*Verifica se a prestação tenha sido cancelada. O intinerário ficará null */

                request.setAttribute("itensItinerario", viagem.getPrestacaoContas().getItinerario() != null ? new ItemItinerarioBO().obterPorItinerario(viagem.getPrestacaoContas().getItinerario()) : new ArrayList<ItemItinerario>());
                request.setAttribute("itensTrecho", new TrechoEfetivoBO().obterPorPrestacao(viagem.getPrestacaoContas()));


                //dyna.set("observacao", viagem.getPrestacaoContas().getItinerario() != null ? viagem.getPrestacaoContas().getItinerario().getObservacao() : viagem.getPrestacaoContas().getObservacao());
                dyna.set("observacao", viagem.getPrestacaoContas().getObservacao());

                dyna.set("viagem", viagem);
                dyna.set("destino", viagem.getDestino().name());
                dyna.set("dataSaidaPrevista", Data.formataData(viagem.getDataSaidaPrevista()));
                dyna.set("horaSaidaPrevista", Data.formataHoraCurta(viagem.getDataSaidaPrevista()));
                dyna.set("dataRetornoPrevista", Data.formataData(viagem.getDataRetornoPrevista()));
                dyna.set("horaRetornoPrevista", Data.formataHoraCurta(viagem.getDataRetornoPrevista()));

                dyna.set("dataSaidaEfetiva", Data.formataData(viagem.getPrestacaoContas().getDataSaidaEfetiva()));
                dyna.set("horaSaidaEfetiva", Data.formataHoraCurta(viagem.getPrestacaoContas().getDataSaidaEfetiva()));
                dyna.set("dataRetornoEfetiva", Data.formataData(viagem.getPrestacaoContas().getDataRetornoEfetiva()));
                dyna.set("horaRetornoEfetiva", Data.formataHoraCurta(viagem.getPrestacaoContas().getDataRetornoEfetiva()));

                dyna.set("diaria", String.valueOf(viagem.getPrestacaoContas().getDiariaEfetiva()));
                dyna.set("diariaP", String.valueOf(viagem.getPrestacaoContas().getDiariaEfetiva()));
                dyna.set("totalDiariaOriginal", String.valueOf(viagem.getTotalDiarias()));
                dyna.set("diariaE", String.valueOf(viagem.getPrestacaoContas().getDiariaEfetiva()));
                dyna.set("descricaoE", viagem.getPrestacaoContas().getDescricaoE());
                dyna.set("descricaoEOriginal", viagem.getPrestacaoContas().getDescricaoE());

                dyna.set("qtDiaria", String.valueOf(viagem.getPrestacaoContas().getQtDiariaEfetiva()));
                dyna.set("totalDiarias", String.valueOf(viagem.getPrestacaoContas().getTotalDiarias()));
                //dyna.set("totalAdiantamento", String.valueOf(viagem.getPrestacaoContas().getTotalAdiantamento()));
                dyna.set("totalAdiantamento", String.valueOf(viagem.getPrestacaoContas().getTotalDiarias() + viagem.getValorAdiantamento()));
                dyna.set("valorAdiantamentoP", String.valueOf(viagem.getValorAdiantamento()));
                dyna.set("valorAdiantamento", String.valueOf(viagem.getValorAdiantamento()));
                PrestacaoContas pc = viagem.getPrestacaoContas();
                dyna.set("qtdeDoc", String.valueOf(viagem.getPrestacaoContas().getQuantidadeDocumentos()));
                request.setAttribute("listaGastos", new GastoBO().obterPorPrestacaoConta(pc));
                request.setAttribute("statusPrestacao", pc.getStatusPrestacaoContas());
                //request.setAttribute("listaTiposDiaria", TipoDiaria.toCollection());

            }
            return mapping.findForward("editar");
        } catch (AplicacaoException ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar selecionar a Solicitação.");
        }
        return mapping.findForward("redirect");
    }

    public ActionForward selecionarDataPagamento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            DynaActionForm dyna = (DynaActionForm) form;
            String id = request.getParameter("id");
            try {
                if (GenericValidator.isInt(id)) {
                    Viagem viagem = new ViagemBO().obterPorPk(Integer.valueOf(id));
                    dyna.set("viagem", viagem);
                    dyna.set("dataPagamento", Data.formataData(viagem.getPrestacaoContas().getDataPagamento()));
                    request.setAttribute("viagem", viagem);
                }
                return mapping.findForward("editarDataPagamento");
            } catch (AplicacaoException ex) {
                ex.printStackTrace();
                MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar selecionar a Prestação de Contas.");
            }
            return listaRecebimento(mapping, form, request, response);
        } catch (Exception ex) {
            Logger.getLogger(PrestacaoContasAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapping.findForward("redirect");
    }

    public ActionForward alterarDataPagamento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            DynaActionForm dyna = (DynaActionForm) form;
            try {
                SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
                Viagem viagemForm = (Viagem) dyna.get("viagem");
                Viagem viagem = new ViagemBO().obterPorPk(viagemForm.getId());
                PrestacaoContas prestacaoContas = new PrestacaoContasBO().obterPorPk(viagem.getPrestacaoContas());
                String dataPagamentoString = (String) dyna.get("dataPagamento");
                Date dataPagamento = simpleDate.parse(dataPagamentoString);
                prestacaoContas.setDataPagamento(dataPagamento);
                if (new Diarias().verificar(dataPagamento, new RHServico().obterFuncionarioPorMatricula(viagem.getCodigoDominioUsuarioViajante()))) {//300
                    new PrestacaoContasBO().alterar(prestacaoContas);
                } else {
                    MensagemTagUtil.adicionarMensagem(request.getSession(), "Não foi possível alterar a data de pagamento pois, na data selecionada, o funcionário já atingiu os 50% do salário em diárias");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar selecionar a Prestação de Contas.");
            }
            return listaRecebimento(mapping, form, request, response);
        } catch (Exception ex) {
            Logger.getLogger(PrestacaoContasAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapping.findForward("redirect");
    }

    public ActionForward alterar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm dyna = (DynaActionForm) form;
        IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);

        try {
            ItemItinerario itemItinerario = null;
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            Viagem viagemForm = (Viagem) dyna.get("viagem");
            Viagem viagem = new ViagemBO().obterPorPk(viagemForm.getId());
            PrestacaoContas prestacaoContas = viagem.getPrestacaoContas();

            String tipoDiaria = (String) dyna.get("tipoDiaria");
            String dataSaidaEfetiva = (String) dyna.get("dataSaidaEfetiva");
            String horaSaidaEfetiva = (String) dyna.get("horaSaidaEfetiva");
            String dataRetornoEfetiva = (String) dyna.get("dataRetornoEfetiva");
            String horaRetornoEfetiva = (String) dyna.get("horaRetornoEfetiva");
            String totalAdiantamento = (String) dyna.get("totalAdiantamento");
            String totalDiarias = (String) dyna.get("totalDiarias");
            String diaria = (String) dyna.get("diaria");
            String descricaoE = (String) dyna.get("descricaoE");

            String tot2 = (String) dyna.get("tot2");
            String vd2 = (String) dyna.get("vd2");

            String[] data = (String[]) dyna.get("data");
            String[] cidadeO = (String[]) dyna.get("cidadeO");
            String[] cidadeD = (String[]) dyna.get("cidadeD");
            String[] obs = (String[]) dyna.get("obs");

            String qtDiaria = (String) dyna.get("qtDiaria");
            String observacao = (String) dyna.get("observacao");
            String qtdeDoc = (String) dyna.get("qtdeDoc");
            //String telServico = (String) dyna.get("telServico");
            //String tUrbanos = (String) dyna.get("tUrbanos");
            //String translado = (String) dyna.get("translado");
            //String passagens = (String) dyna.get("passagens");

            //trecho
            String[] dataI = (String[]) dyna.get("dataI");
            String[] horaI = (String[]) dyna.get("horaI");
            String[] dataF = (String[]) dyna.get("dataF");
            String[] horaF = (String[]) dyna.get("horaF");
            String[] qtDiariaT = (String[]) dyna.get("qtDiariaT");
            String[] valorDiariaT = (String[]) dyna.get("valorDiariaT");
            String[] totalDiariaT = (String[]) dyna.get("totalDiariaT");
            String[] descT = (String[]) dyna.get("descT");

            //gasto
            String[] idsTipoGastos = (String[]) dyna.get("idGasto");
            String[] vlrGastos = (String[]) dyna.get("vlrGasto");

            HibernateUtil.beginTransaction();
            
            prestacaoContas.setCodigoDominioUsuarioPrestacao(usuario.getCodigoDominio());

            prestacaoContas.setDataSaidaEfetiva(simpleDate.parse(dataSaidaEfetiva + " " + horaSaidaEfetiva));
            prestacaoContas.setDataRetornoEfetiva(simpleDate.parse(dataRetornoEfetiva + " " + horaRetornoEfetiva));

            prestacaoContas.setQuantidadeDocumentos(Integer.valueOf(qtdeDoc));
            prestacaoContas.setQtDiariaEfetiva(Valores.desformataValor(qtDiaria));

            prestacaoContas.setDiariaEfetiva(Valores.desformataValor(diaria));

            //prestacaoContas.setTotalAdiantamento(Valores.desformataValor(totalAdiantamento));
            prestacaoContas.setTotalAdiantamento(Valores.desformataValor(tot2));
            //prestacaoContas.setTotalDiarias(Valores.desformataValor(totalDiarias));
            prestacaoContas.setTotalDiarias(Valores.desformataValor(vd2));

            //prestacaoContas.setTipoDiaria(viagem.getPrestacaoContas().getTipoDiaria());
            prestacaoContas.setTipoDiaria(TipoDiaria.getById(Integer.valueOf(tipoDiaria)));

            prestacaoContas.setDescricaoE("");
            if(prestacaoContas.getTipoDiaria().equals(TipoDiaria.ESPECIAL)){
                prestacaoContas.setDescricaoE(descricaoE);
            }

            prestacaoContas.setObservacao(observacao);

            ItemItinerarioBO itemItinerarioBO = new ItemItinerarioBO();
            itemItinerarioBO.excluir(prestacaoContas.getItinerario().getItensItinerario());

            Itinerario itinerario = new ItinerarioBO().obterPorPk(prestacaoContas.getItinerario().getId());
            itinerario.setObservacao(observacao);

            for (int i = 0; i < obs.length; i++) {
                Cidade cO = null;
                if (cidadeO[i].contains(" / ")) {
                    cO = new CidadeBO().obterPorNome(cidadeO[i].substring(0, cidadeO[i].indexOf(" / ")), cidadeO[i].substring(cidadeO[i].indexOf(" / ")+3,cidadeO[i].length()));
                } else {
                    cO = new CidadeBO().obterPorNome(cidadeO[i]);
                }
                Cidade cD = null;
                if (cidadeD[i].contains(" / ")) {
                    cD = new CidadeBO().obterPorNome(cidadeD[i].substring(0, cidadeD[i].indexOf(" / ")), cidadeD[i].substring(cidadeD[i].indexOf(" / ")+3,cidadeD[i].length()));
                } else {
                    cD = new CidadeBO().obterPorNome(cidadeD[i]);
                }
                itemItinerario = new ItemItinerario();
                itemItinerario.setData(Data.formataData(data[i]));
                itemItinerario.setOrigem(cO);
                itemItinerario.setDestino(cD);
                itemItinerario.setObservacoes(obs[i]);
                itemItinerario.setItinerario(itinerario);
                itemItinerarioBO.inserir(itemItinerario);
            }
            new GastoBO().excluir(prestacaoContas.getGastos());
            for (int i = 0; i < idsTipoGastos.length; i++) {
                TipoGasto tipoGasto = new TipoGasto();
                tipoGasto.setId(Integer.valueOf(idsTipoGastos[i]));
                Gasto gasto = new Gasto();
                gasto.setTipoGasto(tipoGasto);
                gasto.setValor(Double.valueOf(Valores.desformataValor(vlrGastos[i])));
                gasto.setPrestacaoContas(prestacaoContas);
                new GastoBO().inserir(gasto);
            }
            if (prestacaoContas.getTotalDiarias() > viagem.getTotalDiarias()) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, +2);
                prestacaoContas.setDataPagamento(calendar.getTime());
                prestacaoContas.setSituacaoDataPagamento(SituacaoDataPagamentoEnum.PREVISTA);
            }
            new PrestacaoContasBO().alterar(prestacaoContas);

            TrechoEfetivoBO trechoEfetivoBO = new TrechoEfetivoBO();
            trechoEfetivoBO.excluir(viagem.getPrestacaoContas().getTrechosEfetivo());
            for (int i = 0; i < descT.length; i++) {
                TrechoEfetivo trechoEfetivo = new TrechoEfetivo();
                trechoEfetivo.setPrestacaoContas(prestacaoContas);
                trechoEfetivo.setDataInicio(Data.formataData(dataI[i] + " " + horaI[i] + ":00", "dd/MM/yyyy HH:mm:ss"));
                trechoEfetivo.setDataFim(Data.formataData(dataF[i] + " " + horaF[i] + ":00", "dd/MM/yyyy HH:mm:ss"));
                trechoEfetivo.setQtDiaria(Double.valueOf(Valores.desformataValor(qtDiariaT[i])));
                trechoEfetivo.setDiaria(Double.valueOf(Valores.desformataValor(valorDiariaT[i])));
                trechoEfetivo.setObservacao(descT[i]);

                trechoEfetivoBO.inserir(trechoEfetivo);
            }

            HibernateUtil.commitTransaction();

            MensagemTagUtil.adicionarMensagem(request.getSession(), "Prestação de Contas alterada com sucesso.");

        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao alterar a Prestação de Contas.");
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(CargoDiariaAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return mapping.findForward("redirect");
    }

    public ActionForward relatorioHtml(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        RelatorioViagem relatorioViagem = new RelatorioViagem();
        Collection<ItemItinerarioDTO> itens = new ArrayList<ItemItinerarioDTO>();
        Collection<TrechoEfetivo> trechos = null;
        Collection<Gasto> gastos = new ArrayList<Gasto>();
        IFuncionario Ifunc = null;
        IColaborador consultor = null;
        RHServico rh = new RHServico();

        try {
            if (GenericValidator.isInt(id)) {
                Viagem viagem = new ViagemBO().obterPorPk(Integer.valueOf(id));
                relatorioViagem.setViagem(viagem);
                Double totalGasto = 0.0;
                Double totalGastos = 0.0;
                Double totalRecebido = 0.0;


                String tipoMoeda = "R$ ";
                if(viagem.getDestino().equals(DestinoViagem.EXTERIOR)){
                    tipoMoeda = "US$ ";
                }

                if(viagem.getCodigoDominioUsuarioSolicitante() != null){
                    Ifunc = rh.obterFuncionarioPorMatricula(viagem.getPrestacaoContas().getCodigoDominioUsuarioPrestacao() != null ? viagem.getPrestacaoContas().getCodigoDominioUsuarioPrestacao() : viagem.getCodigoDominioUsuarioSolicitante());
                }else{
                    Ifunc = new Funcionario();
                }
                relatorioViagem.setNomeSolicitante(Ifunc.getNome());
                if (viagem.getCodigoConsultorViajante() == null) {
                    Ifunc = rh.obterFuncionarioPorMatricula(viagem.getCodigoDominioUsuarioViajante());
                    relatorioViagem.setNomeViajante(Ifunc.getNome());
                    relatorioViagem.setCargoViajante(rh.obterCargoById(viagem.getCodigoDominioCargoViajante()).getNome());
                    relatorioViagem.setDepartamentoViajante(rh.obterDepartamentoPorCodigo(viagem.getCodigoDominioDepartamentoViajante().toString()).getNome());
                    relatorioViagem.setCpfViajante(Ifunc.getCpf());
                    relatorioViagem.setTelViajante(Ifunc.getTelefone());
                    relatorioViagem.setBancoViajante(Ifunc.getBanco());
                    relatorioViagem.setAgViajante(Ifunc.getAgencia());
                    relatorioViagem.setCcViajante(Ifunc.getConta());

                } else {
                    consultor = new EntidadeDAO().obterConsultorPorCodigo(viagem.getCodigoConsultorViajante());
                    relatorioViagem.setNomeViajante(consultor.getNome());
                    relatorioViagem.setCargoViajante("Consultor");
                    relatorioViagem.setDepartamentoViajante("");
                    if(viagem.getCodigoCentroResponsabilidade() != null && !viagem.getCodigoCentroResponsabilidade().isEmpty()){
                        relatorioViagem.setDepartamentoViajante(new CentroCustoDAO().obterCentroResponsabilidadePorId(viagem.getCodigoCentroResponsabilidade()).getDescricao());
                    }
                    relatorioViagem.setCpfViajante(consultor.getCpf());
                    relatorioViagem.setTelViajante("-");
                    relatorioViagem.setBancoViajante(consultor.getBanco());
                    relatorioViagem.setAgViajante(consultor.getAgencia());
                    relatorioViagem.setCcViajante(consultor.getConta());
                }

                relatorioViagem.setCentroCusto("-");
                if (viagem.getCodigoCentroCusto() != null) {
                    //relatorioViagem.setCentroCusto(new CentroCustoDAO().obterCentroCustoOuResponsabilidadePorId(viagem.getCodigoCentroCusto()).getDescricaoCompleta()); 
                    relatorioViagem.setCentroCusto(new CentroCustoDAO().obterCentroDeCustoPorId(viagem.getCodigoCentroCusto()).getDescricaoCompleta());
                }

                relatorioViagem.setCentroResponsabilidade("-");
                if (viagem.getCodigoCentroResponsabilidade() != null) {
                    //relatorioViagem.setCentroResponsabilidade(new CentroCustoDAO().obterCentroCustoOuResponsabilidadePorId(viagem.getCodigoCentroResponsabilidade()).getDescricaoCompleta());
                    relatorioViagem.setCentroResponsabilidade(new CentroCustoDAO().obterCentroDeResponsabilidadePorId(viagem.getCodigoCentroResponsabilidade()).getDescricaoCompleta());
                }

                relatorioViagem.setDataPrestacao(String.valueOf(Data.formataData(viagem.getPrestacaoContas().getDataPrestacao())));

                relatorioViagem.setNatureza(String.valueOf(viagem.getNatureza().getCodigo()));

                relatorioViagem.setDataSaidaPrevista(String.valueOf(Data.formataData(viagem.getPrestacaoContas().getDataSaidaEfetiva())));
                relatorioViagem.setHoraSaidaPrevista(String.valueOf(Data.formataHoraCurta(viagem.getPrestacaoContas().getDataSaidaEfetiva())));
                relatorioViagem.setDataRetornoPrevista(String.valueOf(Data.formataData(viagem.getPrestacaoContas().getDataRetornoEfetiva())));
                relatorioViagem.setHoraRetornoPrevista(String.valueOf(Data.formataHoraCurta(viagem.getPrestacaoContas().getDataRetornoEfetiva())));

                relatorioViagem.setTemReserva("NÃO");
                relatorioViagem.setCodigoReserva("-");
                relatorioViagem.setNomeCompanhia("-");
                if (viagem.getReserva() != null) {
                    relatorioViagem.setTemReserva("SIM");
                    //relatorioViagem.setCodigoReserva(viagem.getReserva().getCodigo());
                    relatorioViagem.setCodigoReserva("");
                    //relatorioViagem.setNomeCompanhia(viagem.getReserva().getCompanhia().getNome());
                    relatorioViagem.setNomeCompanhia("");
                }

                relatorioViagem.setDescricaoServico(viagem.getDescricao());

                /*if(viagem.getPrestacaoContas().getTipoDiaria() == null){
                    relatorioViagem.setTipoDiaria(String.valueOf(viagem.getTipoDiaria().getId()));
                }else{*/
                    relatorioViagem.setTipoDiaria(String.valueOf(viagem.getPrestacaoContas().getTipoDiaria().getId()));
                //}

                relatorioViagem.setDiariaPadrao(viagem.getDiaria());
                relatorioViagem.setDiariaEspecial(viagem.getPrestacaoContas().getDiariaEfetiva());

                relatorioViagem.setQtdDiarias(viagem.getQtDiaria());
                relatorioViagem.setQtdDiariasEfetivo(viagem.getPrestacaoContas().getQtDiariaEfetiva());

                relatorioViagem.setTotalDiariaPadrao(viagem.getTotalDiarias());
                //relatorioViagem.setTotalDiariaEfetivo(viagem.getPrestacaoContas().getTotalDiarias());
                relatorioViagem.setTotalDiariaEfetivo((viagem.getPrestacaoContas().getTotalDiarias() != null ? viagem.getPrestacaoContas().getTotalDiarias() : 0.0));


                relatorioViagem.setValorAdiantamento(viagem.getValorAdiantamento());

                relatorioViagem.setDataEfetivo((relatorioViagem.getDataSaidaPrevista() + " " + relatorioViagem.getHoraSaidaPrevista() + " a " + relatorioViagem.getDataRetornoPrevista() + " " + relatorioViagem.getHoraRetornoPrevista()));

                relatorioViagem.setDataSolicitacao(String.valueOf(Data.formataData(viagem.getPrestacaoContas().getDataPrestacao())));

                relatorioViagem.setObsDiariaEspecial(viagem.getDescricaoE());

                relatorioViagem.setQtdDoc(viagem.getPrestacaoContas().getQuantidadeDocumentos());

                NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "br"));
                numberFormat.setMinimumFractionDigits(2);
                numberFormat.setMaximumFractionDigits(2);

                if (viagem.getPrestacaoContas().getItinerario() != null) {
                    for (ItemItinerario itinerario : new ItemItinerarioBO().obterPorItinerario(viagem.getPrestacaoContas().getItinerario())) {
                        ItemItinerarioDTO itemItinerarioDTO = new ItemItinerarioDTO();
                        itemItinerarioDTO.setData(itinerario.getData());
                        itemItinerarioDTO.setOrigem(itinerario.getOrigem().getNome());
                        itemItinerarioDTO.setDestino(itinerario.getDestino().getNome());
                        itemItinerarioDTO.setId(itinerario.getId());
                        itemItinerarioDTO.setObservacoes(itinerario.getObservacoes());

                        itens.add(itemItinerarioDTO);
                    }
                }

                for (Gasto gasto : new GastoBO().obterPorPrestacaoConta(viagem.getPrestacaoContas())) {
                    Gasto g = new Gasto();
                    g.setTipoGasto(gasto.getTipoGasto());
                    g.setValor(gasto.getValor());
                    totalGasto += gasto.getValor();
                    gastos.add(g);
                }

                //System.out.println("totalRecebido = " + viagem.getTotalAdiantamento());
                //System.out.println("totalGastos = " + viagem.getPrestacaoContas().getTotalAdiantamento());

                //totalGastos = totalGasto + viagem.getPrestacaoContas().getTotalDiarias();
                //totalGastos = totalGasto + (viagem.getPrestacaoContas().getTotalDiarias() != null ? viagem.getPrestacaoContas().getTotalDiarias() : 0.0);
                totalGastos = viagem.getPrestacaoContas().getTotalAdiantamento();
                totalRecebido = viagem.getTotalAdiantamento();

                //relatorioViagem.setTotalDiariaSaldo(viagem.getTotalDiarias() - viagem.getPrestacaoContas().getTotalDiarias());
                relatorioViagem.setTotalDiariaSaldo(viagem.getTotalDiarias() - (viagem.getPrestacaoContas().getTotalDiarias() != null ? viagem.getPrestacaoContas().getTotalDiarias() : 0.0));
                if (relatorioViagem.getTotalDiariaSaldo() < 0.001 && relatorioViagem.getTotalDiariaSaldo() > -0.001) {
                    relatorioViagem.setTotalDiariaSaldo(0.0);
                }
                relatorioViagem.setAdiantamentoSaldo(viagem.getValorAdiantamento() - totalGasto);
                if (relatorioViagem.getAdiantamentoSaldo() < 0.001 && relatorioViagem.getAdiantamentoSaldo() > -0.001) {
                    relatorioViagem.setAdiantamentoSaldo(0.0);
                }
                //relatorioViagem.setTotalPrevisto(viagem.getTotalDiarias() + viagem.getValorAdiantamento());
                relatorioViagem.setTotalPrevisto(totalRecebido);
                //relatorioViagem.setTotalEfetivo(viagem.getPrestacaoContas().getTotalDiarias() + totalGasto);
                relatorioViagem.setTotalEfetivo((viagem.getPrestacaoContas().getTotalDiarias() != null ? viagem.getPrestacaoContas().getTotalDiarias() : 0.0) + totalGasto);

                relatorioViagem.setTotalSaldo(relatorioViagem.getTotalPrevisto() - relatorioViagem.getTotalEfetivo());

                trechos = new TrechoEfetivoBO().obterPorPrestacao(viagem.getPrestacaoContas());

                if ((totalRecebido - totalGastos) > 0) {
                    relatorioViagem.setTextoResumo("A DEVOLVER --> " + tipoMoeda + numberFormat.format(totalRecebido - totalGastos));
                } else {
                    if ((totalRecebido - totalGastos) < 0) {
                        relatorioViagem.setTextoResumo("A RECEBER --> " + tipoMoeda + numberFormat.format((totalRecebido - totalGastos) * (-1)));
                    } else {
                        relatorioViagem.setTextoResumo("SALDO --> " + tipoMoeda + "0,00");
                    }
                }

                request.setAttribute("relatorio", relatorioViagem);
                request.setAttribute("tipoMoeda", tipoMoeda);
                request.setAttribute("itensItinerario", itens);
                request.setAttribute("dataGeracao", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
                request.setAttribute("itensTrecho", trechos);
                request.setAttribute("sizeTrecho", trechos.size());
                //request.setAttribute("listaGastos", gastos);
                request.setAttribute("listaGastos", new GastoBO().obterPorPrestacaoConta(viagem.getPrestacaoContas()));
                request.setAttribute("totalGastos", totalGastos);
                request.setAttribute("totalGasto", totalGasto);
                request.setAttribute("temPeriodo", relatorioViagem.getDataEfetivo().length() > 8 ? true : false);
            }

            return mapping.findForward("relatorioHtml");
        } catch (AplicacaoException ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar selecionar a Solicitação.");
        }
        return unspecified(mapping, form, request, response);
    }

    public ActionForward relatorioPrestacoesPendentes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {

            Map<Integer, Integer> listaFunc = new ViagemBO().obterMatriculaQtViagensPrestacaoAberta(null);
            Map<String, Integer> listaCons = new ViagemBO().obterCodConsultorQtViagensPrestacaoAberta(null);
            Map<Integer, String> mapFunc = new RHServico().obterMatriculasPorNome("");
            //TODO QUANDO CORRIGIR O PROBLEMA DO GEM VOLTAR PARA FORMA MAIS PERFORMATICA
            //Map<String, String> mapEnt = new EntidadeDAO().obterCodigosPorNome("");
            Collection<ViagemDTO> viajantes = new ArrayList<ViagemDTO>();
            String nome = null;

            //IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
            //Collection<IFuncionario> IFList = new DepartamentoUsuarioDAO().obterFuncionariosPorDepartamento(usuario);

            for (Integer matricula : listaFunc.keySet()) {
                ViagemDTO viagemDTO = new ViagemDTO();
                nome = mapFunc.get(matricula);

                viagemDTO.setNomeViajante(nome);
                viagemDTO.setQtPrestacaoIrregular(listaFunc.get(matricula));
                viajantes.add(viagemDTO);
            }

            for (String codigo : listaCons.keySet()) {
                ViagemDTO viagemDTO = new ViagemDTO();
                //TODO QUANDO CORRIGIR O PROBLEMA DO GEM VOLTAR PARA FORMA MAIS PERFORMATICA
                //nome = mapEnt.get(codigo);
                viagemDTO.setNomeViajante(new EntidadeDAO().obterConsultorPorCodigo(codigo).getNome().toUpperCase());
                viagemDTO.setQtPrestacaoIrregular(listaCons.get(codigo));
                viajantes.add(viagemDTO);
            }



            Map parametros = new HashMap();
            String arquivoRelatorio = "";
            arquivoRelatorio = new String("/relatorio/ViajantesPrestacaoIrregular.jasper");

            new SAVCriadorRelatorio().exportaRelatorioPDF(request, response, arquivoRelatorio, parametros, viajantes);



        } catch (SQLException ex) {
            Logger.getLogger(PrestacaoContasAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(PrestacaoContasAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RelatorioSemDadosException ex) {
            Logger.getLogger(PrestacaoContasAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AplicacaoException ex) {
            Logger.getLogger(PrestacaoContasAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public ActionForward receber(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String[] id = request.getParameterValues("id");

        try {

            if (request.getParameterValues("ids_recebimento") != null) {
                id = request.getParameterValues("ids_recebimento");

                HibernateUtil.beginTransaction();
                for (int i = 0; i < id.length; i++) {
                    Viagem viagem = new ViagemBO().obterPorPk(Integer.valueOf(id[i]));
                    PrestacaoContas pc = viagem.getPrestacaoContas();
                    pc.setStatusPrestacaoContas(StatusPrestacaoContas.PRESTACAO_RECEBIDA);
                    new PrestacaoContasBO().alterar(pc);
                }
            }

            MensagemTagUtil.adicionarMensagem(request.getSession(), "Operação realizada com sucesso.");
            HibernateUtil.commitTransaction();

        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao receber a Prestação.");
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(PrestacaoContasAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return mapping.findForward("recebimentoRedirect");
    }

    public ActionForward desfazerRecebimento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] id = new String[0];

        try {
            if (request.getParameterValues("ids_exportacao") != null) {
                id = request.getParameterValues("ids_exportacao");

                HibernateUtil.beginTransaction();

                for (int i = 0; i < id.length; i++) {
                    Viagem viagem = new ViagemBO().obterPorPk(Integer.valueOf(id[i]));
                    PrestacaoContas pc = viagem.getPrestacaoContas();
                    pc.setStatusPrestacaoContas(StatusPrestacaoContas.PRESTACAO_INFORMADA);

                    new PrestacaoContasBO().alterar(pc);
                }

                MensagemTagUtil.adicionarMensagem(request.getSession(), "Operação realizada com sucesso.");
                HibernateUtil.commitTransaction();

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao desfazer recebimento da Viagem.");
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(PrestacaoContasAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return mapping.findForward("recebimentoRedirect");
    }

    public ActionForward exportar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] id = new String[0];
        Map<String, IFonteRecurso> mapFR = new FonteRecursoDAO().obterFonteRecursoPorCodigoAntigo("");
        List<Layout> layout = new ArrayList<Layout>();
        IFuncionario iFunc = null;
        IColaborador consultor = null;
        String cpf = null;
        //String entidade = null;
        boolean temContaBancaria = true;
        Double saldo = 0.0;

        Date dataInclusao = new Date();
        Date dataVencimento = null;
        Calendar cal = new GregorianCalendar();
        //Calendar calHj = new GregorianCalendar();

        PrestacaoContas prestacaoContas = null;
        PrestacaoContasBO prestacaoContasBO = new PrestacaoContasBO();
        
        PercentualDiariaBO percentualDiariaBO = new PercentualDiariaBO();
        PercentualDiaria percentualDiaria = null;
        Usuario usr = null;

        if (request.getParameterValues("ids_exportacao") != null) {
            id = request.getParameterValues("ids_exportacao");
        }

        for (int i = 0; i < id.length; i++) {
            ViagemBO viagemBO = new ViagemBO();
            Viagem viagem = viagemBO.obterPorPk(Integer.valueOf(id[i]));

            //se os gastos efetivos foram maior que os gastos previstos
            if (viagem.getPrestacaoContas().getTotalAdiantamento() > viagem.getTotalAdiantamento()) {
                saldo = viagem.getPrestacaoContas().getTotalAdiantamento() - viagem.getTotalAdiantamento();
                prestacaoContas = viagem.getPrestacaoContas();

                if (viagem.getCodigoConsultorViajante() == null) {
                    iFunc = new RHServico().obterFuncionarioPorMatricula(viagem.getCodigoDominioUsuarioViajante());

                    //if (iFunc.getAgencia().trim().equals("")) {//
                        //temContaBancaria = false;
                    //}
                    
                    cpf = iFunc.getCpf();
                    
                    //OBTER O PERCENTUAL DE DIÁRIA DO VIAJANTE COM BASE EM SUA LOTAÇÃO
                    usr = (Usuario) new UsuarioBO().obterPorCodigoDominio(viagem.getCodigoDominioUsuarioViajante());
                    percentualDiaria = percentualDiariaBO.obterPorDepartamentoDominio(usr.getLotacaoDominio());
                } else {
                    //entidade = viagem.getCodigoConsultorViajante();
                    consultor = new EntidadeDAO().obterConsultorPorCodigo(viagem.getCodigoConsultorViajante());
                    cpf = consultor.getCpf();
                }

                RegistroHeader header = new RegistroHeader();

                cal.setTime(viagem.getDataSaidaPrevista());

                if (viagem.getDestino() != DestinoViagem.EXTERIOR) {
                    header.setMoeda("R$");                                          ///////modificado
                    /*
                    // Pega a data de 2 dias (48 horas) atrás.
                    cal.add(Calendar.DAY_OF_MONTH, -2);

                    // Trata se a data de vencimento não cai em final de semana, nesse caso busca o dia útil anterior.
                    while (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        cal.add(Calendar.DAY_OF_MONTH, -1);
                    }

                    // Trata se a data de vencimento é a data de hoje, nesse caso busca o próximo dia útil.
                    if (calHj.getTimeInMillis() > cal.getTimeInMillis()) {
                        cal.setTimeInMillis(calHj.getTimeInMillis());
                        cal.add(Calendar.DAY_OF_MONTH, 2);
                    }

                    // Trata se a data de vencimento não cai em final de semana, nesse caso busca o próximo dia útil.
                    while (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                    }
                */
                } else {
                    header.setMoeda("U$");                                          ///////modificado
                    /*
                    // Pega a data de 4 dias (96 horas) atrás.
                    cal.add(Calendar.DAY_OF_MONTH, 4);

                    // Trata se a data de vencimento não cai em final de semana, nesse caso busca o dia útil anterior.
                    while (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        cal.add(Calendar.DAY_OF_MONTH, -1);
                    }

                    // Trata se a data de vencimento é a data de hoje, nesse caso busca o próximo dia útil.
                    if (calHj.getTimeInMillis() > cal.getTimeInMillis()) {
                        cal.setTimeInMillis(calHj.getTimeInMillis());
                        cal.add(Calendar.DAY_OF_MONTH, 2);
                    }

                    // Trata se a data de vencimento não cai em final de semana, nesse caso busca o próximo dia útil.
                    while (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    */
                }

                //if (prestacaoContas.getTotalDiarias() > viagem.getTotalDiarias()) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, +2);
                    prestacaoContas.setDataPagamento(calendar.getTime());
                    prestacaoContas.setSituacaoDataPagamento(SituacaoDataPagamentoEnum.PREVISTA);
                    prestacaoContasBO.alterar(prestacaoContas);
                    dataVencimento = prestacaoContas.getDataPagamento();
                //}

                header.setCompanhia("FE01");
                header.setGrupoCP("GFE");

                header.setEntidade(cpf);
                header.setLocal("0001");
                header.setTipoDocumentoReferencia("PVG");

                header.setDocumentoReferencia(String.valueOf(viagem.getId()));      ///////modificado
                header.setTipoDocumentoReferencia2(mapFR.get(viagem.getCodigoFonteRecurso()).getCodigo());
                header.setDocumentoReferencia2("");
                header.setTipoRegistro("01"); // Registro de Header
                header.setDataEmissao(dataInclusao);
                header.setDataRecebimento(dataInclusao);
                header.setDataContabil(dataInclusao);
                header.setCondicaoPagto("AVI");


                header.setDescricao("RESSARCIMENTO DE DESPESAS COM VIAGEM - " + viagem.getProcesso());                         ///////modificado
                header.setDescricao1("");
                header.setDescricao2("");

                ///////modificado
                //header.setCentroResponsabilidade(viagem.getCodigoFonteRecurso());
                header.setCentroResponsabilidade(viagem.getCodigoCentroResponsabilidade());

                if (temContaBancaria) {                                             ///////modificado
                    // Se o tipo de pagamento for crédito em conta (ID = 1), define a conta bancária para depósito
                    header.setContaBancariaForn("001");
                } else {
                    // Se o tipo de pagamento náo for crédito em conta (ID != 1), não define uma conta bancária para depósito
                    header.setContaBancariaForn("");
                }

                //TES ou CGP                                                        ///////modificado
                header.setCentroPagamento(new FonteRecursoDAO().obterTipoCGPouTES(viagem.getCodigoFonteRecurso()));
                //System.out.println(header.getCentroPagamento());

                header.setMetodoPagto("PEL"); //Mudado de bordero BOR para pagamento eletronico PEL// Campo definido pelo Adm. Financeiro
                header.setIndicadorPagamento("0");


                Double dias = Double.valueOf(String.valueOf(Data.dataDiff(viagem.getDataSaidaPrevista(), viagem.getDataRetornoPrevista())));
                Integer horas = Data.horaDiff(viagem.getDataSaidaPrevista(), viagem.getDataRetornoPrevista());
                
                //OBTER O PERCENTUAL DE DIÁRIA DO VIAJANTE COM BASE EM SUA LOTAÇÃO
                if(percentualDiaria == null){
                    percentualDiaria = percentualDiariaBO.obterPorDepartamentoDominio(0);//"PADRAO"
                }

                dias += percentualDiaria.getQuebraDiarias((double)horas.intValue());

                ///////modificado
                //header.setValorTotal((dias * viagem.getDiaria()) + viagem.getValorAdiantamento());
                header.setValorTotal(saldo);


                header.setCategoriaDespesa("");
                header.setCampoUsuario1("");
                header.setCampoUsuario2("");
                header.setCampoUsuario3("");

                header.setSituacaoParcela("1");                                     ///////modificado
                header.setMotivoBloqueio("");

                ///////modificado
                header.setSituacaoDocumento("3"); // Importa como um documento Aprovado
                header.setNomeFornEventual("");
                header.setEnderecoFornEventual("");
                header.setIdentificadorFornEventual("");
                header.setBancoFornEventual("");
                header.setAgenciaFornEventual("");
                header.setContaFornEventual("");

                layout.add(header);

                // O campo CC1 pode ser obtido a partir do primeiro caracter do campo CC2
                String centroContabil1 = String.valueOf(viagem.getCodigoCentroCusto().substring(0, 1));
                /*
                 * Linha de despesa: Registro do valor da Despesa BRUTA
                 */
                RegistroLinha linhaDespesa = new RegistroLinha();
                linhaDespesa.setCompanhia("FE01");
                linhaDespesa.setGrupoCP("GFE");
                linhaDespesa.setEntidade(cpf);
                linhaDespesa.setLocal("0001");
                linhaDespesa.setTipoDocumentoReferencia("PVG");
                linhaDespesa.setDocumentoReferencia(String.valueOf(viagem.getId()));
                linhaDespesa.setTipoRegistro("02"); // "02" = Registro de linha de Despesa
                linhaDespesa.setTipoLinha("1"); // "1" = Tipo de linha de Despesa
                linhaDespesa.setDescricao("RESSARCIMENTO DE DESPESAS COM VIAGEM - " + viagem.getProcesso());
                linhaDespesa.setDescricao1("");
                linhaDespesa.setDescricao2("");
                //linhaDespesa.setValorLinha((dias * viagem.getDiaria()) + viagem.getValorAdiantamento());
                linhaDespesa.setValorLinha(saldo);
                linhaDespesa.setEventoContabil("RFP011");
                linhaDespesa.setContaContabil("");
                linhaDespesa.setCentroContabil1(centroContabil1);
                linhaDespesa.setCentroContabil2(viagem.getCodigoCentroCusto());
                linhaDespesa.setCentroContabil3("");
                linhaDespesa.setCentroContabil4("");
                linhaDespesa.setCodigoTributo("");
                linhaDespesa.setCodigoRetencao("");
                linhaDespesa.setCategoriaDespesa("");

                layout.add(linhaDespesa);

                Parcela p = new Parcela();

                p.setCompanhia("FE01");
                p.setGrupoCP("GFE");
                p.setEntidade(cpf);
                p.setLocal("0001");
                p.setTipoDocumentoReferencia("PVG");
                p.setDocumentoReferencia(String.valueOf(viagem.getId()));
                p.setTipoRegistro("03"); // "03" = Registro Parcela
                p.setDataVencimento(dataVencimento);
                //p.setValorParcela((dias * viagem.getDiaria()) + viagem.getValorAdiantamento());
                p.setValorParcela(saldo);
                p.setTipoDocumentoCobranca("");
                p.setCodigoBarra("");
                p.setNomeFavorecido("");
                p.setDocumentoIdentFavorecido("");
                p.setBancoFavororecido("");
                p.setAgenciaFavorecido("");
                p.setContaFavorecido("");
                p.setComentario1(viagem.getCodigoFonteRecurso());
                p.setComentario2(viagem.getProcesso());
                p.setComentario3("");

                layout.add(p);
            }
        }
        
        if(layout.isEmpty()){
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Nenhuma viagem com valor a reembolsar.");
            return mapping.findForward("recebimentoRedirect");
        }
        StringBuffer output = new StringBuffer();

        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=" + "Exportacao_PrestacaoConta_" + new SimpleDateFormat("ddMMyyyy").format(new Date()) + ".txt");

        //escreve o arquivo no buffer de saida
        ServletOutputStream outStream = response.getOutputStream();

        for (Layout l : layout) {
            output.append(l.toLayout() + "\n");
        }

        outStream.print(output.toString());

        outStream.flush();
        outStream.close();
        return null;
    }

    public ActionForward finalizar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] id = new String[0];

        try {
            if (request.getParameterValues("ids_exportacao") != null) {
                id = request.getParameterValues("ids_exportacao");

                HibernateUtil.beginTransaction();

                for (int i = 0; i < id.length; i++) {
                    Viagem viagem = new ViagemBO().obterPorPk(Integer.valueOf(id[i]));

                    if (viagem.getStatusViagem().equals(StatusViagem.VIAGEM_RECEBIDA)) {
                        viagem.setStatusViagem(StatusViagem.VIAGEM_FINALIZADA);
                        new ViagemBO().alterar(viagem);
                    }

                    PrestacaoContas pc = viagem.getPrestacaoContas();
                    pc.setDataFinalizacao(new Date());
                    pc.setStatusPrestacaoContas(StatusPrestacaoContas.PRESTACAO_FINALIZADA);

                    new PrestacaoContasBO().alterar(pc);

                }

                MensagemTagUtil.adicionarMensagem(request.getSession(), "Operação realizada com sucesso.");
                HibernateUtil.commitTransaction();

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao finalizar a Viagem/Prestação.");
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(PrestacaoContasAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return mapping.findForward("recebimentoRedirect");
    }

    public ActionForward excluir(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");

        try {
            ViagemBO viagemBO = new ViagemBO();
            HibernateUtil.beginTransaction();
            Viagem viagem = viagemBO.obterPorPk(Integer.valueOf(id));
            PrestacaoContas pc = viagem.getPrestacaoContas();
            if (pc.getStatusPrestacaoContas() == StatusPrestacaoContas.PRESTACAO_INFORMADA) {
                viagem.setPrestacaoContas(null);
                viagemBO.alterar(viagem);
                new PrestacaoContasBO().excluir(pc);
            }
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Exclusão realizada com sucesso.");
            HibernateUtil.commitTransaction();

        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao receber a Prestação.");
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(PrestacaoContasAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return mapping.findForward("redirect");
    }

}
