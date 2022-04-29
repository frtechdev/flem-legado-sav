package br.org.flem.sav.action;

import br.org.flem.fw.persistencia.dao.legado.bdprocesso.ControleProcessoDAO;
import br.org.flem.fw.persistencia.dao.legado.control.CentroCustoUsuarioDAO;
import br.org.flem.fw.persistencia.dao.legado.gem.EntidadeDAO;
import br.org.flem.fw.service.CentroResponsabilidade;
import br.org.flem.fw.service.IColaborador;
import br.org.flem.fw.service.IUsuario;
import br.org.flem.fw.util.Constante;
import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fwe.hibernate.util.HibernateUtil;
import br.org.flem.fwe.util.Data;
import br.org.flem.fwe.util.Valores;
import br.org.flem.fwe.web.action.SecurityDispatchAction;
import br.org.flem.fwe.web.annotation.Funcionalidade;
import br.org.flem.fwe.web.util.MensagemTagUtil;
import br.org.flem.sav.bo.CidadeBO;
import br.org.flem.sav.bo.CompanhiaBO;
import br.org.flem.sav.bo.ItemItinerarioBO;
import br.org.flem.sav.bo.ItinerarioBO;
import br.org.flem.sav.bo.LiberacaoViagemBO;
import br.org.flem.sav.bo.ReservaBO;
import br.org.flem.sav.bo.TrechoBO;
import br.org.flem.sav.bo.ViagemBO;
import br.org.flem.sav.dto.ViagemDTO;
import br.org.flem.sav.negocio.Companhia;
import br.org.flem.sav.negocio.DestinoViagem;
import br.org.flem.sav.negocio.ItemItinerario;
import br.org.flem.sav.negocio.Itinerario;
import br.org.flem.sav.negocio.LiberacaoViagem;
import br.org.flem.sav.negocio.Natureza;
import br.org.flem.sav.negocio.Reserva;
import br.org.flem.sav.negocio.StatusViagem;
import br.org.flem.sav.negocio.TipoDiaria;
import br.org.flem.sav.negocio.TipoLiberacao;
import br.org.flem.sav.negocio.Trecho;
import br.org.flem.sav.negocio.Viagem;
import br.org.flem.sav.negocio.util.Cidade;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 *
 * @author mgsilva
 */
public class ViagemConsultorAction extends SecurityDispatchAction{

    @Override
    @Funcionalidade(nomeCurto="solicViagConsultor")
    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            boolean podeSolicitar = true;
            Collection<ViagemDTO> viagensDTO = new ArrayList<ViagemDTO>();
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            List<IColaborador> colaboradores = new EntidadeDAO().obterColaboradores();
            ArrayList<String> resposta = new ArrayList<String>();
            String ids = "";

            request.setAttribute("listaConsultor", colaboradores);

            String codigoConsultor = (String) request.getSession().getAttribute("codigoConsultor");

            if (codigoConsultor == null || codigoConsultor.isEmpty()) {
                //request.setAttribute("novaSolicitacaoTexto", "");
                //request.setAttribute("novaSolicitacao", "");
                request.setAttribute("lista", viagensDTO);
                return mapping.findForward("lista");
            }

            IColaborador consultor = new EntidadeDAO().obterConsultorPorCodigo(codigoConsultor);
            Collection<Viagem> viagens = new ViagemBO().obterTodosConsultor(consultor.getCodigo());

            LiberacaoViagem liberacaoViagem = new LiberacaoViagemBO().obterByCodigoConsultor(codigoConsultor);

            for (Viagem viagem : viagens) {
                ViagemDTO vdto = new ViagemDTO();
                vdto.setViagem(viagem);
                vdto.setPeriodo(simpleDate.format(viagem.getDataSaidaPrevista()) + " " + simpleDate.format(viagem.getDataRetornoPrevista()));
                vdto.setStatusViagem(viagem.getStatusViagem().name());
                vdto.setNomeViajante(consultor.getNome());
                vdto.setPrestouConta("1");

                if (liberacaoViagem != null && liberacaoViagem.isValido()) {
                    resposta.add("true");
                    resposta.add("O Consultor \"" + consultor.getNome().replace("  ", "") + "\" pode solicitar uma viagem porque possui uma liberação!");
                    resposta.add("true");

                    if (viagem.getPrestacaoContas() == null) {
                        vdto.setPrestouConta("0");
                    }

                } else {
                    if (viagem.getPrestacaoContas() == null) {
                        podeSolicitar = false;
                        vdto.setPrestouConta("0");
                        ids += viagem.getId() + ", ";
                    }
                }
                viagensDTO.add(vdto);
            }
            
            if (ids.length() > 0) {
                ids = ids.substring(0, ids.length() - 2);
                resposta.add("false");
                resposta.add("O Consultor \"" + consultor.getNome().replace("  ", "") + "\" não pode solicitar uma viagem porque ainda não prestou conta da(s) seguinte(s) viagem(ns), ID(s) ref.: \"" + ids + "\"! ");
                resposta.add("false");
            }

            if (resposta.isEmpty()) {
                resposta.add("true");
                resposta.add("O Consultor \"" + consultor.getNome().replace("  ", "") + "\" pode solicitar uma viagem!");
                resposta.add("false");
            }

            request.setAttribute("novaSolicitacaoTexto", resposta.get(1));
            request.setAttribute("novaSolicitacao", podeSolicitar);
            request.setAttribute("lista", viagensDTO);

        } catch (AplicacaoException ex) {
            Logger.getLogger(ViagemConsultorAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mapping.findForward("lista");
    }

    @Funcionalidade(nomeCurto="solicViagConsultor")
    public ActionForward filtrar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm dyna = (DynaActionForm) form;
        Viagem filtro = (Viagem) dyna.get("viagem");
        if (filtro.getCodigoConsultorViajante() != null) {
            request.getSession().setAttribute("codigoConsultor", filtro.getCodigoConsultorViajante().toString());
        }
        return unspecified(mapping, form, request, response);
    }

    @Funcionalidade(nomeCurto="solicNovoViagConsultor")
    public ActionForward novo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            DynaActionForm dyna = (DynaActionForm) form;
            Viagem viagemForm = (Viagem) dyna.get("viagem");

            IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
            //IFuncionario solicitante = new HRBIImpl().obterFuncionarioPorMatricula(usuario.getMatricula());
            CentroCustoUsuarioDAO cCustoUsuarioDAO = new CentroCustoUsuarioDAO();
            Collection<CentroResponsabilidade> ccs = cCustoUsuarioDAO.obterCCustoViajante(usuario.getCodigoDominio());

            List listaTiposDiaria = new ArrayList();
            listaTiposDiaria.add(TipoDiaria.ESPECIAL);
            listaTiposDiaria.add(TipoDiaria.DETALHADA);

            request.setAttribute("viajante", new EntidadeDAO().obterConsultorPorCodigo(viagemForm.getCodigoConsultorViajante()));
            request.setAttribute("listaNaturezas", Natureza.values());
            request.setAttribute("usuario", usuario);
            request.setAttribute("listaTiposDiaria", listaTiposDiaria);
            request.setAttribute("listaCentrosCusto", ccs);
            request.setAttribute("listaCentrosResponsabilidade", cCustoUsuarioDAO.obterCRespViajante(usuario.getCodigoDominio()));
            request.setAttribute("listaCompanhias", new CompanhiaBO().obterTodos());
            request.setAttribute("listaDestinoViagem", DestinoViagem.toCollection());
            request.setAttribute("consultor", true);
            request.setAttribute("metodo", "adicionarConsultor");

            LiberacaoViagem liberacaoR = new LiberacaoViagemBO().obterByCodigoConsultorTipo(viagemForm.getCodigoConsultorViajante(), TipoLiberacao.RETROATIVO);
            if ((liberacaoR != null) && (liberacaoR.isValido())) {
                request.setAttribute("liberacaoRetroativo", "true");
            } else {
                request.setAttribute("liberacaoRetroativo", "false");
            }

        } catch (AplicacaoException ex) {
            Logger.getLogger(ViagemConsultorAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mapping.findForward("novo");

    }

    public ActionForward adicionar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        DynaActionForm dyna = (DynaActionForm) form;

        try {
            IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
            Reserva reserva = new Reserva();
            Companhia companhia = new Companhia();
            Itinerario itinerario = new Itinerario();
            ItemItinerario itemItinerario = null;
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            boolean podeSolicitar = true;

            Date dataAtual = Calendar.getInstance().getTime();
            Date dataSaida = null;

            Viagem viagem = (Viagem) dyna.get("viagem");

            String tipoDiaria = (String) dyna.get("tipoDiaria");
            String natureza = (String) dyna.get("natureza");
            String diaria = (String) dyna.get("diaria");
            String diariaP = (String) dyna.get("diariaP");
            String descricaoE = (String) dyna.get("descricaoE");
            String totalDiarias = (String) dyna.get("totalDiarias");
            String qtDiaria = (String) dyna.get("qtDiaria");
            String totalAdiantamento = (String) dyna.get("totalAdiantamento");
            String valorAdiantamento = (String) dyna.get("valorAdiantamento");
            String dataSaidaPrevista = (String) dyna.get("dataSaidaPrevista");
            String horaSaidaPrevista = (String) dyna.get("horaSaidaPrevista");
            String dataRetornoPrevista = (String) dyna.get("dataRetornoPrevista");
            String horaRetornoPrevista = (String) dyna.get("horaRetornoPrevista");
            String temReserva = (String) dyna.get("temReserva");
            String companhiaId = (String) dyna.get("companhiaId");
            String codigoReserva = (String) dyna.get("codigoReserva");
            String destino = (String) dyna.get("destino");
            String observacao = (String) dyna.get("observacao");

            //itinerario
            String[] data = (String[]) dyna.get("data");
            String[] cidadeO = (String[]) dyna.get("cidadeO");
            String[] cidadeD = (String[]) dyna.get("cidadeD");
            String[] obs = (String[]) dyna.get("obs");

            //trecho
            String[] dataI = (String[]) dyna.get("dataI");
            String[] horaI = (String[]) dyna.get("horaI");
            String[] dataF = (String[]) dyna.get("dataF");
            String[] horaF = (String[]) dyna.get("horaF");
            String[] qtDiariaT = (String[]) dyna.get("qtDiariaT");
            String[] valorDiariaT = (String[]) dyna.get("valorDiariaT");
            //String[] totalDiariaT = (String[]) dyna.get("totalDiariaT");
            String[] descT = (String[]) dyna.get("descT");

            //testa se tem prestação em aberto
            Collection<Viagem> ausencia_prestacao = new ViagemBO().obterSemPrestacaoContaPorConsultor(viagem.getCodigoConsultorViajante());
            if (!ausencia_prestacao.isEmpty()) {
                podeSolicitar = false;
            }


            HibernateUtil.beginTransaction();

            //viagem.setCodigoConsultorViajante(viagem.getCodigoConsultorViajante());

            viagem.setStatusViagem(StatusViagem.VIAGEM_ABERTA);
            viagem.setDataSolicitacao(Calendar.getInstance().getTime());
            viagem.setCodigoDominioUsuarioSolicitante(usuario.getCodigoDominio());
            viagem.setNatureza(Natureza.valueOf(natureza));

            viagem.setDataSaidaPrevista(simpleDate.parse(dataSaidaPrevista + " " + horaSaidaPrevista));
            viagem.setDataRetornoPrevista(simpleDate.parse(dataRetornoPrevista + " " + horaRetornoPrevista));

            if (temReserva.equals("1")) {
                //companhia = new CompanhiaBO().obterPorPk(Integer.valueOf(companhiaId));
                companhia = null;
                //reserva.setCodigo(codigoReserva);
                reserva.setCodigo("");
                reserva.setCompanhia(companhia);
                Object objR = new ReservaBO().inserir(reserva);
                reserva = new ReservaBO().obterPorPk((Integer) objR);
                viagem.setReserva(reserva);
            }

            viagem.setDestino(DestinoViagem.valueOf(destino));
            viagem.setTipoDiaria(TipoDiaria.getById(Integer.valueOf(tipoDiaria)));

            viagem.setDiaria(Valores.desformataValor(diaria));
            viagem.setDiariaP(Valores.desformataValor(diariaP));
            if (valorAdiantamento == null || valorAdiantamento.isEmpty()) {
                valorAdiantamento = "0,00";
            }
            viagem.setValorAdiantamento(Valores.desformataValor(valorAdiantamento));
            viagem.setQtDiaria(Valores.desformataValor(qtDiaria));
            if (totalAdiantamento == null || totalAdiantamento.isEmpty() || totalAdiantamento.equals("0,00")) {
                totalAdiantamento = Valores.formataValor(String.valueOf(Valores.desformataValor(totalDiarias) + Valores.desformataValor(valorAdiantamento)));
            }
            viagem.setTotalAdiantamento(Valores.desformataValor(totalAdiantamento));
            viagem.setTotalDiarias(Valores.desformataValor(totalDiarias));

            dataSaida = simpleDate.parse(dataSaidaPrevista + " 23:59");

            viagem.setDescricaoE(descricaoE);
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
            viagem.setItinerario(itinerario);

            // Verifica todas viagens realizadas pelo funcionário no mês
            // #688

            //new ViagemBO().inserir(viagem);
            ViagemBO viagemBO = new ViagemBO();

            Object objV = viagemBO.inserir(viagem);
            viagem = viagemBO.obterPorPk((Integer) objV);

            TrechoBO trechoBO = new TrechoBO();
            for (int i = 0; i < descT.length; i++) {
                Trecho trecho = new Trecho();
                trecho.setViagem(viagem);
                trecho.setDataInicio(Data.formataData(dataI[i] + " " + horaI[i] + ":00", "dd/MM/yyyy HH:mm:ss"));
                trecho.setDataFim(Data.formataData(dataF[i] + " " + horaF[i] + ":00", "dd/MM/yyyy HH:mm:ss"));
                trecho.setQtDiaria(Double.valueOf(Valores.desformataValor(qtDiariaT[i])));
                trecho.setDiaria(Double.valueOf(Valores.desformataValor(valorDiariaT[i])));
                trecho.setObservacao(descT[i]);

                trechoBO.inserir(trecho);
            }


            if (!podeSolicitar) {
                //dar baixa na liberação de pendência se existe!
                LiberacaoViagem liberacao = new LiberacaoViagemBO().obterByCodigoConsultor(viagem.getCodigoConsultorViajante());
                if ((liberacao != null) && (liberacao.isValido())) {
                    liberacao.setValido(false);
                    liberacao.setViagem(viagem);
                    new LiberacaoViagemBO().alterar(liberacao);
                    MensagemTagUtil.adicionarMensagem(request.getSession(), "[ Liberação usada ]");
                }
            }

            if (dataSaida.before(dataAtual)) {
                //dar baixa na liberação de solicitacao retroativa se existe!
                LiberacaoViagem liberacaoR = new LiberacaoViagemBO().obterByCodigoConsultorTipo(viagem.getCodigoConsultorViajante(), TipoLiberacao.RETROATIVO);
                if ((liberacaoR != null) && (liberacaoR.isValido())) {
                    liberacaoR.setValido(false);
                    liberacaoR.setViagem(viagem);
                    new LiberacaoViagemBO().alterar(liberacaoR);
                }
            }


            HibernateUtil.commitTransaction();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Viagem inserida com sucesso.");

        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao inserir a Viagem.");
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(CargoDiariaAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return unspecified(mapping, form, request, response);

    }

    public ActionForward selecionar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        String id = request.getParameter("id");
        EntidadeDAO entidadeDAO = new EntidadeDAO();
        IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
        CentroCustoUsuarioDAO cCustoUsuarioDAO = new CentroCustoUsuarioDAO();

        try {
            if (GenericValidator.isInt(id)) {
                Viagem viagem = new ViagemBO().obterPorPk(Integer.valueOf(id));

                Collection<CentroResponsabilidade> ccs = cCustoUsuarioDAO.obterCCustoViajante(usuario.getCodigoDominio());

                List listaTiposDiaria = new ArrayList();
                listaTiposDiaria.add(TipoDiaria.ESPECIAL);
                listaTiposDiaria.add(TipoDiaria.DETALHADA);


                dyna.set("viagem", viagem);
                dyna.set("destino", viagem.getDestino().name());
                dyna.set("natureza", String.valueOf(viagem.getNatureza().getCodigo()));
                dyna.set("dataSaidaPrevista", Data.formataData(viagem.getDataSaidaPrevista()));
                dyna.set("horaSaidaPrevista", Data.formataHoraCurta(viagem.getDataSaidaPrevista()));
                dyna.set("dataRetornoPrevista", Data.formataData(viagem.getDataRetornoPrevista()));
                dyna.set("horaRetornoPrevista", Data.formataHoraCurta(viagem.getDataRetornoPrevista()));

                if (viagem.getReserva() != null) {
                    dyna.set("temReserva", "1");
                    //dyna.set("companhiaId", String.valueOf(viagem.getReserva().getCompanhia().getId()));
                    //dyna.set("codigoReserva", viagem.getReserva().getCodigo());
                }

                dyna.set("tipoDiaria", String.valueOf(viagem.getTipoDiaria().getId()));

                if (viagem.getTipoDiaria().equals(TipoDiaria.ESPECIAL)) {
                    dyna.set("diariaE", String.valueOf(viagem.getDiaria()));
                }

                //dyna.set("diariaE", String.valueOf(viagem.getDiaria()));
                //double totalDiarias = viagem.getQtDiaria()*viagem.getDiaria();
                //double totalAdiantamento = totalDiarias + viagem.getValorAdiantamento();

                dyna.set("diaria", String.valueOf(viagem.getDiaria()));
                dyna.set("diariaP", String.valueOf(viagem.getDiariaP()));
                dyna.set("qtDiaria", String.valueOf(viagem.getQtDiaria()));
                dyna.set("valorAdiantamento", String.valueOf(viagem.getValorAdiantamento()));
                dyna.set("totalDiarias", String.valueOf(viagem.getTotalDiarias()));
                dyna.set("totalAdiantamento", String.valueOf(viagem.getTotalAdiantamento()));
                dyna.set("descricaoE", viagem.getDescricaoE());
                dyna.set("observacao", viagem.getItinerario().getObservacao());

                request.setAttribute("viajante", entidadeDAO.obterConsultorPorCodigo(viagem.getCodigoConsultorViajante()));
                request.setAttribute("listaNaturezas", Natureza.values());
                request.setAttribute("listaTiposDiaria", listaTiposDiaria);
                request.setAttribute("listaCentrosCusto", ccs);
                request.setAttribute("listaCentrosResponsabilidade", cCustoUsuarioDAO.obterCRespViajante(usuario.getCodigoDominio()));
                request.setAttribute("listaCompanhias", new CompanhiaBO().obterTodos());
                request.setAttribute("itensItinerario", new ItemItinerarioBO().obterPorItinerario(viagem.getItinerario()));
                request.setAttribute("itensTrecho", new TrechoBO().obterPorViagem(viagem));
                request.setAttribute("listaDestinoViagem", DestinoViagem.toCollection());

                LiberacaoViagem liberacaoR = new LiberacaoViagemBO().obterByViagemTipo(viagem, TipoLiberacao.RETROATIVO);
                if (liberacaoR != null) {
                    request.setAttribute("liberacaoRetroativo", "true");
                } else {
                    request.setAttribute("liberacaoRetroativo", "false");
                }

            }
            return mapping.findForward("editar");
        } catch (AplicacaoException ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar selecionar a Solicitação.");
        }
        return mapping.findForward("redirect");
    }

    public ActionForward alterar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        DynaActionForm dyna = (DynaActionForm) form;

        try {
            IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
            ItemItinerario itemItinerario = null;
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            Viagem viagemForm = (Viagem) dyna.get("viagem");

            Viagem viagem = new ViagemBO().obterPorPk(viagemForm.getId());

            String tipoDiaria = (String) dyna.get("tipoDiaria");
            String natureza = (String) dyna.get("natureza");
            String diaria = (String) dyna.get("diaria");
            String diariaP = (String) dyna.get("diariaP");
            String descricaoE = (String) dyna.get("descricaoE");
            String qtDiaria = (String) dyna.get("qtDiaria");
            String valorAdiantamento = (String) dyna.get("valorAdiantamento");
            String totalAdiantamento = (String) dyna.get("totalAdiantamento");
            String totalDiarias = (String) dyna.get("totalDiarias");
            String dataSaidaPrevista = (String) dyna.get("dataSaidaPrevista");
            String horaSaidaPrevista = (String) dyna.get("horaSaidaPrevista");
            String dataRetornoPrevista = (String) dyna.get("dataRetornoPrevista");
            String horaRetornoPrevista = (String) dyna.get("horaRetornoPrevista");
            String temReserva = (String) dyna.get("temReserva");
            String companhiaId = (String) dyna.get("companhiaId");
            String codigoReserva = (String) dyna.get("codigoReserva");
            String observacao = (String) dyna.get("observacao");

            //itinerario
            String[] data = (String[]) dyna.get("data");
            String[] cidadeO = (String[]) dyna.get("cidadeO");
            String[] cidadeD = (String[]) dyna.get("cidadeD");
            String[] obs = (String[]) dyna.get("obs");

            //trecho
            String[] dataI = (String[]) dyna.get("dataI");
            String[] horaI = (String[]) dyna.get("horaI");
            String[] dataF = (String[]) dyna.get("dataF");
            String[] horaF = (String[]) dyna.get("horaF");
            String[] qtDiariaT = (String[]) dyna.get("qtDiariaT");
            String[] valorDiariaT = (String[]) dyna.get("valorDiariaT");
            //String[] totalDiariaT = (String[]) dyna.get("totalDiariaT");
            String[] descT = (String[]) dyna.get("descT");

            viagem.setCodigoDominioUsuarioSolicitante(usuario.getCodigoDominio());
            viagem.setNatureza(Natureza.valueOf(natureza));

            viagem.setDataSaidaPrevista(simpleDate.parse(dataSaidaPrevista + " " + horaSaidaPrevista));
            viagem.setDataRetornoPrevista(simpleDate.parse(dataRetornoPrevista + " " + horaRetornoPrevista));

            viagem.setCodigoCentroCusto(viagemForm.getCodigoCentroCusto());
            viagem.setCodigoCentroResponsabilidade(viagemForm.getCodigoCentroResponsabilidade());
            viagem.setDescricao(viagemForm.getDescricao());
            viagem.setDescricaoE(descricaoE);

            HibernateUtil.beginTransaction();

            //RERESERVA PARA A VIAGEM --------------------------------------------------------------------

            ReservaBO reservaBO = new ReservaBO();
            Reserva reserva = new Reserva();
            Companhia companhia = new Companhia();
            //Se a reserva existente foi alterada
            if (temReserva.equals("1") && viagem.getReserva() != null) {
                //companhia = new CompanhiaBO().obterPorPk(Integer.valueOf(companhiaId));
                //viagem.getReserva().setCodigo(codigoReserva);
                //viagem.getReserva().setCompanhia(companhia);
                //reservaBO.alterar(viagem.getReserva());
                //Se a reserva não existirá mais
            } else if (temReserva.equals("0") && viagem.getReserva() != null) {
                reserva = viagem.getReserva();
                viagem.setReserva(null);
                reservaBO.excluir(reserva);
                //Se agora uma reserva foi criada
            } else if (temReserva.equals("1") && viagem.getReserva() == null) {
                //reserva.setCodigo(codigoReserva);
                reserva.setCodigo("");
                //companhia = new CompanhiaBO().obterPorPk(Integer.valueOf(companhiaId));
                companhia = null;
                reserva.setCompanhia(companhia);
                Integer idReserva = (Integer) reservaBO.inserir(reserva);
                reserva.setId(idReserva);
                viagem.setReserva(reserva);
            }

            // END OF RERESERVA PARA A VIAGEM --------------------------------------------------------------------

            viagem.setTipoDiaria(TipoDiaria.getById(Integer.valueOf(tipoDiaria)));

            viagem.setQtDiaria(Valores.desformataValor(qtDiaria));
            viagem.setDiaria(Valores.desformataValor(diaria));
            viagem.setDiariaP(Valores.desformataValor(diariaP));
            viagem.setValorAdiantamento(Valores.desformataValor(valorAdiantamento));
            viagem.setTotalAdiantamento(Valores.desformataValor(totalAdiantamento));
            viagem.setTotalDiarias(Valores.desformataValor(totalDiarias));

            ItinerarioBO itinerarioBO = new ItinerarioBO();
            ItemItinerarioBO itemItinerarioBO = new ItemItinerarioBO();

            itemItinerarioBO.excluir(viagem.getItinerario().getItensItinerario());

            Itinerario itinerario = itinerarioBO.obterPorPk(viagem.getItinerario().getId());

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
            viagem.setItinerario(itinerario);

            new ViagemBO().alterar(viagem);

            //viagemBO.alterar(viagem);

            TrechoBO trechoBO = new TrechoBO();
            trechoBO.excluir(viagem.getTrechos());
            for (int i = 0; i < descT.length; i++) {
                Trecho trecho = new Trecho();
                trecho.setViagem(viagem);
                trecho.setDataInicio(Data.formataData(dataI[i] + " " + horaI[i] + ":00", "dd/MM/yyyy HH:mm:ss"));
                trecho.setDataFim(Data.formataData(dataF[i] + " " + horaF[i] + ":00", "dd/MM/yyyy HH:mm:ss"));
                trecho.setQtDiaria(Double.valueOf(Valores.desformataValor(qtDiariaT[i])));
                trecho.setDiaria(Double.valueOf(Valores.desformataValor(valorDiariaT[i])));
                trecho.setObservacao(descT[i]);

                trechoBO.inserir(trecho);
            }

            HibernateUtil.commitTransaction();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Viagem alterada com sucesso.");


        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao alterar a Viagem.");

            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(CargoDiariaAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return mapping.findForward("redirect");

    }

    public ActionForward excluir(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            String id = request.getParameter("id");

            HibernateUtil.beginTransaction();
            ViagemBO viagemBO = new ViagemBO();
            Viagem viagem = viagemBO.obterPorPk(Integer.valueOf(id));
            if (viagem.getProcesso() != null) {
                new ControleProcessoDAO().cancelaProcessoNumero(viagem.getProcesso());
            }
            viagemBO.excluir(viagem);

            MensagemTagUtil.adicionarMensagem(request.getSession(), "Exclusão realizada com sucesso!");
            HibernateUtil.commitTransaction();


        } catch (AplicacaoException ex) {
            Logger.getLogger(ViagemConsultorAction.class.getName()).log(Level.SEVERE, null, ex);
            MensagemTagUtil.adicionarMensagem(request.getSession(), "A Viagem está associada. Não pode ser excluída!");
        }


        return mapping.findForward("redirect");
    }

}
