package br.org.flem.sav.action;

import br.org.flem.fw.arquivo.Layout;
import br.org.flem.fw.arquivo.gem.layout.Parcela;
import br.org.flem.fw.arquivo.gem.layout.RegistroHeader;
import br.org.flem.fw.arquivo.gem.layout.RegistroLinha;
import br.org.flem.fw.persistencia.dao.legado.bdcolaborador.ColaboradorDAO;
import br.org.flem.fw.persistencia.dao.legado.bdprocesso.ControleProcessoDAO;
import br.org.flem.fw.persistencia.dao.legado.control.CentroCustoUsuarioDAO;
import br.org.flem.fw.persistencia.dao.legado.control.DepartamentoTipoSolicitacaoDAO;
import br.org.flem.fw.persistencia.dao.legado.control.DepartamentoUsuarioDAO;
import br.org.flem.fw.persistencia.dao.legado.gem.CentroCustoDAO;
import br.org.flem.fw.persistencia.dao.legado.gem.EntidadeDAO;
import br.org.flem.fw.persistencia.dao.legado.gem.FonteRecursoDAO;
import br.org.flem.fw.persistencia.dto.FonteRecurso;
import br.org.flem.fw.persistencia.dto.Funcionario;
import br.org.flem.fw.persistencia.dto.Usuario;
import br.org.flem.fw.service.CentroResponsabilidade;
import br.org.flem.fw.service.IColaborador;
import br.org.flem.fw.service.IFonteRecurso;
import br.org.flem.fw.service.IFuncionario;
import br.org.flem.fw.service.IUsuario;
import br.org.flem.fw.service.impl.RHServico;
import br.org.flem.fw.util.Constante;
import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fwe.hibernate.util.HibernateUtil;
import br.org.flem.fwe.service.IUsuarioExterno;
import br.org.flem.fwe.util.CPF;
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
import br.org.flem.sav.bo.ReservaBO;
import br.org.flem.sav.bo.SolicitarViagemBO;
import br.org.flem.sav.bo.TrechoBO;
import br.org.flem.sav.bo.UsuarioBO;
import br.org.flem.sav.bo.ViagemBO;
import br.org.flem.sav.dto.ItemItinerarioDTO;
import br.org.flem.sav.dto.RelatorioViagem;
import br.org.flem.sav.dto.ViagemDTO;
import br.org.flem.sav.negocio.Companhia;
import br.org.flem.sav.negocio.ContaCorrenteViagem;
import br.org.flem.sav.negocio.DestinoViagem;
import br.org.flem.sav.negocio.ItemItinerario;
import br.org.flem.sav.negocio.Itinerario;
import br.org.flem.sav.negocio.LiberacaoViagem;
import br.org.flem.sav.negocio.Natureza;
import br.org.flem.sav.negocio.PercentualDiaria;
import br.org.flem.sav.negocio.Reserva;
import br.org.flem.sav.negocio.SituacaoDataPagamentoEnum;
import br.org.flem.sav.negocio.StatusAgendamento;
import br.org.flem.sav.negocio.StatusViagem;
import br.org.flem.sav.negocio.TipoDiaria;
import br.org.flem.sav.negocio.TipoLiberacao;
import br.org.flem.sav.negocio.Trecho;
import br.org.flem.sav.negocio.Viagem;
import br.org.flem.sav.negocio.ViagemComAgendamento;
import br.org.flem.sav.negocio.util.Cidade;
import br.org.flem.sav.negocio.util.Diarias;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
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
public class ViagemAction extends SecurityDispatchAction {

    /**
     * ********************************
     *
     * SOLICITAÇÃO VIAGEM - INICIO
     *
     ********************************
     */
    @Override
    @Funcionalidade(nomeCurto = "solicViagFuncionario")
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
        Usuario usuarioViajante;
        IFuncionario func_sessao = null;
        IFuncionario func_atual = null;
        boolean podeSolicitar = true;
        boolean deFerias = false;
        String novaSolicitacaoTexto = "";
        ArrayList<String> resposta = new ArrayList<String>();

        Map<Integer, IFuncionario> mapFunc = new RHServico().obterMapTodos();

        // início da lista de usuários
        Collection<IFuncionario> IFList = new DepartamentoUsuarioDAO().obterFuncionariosPorDepartamento(usuario);

        String matriculaUsuario = (String) request.getSession().getAttribute("matriculaUsuario");

        // func_atual é o usuário logado ou o usuário selecionado na combo
        if (matriculaUsuario != null && !matriculaUsuario.isEmpty()) {
            func_atual = (IFuncionario) mapFunc.get(Integer.parseInt(matriculaUsuario));
        } else {
            if (usuario.getCodigoDominio() != null && usuario.getCodigoDominio() > 0) {
                func_atual = (IFuncionario) mapFunc.get(usuario.getCodigoDominio());
            } else {
                func_atual = ((List<IFuncionario>) IFList).get(0);
            }
        }

        //Checa se o func_atual pode viajar
        usuarioViajante = (Usuario) new UsuarioBO().obterPorCodigoDominio(func_atual.getCodigoDominio());
        resposta = new SolicitarViagemBO().funcionarioPodeViajar(usuarioViajante);//Inclusão da verificação de Conta Corrente de funcionário
        if(resposta != null && !resposta.isEmpty()){
            if (resposta.get(0).equals("false")) {
                podeSolicitar = false;
            }
            novaSolicitacaoTexto = resposta.get(1);
            if(resposta.get(2).equals("conta")){
                if(usuario.getPermissoes().contains("admCadCc")){
                    novaSolicitacaoTexto = novaSolicitacaoTexto + " regularize no <a href=\"ContaCorrenteViagem.do?metodo=novo&matricula="+usuarioViajante.getCodigoDominio()+ "\">Regularizar</a>";
                }
                
            }
        }

        usuarioViajante = new UsuarioBO().atualizarStatusFerias(usuarioViajante);
        if (usuarioViajante.isBloqueadoAd() || usuarioViajante.isBloqueadoSe()) {
            deFerias = true;
        }

        request.setAttribute("novaSolicitacao", podeSolicitar);
        request.setAttribute("deFerias", deFerias);
        request.setAttribute("novaSolicitacaoTexto", novaSolicitacaoTexto);
        request.setAttribute("listaFuncionario", IFList);
        request.setAttribute("nmeUsuarioLogado", usuario.getNome());
        request.setAttribute("matriculaUsuarioLogado", usuario.getCodigoDominio());

        request.setAttribute("solicEditarViagFuncionario", "0");
        request.setAttribute("solicCriarEditarPrestFuncionario", "0");
        IUsuarioExterno usuarioExterno = (IUsuarioExterno) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
        if (usuarioExterno.getPermissoes().contains("solicEditarViagFuncionario")) {
            request.setAttribute("solicEditarViagFuncionario", "1");
        }
        if (usuarioExterno.getPermissoes().contains("solicCriarEditarPrestFuncionario")) {
            request.setAttribute("solicCriarEditarPrestFuncionario", "1");
        }

        try {
            Collection<ViagemDTO> viagensDTO = new ArrayList<ViagemDTO>();
            ViagemBO viagemBO = new ViagemBO();

            Collection<Viagem> viagens = viagemBO.obterTodosFuncionario(func_atual);

            boolean exibir = false;
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            ViagemComAgendamento viagemAgendada = new ViagemComAgendamento();

            for (Viagem viagem : viagens) {
                exibir = false;
                ViagemDTO vdto = new ViagemDTO();
                vdto.setViagem(viagem);

                if (viagem instanceof ViagemComAgendamento) {
                    viagemAgendada = (ViagemComAgendamento) viagem;

                    if (viagemAgendada.getStatusAgendamento().equals(StatusAgendamento.AGENDAMENTO_APROVADO)) {
                        exibir = true;
                        vdto.setStatusViagem(StatusAgendamento.AGENDAMENTO_APROVADO.toString());
                        vdto.setViagemComAgendamento(viagemAgendada);
                    } else if (viagemAgendada.getStatusAgendamento().equals(StatusAgendamento.AGENDAMENTO_REPROVADO)) {
                        exibir = true;
                        vdto.setStatusViagem(StatusAgendamento.AGENDAMENTO_REPROVADO.toString());
                        vdto.setViagemComAgendamento(viagemAgendada);
                    }
                } else {
                    exibir = true;
                    vdto.setStatusViagem(viagem.getStatusViagem().name());
                }

                func_sessao = mapFunc.get(viagem.getCodigoDominioUsuarioViajante());
                vdto.setNomeViajante(func_sessao.getNome());

                if (viagem.getPrestacaoContas() == null) {
                    vdto.setPrestouConta("0");
                    vdto.setPeriodo(simpleDate.format(viagem.getDataSaidaPrevista()) + " " + simpleDate.format(viagem.getDataRetornoPrevista()));
                } else {
                    vdto.setPrestouConta("1");
                    vdto.setPeriodo(simpleDate.format(viagem.getPrestacaoContas().getDataSaidaEfetiva()) + " " + simpleDate.format(viagem.getPrestacaoContas().getDataRetornoEfetiva()));
                }

                if (exibir) {
                    viagensDTO.add(vdto);
                }
            }

            request.setAttribute("lista", viagensDTO);
            request.setAttribute("iniFerias", "0");
            request.setAttribute("fimFerias", "0");
            if (usuarioViajante.getDataInicioBloqueio() != null && usuarioViajante.getDataFimBloqueio() != null) {
                request.setAttribute("iniFerias", Data.formataData(usuarioViajante.getDataInicioBloqueio(), "dd/MM/yyyy"));
                request.setAttribute("fimFerias", Data.formataData(usuarioViajante.getDataFimBloqueio(), "dd/MM/yyyy"));
            }

        } catch (Exception ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", this.getClass().getName(), ex);
        }
        return mapping.findForward("lista");
    }

    @Funcionalidade(nomeCurto = "solicViagFuncionario")
    public ActionForward filtrar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        Viagem filtro = (Viagem) dyna.get("viagem");
        if (filtro.getCodigoDominioUsuarioViajante() != null) {
            request.getSession().setAttribute("matriculaUsuario", filtro.getCodigoDominioUsuarioViajante().toString());
        }
        return unspecified(mapping, form, request, response);
    }

    public ActionForward novo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
        try {
            DynaActionForm dyna = (DynaActionForm) form;
            RHServico rh = new RHServico();
            CentroCustoUsuarioDAO cCustoUsuarioDAO = new CentroCustoUsuarioDAO();
            boolean deFerias = false;

            Viagem viagemForm = (Viagem) dyna.get("viagem");

            // func_atual é o usuário logado ou o usuário selecionado na combo
            IFuncionario viajante = (IFuncionario) rh.obterFuncionarioPorMatricula(viagemForm.getCodigoDominioUsuarioViajante());
            
              ContaCorrenteViagem conta = new ContaCorrenteViagemBO().obterPorMatriculaFuncionario(Integer.toString(viajante.getCodigoDominio()));
           
          
            /*
            if (conta == null) {
                throw new Exception("Não é possivel gerar uma Viagem para Funcionário sem Conta Corrente cadastrada");
            }
            */
            if (conta == null) {
/*  258 */         conta = new ContaCorrenteViagem();
/*  259 */         conta.setAgencia(viajante.getAgencia());
/*  260 */         conta.setConta(viajante.getConta());
/*  261 */         conta.setBanco((new BancoBO()).obterPorCodigo(viajante.getBanco()));
              } 
            
            Usuario usr = (Usuario) new UsuarioBO().obterPorCodigoDominio(viajante.getCodigoDominio());
            usr = new UsuarioBO().atualizarStatusFerias(usr);
            if (usr.isBloqueadoAd() || usr.isBloqueadoSe()) {
                deFerias = true;
            }

            Collection<CentroResponsabilidade> ccs = cCustoUsuarioDAO.obterCCustoViajante(usuario.getCodigoDominio());


            //OBTER O PERCENTUAL DE DIÁRIA DO VIAJANTE COM BASE EM SUA LOTAÇÃO
            PercentualDiariaBO percentualDiariaBO = new PercentualDiariaBO();
            PercentualDiaria percentualDiaria = percentualDiariaBO.obterPorDepartamentoDominio(usr.getLotacaoDominio());
            if (percentualDiaria == null) {
                percentualDiaria = percentualDiariaBO.obterPorDepartamentoDominio(0);//"PADRAO"
            }
            request.setAttribute("percentualDiaria", percentualDiaria);


            request.setAttribute("viajante", viajante);
            request.setAttribute("conta", conta);
            request.setAttribute("usuario", usuario);
            request.setAttribute("listaNaturezas", Natureza.values());
            request.setAttribute("listaTiposDiaria", TipoDiaria.toCollection());
            request.setAttribute("listaCentrosCusto", ccs);
            request.setAttribute("listaCentrosResponsabilidade", cCustoUsuarioDAO.obterCRespViajante(usuario.getCodigoDominio()));
            request.setAttribute("listaCompanhias", new CompanhiaBO().obterTodos());
            request.setAttribute("listaDestinoViagem", DestinoViagem.toCollection());

            request.setAttribute("deFerias", deFerias);
            request.setAttribute("iniFerias", "0");
            request.setAttribute("fimFerias", "0");
            if (usr.getDataInicioBloqueio() != null && usr.getDataFimBloqueio() != null) {
                request.setAttribute("iniFerias", Data.formataData(usr.getDataInicioBloqueio(), "dd/MM/yyyy"));
                request.setAttribute("fimFerias", Data.formataData(usr.getDataFimBloqueio(), "dd/MM/yyyy"));
            }

            LiberacaoViagem liberacaoR = new LiberacaoViagemBO().obterByIFuncionarioTipo(viajante, TipoLiberacao.RETROATIVO);
            if ((liberacaoR != null) && (liberacaoR.isValido()) || new DepartamentoTipoSolicitacaoDAO().lotacaoPodeSolicitarViagemRetroativa(usr.getLotacaoDominio())) {
                request.setAttribute("liberacaoRetroativo", "true");
            } else {
                request.setAttribute("liberacaoRetroativo", "false");
            }

            LiberacaoViagem liberacaoL50 = new LiberacaoViagemBO().obterByIFuncionarioTipo(viajante, TipoLiberacao.LIMITE_50);
            if (liberacaoL50 != null) {
                request.setAttribute("liberacaoL50", "true");
            } else {
                request.setAttribute("liberacaoL50", "false");
            }

        } catch (Exception ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", this.getClass().getName(), ex);
        }

        return mapping.findForward("novo");
    }

    public ActionForward adicionar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;

        try {
            IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
            Reserva reserva = new Reserva();
            Companhia companhia = new Companhia();
            Itinerario itinerario = new Itinerario();
            ItemItinerario itemItinerario = null;
            Usuario usuarioViajante;
            boolean podeSolicitar = true;
            ArrayList<String> resposta = new ArrayList<String>();

            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            Date dataAtual = Calendar.getInstance().getTime();
            Date dataSaida = null;
            
            Viagem viagem = (Viagem) dyna.get("viagem");

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
            //String companhiaId = (String) dyna.get("companhiaId");
            //String codigoReserva = (String) dyna.get("codigoReserva");
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



            //Checa se o func_atual pode viajar 
            usuarioViajante = (Usuario) new UsuarioBO().obterPorCodigoDominio(viagem.getCodigoDominioUsuarioViajante());
            resposta = new SolicitarViagemBO().funcionarioPodeViajar(usuarioViajante);
            if (resposta.get(0).equals("false") || resposta.get(2).equals("true")) {
                podeSolicitar = false;
            }

            HibernateUtil.beginTransaction();

            viagem.setStatusViagem(StatusViagem.VIAGEM_ABERTA);

            viagem.setDataSolicitacao(dataAtual);

            viagem.setCodigoDominioUsuarioSolicitante(usuario.getCodigoDominio());
            IFuncionario Ifunc = new RHServico().obterFuncionarioPorMatricula(viagem.getCodigoDominioUsuarioViajante());
            viagem.setCodigoConsultorViajante(null);
            viagem.setCodigoDominioCargoViajante(Ifunc.getCargo().getId());
            viagem.setCodigoDominioDepartamentoViajante(Ifunc.getDepartamento().getCodigoDominio());

            viagem.setNatureza(Natureza.valueOf(natureza));

            viagem.setDataSaidaPrevista(simpleDate.parse(dataSaidaPrevista + " " + horaSaidaPrevista));
            viagem.setDataRetornoPrevista(simpleDate.parse(dataRetornoPrevista + " " + horaRetornoPrevista));

            dataSaida = simpleDate.parse(dataSaidaPrevista + " 23:59");

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

            viagem.setTipoDiaria(TipoDiaria.getById(Integer.valueOf(tipoDiaria)));

            viagem.setDescricaoE(descricaoE);

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

            viagem.setDestino(DestinoViagem.valueOf(destino));

            itinerario.setObservacao(observacao);
            Object objI = new ItinerarioBO().inserir(itinerario);
            itinerario = new ItinerarioBO().obterPorPk((Integer) objI);
            for (int i = 0; i < obs.length; i++) {
                Cidade cO = null;
                if (cidadeO[i].contains(" / ")) {
                    cO = new CidadeBO().obterPorNome(cidadeO[i].substring(0, cidadeO[i].indexOf(" / ")), cidadeO[i].substring(cidadeO[i].indexOf(" / ") + 3, cidadeO[i].length()));
                } else {
                    cO = new CidadeBO().obterPorNome(cidadeO[i]);
                }
                Cidade cD = null;
                if (cidadeD[i].contains(" / ")) {
                    cD = new CidadeBO().obterPorNome(cidadeD[i].substring(0, cidadeD[i].indexOf(" / ")), cidadeD[i].substring(cidadeD[i].indexOf(" / ") + 3, cidadeD[i].length()));
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

            ViagemBO viagemBO = new ViagemBO();
            viagem.setDataPagamento(viagemBO.calcularDataPagamentoPrevisto(viagem.getDataSaidaPrevista(), viagem.getDestino()));
            viagem.setSituacaoDataPagamento(SituacaoDataPagamentoEnum.PREVISTA);

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
                LiberacaoViagem liberacao = new LiberacaoViagemBO().obterByIFuncionario(Ifunc);
                if ((liberacao != null) && (liberacao.isValido())) {
                    liberacao.setValido(false);
                    liberacao.setViagem(viagem);
                    new LiberacaoViagemBO().alterar(liberacao);
                    MensagemTagUtil.adicionarMensagem(request.getSession(), "[ Liberação usada ]");
                }
            }

            if (dataSaida.before(dataAtual)) {
                //dar baixa na liberação de solicitacao retroativa se existe!
                LiberacaoViagem liberacaoR = new LiberacaoViagemBO().obterByIFuncionarioTipo(Ifunc, TipoLiberacao.RETROATIVO);
                if ((liberacaoR != null) && (liberacaoR.isValido())) {
                    liberacaoR.setValido(false);
                    liberacaoR.setViagem(viagem);
                    new LiberacaoViagemBO().alterar(liberacaoR);
                }
            }

            //dar baixa na liberação de solicitacao limite 50 se existe!
            LiberacaoViagem liberacaoL50 = new LiberacaoViagemBO().obterByIFuncionarioTipo(Ifunc, TipoLiberacao.LIMITE_50);
            if ((liberacaoL50 != null) && (liberacaoL50.isValido())) {
                liberacaoL50.setValido(false);
                liberacaoL50.setViagem(viagem);
                new LiberacaoViagemBO().alterar(liberacaoL50);
            }

            MensagemTagUtil.adicionarMensagem(request.getSession(), "Viagem inserida com sucesso.");

            if (viagem.getTotalDiarias() == 0d) {
                MensagemTagUtil.adicionarMensagem(request.getSession(), "");
                MensagemTagUtil.adicionarMensagem(request.getSession(), "AVISO: Viagem inserida sem diária!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao inserir a Viagem.");
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(ViagemAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        try {
            HibernateUtil.commitTransaction();
        } catch (AplicacaoException ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage());
        }
        return mapping.findForward("redirect");
    }

    public ActionForward selecionar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        String id = request.getParameter("id");
        ViagemDTO viagemDTO = new ViagemDTO();
        CentroCustoUsuarioDAO cCustoUsuarioDAO = new CentroCustoUsuarioDAO();
        try {
            LiberacaoViagemBO liberacaoViagemBO = new LiberacaoViagemBO();
            boolean deFerias = false;

            IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
            if (GenericValidator.isInt(id)) {
                Viagem viagem = new ViagemBO().obterPorPk(Integer.valueOf(id));
                viagemDTO.setViagem(viagem);

                IFuncionario viajante = new RHServico().obterFuncionarioPorMatricula(viagem.getCodigoDominioUsuarioViajante());
                Collection<CentroResponsabilidade> ccs = cCustoUsuarioDAO.obterCCustoViajante(usuario.getCodigoDominio());

                Usuario usr = (Usuario) new UsuarioBO().obterPorCodigoDominio(viajante.getCodigoDominio());
                usr = new UsuarioBO().atualizarStatusFerias(usr);
                if (usr.isBloqueadoAd() || usr.isBloqueadoSe()) {
                    deFerias = true;
                }

                CentroResponsabilidade cResponsabilidade = new CentroCustoDAO().obterCentroDeResponsabilidadePorId(viagem.getCodigoCentroResponsabilidade());
                ArrayList<CentroResponsabilidade> crs = new ArrayList<CentroResponsabilidade>();
                crs.add(cResponsabilidade);

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

                dyna.set("diaria", String.valueOf(viagem.getDiaria()));
                dyna.set("diariaP", String.valueOf(viagem.getDiariaP()));
                dyna.set("qtDiaria", String.valueOf(viagem.getQtDiaria()));
                dyna.set("valorAdiantamento", String.valueOf(viagem.getValorAdiantamento()));
                dyna.set("totalDiarias", String.valueOf(viagem.getTotalDiarias()));
                dyna.set("totalAdiantamento", String.valueOf(viagem.getTotalAdiantamento()));
                dyna.set("descricaoE", viagem.getDescricaoE());
                dyna.set("observacao", viagem.getItinerario().getObservacao());

                //OBTER O PERCENTUAL DE DIÁRIA DO VIAJANTE COM BASE EM SUA LOTAÇÃO
                PercentualDiariaBO percentualDiariaBO = new PercentualDiariaBO();
                PercentualDiaria percentualDiaria = percentualDiariaBO.obterPorDepartamentoDominio(usr.getLotacaoDominio());
                if (percentualDiaria == null) {
                    percentualDiaria = percentualDiariaBO.obterPorDepartamentoDominio(0);//"PADRAO"
                }
                request.setAttribute("percentualDiaria", percentualDiaria);

                request.setAttribute("viagensDTO", viagemDTO);
                request.setAttribute("viajante", viajante);
                request.setAttribute("listaNaturezas", Natureza.values());
                request.setAttribute("listaTiposDiaria", TipoDiaria.toCollection());
                request.setAttribute("listaCentrosCusto", ccs);
                //request.setAttribute("listaCentrosResponsabilidade", cCustoUsuarioDAO.obterCRespViajante(usuario.getMatricula()));
                request.setAttribute("listaCentrosResponsabilidade", crs);
                request.setAttribute("listaCompanhias", new CompanhiaBO().obterTodos());
                request.setAttribute("itensItinerario", new ItemItinerarioBO().obterPorItinerario(viagem.getItinerario()));
                request.setAttribute("itensTrecho", new TrechoBO().obterPorViagem(viagem));
                request.setAttribute("listaDestinoViagem", DestinoViagem.toCollection());

                request.setAttribute("deFerias", deFerias);
                request.setAttribute("iniFerias", "0");
                request.setAttribute("fimFerias", "0");
                if (usr.getDataInicioBloqueio() != null && usr.getDataFimBloqueio() != null) {
                    request.setAttribute("iniFerias", Data.formataData(usr.getDataInicioBloqueio(), "dd/MM/yyyy"));
                    request.setAttribute("fimFerias", Data.formataData(usr.getDataFimBloqueio(), "dd/MM/yyyy"));
                }

                LiberacaoViagem liberacaoR = liberacaoViagemBO.obterByIFuncionarioTipo(viajante, TipoLiberacao.RETROATIVO);
                if ((liberacaoR != null) && (liberacaoR.isValido()) || new DepartamentoTipoSolicitacaoDAO().lotacaoPodeSolicitarViagemRetroativa(usr.getLotacaoDominio())) {
                    request.setAttribute("liberacaoRetroativo", "true");
                } else {
                    request.setAttribute("liberacaoRetroativo", "false");
                }

                LiberacaoViagem liberacaoL50 = liberacaoViagemBO.obterByViagemTipo(viagem, TipoLiberacao.LIMITE_50);
                if (liberacaoL50 != null) {
                    request.setAttribute("liberacaoL50", "true");
                } else {
                    liberacaoL50 = liberacaoViagemBO.obterByIFuncionarioTipo(viajante, TipoLiberacao.LIMITE_50);
                    if (liberacaoL50 != null) {
                        request.setAttribute("liberacaoL50", "true");
                    } else {
                        request.setAttribute("liberacaoL50", "false");
                    }
                }

                return mapping.findForward("editar");
            }
        } catch (AplicacaoException ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar selecionar a Solicitação.");
        }
        return unspecified(mapping, form, request, response);
    }

    public ActionForward alterar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
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
            String qtDiaria = (String) dyna.get("qtDiaria");
            String descricaoE = (String) dyna.get("descricaoE");
            String valorAdiantamento = (String) dyna.get("valorAdiantamento");
            String totalDiarias = (String) dyna.get("totalDiarias");
            String totalAdiantamento = (String) dyna.get("totalAdiantamento");
            String dataSaidaPrevista = (String) dyna.get("dataSaidaPrevista");
            String horaSaidaPrevista = (String) dyna.get("horaSaidaPrevista");
            String dataRetornoPrevista = (String) dyna.get("dataRetornoPrevista");
            String horaRetornoPrevista = (String) dyna.get("horaRetornoPrevista");
            String temReserva = (String) dyna.get("temReserva");
            //String companhiaId = (String) dyna.get("companhiaId");
            //String codigoReserva = (String) dyna.get("codigoReserva");
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

            viagem.setCodigoDominioUsuarioSolicitante(usuario.getCodigoDominio());

            IFuncionario Ifunc = new RHServico().obterFuncionarioPorMatricula(viagem.getCodigoDominioUsuarioViajante());
            viagem.setCodigoDominioCargoViajante(Ifunc.getCargo().getId());
            viagem.setCodigoDominioDepartamentoViajante(Ifunc.getDepartamento().getCodigoDominio());

            viagem.setCodigoCentroCusto(viagemForm.getCodigoCentroCusto());
            viagem.setCodigoCentroResponsabilidade(viagemForm.getCodigoCentroResponsabilidade());
            viagem.setNatureza(Natureza.valueOf(natureza));

            viagem.setDescricaoE(descricaoE);
            viagem.setDescricao(viagemForm.getDescricao());

            viagem.setDataSaidaPrevista(simpleDate.parse(dataSaidaPrevista + " " + horaSaidaPrevista));
            viagem.setDataRetornoPrevista(simpleDate.parse(dataRetornoPrevista + " " + horaRetornoPrevista));

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
            if (valorAdiantamento == null || valorAdiantamento.isEmpty()) {
                valorAdiantamento = "0,00";
            }
            viagem.setValorAdiantamento(Valores.desformataValor(valorAdiantamento));
            if (totalAdiantamento == null || totalAdiantamento.isEmpty() || totalAdiantamento.equals("0,00")) {
                totalAdiantamento = Valores.formataValor(String.valueOf(Valores.desformataValor(totalDiarias) + Valores.desformataValor(valorAdiantamento)));
            }
            viagem.setTotalAdiantamento(Valores.desformataValor(totalAdiantamento));
            viagem.setTotalDiarias(Valores.desformataValor(totalDiarias));

            viagem.setDestino(DestinoViagem.valueOf(destino));

            ViagemBO viagemBO = new ViagemBO();


            ItemItinerarioBO itemItinerarioBO = new ItemItinerarioBO();
            itemItinerarioBO.excluir(viagem.getItinerario().getItensItinerario());

            Itinerario itinerario = new ItinerarioBO().obterPorPk(viagem.getItinerario().getId());
            itinerario.setObservacao(observacao);

            for (int i = 0; i < obs.length; i++) {
                Cidade cO = null;
                if (cidadeO[i].contains(" / ")) {
                    cO = new CidadeBO().obterPorNome(cidadeO[i].substring(0, cidadeO[i].indexOf(" / ")), cidadeO[i].substring(cidadeO[i].indexOf(" / ") + 3, cidadeO[i].length()));
                } else {
                    cO = new CidadeBO().obterPorNome(cidadeO[i]);
                }
                Cidade cD = null;
                if (cidadeD[i].contains(" / ")) {
                    cD = new CidadeBO().obterPorNome(cidadeD[i].substring(0, cidadeD[i].indexOf(" / ")), cidadeD[i].substring(cidadeD[i].indexOf(" / ") + 3, cidadeD[i].length()));
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

            viagem.setDataPagamento(viagemBO.calcularDataPagamentoPrevisto(viagem.getDataSaidaPrevista(), viagem.getDestino()));
            viagem.setSituacaoDataPagamento(SituacaoDataPagamentoEnum.PREVISTA);
            viagemBO.alterar(viagem);

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

            if (viagem.getTotalDiarias() == 0d) {
                MensagemTagUtil.adicionarMensagem(request.getSession(), "");
                MensagemTagUtil.adicionarMensagem(request.getSession(), "AVISO: Viagem alterada está sem diária!");
            }

        } catch (AcessoDadosException ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao alterar a Viagem.");
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(ViagemAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (AplicacaoException ex2) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao alterar a Viagem.");
            Logger.getLogger(ViagemAction.class.getName()).log(Level.SEVERE, null, ex2);
        } catch (ParseException ex3) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao alterar a Viagem.");
            Logger.getLogger(ViagemAction.class.getName()).log(Level.SEVERE, null, ex3);
        }

        return mapping.findForward("redirect");

    }

    public ActionForward selecionarDataPagamento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        String id = request.getParameter("id");
        try {
            if (GenericValidator.isInt(id)) {
                Viagem viagem = new ViagemBO().obterPorPk(Integer.valueOf(id));

                dyna.set("viagem", viagem);
                dyna.set("dataPagamento", Data.formataData(viagem.getDataPagamento()));

                request.setAttribute("viagem", viagem);
            }
            return mapping.findForward("editarDataPagamento");
        } catch (AplicacaoException ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar selecionar a Solicitação.");
        }
        return unspecified(mapping, form, request, response);
    }

    public ActionForward alterarDataPagamento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;

        try {
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");

            Viagem viagemForm = (Viagem) dyna.get("viagem");
            Viagem viagem = new ViagemBO().obterPorPk(viagemForm.getId());

            String dataPagamentoString = (String) dyna.get("dataPagamento");
            Date dataPagamento = simpleDate.parse(dataPagamentoString);
            viagem.setDataPagamento(dataPagamento);
            if (new Diarias().verificar(dataPagamento, new RHServico().obterFuncionarioPorMatricula(viagem.getCodigoDominioUsuarioViajante()))) {
                new ViagemBO().alterar(viagem);
            } else {
                MensagemTagUtil.adicionarMensagem(request.getSession(), "Não foi possível alterar a data de pagamento pois, na data selecionada, o funcionário já atingiu os 50% do salário em diárias");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar selecionar a Solicitação.");
        }
        return listaRecebimento(mapping, form, request, response);
    }

    public ActionForward excluir(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {

            String[] id = new String[0];
            if (request.getParameterValues("ids_exclusao") != null) {
                id = request.getParameterValues("ids_exclusao");
            }

            HibernateUtil.beginTransaction();
            ViagemBO viagemBO = new ViagemBO();
            for (int i = 0; i < id.length; i++) {
                Viagem viagem = viagemBO.obterPorPk(Integer.valueOf(id[i]));
                if ((viagem.getProcesso() != null) && (viagem.getAnoProcesso() != null)) {
                    new ControleProcessoDAO().cancelaProcessoNumero(viagem.getProcesso());
                }
                viagemBO.excluir(viagem);
            }
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Exclusão realizada com sucesso!");
            HibernateUtil.commitTransaction();

        } catch (AplicacaoException ex) {
            ex.printStackTrace();
            Logger.getLogger(ViagemAction.class.getName()).log(Level.SEVERE, null, ex);
            MensagemTagUtil.adicionarMensagem(request.getSession(), "A Viagem está associada. Não pode ser excluída!");
        }

        return mapping.findForward("redirect");
    }

    public ActionForward relatorioHtml(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        RelatorioViagem relatorioViagem = new RelatorioViagem();
        Collection<ItemItinerarioDTO> itens = new ArrayList<ItemItinerarioDTO>();
        Collection<Trecho> trechos = null;
        IFuncionario Ifunc = null;
        RHServico rh = new RHServico();
        CentroCustoDAO ccDAO = new CentroCustoDAO();

        try {
            if (GenericValidator.isInt(id)) {
                Viagem viagem = new ViagemBO().obterPorPk(Integer.valueOf(id));
                relatorioViagem.setViagem(viagem);

                String tipoMoeda = "R$ ";
                if (viagem.getDestino().equals(DestinoViagem.EXTERIOR)) {
                    tipoMoeda = "US$ ";
                }

                if (viagem.getCodigoDominioUsuarioSolicitante() != null) {
                    Ifunc = rh.obterFuncionarioPorMatricula(viagem.getCodigoDominioUsuarioSolicitante());
                } else {
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
                } else {
                    IColaborador consultor = new EntidadeDAO().obterConsultorPorCodigo(viagem.getCodigoConsultorViajante());
                    relatorioViagem.setNomeViajante(consultor.getNome().toUpperCase());
                    relatorioViagem.setCargoViajante("Consultor");
                    relatorioViagem.setDepartamentoViajante(ccDAO.obterCentroResponsabilidadePorId(viagem.getCodigoCentroResponsabilidade()).getDescricao());
                    relatorioViagem.setCpfViajante(consultor.getCpf());
                    relatorioViagem.setTelViajante("-");
                }

                relatorioViagem.setBancoViajante(viagem.getBanco());
                relatorioViagem.setAgViajante(viagem.getAgencia());
                relatorioViagem.setCcViajante(viagem.getConta());

                relatorioViagem.setCentroCusto("-");
                if (viagem.getCodigoCentroCusto() != null) {
                    relatorioViagem.setCentroCusto(ccDAO.obterCentroDeCustoPorId(viagem.getCodigoCentroCusto()).getDescricaoCompleta());
                }

                relatorioViagem.setCentroResponsabilidade("-");
                if (viagem.getCodigoCentroResponsabilidade() != null) {
                    relatorioViagem.setCentroResponsabilidade(ccDAO.obterCentroDeResponsabilidadePorId(viagem.getCodigoCentroResponsabilidade()).getDescricao());
                }

                relatorioViagem.setNatureza(String.valueOf(viagem.getNatureza().getCodigo()));
                relatorioViagem.setDataSaidaPrevista(String.valueOf(Data.formataData(viagem.getDataSaidaPrevista())));
                relatorioViagem.setHoraSaidaPrevista(String.valueOf(Data.formataHoraCurta(viagem.getDataSaidaPrevista())));
                relatorioViagem.setDataRetornoPrevista(String.valueOf(Data.formataData(viagem.getDataRetornoPrevista())));
                relatorioViagem.setHoraRetornoPrevista(String.valueOf(Data.formataHoraCurta(viagem.getDataRetornoPrevista())));

                relatorioViagem.setTemReserva("NÃO");
                relatorioViagem.setCodigoReserva("-");
                relatorioViagem.setNomeCompanhia("-");
                if (viagem.getReserva() != null) {
                    relatorioViagem.setTemReserva("SIM");
                    relatorioViagem.setCodigoReserva("");
                    relatorioViagem.setNomeCompanhia("");
                }

                relatorioViagem.setDescricaoServico(viagem.getDescricao());

                relatorioViagem.setTipoDiaria(String.valueOf(viagem.getTipoDiaria().getId()));

                relatorioViagem.setDiariaPadrao(viagem.getDiariaP());
                relatorioViagem.setDiariaEspecial(viagem.getDiaria());

                relatorioViagem.setQtdDiarias(viagem.getQtDiaria());
                relatorioViagem.setQtdDiariasEfetivo(viagem.getPrestacaoContas() != null
                        ? viagem.getPrestacaoContas().getQtDiariaEfetiva() : viagem.getQtDiaria());

                relatorioViagem.setTotalDiariaPadrao(viagem.getTotalDiarias());
                relatorioViagem.setTotalDiariaEfetivo(viagem.getTotalDiarias());

                relatorioViagem.setValorAdiantamento(viagem.getValorAdiantamento());

                relatorioViagem.setTotalDiariaAdiantamento(viagem.getTotalAdiantamento());

                relatorioViagem.setDataPrestacao("-");
                if (viagem.getPrestacaoContas() != null) {
                    relatorioViagem.setDataPrestacao(String.valueOf(Data.formataData(viagem.getPrestacaoContas().getDataPrestacao())));
                }

                relatorioViagem.setDataEfetivo((relatorioViagem.getDataSaidaPrevista() + " " + relatorioViagem.getHoraSaidaPrevista() + " a " + relatorioViagem.getDataRetornoPrevista() + " " + relatorioViagem.getHoraRetornoPrevista()));

                relatorioViagem.setDataSolicitacao(String.valueOf(Data.formataData(viagem.getDataSolicitacao())));

                relatorioViagem.setObsDiariaEspecial(viagem.getDescricaoE());

                for (ItemItinerario itinerario : new ItemItinerarioBO().obterPorItinerario(viagem.getItinerario())) {
                    ItemItinerarioDTO itemItinerarioDTO = new ItemItinerarioDTO();
                    itemItinerarioDTO.setData(itinerario.getData());
                    itemItinerarioDTO.setOrigem(itinerario.getOrigem().getNome());
                    itemItinerarioDTO.setDestino(itinerario.getDestino().getNome());
                    itemItinerarioDTO.setId(itinerario.getId());
                    itemItinerarioDTO.setObservacoes(itinerario.getObservacoes());

                    itens.add(itemItinerarioDTO);
                }

                trechos = new TrechoBO().obterPorViagem(viagem);

                request.setAttribute("tipoMoeda", tipoMoeda);
                request.setAttribute("relatorio", relatorioViagem);
                request.setAttribute("itensItinerario", itens);
                request.setAttribute("dataGeracao", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
                request.setAttribute("itensTrecho", trechos);
                request.setAttribute("sizeTrecho", trechos.size());
                request.setAttribute("temPeriodo", relatorioViagem.getDataEfetivo().length() > 8 ? true : false);
            }

            return mapping.findForward("relatorioHtml");
        } catch (AplicacaoException ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar selecionar a Solicitação.");
        }
        return unspecified(mapping, form, request, response);
    }

    public ActionForward exportar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            String[] id = new String[0];
            Map<String, IFonteRecurso> mapFR = new FonteRecursoDAO().obterFonteRecursoPorCodigoAntigo("");
            List<Layout> layout = new ArrayList<Layout>();
            IFuncionario iFunc = null;
            IColaborador consultor = null;
            String cpf = null;
            //String entidade = null;
            boolean temContaBancaria = true;

            Date dataInclusao = new Date();
            Date dataVencimento = null;
            Calendar cal = new GregorianCalendar();
            //Calendar calHj = new GregorianCalendar();

            PercentualDiaria percentualDiaria = null;
            Usuario usr = null;

            if (request.getParameterValues("ids_exportacao") != null) {
                id = request.getParameterValues("ids_exportacao");
            }

            PercentualDiariaBO percentualDiariaBO = new PercentualDiariaBO();

            for (int i = 0; i < id.length; i++) {
                ViagemBO viagemBO = new ViagemBO();
                Viagem viagem = viagemBO.obterPorPk(Integer.valueOf(id[i]));

                if (viagem.getCodigoConsultorViajante() == null) {
                    iFunc = new RHServico().obterFuncionarioPorMatricula(viagem.getCodigoDominioUsuarioViajante());

                    /*if (iFunc.getAgencia().trim().equals("")) {
                        temContaBancaria = false;
                    }*/

                    cpf = iFunc.getCpf();

                    //System.out.println("cpf iFunc = " + cpf);

                    //OBTER O PERCENTUAL DE DIÁRIA DO VIAJANTE COM BASE EM SUA LOTAÇÃO
                    usr = (Usuario) new UsuarioBO().obterPorCodigoDominio(viagem.getCodigoDominioUsuarioViajante());
                    percentualDiaria = percentualDiariaBO.obterPorDepartamentoDominio(usr.getLotacaoDominio());

                } else {
                    //entidade = viagem.getCodigoConsultorViajante();
                    consultor = new EntidadeDAO().obterConsultorPorCodigo(viagem.getCodigoConsultorViajante());
                    if (consultor != null && consultor.getCpf() != null) {
                        cpf = consultor.getCpf();
                    } else {
                        cpf = viagem.getCodigoConsultorViajante();//"06012187793";
                    }

                    //System.out.println("codigo consultor = " + viagem.getCodigoConsultorViajante());
                    //System.out.println("cpf consultor = " + cpf);
                }

                if (!CPF.validarCPF(cpf)) {
                    MensagemTagUtil.adicionarMensagem(request.getSession(), "O CPF '" + cpf + "' é inválido.", "erro", "", null);
                    return mapping.findForward("recebimentoLista");
                }

                //OBTER O PERCENTUAL DE DIÁRIA DO VIAJANTE COM BASE EM SUA LOTAÇÃO
                if (percentualDiaria == null) {
                    percentualDiaria = percentualDiariaBO.obterPorDepartamentoDominio(0);//"PADRAO"
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
                     cal.add(Calendar.DAY_OF_MONTH, -4);

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
                     }*/
                }
                if (viagem.getDataPagamento() == null) {
                    viagem.setDataPagamento(viagemBO.calcularDataPagamentoPrevisto(viagem.getDataSaidaPrevista(), viagem.getDestino()));
                    viagemBO.alterar(viagem);
                }
                dataVencimento = viagem.getDataPagamento();

                header.setCompanhia("FE01");
                header.setGrupoCP("GFE");

                header.setEntidade(cpf);
                header.setLocal("0001");
                header.setTipoDocumentoReferencia("SVG");

                header.setDocumentoReferencia(String.valueOf(viagem.getId()));      ///////modificado
                //header.setTipoDocumentoReferencia2(viagem.getCodigoFonteRecurso());
                header.setTipoDocumentoReferencia2(mapFR.get(viagem.getCodigoFonteRecurso()).getCodigo());
                header.setDocumentoReferencia2("");
                header.setTipoRegistro("01"); // Registro de Header
                header.setDataEmissao(dataInclusao);
                header.setDataRecebimento(dataInclusao);
                header.setDataContabil(dataInclusao);
                header.setCondicaoPagto("AVI");


                header.setDescricao("ADIANTAMENTO PARA VIAGEM - " + viagem.getProcesso());                         ///////modificado
                header.setDescricao1("");
                header.setDescricao2("");

                ///////modificado
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

                header.setMetodoPagto("PEL"); //Mudado de bordero BOR para pagamento eletronico PEL// Campo definido pelo Adm. Financeiro
                header.setIndicadorPagamento("0");


                Double dias = Double.valueOf(String.valueOf(Data.dataDiff(viagem.getDataSaidaPrevista(), viagem.getDataRetornoPrevista())));
                Integer horas = Data.horaDiff(viagem.getDataSaidaPrevista(), viagem.getDataRetornoPrevista());


                dias += percentualDiaria.getQuebraDiarias((double) horas.intValue());

                ///////modificado
                header.setValorTotal(viagem.getTotalAdiantamento());


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
                linhaDespesa.setEntidade(cpf);//TODO ALTERAR
                linhaDespesa.setLocal("0001");
                linhaDespesa.setTipoDocumentoReferencia("SVG");
                linhaDespesa.setDocumentoReferencia(String.valueOf(viagem.getId()));
                linhaDespesa.setTipoRegistro("02"); // "02" = Registro de linha de Despesa
                linhaDespesa.setTipoLinha("1"); // "1" = Tipo de linha de Despesa
                linhaDespesa.setDescricao("ADIANTAMENTO PARA VIAGEM - " + viagem.getProcesso());
                linhaDespesa.setDescricao1("");
                linhaDespesa.setDescricao2("");
                linhaDespesa.setValorLinha(viagem.getTotalAdiantamento());
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
                p.setTipoDocumentoReferencia("SVG");
                p.setDocumentoReferencia(String.valueOf(viagem.getId()));
                p.setTipoRegistro("03"); // "03" = Registro Parcela //
                p.setDataVencimento(dataVencimento);
                p.setValorParcela(viagem.getTotalAdiantamento());
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

            StringBuffer output = new StringBuffer();

            response.setContentType("text/plain");
            response.setHeader("Content-Disposition", "attachment; filename=" + "Exportacao_Viagem_" + new SimpleDateFormat("ddMMyyyy").format(new Date()) + ".txt");

            //escreve o arquivo no buffer de saida
            ServletOutputStream outStream = response.getOutputStream();

            for (Layout l : layout) {
                output.append(l.toLayout() + "\n");
            }

            outStream.print(output.toString());

            outStream.flush();
            outStream.close();

        } catch (SQLException ex) {
            Logger.getLogger(ViagemAction.class.getName()).log(Level.SEVERE, null, ex);

        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", this.getClass().getName(), ex);
            return mapping.findForward("recebimentoLista");
        }
        return null;
    }

    /**
     * ********************************
     *
     * SOLICITAÇÃO VIAGEM - FIM
     *
     ********************************
     */
    /**
     * ********************************
     *
     * RECEBER SOLICITAÇÃO - INICIO
     *
     ********************************
     */
    @Funcionalidade(nomeCurto = "solicRecbSolicViagem")
    public ActionForward listaRecebimento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            Collection<ViagemDTO> viagensDTO = new ArrayList<ViagemDTO>();
            Collection<ViagemDTO> viagensDTOr = new ArrayList<ViagemDTO>();
            Collection<Viagem> viagens = new ViagemBO().obterTodosByStatusViagemAbertoRecebidoSemPrestacao();
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            //Map<String, IFonteRecurso> mapFR = new FonteRecursoDAO().obterFonteRecursoPorCodigo("");
            Map<String, IFonteRecurso> mapFR = new FonteRecursoDAO().obterFonteRecursoPorCodigoAntigo("");
            Map<Integer, String> mapFunc = new RHServico().obterMatriculasPorNome("");
            Map<String, String> mapEnt = new EntidadeDAO().obterMapConsultoresPorNome("");
            Map<String, Integer> mapProcesso = new ControleProcessoDAO().obterMapProcesso();

            for (Viagem viagem : viagens) {
                if (viagem.getPrestacaoContas() == null) {

                    ViagemDTO vdto = new ViagemDTO();
                    vdto.setViagem(viagem);
                    vdto.setFonteRecurso(mapFR.get(vdto.getViagem().getCodigoFonteRecurso()));
                    vdto.setPeriodo(simpleDate.format(viagem.getDataSaidaPrevista()) + " " + simpleDate.format(viagem.getDataRetornoPrevista()));
                    vdto.setStatusViagem(viagem.getStatusViagem().name());

                    if (viagem.getCodigoConsultorViajante() == null) {
                        vdto.setNomeViajante(mapFunc.get(viagem.getCodigoDominioUsuarioViajante()));
                    } else {
                        vdto.setNomeViajante(mapEnt.get(viagem.getCodigoConsultorViajante()));
                    }

                    if (viagem.getStatusViagem().equals(StatusViagem.VIAGEM_ABERTA)) {
                        viagensDTO.add(vdto);
                    } else {
                        if (viagem.getStatusViagem().equals(StatusViagem.VIAGEM_RECEBIDA) || viagem.getStatusViagem().equals(StatusViagem.VIAGEM_CANCELADA)) {

                            if (viagem.getProcesso() != null) {
                                //vdto.setProcesso(mapProcesso.get(viagem.getProcesso().substring(0, viagem.getProcesso().indexOf("/"))));
                                if (mapProcesso.get(viagem.getProcesso()) != null) {
                                    vdto.setProcesso(mapProcesso.get(viagem.getProcesso()));
                                } else {
                                    vdto.setProcesso(0);
                                }
                            } else {
                                vdto.setProcesso(0);
                            }

                            viagensDTOr.add(vdto);
                        }
                    }
                }
            }

            request.setAttribute("lista", viagensDTO);
            request.setAttribute("listaR", viagensDTOr);

            //Ordenando a lista de fontes de recurso pelo código.
            List valuesMapFROrdenado = new ArrayList(mapFR.values());
            Collections.sort(valuesMapFROrdenado, new Comparator<FonteRecurso>() {
                public int compare(FonteRecurso o1, FonteRecurso o2) {
                    return o1.getCodigo().compareTo(o2.getCodigo());
                }
            });
            request.setAttribute("listaFonteRecurso", valuesMapFROrdenado);
        } catch (Exception ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", this.getClass().getName(), ex);
        }

        return mapping.findForward("recebimentoLista");
    }

    public ActionForward receber(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        String[] id = new String[0];

        try {
            if (request.getParameterValues("ids_recebimento") != null) {
                id = request.getParameterValues("ids_recebimento");

                HibernateUtil.beginTransaction();

                ViagemBO viagemBO = new ViagemBO();
                String codigoFonteRecurso = new FonteRecursoDAO().obterFonteRecursoPorCodigoNovo((String) dyna.get("codigoFonteRecurso")).getCodigoAntigo();

                String numProcesso;

                for (int i = 0; i < id.length; i++) {
                    Viagem viagem = viagemBO.obterPorPk(Integer.valueOf(id[i]));
                    viagem.setCodigoFonteRecurso(codigoFonteRecurso);
                    viagem.setStatusViagem(StatusViagem.VIAGEM_RECEBIDA);

                    if (viagem.getDataPagamento() == null) {
                        viagem.setDataPagamento(viagemBO.calcularDataPagamentoPrevisto(viagem.getDataSaidaPrevista(), viagem.getDestino()));
                    }

                    /* =================================GERAÇÃO NÚMERO DO PROCESSO========================================== */
                    ControleProcessoDAO cpDAO = new ControleProcessoDAO();
                    if (viagem.getProcesso() == null) {

                        String ano = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
                        Integer tipoCredor = null;
                        Long idCredor = null;

                        if (viagem.getCodigoDominioUsuarioViajante() != null) {
                            tipoCredor = 3;
                            idCredor = new Long(new ColaboradorDAO().obterIdPorMatriculaBDAA(viagem.getCodigoDominioUsuarioViajante().toString()));
                        } else if (viagem.getCodigoConsultorViajante() != null) {
                            tipoCredor = 2;
                            idCredor = Long.valueOf(viagem.getCodigoConsultorViajante());
                        }

                        numProcesso = cpDAO.inserir(ano, viagem.getTotalAdiantamento(), tipoCredor, idCredor, codigoFonteRecurso, viagem.getCodigoCentroResponsabilidade());

                        viagem.setProcesso(numProcesso);
                        viagem.setAnoProcesso(ano);
                        /* ============================================================================================= */
                    } else {
                        cpDAO.alterar(viagem.getTotalAdiantamento(), codigoFonteRecurso, viagem.getCodigoCentroResponsabilidade(), viagem.getProcesso());
                    }

                    LiberacaoViagem liberacao;
                    if(viagem.getCodigoDominioUsuarioViajante() != null){ // Checa se é Funcionario ou Consultor
                        IFuncionario viajante = new RHServico().obterFuncionarioPorMatricula(viagem.getCodigoDominioUsuarioViajante());
                        liberacao = new LiberacaoViagemBO().obterByIFuncionarioTipo(viajante, TipoLiberacao.RETROATIVO);
                    }else{
                        liberacao = new LiberacaoViagemBO().obterByCodigoConsultorTipo(viagem.getCodigoConsultorViajante(), TipoLiberacao.RETROATIVO);
                    }
                    if (liberacao != null) {
                        liberacao.setValido(false);
                        liberacao.setViagem(viagem);
                        new LiberacaoViagemBO().alterar(liberacao);
                        MensagemTagUtil.adicionarMensagem(request.getSession(), "[ Liberação RetroAtivo usada ]");
                    }
                    viagemBO.alterar(viagem);
                }

                HibernateUtil.commitTransaction();
                MensagemTagUtil.adicionarMensagem(request.getSession(), "Viagem(s) recebida(s) com sucesso.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao receber a Viagem.");
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(ViagemAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return mapping.findForward("recebimentoRedirect");
    }

    public ActionForward desfazerRecebimento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String[] id = new String[0];

        try {
            if (request.getParameterValues("ids_exportacao") != null) {
                id = request.getParameterValues("ids_exportacao");

                HibernateUtil.beginTransaction();

                ViagemBO viagemBO = new ViagemBO();

                for (int i = 0; i < id.length; i++) {
                    Viagem viagem = viagemBO.obterPorPk(Integer.valueOf(id[i]));
                    viagem.setCodigoFonteRecurso(null);
                    viagem.setStatusViagem(StatusViagem.VIAGEM_ABERTA);

                    viagemBO.alterar(viagem);
                }

                HibernateUtil.commitTransaction();

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao desfazer recebimento da Viagem.");
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(ViagemAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return mapping.findForward("recebimentoRedirect");
    }
    /**
     * ********************************
     *
     * RECEBER SOLICITAÇÃO - FIM
     *
     ********************************
     */
}
