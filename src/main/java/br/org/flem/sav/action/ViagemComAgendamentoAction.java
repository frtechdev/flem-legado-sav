package br.org.flem.sav.action; 

import br.org.flem.fw.persistencia.dao.legado.control.DepartamentoUsuarioDAO;
import br.org.flem.fw.persistencia.dao.legado.gem.EntidadeDAO;
import br.org.flem.fw.persistencia.dto.Entidade;
import br.org.flem.fw.persistencia.dto.Usuario;
import br.org.flem.fw.service.IFuncionario;
import br.org.flem.fw.service.IUsuario;
import br.org.flem.fw.service.impl.RHServico;
import br.org.flem.fw.util.Constante;
import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fwe.hibernate.util.HibernateUtil;
import br.org.flem.fwe.util.Data;
import br.org.flem.fwe.util.Valores;
import br.org.flem.fwe.web.action.SecurityDispatchAction;
import br.org.flem.fwe.web.annotation.Funcionalidade;
import br.org.flem.fwe.web.util.MensagemTagUtil;
import br.org.flem.sav.bo.BancoBO;
import br.org.flem.sav.bo.CidadeBO;
import br.org.flem.sav.bo.CompanhiaBO;
import br.org.flem.sav.bo.ContaCorrenteViagemBO;
import br.org.flem.sav.bo.ItemItinerarioBO;
import br.org.flem.sav.bo.ItinerarioBO;
import br.org.flem.sav.bo.LiberacaoViagemBO;
import br.org.flem.sav.bo.PercentualDiariaBO;
import br.org.flem.sav.bo.SolicitarViagemBO;
import br.org.flem.sav.bo.TrechoBO;
import br.org.flem.sav.bo.UsuarioBO;
import br.org.flem.sav.bo.ViagemComAgendamentoBO;
import br.org.flem.sav.dto.ViagemDTO;
import br.org.flem.sav.negocio.ContaCorrenteViagem;
import br.org.flem.sav.negocio.DestinoViagem;
import br.org.flem.sav.negocio.ItemItinerario;
import br.org.flem.sav.negocio.Itinerario;
import br.org.flem.sav.negocio.LiberacaoViagem;
import br.org.flem.sav.negocio.Natureza;
import br.org.flem.sav.negocio.PercentualDiaria;
import br.org.flem.sav.negocio.StatusAgendamento;
import br.org.flem.sav.negocio.StatusViagem;
import br.org.flem.sav.negocio.TipoDiaria;
import br.org.flem.sav.negocio.Trecho;
import br.org.flem.sav.negocio.Viagem;
import br.org.flem.sav.negocio.ViagemComAgendamento;
import br.org.flem.sav.negocio.util.Cidade;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
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
 * @author mccosta
 */
public class ViagemComAgendamentoAction extends SecurityDispatchAction {

    /**********************************

    AGENDAMENTO VIAGEM - INICIO

     *********************************/
    @Override
    @Funcionalidade(nomeCurto="agendViagFuncionario")
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
            Collection<ViagemDTO> viagensDTO = new ArrayList<ViagemDTO>();
            IFuncionario func_atual = null;
            boolean deFerias = false;

            String matriculaUsuario = (String) request.getSession().getAttribute("matriculaUsuario");

            // func_atual é o usuário logado ou o usuário selecionado na combo
            if (matriculaUsuario != null && !matriculaUsuario.isEmpty()) {
                func_atual = (IFuncionario) new RHServico().obterFuncionarioPorMatricula(Integer.parseInt(matriculaUsuario));
            } else {
                func_atual = (IFuncionario) new RHServico().obterFuncionarioPorMatricula(usuario.getCodigoDominio());
            }

            // início da lista de usuários
            Collection<IFuncionario> IFList = new DepartamentoUsuarioDAO().obterFuncionariosPorDepartamento(usuario);

            Collection<ViagemComAgendamento> viagens = new ViagemComAgendamentoBO().obterTodosByStatusAgendamentoAbertoReprovado(func_atual);
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            for (ViagemComAgendamento viagem : viagens) {
                ViagemDTO vdto = new ViagemDTO();
                vdto.setViagem(viagem);
                vdto.setPeriodo(simpleDate.format(viagem.getDataSaidaPrevista()) + " " + simpleDate.format(viagem.getDataRetornoPrevista()));
                vdto.setNomeViajante(func_atual.getNome());
                viagensDTO.add(vdto);
            }
            
            Usuario usuarioViajante = (Usuario) new UsuarioBO().obterPorCodigoDominio(func_atual.getCodigoDominio());
            usuarioViajante = new UsuarioBO().atualizarStatusFerias(usuarioViajante);
            if(usuarioViajante.isBloqueadoAd() || usuarioViajante.isBloqueadoSe()){
                deFerias = true;
            }
            
            request.setAttribute("deFerias", deFerias);
            request.setAttribute("lista", viagensDTO);
            request.setAttribute("listaFuncionario", IFList);
            request.setAttribute("nmeUsuarioLogado", usuario.getNome());
            request.setAttribute("matriculaUsuarioLogado", usuario.getCodigoDominio());
            request.setAttribute("matriculaUsuario", func_atual.getCodigoDominio());

        } catch (AplicacaoException ex) {
            Logger.getLogger(ViagemComAgendamentoAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapping.findForward("lista");
    }

    @Funcionalidade(nomeCurto="agendViagFuncionario")
    public ActionForward filtrar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm dyna = (DynaActionForm) form;
        ViagemComAgendamento filtro = (ViagemComAgendamento) dyna.get("viagem");
        if (filtro.getCodigoDominioUsuarioViajante() != null) {
            request.getSession().setAttribute("matriculaUsuario", filtro.getCodigoDominioUsuarioViajante().toString());
        }
        return unspecified(mapping, form, request, response);
    }

    @Funcionalidade(nomeCurto="agendViagNovoFuncionario")
    public ActionForward novo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
            DynaActionForm dyna = (DynaActionForm) form;

            ViagemComAgendamento viagemForm = (ViagemComAgendamento) dyna.get("viagem");

            IFuncionario viajante = new RHServico().obterFuncionarioPorMatricula(viagemForm.getCodigoDominioUsuarioViajante());
            ContaCorrenteViagem conta = new ContaCorrenteViagemBO().obterPorMatriculaFuncionario(Integer.toString(viajante.getCodigoDominio()));

            Usuario usr = (Usuario) new UsuarioBO().obterPorCodigoDominio(viajante.getCodigoDominio());
            usr = new UsuarioBO().atualizarStatusFerias(usr);
            
            if (conta == null) {
                conta = new ContaCorrenteViagem();
                conta.setAgencia(viajante.getAgencia());
                conta.setConta(viajante.getConta());
                conta.setBanco(new BancoBO().obterPorCodigo(viajante.getBanco()));
            }
            Collection<String> destinos = new ArrayList<String>();
            for (String destino : DestinoViagem.toCollection()) {
                if (!destino.equals(DestinoViagem.EXTERIOR.name())) {
                    destinos.add(destino);
                }
            }
            request.setAttribute("viajante", viajante);
            request.setAttribute("conta", conta);
            request.setAttribute("destino", DestinoViagem.BAHIA.name());
            request.setAttribute("usuario", usuario);
            request.setAttribute("listaTiposDiaria", TipoDiaria.toAgendamentoCollection());
            request.setAttribute("listaNaturezas", Natureza.values());
            request.setAttribute("listaCompanhias", new CompanhiaBO().obterTodos());
            request.setAttribute("listaDestinoViagem", destinos);
            
            request.setAttribute("iniFerias", Data.formataData(usr.getDataInicioBloqueio(), "dd/MM/yyyy"));
            request.setAttribute("fimFerias", Data.formataData(usr.getDataFimBloqueio(), "dd/MM/yyyy"));
            
            //OBTER O PERCENTUAL DE DIÁRIA DO VIAJANTE COM BASE EM SUA LOTAÇÃO
            PercentualDiariaBO percentualDiariaBO = new PercentualDiariaBO();
            PercentualDiaria percentualDiaria = percentualDiariaBO.obterPorDepartamentoDominio(usr.getLotacaoDominio());
            if (percentualDiaria == null) {
                percentualDiaria = percentualDiariaBO.obterPorDepartamentoDominio(0);//"PADRAO"
            }
            request.setAttribute("percentualDiaria", percentualDiaria);

        } catch (AplicacaoException ex) {
            Logger.getLogger(ViagemComAgendamentoAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapping.findForward("novo");
    }

    public ActionForward adicionar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;

        try {
            IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
            Itinerario itinerario = new Itinerario();
            ItemItinerario itemItinerario = null;
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            ViagemComAgendamento viagem = (ViagemComAgendamento) dyna.get("viagem");

            String natureza = (String) dyna.get("natureza");
            String tipoDiaria = (String) dyna.get("tipoDiaria");
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
            String destino = (String) dyna.get("destino");
            String observacao = (String) dyna.get("observacao");
            
            ////itinerario
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
            
            HibernateUtil.beginTransaction();

            viagem.setStatusAgendamento(StatusAgendamento.AGENDAMENTO_ABERTO);

            viagem.setDataSolicitacao(Calendar.getInstance().getTime());

            viagem.setCodigoDominioUsuarioSolicitante(usuario.getCodigoDominio());
            IFuncionario IfuncViajante = new RHServico().obterFuncionarioPorMatricula(viagem.getCodigoDominioUsuarioViajante());
            viagem.setCodigoConsultorViajante(null);
            viagem.setCodigoDominioCargoViajante(IfuncViajante.getCargo().getId());
            viagem.setCodigoDominioDepartamentoViajante(IfuncViajante.getDepartamento().getCodigoDominio());

            viagem.setNatureza(Natureza.valueOf(natureza));

            viagem.setDataSaidaPrevista(simpleDate.parse(dataSaidaPrevista + " " + horaSaidaPrevista));
            viagem.setDataRetornoPrevista(simpleDate.parse(dataRetornoPrevista + " " + horaRetornoPrevista));

            /*
            if (temReserva.equals("1")) {
            Reserva reserva = new Reserva();
            companhia = new CompanhiaBO().obterPorPk(Integer.valueOf(companhiaId));
            reserva.setCodigo(codigoReserva);
            reserva.setCompanhia(companhia);
            Object objR = new ReservaBO().inserir(reserva);
            reserva = new ReservaBO().obterPorPk((Integer) objR);
            viagem.setReserva(reserva);
            }
             */

            viagem.setBanco(viagem.getBanco());
            viagem.setConta(viagem.getConta());
            viagem.setAgencia(viagem.getAgencia());

            //viagem.setTipoDiaria(TipoDiaria.PADRAO);
            viagem.setTipoDiaria(TipoDiaria.getById(Integer.valueOf(tipoDiaria)));
            viagem.setStatusViagem(StatusViagem.VIAGEM_ABERTA);

            viagem.setQtDiaria(Valores.desformataValor(qtDiaria));
            viagem.setDiaria(Valores.desformataValor(diaria));
            viagem.setDiariaP(Valores.desformataValor(diariaP));
            viagem.setValorAdiantamento(Valores.desformataValor(valorAdiantamento));
            viagem.setTotalAdiantamento(Valores.desformataValor(totalAdiantamento));
            viagem.setTotalDiarias(Valores.desformataValor(totalDiarias));

            viagem.setDescricaoE(descricaoE);
            viagem.setDestino(DestinoViagem.valueOf(destino));

            itinerario.setObservacao(observacao);
            Object objI = new ItinerarioBO().inserir(itinerario);
            itinerario = new ItinerarioBO().obterPorPk((Integer) objI);

            for (int i = 0; i < data.length; i++) {
                Cidade cO = null;
                if(cidadeO[i].contains(" / ")){
                    cO = new CidadeBO().obterPorNome(cidadeO[i].substring(0, cidadeO[i].indexOf(" / ")), cidadeO[i].substring(cidadeO[i].indexOf(" / ")+3,cidadeO[i].length()));
                }else{
                    cO = new CidadeBO().obterPorNome(cidadeO[i]);
                }
                Cidade cD = null;
                if(cidadeD[i].contains(" / ")){
                    cD = new CidadeBO().obterPorNome(cidadeD[i].substring(0, cidadeD[i].indexOf(" / ")), cidadeD[i].substring(cidadeD[i].indexOf(" / ")+3,cidadeD[i].length()));
                }else{
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

            ViagemComAgendamentoBO viagemComAgendamentoBO = new ViagemComAgendamentoBO();

            Object objV = viagemComAgendamentoBO.inserir(viagem);
            viagem = viagemComAgendamentoBO.obterPorPk((Integer) objV);

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

            
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Agendamento inserido com sucesso.");
            HibernateUtil.commitTransaction();

        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao inserir o Agendamento.");
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(CargoDiariaAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return mapping.findForward("redirect");
    }

    public ActionForward selecionar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        String id = request.getParameter("id");
        try {
            if (GenericValidator.isInt(id)) {
                ViagemComAgendamento viagem = new ViagemComAgendamentoBO().obterPorPk(Integer.valueOf(id));

                IFuncionario viajante = new RHServico().obterFuncionarioPorMatricula(viagem.getCodigoDominioUsuarioViajante());
                
                Usuario usr = (Usuario) new UsuarioBO().obterPorCodigoDominio(viajante.getCodigoDominio());
                usr = new UsuarioBO().atualizarStatusFerias(usr);

                dyna.set("viagem", viagem);
                dyna.set("destino", viagem.getDestino().name());
                dyna.set("natureza", String.valueOf(viagem.getNatureza().getCodigo()));
                dyna.set("dataSaidaPrevista", Data.formataData(viagem.getDataSaidaPrevista()));
                dyna.set("horaSaidaPrevista", Data.formataHoraCurta(viagem.getDataSaidaPrevista()));
                dyna.set("dataRetornoPrevista", Data.formataData(viagem.getDataRetornoPrevista()));
                dyna.set("horaRetornoPrevista", Data.formataHoraCurta(viagem.getDataRetornoPrevista()));

                dyna.set("diaria", String.valueOf(viagem.getDiaria()));
                dyna.set("diariaP", String.valueOf(viagem.getDiariaP()));
                dyna.set("qtDiaria", String.valueOf(viagem.getQtDiaria()));
                dyna.set("valorAdiantamento", String.valueOf(viagem.getValorAdiantamento()));
                dyna.set("totalDiarias", String.valueOf(viagem.getTotalDiarias()));
                dyna.set("totalAdiantamento", String.valueOf(viagem.getTotalAdiantamento()));
                dyna.set("descricaoE", viagem.getDescricaoE());
                dyna.set("observacao", viagem.getItinerario().getObservacao());

                dyna.set("tipoDiaria", String.valueOf(viagem.getTipoDiaria().getId()));

                request.setAttribute("viajante", viajante);
                request.setAttribute("listaTiposDiaria", TipoDiaria.toAgendamentoCollection());
                request.setAttribute("listaNaturezas", Natureza.values());
                request.setAttribute("itensItinerario", new ItemItinerarioBO().obterPorItinerario(viagem.getItinerario()));
                request.setAttribute("itensTrecho", new TrechoBO().obterPorViagem(viagem));
                request.setAttribute("listaDestinoViagem", DestinoViagem.toCollection());
                
                request.setAttribute("iniFerias", Data.formataData(usr.getDataInicioBloqueio(), "dd/MM/yyyy"));
                request.setAttribute("fimFerias", Data.formataData(usr.getDataFimBloqueio(), "dd/MM/yyyy"));
                
                //OBTER O PERCENTUAL DE DIÁRIA DO VIAJANTE COM BASE EM SUA LOTAÇÃO
                PercentualDiariaBO percentualDiariaBO = new PercentualDiariaBO();
                PercentualDiaria percentualDiaria = percentualDiariaBO.obterPorDepartamentoDominio(usr.getLotacaoDominio());
                if (percentualDiaria == null) {
                    percentualDiaria = percentualDiariaBO.obterPorDepartamentoDominio(0);//"PADRAO"
                }
                request.setAttribute("percentualDiaria", percentualDiaria);

            }
            return mapping.findForward("editar");
        } catch (AplicacaoException ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar selecionar a Solicitação.");
        }
        return unspecified(mapping, form, request, response);
    }

    public ActionForward alterar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;

        try {
            ItemItinerario itemItinerario = null;
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            ViagemComAgendamento viagemComAgendamentoForm = (ViagemComAgendamento) dyna.get("viagem");
            ViagemComAgendamento viagem = new ViagemComAgendamentoBO().obterPorPk(viagemComAgendamentoForm.getId());

            String natureza = (String) dyna.get("natureza");
            String tipoDiaria = (String) dyna.get("tipoDiaria");
            String diaria = (String) dyna.get("diaria");
            String diariaP = (String) dyna.get("diariaP");
            String descricaoE = (String) dyna.get("descricaoE");
            String qtDiaria = (String) dyna.get("qtDiaria");
            String totalAdiantamento = (String) dyna.get("totalAdiantamento");
            String totalDiarias = (String) dyna.get("totalDiarias");
            String valorAdiantamento = (String) dyna.get("valorAdiantamento");
            String dataSaidaPrevista = (String) dyna.get("dataSaidaPrevista");
            String horaSaidaPrevista = (String) dyna.get("horaSaidaPrevista");
            String dataRetornoPrevista = (String) dyna.get("dataRetornoPrevista");
            String horaRetornoPrevista = (String) dyna.get("horaRetornoPrevista");
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

            HibernateUtil.beginTransaction();

            /*
            viagem.setMatriculaUsuarioSolicitante(usuario.getCodigoDominio());
            viagem.setMatriculaUsuarioViajante(viagemComAgendamentoForm.getMatriculaUsuarioViajante());
            IFuncionario Ifunc = hRBI.obterFuncionarioPorMatricula(viagem.getMatriculaUsuarioViajante());
            viagem.setCodigoCargoViajante(Ifunc.getCargo().getId());
            viagem.setCodigoDepartamentoViajante(Ifunc.getDepartamento().getCodigo());
             */

            viagem.setNatureza(Natureza.valueOf(natureza));

            viagem.setDescricao(viagemComAgendamentoForm.getDescricao());

            viagem.setDataSaidaPrevista(simpleDate.parse(dataSaidaPrevista + " " + horaSaidaPrevista));
            viagem.setDataRetornoPrevista(simpleDate.parse(dataRetornoPrevista + " " + horaRetornoPrevista));

            //viagem.setTipoDiaria(TipoDiaria.PADRAO);
            viagem.setTipoDiaria(TipoDiaria.getById(Integer.valueOf(tipoDiaria)));

            viagem.setQtDiaria(Valores.desformataValor(qtDiaria));
            viagem.setDiaria(Valores.desformataValor(diaria));
            viagem.setDiariaP(Valores.desformataValor(diariaP));
            viagem.setValorAdiantamento(Valores.desformataValor(valorAdiantamento));
            viagem.setTotalAdiantamento(Valores.desformataValor(totalAdiantamento));
            viagem.setTotalDiarias(Valores.desformataValor(totalDiarias));

            viagem.setDestino(DestinoViagem.valueOf(destino));

            viagem.setDescricaoE(descricaoE);

            ItemItinerarioBO itemItinerarioBO = new ItemItinerarioBO();
            itemItinerarioBO.excluir(viagem.getItinerario().getItensItinerario());

            Itinerario itinerario = new ItinerarioBO().obterPorPk(viagem.getItinerario().getId());
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

            new ViagemComAgendamentoBO().alterar(viagem);
            //new ViagemBO().alterar(viagem);

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
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Agendamento alterado com sucesso.");

        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao alterar o Agendamento.");
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(ViagemComAgendamentoAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return mapping.findForward("redirect");
    }

    public ActionForward excluir(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        try {
            String[] id = new String[0];

            if (request.getParameterValues("ids_exclusao") != null) {
                id = request.getParameterValues("ids_exclusao");
            }
            ViagemComAgendamentoBO viagemComAgendamentoBO = new ViagemComAgendamentoBO();
            HibernateUtil.beginTransaction();
            for (int i = 0; i < id.length; i++) {

                ViagemComAgendamento viagemComAgendamento = viagemComAgendamentoBO.obterPorPk(Integer.valueOf(id[i]));

                viagemComAgendamentoBO.excluir(viagemComAgendamento);

                MensagemTagUtil.adicionarMensagem(request.getSession(), "Exclusão realizada com sucesso!");

            }
            HibernateUtil.commitTransaction();


        } catch (AplicacaoException ex) {
            Logger.getLogger(ViagemComAgendamentoAction.class.getName()).log(Level.SEVERE, null, ex);
            MensagemTagUtil.adicionarMensagem(request.getSession(), "A Viagem está associada. Não pode ser excluída!");
        }

        return mapping.findForward("redirect");

    }

    /**********************************

    AGENDAMENTO VIAGEM - FIM

     *********************************/
    /**********************************

    RECEBER SOLICITAÇÃO - INICIO

     *********************************/
    @Funcionalidade(nomeCurto="agendAprovAgendViagem")
    public ActionForward listaRecebimento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            Collection<ViagemDTO> viagensL1DTO = new ArrayList<ViagemDTO>();
            Collection<ViagemDTO> viagensL2DTO = new ArrayList<ViagemDTO>();
            EntidadeDAO entidadeDAO = new EntidadeDAO();

            Map<Integer, IFuncionario> mapFunc = new RHServico().obterMapTodos();

            //Collection<ViagemComAgendamento> viagens = new ViagemComAgendamentoBO().obterTodos();
            Collection<ViagemComAgendamento> viagens = new ViagemComAgendamentoBO().obterTodosNaoRecebido();
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            for (Viagem viagem : viagens) {
                ViagemDTO viagemDTO = new ViagemDTO();
                String nome = null;

                if (viagem.getCodigoConsultorViajante() == null) {
                    //nome = new HRBIImpl().obterFuncionarioPorMatricula(viagem.getMatriculaUsuarioViajante()).getNome();
                    nome = ((IFuncionario) mapFunc.get(viagem.getCodigoDominioUsuarioViajante())).getNome();
                } else {
                    nome = ((Entidade) entidadeDAO.obterConsultorPorCodigo(viagem.getCodigoConsultorViajante())).getNomeExtenso();
                }

                if(viagem.getCodigoCentroCusto() != null && viagem.getCodigoCentroResponsabilidade() != null){
                    viagemDTO.setAgendamentoOK("Sim");
                }

                switch (((ViagemComAgendamento) viagem).getStatusAgendamento()) {
                    case AGENDAMENTO_ABERTO:
                        viagemDTO.setNomeViajante(nome);
                        viagemDTO.setViagem(viagem);
                        viagemDTO.setPeriodo(simpleDate.format(viagem.getDataSaidaPrevista()) + " " + simpleDate.format(viagem.getDataRetornoPrevista()));
                        viagensL1DTO.add(viagemDTO);
                        break;

                    case AGENDAMENTO_APROVADO:
                        viagemDTO.setNomeViajante(nome);
                        viagemDTO.setViagem(viagem);
                        viagemDTO.setPeriodo(simpleDate.format(viagem.getDataSaidaPrevista()) + " " + simpleDate.format(viagem.getDataRetornoPrevista()));
                        viagensL2DTO.add(viagemDTO);
                        break;

                    /*case AGENDAMENTO_REPROVADO:
                        viagemDTO.setNomeViajante(nome);
                        viagemDTO.setViagem(viagem);
                        viagemDTO.setPeriodo(simpleDate.format(viagem.getDataSaidaPrevista()) + " " + simpleDate.format(viagem.getDataRetornoPrevista()));
                        viagensL1DTO.add(viagemDTO);
                        break;*/
                }
            }

            request.setAttribute("lista1", viagensL1DTO);
            request.setAttribute("lista2", viagensL2DTO);
        } catch (AplicacaoException ex) {
            Logger.getLogger(ViagemComAgendamentoAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapping.findForward("recebimentoAgenLista");
    }

    public ActionForward receberViagem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        String id = request.getParameter("id");

        try {
            if (GenericValidator.isInt(id)) {
                ViagemComAgendamento viagem = new ViagemComAgendamentoBO().obterPorPk(Integer.valueOf(id));

                IFuncionario viajante = new RHServico().obterFuncionarioPorMatricula(viagem.getCodigoDominioUsuarioViajante());
                ContaCorrenteViagem conta = new ContaCorrenteViagemBO().obterPorMatriculaFuncionario(Integer.toString(viagem.getCodigoDominioUsuarioViajante()));

                if (conta == null) {
                    conta = new ContaCorrenteViagem();
                    conta.setAgencia(viajante.getAgencia());
                    conta.setConta(viajante.getConta());
                    conta.setBanco(new BancoBO().obterPorCodigo(viajante.getBanco()));
                }

                request.setAttribute("viajante", viajante);
                request.setAttribute("conta", conta);
                request.setAttribute("idViagem", viagem.getId());
                request.setAttribute("listaNaturezas", Natureza.values());

                dyna.set("viagem", viagem);
                dyna.set("destino", viagem.getDestino().name());
                dyna.set("natureza", viagem.getNatureza().getCodigo());
                dyna.set("dataSaidaPrevista", Data.formataData(viagem.getDataSaidaPrevista()));
                dyna.set("horaSaidaPrevista", Data.formataHoraCurta(viagem.getDataSaidaPrevista()));
                dyna.set("dataRetornoPrevista", Data.formataData(viagem.getDataRetornoPrevista()));
                dyna.set("horaRetornoPrevista", Data.formataHoraCurta(viagem.getDataRetornoPrevista()));
                dyna.set("qtDiaria", String.valueOf(viagem.getQtDiaria()));
                dyna.set("diaria", String.valueOf(viagem.getDiaria()));
                dyna.set("diariaP", String.valueOf(viagem.getDiariaP()));
                dyna.set("valorAdiantamento", String.valueOf(viagem.getValorAdiantamento()));
                dyna.set("descricaoE", viagem.getDescricaoE());
                dyna.set("observacao", viagem.getItinerario().getObservacao());

            }
            return mapping.findForward("recebimentoAgenEditar");
        } catch (AplicacaoException ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar selecionar a Solicitação.");
        }
        return mapping.findForward("recebimentoAgenRedirect");
    }

    public ActionForward aprovarAgendamento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            String[] id = new String[0];
            if (request.getParameterValues("ids_selecionados") != null) {
                id = request.getParameterValues("ids_selecionados");
            }

            HibernateUtil.beginTransaction();
            Usuario usuarioViajante = null;
            ArrayList<String> resposta = null;
            boolean podeSolicitar = true;
            ViagemComAgendamento viagem = null;

            for (int i = 0; i < id.length; i++) {
                podeSolicitar = true;
                viagem = new ViagemComAgendamentoBO().obterPorPk(Integer.valueOf(id[i]));

                //Checa se o func_atual pode viajar
                usuarioViajante = (Usuario) new UsuarioBO().obterPorCodigoDominio(viagem.getCodigoDominioUsuarioViajante());
                resposta = new SolicitarViagemBO().funcionarioPodeViajar(usuarioViajante);
                if (resposta.get(0).equals("false") || resposta.get(2).equals("true")) {
                    podeSolicitar = false;
                }

                //checar se a linha abaixo está igual a: if(!podeSolicitar && resposta.get(2).equals("false")){
                if(!podeSolicitar && resposta.get(2).equals("false")){
                    MensagemTagUtil.adicionarMensagem(request.getSession(), resposta.get(1));

                }else{
                    viagem.setStatusAgendamento(StatusAgendamento.AGENDAMENTO_APROVADO);
                    viagem.setStatusViagem(StatusViagem.VIAGEM_ABERTA);
                    new ViagemComAgendamentoBO().alterar(viagem);
                    MensagemTagUtil.adicionarMensagem(request.getSession(), "["+viagem.getId()+"] Operação realizada com sucesso.");

                    if(!podeSolicitar){
                        //dar baixa na liberação de pendência se existe!
                        IFuncionario Ifunc = new RHServico().obterFuncionarioPorMatricula(viagem.getCodigoDominioUsuarioViajante());
                        LiberacaoViagem liberacao = new LiberacaoViagemBO().obterByIFuncionario(Ifunc);
                        if ((liberacao != null) && (liberacao.isValido())) {
                            liberacao.setValido(false);
                            liberacao.setViagem(viagem);
                            new LiberacaoViagemBO().alterar(liberacao);
                            MensagemTagUtil.adicionarMensagem(request.getSession(), "[ Liberação usada ]");
                        }
                    }
                }

            }
            HibernateUtil.commitTransaction();

        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao aprovar o Agendamento.");
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(ViagemComAgendamentoAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return mapping.findForward("recebimentoAgenRedirect");
    }

    public ActionForward reprovarAgendamento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            String[] id = new String[0];

            if (request.getParameterValues("ids_selecionados") != null) {
                id = request.getParameterValues("ids_selecionados");
            }

            HibernateUtil.beginTransaction();

            for (int i = 0; i < id.length; i++) {

                ViagemComAgendamento viagem = new ViagemComAgendamentoBO().obterPorPk(Integer.valueOf(id[i]));
                viagem.setStatusAgendamento(StatusAgendamento.AGENDAMENTO_REPROVADO);
                viagem.setStatusViagem(StatusViagem.VIAGEM_CANCELADA);
                viagem.setDescricao("Agendamento cancelado");
                new ViagemComAgendamentoBO().alterar(viagem);
                MensagemTagUtil.adicionarMensagem(request.getSession(), "Operação realizada com sucesso.");
            }

            HibernateUtil.commitTransaction();

        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao reprovar o Agendamento.");
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(ViagemComAgendamentoAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return mapping.findForward("recebimentoAgenRedirect");
    }

    /**********************************

    RECEBER SOLICITAÇÃO - FIM

     *********************************/
}