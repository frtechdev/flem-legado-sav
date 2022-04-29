package br.org.flem.sav.action;

import br.org.flem.fw.persistencia.dao.legado.control.CentroCustoUsuarioDAO;
import br.org.flem.fw.persistencia.dao.legado.gem.CentroCustoDAO;
import br.org.flem.fw.persistencia.dao.legado.gem.EntidadeDAO;
import br.org.flem.fw.persistencia.dao.legado.gem.FonteRecursoDAO;
import br.org.flem.fw.persistencia.dto.Funcionario;
import br.org.flem.fw.service.IColaborador;
import br.org.flem.fw.service.IFonteRecurso;
import br.org.flem.fw.service.IFuncionario;
import br.org.flem.fw.service.IUsuario;
import br.org.flem.fw.service.impl.RHServico;
import br.org.flem.fw.util.Constante;
import br.org.flem.fwe.exception.RelatorioSemDadosException;
import br.org.flem.fwe.util.Data;
import br.org.flem.fwe.web.util.MensagemTagUtil;
import br.org.flem.sav.bo.RelatorioGastosBO;
import br.org.flem.sav.bo.RelatorioTotalSDVBO;
import br.org.flem.sav.bo.TipoGastoBO;
import br.org.flem.sav.bo.ViagemBO;
import br.org.flem.sav.dto.FuncionarioDiariaDTO;
import br.org.flem.sav.dto.PlanilhaGastosDTO;
import br.org.flem.sav.dto.RelatorioAPPDTO;
import br.org.flem.sav.dto.RelatorioDiariaSalarioColaboradoresDTO;
import br.org.flem.sav.dto.RelatorioGastoDTO;
import br.org.flem.sav.dto.ViagemDTO;
import br.org.flem.sav.negocio.PrestacaoContas;
import br.org.flem.sav.negocio.StatusPrestacaoContas;
import br.org.flem.sav.negocio.StatusViagem;
import br.org.flem.sav.negocio.Viagem;
import br.org.flem.sav.negocio.util.Diarias;
import br.org.flem.sav.relatorio.SAVCriadorRelatorio;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author ilfernandes
 */
public class RelatorioAction extends DispatchAction {
   
  
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Boolean filter = false;
        request.setAttribute("filter", filter);
        return mapping.findForward("lista");
    }

    public ActionForward filtroTotalPrestaoContasEmAberto(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
        request.setAttribute("listaCC", new CentroCustoUsuarioDAO().obterCCustoViajante(usuario.getCodigoDominio()));
        return mapping.findForward("totalPrestacaoContasEmAberto");
    }

    public ActionForward totalPrestaoContasEmAberto(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
        String codigoCentroCustoInicial = request.getParameter("codigoCentroCustoInicial");
        String codigoCentroCustoFinal = request.getParameter("codigoCentroCustoFinal");
        List<String> centrosCustoUsuario = new ArrayList(new CentroCustoUsuarioDAO().obterCodigoCCustoViajante(usuario.getCodigoDominio()));
        int startIndex = 0, finalIndex = centrosCustoUsuario.size() - 1;

        if (codigoCentroCustoInicial != null && !codigoCentroCustoInicial.isEmpty()) {
            startIndex = centrosCustoUsuario.indexOf((codigoCentroCustoInicial));
        }
        if (codigoCentroCustoFinal != null && !codigoCentroCustoFinal.isEmpty()) {
            finalIndex = centrosCustoUsuario.indexOf(codigoCentroCustoFinal);
        }
        Collection<String> listaCodigos = centrosCustoUsuario.subList((startIndex < finalIndex ? startIndex : finalIndex), (startIndex < finalIndex ? finalIndex : startIndex));
        listaCodigos.add(centrosCustoUsuario.get((startIndex < finalIndex ? finalIndex : startIndex)));
        RHServico rh = new RHServico();
        try {

            Collection<Funcionario> IFList = rh.obterFuncionarios();
            Map<Integer, Integer> listaFunc = new ViagemBO().obterMatriculaQtViagensPrestacaoAberta(listaCodigos);
            Map<String, Integer> listaCons = new ViagemBO().obterCodConsultorQtViagensPrestacaoAberta(listaCodigos);
            //Map<Integer, String> mapFunc = new FuncionarioDAO().obterMatriculasPorNome("");
            //TODO QUANDO CORRIGIR O PROBLEMA DO GEM VOLTAR PARA FORMA MAIS PERFORMATICA
            //Map<String, String> mapEnt = new EntidadeDAO().obterCodigosPorNome("");
            Collection<ViagemDTO> viajantes = new ArrayList<ViagemDTO>();
            IFList.add((Funcionario)rh.obterFuncionarioPorMatricula(usuario.getCodigoDominio()));

            for (IFuncionario funcionario : IFList) {
                if (listaFunc.keySet().contains(funcionario.getCodigoDominio())) {
                    ViagemDTO viagemDTO = new ViagemDTO();
                    viagemDTO.setNomeViajante(funcionario.getNome());
                    viagemDTO.setQtPrestacaoIrregular(listaFunc.get(funcionario.getCodigoDominio()));
                    viajantes.add(viagemDTO);
                }
            }

            for (String codigo : listaCons.keySet()) {
                ViagemDTO viagemDTO = new ViagemDTO();
                //TODO QUANDO CORRIGIR O PROBLEMA DO GEM VOLTAR PARA FORMA MAIS PERFORMATICA
                //viagemDTO.setNomeViajante(mapEnt.get(codigo));
                viagemDTO.setNomeViajante(new EntidadeDAO().obterConsultorPorCodigo(codigo).getNome().toUpperCase());
                viagemDTO.setQtPrestacaoIrregular(listaCons.get(codigo));
                viajantes.add(viagemDTO);
            }


            Map parametros = new HashMap();
            parametros.put("centroCustoInicial", codigoCentroCustoInicial);
            parametros.put("centroCustoFinal", codigoCentroCustoFinal);
            String arquivoRelatorio = "/relatorio/ViajantesPrestacaoIrregular.jasper";

            new SAVCriadorRelatorio().exportaRelatorioPDF(request, response, arquivoRelatorio, parametros, viajantes);

        } catch (Exception ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", RelatorioAction.class.getName(), ex);
            Logger.getLogger(PrestacaoContasAction.class.getName()).log(Level.SEVERE, null, ex);
            return filtroTotalPrestaoContasEmAberto(mapping, form, request, response);
        }

        return null;
    }

    public ActionForward filtroTotalSDVStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
        request.setAttribute("listaCC", new CentroCustoUsuarioDAO().obterCCustoViajante(usuario.getCodigoDominio()));
        request.setAttribute("listaStatusViagem", StatusViagem.toCollection());
        request.setAttribute("listaStatusPrestacao", StatusPrestacaoContas.toCollection());
        return mapping.findForward("totalSDVStatus");
    }

    public ActionForward totalSDVStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
        String codigoCentroCustoInicial = dyna.getString("codigoCentroCustoInicial");
        String codigoCentroCustoFinal = dyna.getString("codigoCentroCustoFinal");
        Viagem viagemFiltro = new Viagem();
        String idStatusPrestacaoContas = request.getParameter("statusPrestacaoContas");
        String idStatusViagem = request.getParameter("statusViagem");
        String dataInicial = request.getParameter("dataInicial");
        String dataFinal = request.getParameter("dataFinal");
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            if (idStatusPrestacaoContas != null && !idStatusPrestacaoContas.isEmpty() && !idStatusPrestacaoContas.equals("-1")) {
                PrestacaoContas prestacaoContas = new PrestacaoContas();
                prestacaoContas.setStatusPrestacaoContas(StatusPrestacaoContas.getById(Integer.valueOf(idStatusPrestacaoContas)));
                viagemFiltro.setPrestacaoContas(prestacaoContas);
            }
            if (idStatusViagem != null && !idStatusViagem.isEmpty()) {
                viagemFiltro.setStatusViagem(StatusViagem.getById(Integer.valueOf(idStatusViagem)));
            }

            if (dataInicial != null && !dataInicial.isEmpty()) {
                viagemFiltro.setDataSaidaPrevista(simpleDate.parse(dataInicial + " 00:00"));
            }
            if (dataFinal != null && !dataFinal.isEmpty()) {
                viagemFiltro.setDataRetornoPrevista(simpleDate.parse(dataFinal + " 23:59"));
            }
            Collection<String> listaCodigos = new ArrayList<String>();
            List<String> centrosCustoUsuario = new ArrayList(new CentroCustoUsuarioDAO().obterCodigoCCustoViajante(usuario.getCodigoDominio()));
            if (centrosCustoUsuario != null) {
                int startIndex = 0, finalIndex = centrosCustoUsuario.size() - 1;

                if (codigoCentroCustoInicial != null && !codigoCentroCustoInicial.isEmpty()) {
                    startIndex = centrosCustoUsuario.indexOf((codigoCentroCustoInicial));
                }
                if (codigoCentroCustoFinal != null && !codigoCentroCustoFinal.isEmpty()) {
                    finalIndex = centrosCustoUsuario.indexOf(codigoCentroCustoFinal);
                }
                listaCodigos = centrosCustoUsuario.subList((startIndex < finalIndex ? startIndex : finalIndex), (startIndex < finalIndex ? finalIndex : startIndex));
                listaCodigos.add(centrosCustoUsuario.get((startIndex < finalIndex ? finalIndex : startIndex)));
            }

            RHServico rh = new RHServico();
            Collection<Funcionario> funcionarios = rh.obterFuncionarios();
            Collection<Viagem> viagens = new ViagemBO().obterPorFiltroFaixaCentroCusto(viagemFiltro, listaCodigos);
            funcionarios.add((Funcionario)rh.obterFuncionarioPorMatricula(usuario.getCodigoDominio()));
            //Map<Integer, String> mapFunc = new FuncionarioDAO().obterMatriculasPorNome("");
            List<IColaborador> consultores = new EntidadeDAO().obterColaboradores();
            Collection<Viagem> listaFinalViagens = new ArrayList<Viagem>();
            //<!-- -1 pq a prestação em aberto tem a viagem com prestação null -->
            if (idStatusPrestacaoContas.equals("-1")) {
                for (Viagem viagem : viagens) {
                    if (viagem.getPrestacaoContas() == null) {
                        listaFinalViagens.add(viagem);
                    }
                }
            } else {
                listaFinalViagens = viagens;
            }
            Collection<ViagemDTO> viajantes = new RelatorioTotalSDVBO().prepararColecaoDTO(listaFinalViagens, funcionarios, consultores);
            Map parametros = new HashMap();
            parametros.put("centroCustoInicial", codigoCentroCustoInicial);
            parametros.put("centroCustoFinal", codigoCentroCustoFinal);
            parametros.put("dataInicial", viagemFiltro.getDataSaidaPrevista());
            parametros.put("dataFinal", viagemFiltro.getDataRetornoPrevista());
            String arquivoRelatorio = "/relatorio/relatorioTotalPrestaoContasViagens.jasper";

            new SAVCriadorRelatorio().exportaRelatorioPDF(request, response, arquivoRelatorio, parametros, viajantes);

        } catch (Exception ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", RelatorioAction.class.getName(), ex);
            Logger.getLogger(PrestacaoContasAction.class.getName()).log(Level.SEVERE, null, ex);
            return filtroTotalSDVStatus(mapping, form, request, response);
        }

        return null;
    }

    public ActionForward filtroFuncionarioDiarias(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
        request.setAttribute("listaCC", new CentroCustoUsuarioDAO().obterCCustoViajante(usuario.getCodigoDominio()));
        return mapping.findForward("percentualFuncionarioDiaria");
    }

    public ActionForward percentualFuncionarioDiaria(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {

            IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
            String codigoCentroCustoInicial = request.getParameter("codigoCentroCustoInicial");
            String codigoCentroCustoFinal = request.getParameter("codigoCentroCustoFinal");
            String dateMesAno = request.getParameter("mesAno");
            Date data = Data.formataData(dateMesAno, "MM/yyyy");
            List<String> centrosCustoUsuario = new ArrayList(new CentroCustoUsuarioDAO().obterCodigoCCustoViajante(usuario.getCodigoDominio()));
            Collection<FuncionarioDiariaDTO> funcionarioDiariaDTOs = new ArrayList<FuncionarioDiariaDTO>();
            int startIndex = 0, finalIndex = centrosCustoUsuario.size() - 1;

            if (codigoCentroCustoInicial != null && !codigoCentroCustoInicial.isEmpty()) {
                startIndex = centrosCustoUsuario.indexOf((codigoCentroCustoInicial));
            }
            if (codigoCentroCustoFinal != null && !codigoCentroCustoFinal.isEmpty()) {
                finalIndex = centrosCustoUsuario.indexOf(codigoCentroCustoFinal);
            }
            Collection<String> listaCodigos = centrosCustoUsuario.subList((startIndex < finalIndex ? startIndex : finalIndex), (startIndex < finalIndex ? finalIndex : startIndex));
            listaCodigos.add(centrosCustoUsuario.get((startIndex < finalIndex ? finalIndex : startIndex)));


            funcionarioDiariaDTOs = new Diarias().calcularPorcentagem(data, listaCodigos);


            Map parametros = new HashMap();
            parametros.put("centroCustoInicial", codigoCentroCustoInicial);
            parametros.put("centroCustoFinal", codigoCentroCustoFinal);
            parametros.put("mesAno", data);
            String arquivoRelatorio = "/relatorio/funcionarioDiaria.jasper";

            new SAVCriadorRelatorio().exportaRelatorioPDF(request, response, arquivoRelatorio, parametros, funcionarioDiariaDTOs);

        } catch (Exception ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", RelatorioAction.class.getName(), ex);
            Logger.getLogger(PrestacaoContasAction.class.getName()).log(Level.SEVERE, null, ex);
            return filtroFuncionarioDiarias(mapping, form, request, response);
        }

        return null;
    }

    public ActionForward filtroFuncionarioDiariasDetalhada(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
        request.setAttribute("listaCC", new CentroCustoUsuarioDAO().obterCCustoViajante(usuario.getCodigoDominio()));
        return mapping.findForward("percentualFuncionarioDiariaDetalhada");
    }

    
    public ActionForward percentualFuncionarioDiariaDetalhada(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {

            IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
            String codigoCentroCustoInicial = request.getParameter("codigoCentroCustoInicial");
            String codigoCentroCustoFinal = request.getParameter("codigoCentroCustoFinal");
            Integer situacaoFuncionario = Integer.valueOf(request.getParameter("situacaoFuncionario"));
            String dataInicialform = "01/"+request.getParameter("dataInicial");
            Date dataInicial = Data.formataData(dataInicialform, "MM/yyyy");
            
            String[] pieces = request.getParameter("dataFinal").split("/");
            Calendar cal = new GregorianCalendar(Integer.parseInt(pieces[1]),Integer.parseInt(pieces[0])-1, 1);
            cal.set(GregorianCalendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date dataFinal = cal.getTime();
            String dataFinalform = Data.formataData(dataFinal, "dd/MM/yyyy");
            
            List<String> centrosCustoUsuario = new ArrayList(new CentroCustoUsuarioDAO().obterCodigoCCustoViajante(usuario.getCodigoDominio()));
            Collection<FuncionarioDiariaDTO> funcionarioDiariaDTOs = new ArrayList<FuncionarioDiariaDTO>();
            int startIndex = 0, finalIndex = centrosCustoUsuario.size() - 1;
            
            if (codigoCentroCustoInicial != null && !codigoCentroCustoInicial.isEmpty()) {
                startIndex = centrosCustoUsuario.indexOf((codigoCentroCustoInicial));
            }
            if (codigoCentroCustoFinal != null && !codigoCentroCustoFinal.isEmpty()) {
                finalIndex = centrosCustoUsuario.indexOf(codigoCentroCustoFinal);
            }
            
            Collection<String> listaCodigos = centrosCustoUsuario.subList((startIndex < finalIndex ? startIndex : finalIndex), (startIndex < finalIndex ? finalIndex : startIndex));
            listaCodigos.add(centrosCustoUsuario.get((startIndex < finalIndex ? finalIndex : startIndex)));


            funcionarioDiariaDTOs = new Diarias().calcularPorcentagem(dataInicialform, dataFinalform, listaCodigos, situacaoFuncionario);


            Map parametros = new HashMap();
            parametros.put("centroCustoInicial", codigoCentroCustoInicial);
            parametros.put("centroCustoFinal", codigoCentroCustoFinal);
            //parametros.put("centroCustoInicial", "202860000");
            //parametros.put("centroCustoFinal", "2028630040");
            parametros.put("dataInicial", dataInicial);
            parametros.put("dataFinal", dataFinal);
            parametros.put("situacao", (situacaoFuncionario == 2 ? "Ativo/Desligado" : (situacaoFuncionario == 1 ? "Ativo" : "Desligado")));
            parametros.put("SUBREPORT_DIR", request.getSession().getServletContext().getRealPath("/relatorio/funcionarioDiariaDetalhada_sub_Itinerario.jasper"));
            String arquivoRelatorio = "/relatorio/funcionarioDiariaDetalhada.jasper";

            new SAVCriadorRelatorio().exportaRelatorioPDF(request, response, arquivoRelatorio, parametros, funcionarioDiariaDTOs);

        } catch (Exception ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", RelatorioAction.class.getName(), ex);
            Logger.getLogger(PrestacaoContasAction.class.getName()).log(Level.SEVERE, null, ex);
            return filtroFuncionarioDiariasDetalhada(mapping, form, request, response);
        }

        return null;
    }
    
    
    public ActionForward percentualFuncionarioDiariaDetalhadaExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {

            IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
            String codigoCentroCustoInicial = request.getParameter("codigoCentroCustoInicial");
            String codigoCentroCustoFinal = request.getParameter("codigoCentroCustoFinal");
            Integer situacaoFuncionario = Integer.valueOf(request.getParameter("situacaoFuncionario"));
            String dataInicialform = "01/"+request.getParameter("dataInicial");
            Date dataInicial = Data.formataData(dataInicialform, "MM/yyyy");
            
            String[] pieces = request.getParameter("dataFinal").split("/");
            Calendar cal = new GregorianCalendar(Integer.parseInt(pieces[1]),Integer.parseInt(pieces[0])-1, 1);
            cal.set(GregorianCalendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date dataFinal = cal.getTime();
            String dataFinalform = Data.formataData(dataFinal, "dd/MM/yyyy");
            
            List<String> centrosCustoUsuario = new ArrayList(new CentroCustoUsuarioDAO().obterCodigoCCustoViajante(usuario.getCodigoDominio()));
            Collection<FuncionarioDiariaDTO> funcionarioDiariaDTOs = new ArrayList<FuncionarioDiariaDTO>();
            int startIndex = 0, finalIndex = centrosCustoUsuario.size() - 1;
            
            if (codigoCentroCustoInicial != null && !codigoCentroCustoInicial.isEmpty()) {
                startIndex = centrosCustoUsuario.indexOf((codigoCentroCustoInicial));
            }
            if (codigoCentroCustoFinal != null && !codigoCentroCustoFinal.isEmpty()) {
                finalIndex = centrosCustoUsuario.indexOf(codigoCentroCustoFinal);
            }
            
            Collection<String> listaCodigos = centrosCustoUsuario.subList((startIndex < finalIndex ? startIndex : finalIndex), (startIndex < finalIndex ? finalIndex : startIndex));
            listaCodigos.add(centrosCustoUsuario.get((startIndex < finalIndex ? finalIndex : startIndex)));


            funcionarioDiariaDTOs = new Diarias().calcularPorcentagem(dataInicialform, dataFinalform, listaCodigos, situacaoFuncionario);


            Map parametros = new HashMap();
            parametros.put("centroCustoInicial", codigoCentroCustoInicial);
            parametros.put("centroCustoFinal", codigoCentroCustoFinal);
            parametros.put("dataInicial", dataInicial);
            parametros.put("dataFinal", dataFinal);
            parametros.put("situacao", (situacaoFuncionario == 2 ? "Ativo/Desligado" : (situacaoFuncionario == 1 ? "Ativo" : "Desligado")));
            parametros.put("SUBREPORT_DIR", request.getSession().getServletContext().getRealPath("/relatorio/funcionarioDiariaDetalhada_sub_ItinerarioXSL.jasper"));
            String arquivoRelatorio = "/relatorio/funcionarioDiariaDetalhada.jasper";

            new SAVCriadorRelatorio().exportaRelatorioXLS(request, response, arquivoRelatorio, parametros, funcionarioDiariaDTOs);
            
        } catch (Exception ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", RelatorioAction.class.getName(), ex);
            Logger.getLogger(PrestacaoContasAction.class.getName()).log(Level.SEVERE, null, ex);
            return filtroFuncionarioDiariasDetalhada(mapping, form, request, response);
        }

        return null;
    }

    public ActionForward filtroRelatorioConferenciaViagens(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
        request.setAttribute("listaCC", new CentroCustoUsuarioDAO().obterCCustoViajante(usuario.getCodigoDominio()));
        return mapping.findForward("relatorioConferenciaViagens");
    }

    
    public ActionForward relatorioConferenciaViagens(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
            String codigoCentroCustoInicial = request.getParameter("codigoCentroCustoInicial");
            String codigoCentroCustoFinal = request.getParameter("codigoCentroCustoFinal");
            String dataInicialform = "01/"+request.getParameter("dataInicial");//01/2014
            Date dataInicial = Data.formataData(dataInicialform, "dd/MM/yyyy");

            String[] pieces = request.getParameter("dataFinal").split("/");
            Calendar cal = new GregorianCalendar(Integer.parseInt(pieces[1]),Integer.parseInt(pieces[0])-1, 1);
            cal.set(GregorianCalendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date dataFinal = cal.getTime();
            String dataFinalform = Data.formataData(dataFinal, "dd/MM/yyyy");
            
            List<String> centrosCustoUsuario = new ArrayList(new CentroCustoUsuarioDAO().obterCodigoCCustoViajante(usuario.getCodigoDominio()));
            int startIndex = 0, finalIndex = centrosCustoUsuario.size() - 1;
            
            if (codigoCentroCustoInicial != null && !codigoCentroCustoInicial.isEmpty()) {
                startIndex = centrosCustoUsuario.indexOf((codigoCentroCustoInicial));
            }
            if (codigoCentroCustoFinal != null && !codigoCentroCustoFinal.isEmpty()) {
                finalIndex = centrosCustoUsuario.indexOf(codigoCentroCustoFinal);
            }
            
            Collection<String> listaCodigos = centrosCustoUsuario.subList((startIndex < finalIndex ? startIndex : finalIndex), (startIndex < finalIndex ? finalIndex : startIndex));
            listaCodigos.add(centrosCustoUsuario.get((startIndex < finalIndex ? finalIndex : startIndex)));
            
            Collection<ViagemDTO> viagensDTO = new ViagemBO().obterViagensParaConferencia(dataInicialform, dataFinalform, listaCodigos);
           
            Map parametros = new HashMap();
            parametros.put("centroCustoInicial", codigoCentroCustoInicial);
            parametros.put("centroCustoFinal", codigoCentroCustoFinal);
            parametros.put("dataInicial", dataInicial);
            parametros.put("dataFinal", dataFinal);
            parametros.put("SUBREPORT_DIR", request.getSession().getServletContext().getRealPath("/relatorio/funcionarioDiariaDetalhada_sub_Itinerario.jasper"));
            String arquivoRelatorio = "/relatorio/relatorioConferenciaViagens.jasper";

            new SAVCriadorRelatorio().exportaRelatorioPDF(request, response, arquivoRelatorio, parametros, viagensDTO);            

        } catch (Exception ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", RelatorioAction.class.getName(), ex);
            Logger.getLogger(PrestacaoContasAction.class.getName()).log(Level.SEVERE, null, ex);
            return filtroRelatorioConferenciaViagens(mapping, form, request, response);
        }

        return null;
    }
    
    public ActionForward relatorioConferenciaViagensExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
            String codigoCentroCustoInicial = request.getParameter("codigoCentroCustoInicial");
            String codigoCentroCustoFinal = request.getParameter("codigoCentroCustoFinal");
            String dataInicialform = "01/"+request.getParameter("dataInicial");//01/2014
            Date dataInicial = Data.formataData(dataInicialform, "dd/MM/yyyy");
            String[] pieces = request.getParameter("dataFinal").split("/");
            Calendar cal = new GregorianCalendar(Integer.parseInt(pieces[1]),Integer.parseInt(pieces[0])-1, 1);
            cal.set(GregorianCalendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date dataFinal = cal.getTime();
            String dataFinalform = Data.formataData(dataFinal, "dd/MM/yyyy");
        
            List<String> centrosCustoUsuario = new ArrayList(new CentroCustoUsuarioDAO().obterCodigoCCustoViajante(usuario.getCodigoDominio()));
            int startIndex = 0, finalIndex = centrosCustoUsuario.size() - 1;
            
            if (codigoCentroCustoInicial != null && !codigoCentroCustoInicial.isEmpty()) {
                startIndex = centrosCustoUsuario.indexOf((codigoCentroCustoInicial));
            }
            if (codigoCentroCustoFinal != null && !codigoCentroCustoFinal.isEmpty()) {
                finalIndex = centrosCustoUsuario.indexOf(codigoCentroCustoFinal);
            }
            
            Collection<String> listaCodigos = centrosCustoUsuario.subList((startIndex < finalIndex ? startIndex : finalIndex), (startIndex < finalIndex ? finalIndex : startIndex));
            listaCodigos.add(centrosCustoUsuario.get((startIndex < finalIndex ? finalIndex : startIndex)));
            
            Collection<ViagemDTO> viagensDTO = new ViagemBO().obterViagensParaConferencia(dataInicialform, dataFinalform, listaCodigos);
           
            Map parametros = new HashMap();
            parametros.put("centroCustoInicial", codigoCentroCustoInicial);
            parametros.put("centroCustoFinal", codigoCentroCustoFinal);
            parametros.put("dataInicial", dataInicial);
            parametros.put("dataFinal", dataFinal);
            parametros.put("SUBREPORT_DIR", request.getSession().getServletContext().getRealPath("/relatorio/funcionarioDiariaDetalhada_sub_Itinerario.jasper"));
            String arquivoRelatorio = "/relatorio/relatorioConferenciaViagensExcel.jasper";

            new SAVCriadorRelatorio().exportaRelatorioXLS(request, response, arquivoRelatorio, parametros, viagensDTO);            

        } catch (Exception ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", RelatorioAction.class.getName(), ex);
            Logger.getLogger(PrestacaoContasAction.class.getName()).log(Level.SEVERE, null, ex);
            return filtroRelatorioConferenciaViagens(mapping, form, request, response);
        }

        return null;
    }
    
    public ActionForward filtroRelatorioGastos(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
        request.setAttribute("listaCC", new CentroCustoUsuarioDAO().obterCCustoViajante(usuario.getCodigoDominio()));
        return mapping.findForward("relatorioGastos");
    }

    public ActionForward relatorioGastos(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
        String codigoCentroCustoInicial = dyna.getString("codigoCentroCustoInicial");
        String codigoCentroCustoFinal = dyna.getString("codigoCentroCustoFinal");
        String dataInicial = request.getParameter("dataInicial");
        String dataFinal = request.getParameter("dataFinal");

        try {
            Collection<String> listaCodigos = new ArrayList<String>();
            List<String> centrosCustoUsuario = new ArrayList(new CentroCustoUsuarioDAO().obterCodigoCCustoViajante(usuario.getCodigoDominio()));
            if (centrosCustoUsuario != null) {
                int startIndex = 0, finalIndex = centrosCustoUsuario.size() - 1;

                if (codigoCentroCustoInicial != null && !codigoCentroCustoInicial.isEmpty()) {
                    startIndex = centrosCustoUsuario.indexOf((codigoCentroCustoInicial));
                }
                if (codigoCentroCustoFinal != null && !codigoCentroCustoFinal.isEmpty()) {
                    finalIndex = centrosCustoUsuario.indexOf(codigoCentroCustoFinal);
                }
                listaCodigos = centrosCustoUsuario.subList((startIndex < finalIndex ? startIndex : finalIndex), (startIndex < finalIndex ? finalIndex : startIndex));
                listaCodigos.add(centrosCustoUsuario.get((startIndex < finalIndex ? finalIndex : startIndex)));
            }

            RHServico rh = new RHServico();
            Map<Integer, IFuncionario> funcionarios = rh.obterMapTodos();
            funcionarios.put(usuario.getCodigoDominio(), rh.obterFuncionarioPorMatricula(usuario.getCodigoDominio()));
            //Map<Integer, String> mapFunc = new FuncionarioDAO().obterMatriculasPorNome("");
            Map<String, IColaborador> consultores = new EntidadeDAO().obterMapColaboradores();
            Collection<RelatorioGastoDTO> viajantes = new RelatorioGastosBO().prepararColecaoDTO(listaCodigos, dataInicial, dataFinal, funcionarios, consultores);


            Map parametros = new HashMap();
            parametros.put("centroCustoInicial", codigoCentroCustoInicial);
            parametros.put("centroCustoFinal", codigoCentroCustoFinal);
            parametros.put("dataInicial", Data.formataData(dataInicial));
            parametros.put("dataFinal", Data.formataData(dataFinal));
            parametros.put("subRelatorio", request.getSession().getServletContext().getRealPath("/relatorio/subRelatorioGastos.jasper"));
            String arquivoRelatorio = "/relatorio/relatorioDeGastosDeViagem.jasper";

            new SAVCriadorRelatorio().exportaRelatorioPDF(request, response, arquivoRelatorio, parametros, viajantes);

        } catch (Exception ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", RelatorioAction.class.getName(), ex);
            Logger.getLogger(PrestacaoContasAction.class.getName()).log(Level.SEVERE, null, ex);
            return filtroRelatorioGastos(mapping, form, request, response);
        }

        return null;
    }

    public ActionForward filtroRelatorioPlanilhaGastos(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
            request.setAttribute("listaCC", new CentroCustoUsuarioDAO().obterCCustoViajante(usuario.getCodigoDominio()));
            request.setAttribute("listaTiposGastos", new TipoGastoBO().obterTodosAtivos());
        } catch (Exception ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", RelatorioAction.class.getName(), ex);
            Logger.getLogger(RelatorioAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapping.findForward("relatorioPlanilhaGastos");
    }

    public ActionForward relatorioPlanilhaGastos(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
        String codigoCentroCustoInicial = dyna.getString("codigoCentroCustoInicial");
        String codigoCentroCustoFinal = dyna.getString("codigoCentroCustoFinal");
        String dataInicial = request.getParameter("dataInicial");
        String dataFinal = request.getParameter("dataFinal");
        String[] arrayTiposGastos = dyna.getStrings("tiposGastos");
        Set<String> tiposGastos = new HashSet<String>();
        tiposGastos.addAll(Arrays.asList(arrayTiposGastos));

        try {
            Collection<String> listaCodigos = new ArrayList<String>();
            List<String> centrosCustoUsuario = new ArrayList(new CentroCustoUsuarioDAO().obterCodigoCCustoViajante(usuario.getCodigoDominio()));
            if (centrosCustoUsuario != null) {
                int startIndex = 0, finalIndex = centrosCustoUsuario.size() - 1;

                if (codigoCentroCustoInicial != null && !codigoCentroCustoInicial.isEmpty()) {
                    startIndex = centrosCustoUsuario.indexOf((codigoCentroCustoInicial));
                }
                if (codigoCentroCustoFinal != null && !codigoCentroCustoFinal.isEmpty()) {
                    finalIndex = centrosCustoUsuario.indexOf(codigoCentroCustoFinal);
                }
                listaCodigos = centrosCustoUsuario.subList((startIndex < finalIndex ? startIndex : finalIndex), (startIndex < finalIndex ? finalIndex : startIndex));
                listaCodigos.add(centrosCustoUsuario.get((startIndex < finalIndex ? finalIndex : startIndex)));
            }


            Map<Integer, IFuncionario> funcionarios = new RHServico().obterMapTodos();
            //funcionarios.put(usuario.getCodigoDominio(), new FuncionarioDAO().obterPorMatricula(usuario.getCodigoDominio()));
            //Map<Integer, String> mapFunc = new FuncionarioDAO().obterMatriculasPorNome("");
            Map<String, IColaborador> consultores = new EntidadeDAO().obterMapColaboradores();
            Collection<PlanilhaGastosDTO> viajantes = new RelatorioGastosBO().prepararRelatorioPlanilha(listaCodigos, dataInicial, dataFinal, funcionarios, consultores, tiposGastos);
            StringBuilder sb = new StringBuilder();
            for (String codigosCc : listaCodigos) {
                sb.append(codigosCc).append(", ");
            }
            Map parametros = new HashMap();
            parametros.put("centroCustoInicial", codigoCentroCustoInicial);
            parametros.put("centroCustoFinal", codigoCentroCustoFinal);
            parametros.put("dataInicial", Data.formataData(dataInicial));
            parametros.put("dataFinal", Data.formataData(dataFinal));
            parametros.put("centrosCustos", sb.substring(0, sb.length()));
            String arquivoRelatorio = "/relatorio/Planilha_de_Gastos.jasper";

            new SAVCriadorRelatorio().exportaRelatorioXLS(request, response, arquivoRelatorio, parametros, viajantes);

        } catch (Exception ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", RelatorioAction.class.getName(), ex);
            Logger.getLogger(PrestacaoContasAction.class.getName()).log(Level.SEVERE, null, ex);
            return filtroRelatorioPlanilhaGastos(mapping, form, request, response);
        }

        return null;
    }

    public ActionForward filtroRelatorioAnaliticoPrestacaoPendente(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("listaCC", new CentroCustoDAO().obterCentroDeCusto());
        return mapping.findForward("relatorioAnalíticoPrestacaoPendente");
    }

    public ActionForward relatorioAnaliticoPrestacaoPendente(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            DynaActionForm dyna = (DynaActionForm) form;
            ViagemBO viagemBO = new ViagemBO();
            String codigoCentroCustoInicial = dyna.getString("codigoCentroCustoInicial");
            String codigoCentroCustoFinal = dyna.getString("codigoCentroCustoFinal");
            String dataInicial = request.getParameter("dataInicial");
            String dataFinal = request.getParameter("dataFinal");
            Collection<String> centrosCustos = new CentroCustoDAO().obterFaixaCentroDeCusto(codigoCentroCustoInicial, codigoCentroCustoFinal);
            Map<Integer, IFuncionario> funcionarios = new RHServico().obterMapTodos();
            Map<String, IColaborador> consultores = new EntidadeDAO().obterMapColaboradores();
            Map<String, IFonteRecurso> mapFR = new FonteRecursoDAO().obterFonteRecursoPorCodigo("");
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Viagem viagemFiltro = new Viagem();
            if (dataInicial != null && !dataInicial.isEmpty()) {
                viagemFiltro.setDataSaidaPrevista(simpleDate.parse(dataInicial + " 00:00"));
            }
            if (dataFinal != null && !dataFinal.isEmpty()) {
                viagemFiltro.setDataRetornoPrevista(simpleDate.parse(dataFinal + " 23:59"));
            }

            Collection<Viagem> viagens = viagemBO.relatorioAnalíticoPrestacaoPendente(viagemFiltro, centrosCustos);
            viagens.addAll(viagemBO.relatorioAnalíticoPrestacaoInformada(viagemFiltro, centrosCustos));
            Collection<RelatorioAPPDTO> viajantes = new ArrayList<RelatorioAPPDTO>();
            for (Viagem viagem : viagens) {
                //System.out.println("ID: " + viagem.getId() + ", Status: " + viagem.getStatusViagem().getDescricao());
                RelatorioAPPDTO dto = new RelatorioAPPDTO();
                dto.setIdViagem(viagem.getId());
                if (viagem.getCodigoDominioUsuarioViajante() != null) {
                    dto.setNomeViajante(funcionarios.get(viagem.getCodigoDominioUsuarioViajante()).getNome());
                    dto.setCodigoViajante(viagem.getCodigoDominioUsuarioViajante().toString());
                } else {
                    dto.setNomeViajante(consultores.get(viagem.getCodigoConsultorViajante()).getNome());
                    dto.setCodigoViajante(viagem.getCodigoConsultorViajante());
                }
                dto.setFonteRecurso((mapFR.get(viagem.getCodigoFonteRecurso()) != null ? mapFR.get(viagem.getCodigoFonteRecurso()).getDescricaoCompleta() : ""));
                dto.setValorDiarias(viagem.getTotalDiarias());
                dto.setAdiantamento(viagem.getTotalAdiantamento() - viagem.getTotalDiarias());
                dto.setValorTotal(viagem.getTotalAdiantamento());
                dto.setDataSaida(viagem.getDataSaidaPrevista());
                dto.setDataRetorno(viagem.getDataRetornoPrevista());
                dto.setProcesso(viagem.getProcesso());
                viajantes.add(dto);
            }
            Map parametros = new HashMap();
            parametros.put("centroCustoInicial", codigoCentroCustoInicial);
            parametros.put("centroCustoFinal", codigoCentroCustoFinal);
            parametros.put("dataInicial", Data.formataData(dataInicial));
            parametros.put("dataFinal", Data.formataData(dataFinal));
            String arquivoRelatorio = "/relatorio/relatorioAnaliticoPrestacaoPendente.jasper";

            new SAVCriadorRelatorio().exportaRelatorioPDF(request, response, arquivoRelatorio, parametros, viajantes);

        } catch(RelatorioSemDadosException ex){
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage());
            Logger.getLogger(RelatorioAction.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return filtroRelatorioAnaliticoPrestacaoPendente(mapping, form, request, response);
        } catch (Exception ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Houve um problema ao gerar o relatório: " + ex.getMessage());
            Logger.getLogger(RelatorioAction.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return filtroRelatorioAnaliticoPrestacaoPendente(mapping, form, request, response);
        }
        return null;
    }
    
    public ActionForward filtrar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
         String mes = (String) dyna.get("mesAno");
         String ano = (String) dyna.get("ano");


        try {
         request.setAttribute("lista",new ViagemBO().relatorioDiariaSalarioColaboradores(mes, ano));
        return mapping.findForward("lista");
        } catch (Exception e) {
        }
        return mapping.findForward("lista");
    }
    
    public ActionForward relatorio(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
        try {
         DynaActionForm dyna = (DynaActionForm) form;
         String mes = (String) dyna.get("mesAno");
         String ano = (String) dyna.get("ano");
         List<RelatorioDiariaSalarioColaboradoresDTO> dto = new ArrayList<RelatorioDiariaSalarioColaboradoresDTO>();
         
         dto = new ViagemBO().relatorioDiariaSalarioColaboradores(mes, ano);
         Map parametros = new HashMap();
         parametros.put("mes", mes);
         parametros.put("ano", ano);
         String arquivoRelatorio = "/relatorio/Relatorio_Diaria_Colaboradores.jasper";
         new SAVCriadorRelatorio().exportaRelatorioPDF(request, response, arquivoRelatorio, parametros, dto);
            
        } catch(RelatorioSemDadosException ex){
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage());
            Logger.getLogger(RelatorioAction.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return unspecified(mapping, form, request, response);
        } catch (Exception ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Houve um problema ao gerar o relatório: " + ex.getMessage());
            Logger.getLogger(RelatorioAction.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return unspecified(mapping, form, request, response);
        }

        return null;
    }
}
