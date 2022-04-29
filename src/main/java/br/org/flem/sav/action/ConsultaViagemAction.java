package br.org.flem.sav.action;

import br.org.flem.fw.persistencia.dao.legado.gem.CentroCustoDAO;
import br.org.flem.fw.persistencia.dao.legado.gem.EntidadeDAO;
import br.org.flem.fw.persistencia.dao.legado.gem.FonteRecursoDAO;
import br.org.flem.fw.service.IFonteRecurso;
import br.org.flem.fw.service.impl.RHServico;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fwe.util.Data;
import br.org.flem.fwe.util.StringUtil;
import br.org.flem.fwe.web.action.SecurityDispatchAction;
import br.org.flem.fwe.web.annotation.Funcionalidade;
import br.org.flem.fwe.web.util.MensagemTagUtil;
import br.org.flem.sav.bo.ViagemBO;
import br.org.flem.sav.dao.ViagemDAO;
import br.org.flem.sav.dto.ViagemDTO;
import br.org.flem.sav.enums.TipoFiltro;
import br.org.flem.sav.negocio.DestinoViagem;
import br.org.flem.sav.negocio.StatusAgendamento;
import br.org.flem.sav.negocio.Viagem;
import br.org.flem.sav.negocio.ViagemComAgendamento;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
 * @author mgsilva
 */
public class ConsultaViagemAction extends SecurityDispatchAction{

    /* forward name="success" path="" */
    private final static String SUCCESS = "success";

    @Override
    @Funcionalidade(nomeCurto="consulViagem")
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            CentroCustoDAO centroCustoDAO = new CentroCustoDAO();
            FonteRecursoDAO fonteRecursoDAO = new FonteRecursoDAO();
            
            Map<Integer, String> matriculas = new HashMap<Integer, String>();
            Map<String, String> consultores = new HashMap<String, String>();
            Collection<Viagem> viagens = new ArrayList<Viagem>();
            Viagem viagemFiltro = (Viagem) request.getAttribute("filtro_consultaViagem");
            Collection<ViagemDTO> viagensDTO = new ArrayList<ViagemDTO>();
            String tipoFiltro = (String) request.getAttribute("filtro_tipoFiltro");
            String tipoFiltroValue = (String) request.getAttribute("filtro_tipoFiltroValue");

            if (viagemFiltro != null) {
                if(viagemFiltro.getCodigoFonteRecurso() != null && !viagemFiltro.getCodigoFonteRecurso().isEmpty()){//Correção da Fonte da recurso de como está na tela, para como é salva no banco de dados
                    viagemFiltro.setCodigoFonteRecurso(fonteRecursoDAO.obterFonteRecursoPorCodigoNovo(viagemFiltro.getCodigoFonteRecurso()).getCodigoAntigo());
                }
                RHServico rh  = new RHServico();
                EntidadeDAO entidadeDAO = new EntidadeDAO();
                Map<Integer, String> mapFunc = rh.obterMatriculasPorNome("");
                Map<String, String> mapEnt = entidadeDAO.obterMapConsultoresPorNome("");
                if (GenericValidator.isInt(tipoFiltro) && !GenericValidator.isBlankOrNull(tipoFiltroValue)) {
                    switch (TipoFiltro.getById(tipoFiltro)) {
                        case CODIGO_VIAGEM: 
                            if (GenericValidator.isInt(tipoFiltroValue)) {
                                Viagem viagem = new ViagemBO().obterPorPk(Integer.valueOf(tipoFiltroValue));
                                if (viagem != null) {
                                    viagens.add(viagem);
                                }
                            } else {
                                //caso os usuario digite um código de consultor ou nome selecionado o codigo da viagem na combo
                                throw new AplicacaoException("Digite um código de solicitação de viagem válido");
                            }
                        break;
                        
                        case CODIGO_VIAJANTE: 
                            if (GenericValidator.isInt(tipoFiltroValue)) {
                                viagemFiltro.setCodigoDominioUsuarioViajante(Integer.valueOf(tipoFiltroValue));
                            } else if (tipoFiltroValue.startsWith("P") || tipoFiltroValue.startsWith("p")) {
                                viagemFiltro.setCodigoConsultorViajante(tipoFiltroValue);
                            } else {
                                throw new AplicacaoException("Digite um código de viajante válido");
                            }
                        break;
                        
                        case NOME_VIAJANTE: 
                            if (tipoFiltroValue.length() < 4) {
                                throw new AplicacaoException("Para filtrar por nome utilize pelo menos 4 letras");
                            } else {
                                matriculas = rh.obterMatriculasPorNome(StringUtil.padraoMainFrame(tipoFiltroValue));
                                consultores = entidadeDAO.obterMapConsultoresPorNome(StringUtil.padraoMainFrame(tipoFiltroValue));
                                if(matriculas.isEmpty() && consultores.isEmpty()){
                                     throw new AplicacaoException("Nome Viajante não encontrado!");
                                }
                            }
                        break;
                    }
                }
                if (viagens.isEmpty()) {
                    viagens = new ViagemDAO().consultaViagem(viagemFiltro, matriculas.keySet(), consultores.keySet());
                }

                SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                for (Viagem viagem : viagens) {
                    ViagemDTO vdto = new ViagemDTO();
                    vdto.setViagem(viagem);
                    vdto.setPeriodo(simpleDate.format(viagem.getDataSaidaPrevista()) + " " + simpleDate.format(viagem.getDataRetornoPrevista()));

                    if (viagem instanceof ViagemComAgendamento) {
                        ViagemComAgendamento viagemAgendada = (ViagemComAgendamento) viagem;
                        if (viagemAgendada.getStatusAgendamento().equals(StatusAgendamento.AGENDAMENTO_APROVADO)) {
                            vdto.setStatusViagem(StatusAgendamento.AGENDAMENTO_APROVADO.toString());
                            vdto.setViagemComAgendamento(viagemAgendada);
                        }
                    } else {
                        vdto.setStatusViagem(viagem.getStatusViagem().name());
                    }
                    if (viagem.getCodigoDominioUsuarioViajante() != null) {
                        vdto.setNomeViajante(mapFunc.get(viagem.getCodigoDominioUsuarioViajante()));
                    } else {
                        vdto.setNomeViajante(mapEnt.get(viagem.getCodigoConsultorViajante()));
                    }

                    vdto.setPrestouConta("1");

                    if (viagem.getPrestacaoContas() == null) {
                        vdto.setPrestouConta("0");
                    }
                    viagensDTO.add(vdto);

                }
            }

            request.setAttribute("listaFonteRecurso", fonteRecursoDAO.obterFonteRecurso());
            request.setAttribute("listaDestinos", DestinoViagem.toCollection());
            request.setAttribute("listaCentrosCusto", centroCustoDAO.obterCentroDeCusto());
            request.setAttribute("listaCentrosResponsabilidade", centroCustoDAO.obterCentroDeResponsabilidade());
            request.setAttribute("lista", viagensDTO);
            request.setAttribute("listaTipoFiltro", TipoFiltro.getLista());

        } catch (Exception ex) {
            request.setAttribute("filtro_consultaViagem", null);
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", ConsultaViagemAction.class.getName(), ex);
            Logger.getLogger(ConsultaViagemAction.class.getName()).log(Level.SEVERE, null, ex);
            return unspecified(mapping, form, request, response);
        }

        return mapping.findForward("lista");
    }

    public ActionForward filtrar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            DynaActionForm dyna = (DynaActionForm) form;
            Viagem viagem = (Viagem) dyna.get("viagem");
            String dataSaida = (String) dyna.get("dataSaidaPrevista");
            String dataRetorno = (String) dyna.get("dataRetornoPrevista");
            String destino = (String) dyna.get("destino");
            if (destino != null && !destino.isEmpty()) {
                viagem.setDestino(DestinoViagem.valueOf(destino));
            }
            if (dataSaida != null && !dataSaida.isEmpty()) {
                viagem.setDataSaidaPrevista(Data.formataData(dataSaida));
            }
            if (dataRetorno != null && !dataRetorno.isEmpty()) {
                viagem.setDataRetornoPrevista(Data.formataData(dataRetorno));
            }
            request.setAttribute("filtro_consultaViagem", viagem);
            request.setAttribute("filtro_tipoFiltro", dyna.getString("tipoFiltro"));
            request.setAttribute("filtro_tipoFiltroValue", dyna.getString("tipoFiltroValue"));

        } catch (Exception ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", ConsultaViagemAction.class.getName(), ex);
            Logger.getLogger(ConsultaViagemAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return unspecified(mapping, form, request, response);
    }
}
