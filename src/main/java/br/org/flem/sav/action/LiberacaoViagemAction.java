package br.org.flem.sav.action; 

import br.org.flem.fw.persistencia.dao.legado.gem.EntidadeDAO;
import br.org.flem.fw.persistencia.dto.Funcionario;
import br.org.flem.fw.service.IColaborador;
import br.org.flem.fw.service.IFuncionario;
import br.org.flem.fw.service.IUsuario;
import br.org.flem.fw.service.impl.RHServico;
import br.org.flem.fw.util.Constante;
import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fwe.hibernate.util.HibernateUtil;
import br.org.flem.fwe.web.action.SecurityDispatchAction;
import br.org.flem.fwe.web.annotation.Funcionalidade;
import br.org.flem.fwe.web.util.MensagemTagUtil;
import br.org.flem.sav.bo.LiberacaoViagemBO;
import br.org.flem.sav.dto.ViagemDTO;
import br.org.flem.sav.negocio.LiberacaoViagem;
import br.org.flem.sav.negocio.TipoLiberacao;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 *
 * @author mccosta
 */
public class LiberacaoViagemAction extends SecurityDispatchAction{

    @Override
    @Funcionalidade(nomeCurto="admLiberViagem")
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        /*********************************
         listaPendencia
         **********************************/
        try {
            Collection<ViagemDTO> viagensDTO = new ArrayList<ViagemDTO>();
            IFuncionario Ifunc = new Funcionario();
            EntidadeDAO entidadeDAO = new EntidadeDAO();

            Map<Integer, IFuncionario> mapFunc = new RHServico().obterMapTodos();
            Collection<LiberacaoViagem> lbLista = new LiberacaoViagemBO().obterTodosByTipo(TipoLiberacao.PENDENCIA);

            for (LiberacaoViagem lb : lbLista) {
                ViagemDTO vdto = new ViagemDTO();
                if(lb.getCodigoDominioUsuarioViajante() != null){
                    Ifunc = mapFunc.get(lb.getCodigoDominioUsuarioViajante());
                    vdto.setNomeViajante(Ifunc.getNome());
                }else{
                    vdto.setNomeViajante(entidadeDAO.obterConsultorPorCodigo(lb.getCodigoConsultorViajante()).getNome());
                }

                Ifunc = mapFunc.get(lb.getCodigoDominioUsuarioSolicitante());
                vdto.setNomeSolicitante(Ifunc.getNome());
                vdto.setLiberacaoViagem(lb);
                viagensDTO.add(vdto);
            }

            request.setAttribute("lista", viagensDTO);

        } catch (AplicacaoException ex) {
            Logger.getLogger(LiberacaoViagemAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mapping.findForward("lista");
    }

    @Funcionalidade(nomeCurto="admLiberViagemR")
    public ActionForward listaRetroativo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        /*********************************
         listaRetroativo
         **********************************/
        try {
            Collection<ViagemDTO> viagensDTO = new ArrayList<ViagemDTO>();
            IFuncionario Ifunc = new Funcionario();
            EntidadeDAO entidadeDAO = new EntidadeDAO();

            Map<Integer, IFuncionario> mapFunc = new RHServico().obterMapTodos();
            Collection<LiberacaoViagem> lbLista = new LiberacaoViagemBO().obterTodosByTipo(TipoLiberacao.RETROATIVO);

            for (LiberacaoViagem lb : lbLista) {
                ViagemDTO vdto = new ViagemDTO();
                if(lb.getCodigoDominioUsuarioViajante() != null){
                    Ifunc = mapFunc.get(lb.getCodigoDominioUsuarioViajante());
                    vdto.setNomeViajante(Ifunc.getNome());
                }else{
                    vdto.setNomeViajante(entidadeDAO.obterConsultorPorCodigo(lb.getCodigoConsultorViajante()).getNome());
                }

                Ifunc = mapFunc.get(lb.getCodigoDominioUsuarioSolicitante());
                vdto.setNomeSolicitante(Ifunc.getNome());
                vdto.setLiberacaoViagem(lb);
                viagensDTO.add(vdto);
            }

            request.setAttribute("listaRetroativo", viagensDTO);

        } catch (AplicacaoException ex) {
            Logger.getLogger(LiberacaoViagemAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mapping.findForward("listaRetroativo");
    }

    @Funcionalidade(nomeCurto="admLiberViagemL50")
    public ActionForward listaLimite50(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        /*********************************
         listaLimite50
         **********************************/
        try {
            Collection<ViagemDTO> viagensDTO = new ArrayList<ViagemDTO>();
            IFuncionario Ifunc = new Funcionario();
            EntidadeDAO entidadeDAO = new EntidadeDAO();

            Map<Integer, IFuncionario> mapFunc = new RHServico().obterMapTodos();
            Collection<LiberacaoViagem> lbLista = new LiberacaoViagemBO().obterTodosByTipo(TipoLiberacao.LIMITE_50);

            for (LiberacaoViagem lb : lbLista) {
                ViagemDTO vdto = new ViagemDTO();
                if(lb.getCodigoDominioUsuarioViajante() != null){
                    Ifunc = mapFunc.get(lb.getCodigoDominioUsuarioViajante());
                    vdto.setNomeViajante(Ifunc.getNome());
                }else{
                    vdto.setNomeViajante(entidadeDAO.obterConsultorPorCodigo(lb.getCodigoConsultorViajante()).getNome());
                }

                Ifunc = mapFunc.get(lb.getCodigoDominioUsuarioSolicitante());
                vdto.setNomeSolicitante(Ifunc.getNome());
                vdto.setLiberacaoViagem(lb);
                viagensDTO.add(vdto);
            }

            request.setAttribute("listaLimite50", viagensDTO);

        } catch (AplicacaoException ex) {
            Logger.getLogger(LiberacaoViagemAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mapping.findForward("listaLimite50");
    }

    @Funcionalidade(nomeCurto="admNovoLiberViagem")
    public ActionForward novo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        /*********************************
         novoPendencia
         **********************************/
        Map<Integer, IFuncionario> mapLista = new RHServico().obterMapIdFuncionarioAtivo();
        //Map<Integer, LiberacaoViagem> mapLV = new LiberacaoViagemBO().obterTodosValidoByTipo(TipoLiberacao.RETROATIVO);
        ArrayList<IFuncionario> IFList = new ArrayList<IFuncionario>();
        List<IColaborador> colaboradores = new EntidadeDAO().obterColaboradores();

        for (IFuncionario iFunc : mapLista.values()) {
            //se o funcionário já possui uma liberação, seu nome não é mostrado para solicitar uma nova liberação
            //if (mapLV.get(iFunc.getMatricula()) == null) {
                IFList.add(iFunc);
            //}
        }

        Collections.sort(IFList, new Comparator<IFuncionario>() {
            public int compare(IFuncionario arg0, IFuncionario arg1) {
                if (arg0.getNome().compareTo(arg1.getNome()) != 0) {
                    return arg0.getNome().compareTo(arg1.getNome());
                } else {
                    return arg0.getNome().compareTo(arg1.getNome());
                }
            }
        });

        request.setAttribute("listaFuncionario", IFList);
        request.setAttribute("listaConsultor", colaboradores);
        request.setAttribute("tipoLiberacao", TipoLiberacao.PENDENCIA.getId());

        return mapping.findForward("novo");
    }

    @Funcionalidade(nomeCurto="admNovoLiberViagemR")
    public ActionForward novoRetroativo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        /*********************************
         novoRetroativo
         **********************************/
        try {
            Map<Integer, IFuncionario> mapLista = new RHServico().obterMapIdFuncionarioAtivo();
            //Map<Integer, LiberacaoViagem> mapLV = new LiberacaoViagemBO().obterTodosValidoByTipo(TipoLiberacao.RETROATIVO);
            ArrayList<IFuncionario> IFList = new ArrayList<IFuncionario>();
            List<IColaborador> colaboradores = new EntidadeDAO().obterColaboradores();
            
            for (IFuncionario iFunc : mapLista.values()) {
                //se o funcionário já possui uma liberação, seu nome não é mostrado para solicitar uma nova liberação
                //if (mapLV.get(iFunc.getMatricula()) == null) {
                    IFList.add(iFunc);
                //}
            }

            Collections.sort(IFList, new Comparator<IFuncionario>() {
                public int compare(IFuncionario arg0, IFuncionario arg1) {
                    if (arg0.getNome().compareTo(arg1.getNome()) != 0) {
                        return arg0.getNome().compareTo(arg1.getNome());
                    } else {
                        return arg0.getNome().compareTo(arg1.getNome());
                    }
                }
            });
            request.setAttribute("listaFuncionario", IFList);
            request.setAttribute("listaConsultor", colaboradores);
            request.setAttribute("tipoLiberacao", TipoLiberacao.RETROATIVO.getId());

        } catch (Exception ex) {
            Logger.getLogger(LiberacaoViagemAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mapping.findForward("novoRetroativo");
    }

    @Funcionalidade(nomeCurto="admNovoLiberViagemL50")
    public ActionForward novoLimite50(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        /*********************************
         novoLimite50
         **********************************/
        try {
            Map<Integer, IFuncionario> mapLista = new RHServico().obterMapIdFuncionarioAtivo();
            //Map<Integer, LiberacaoViagem> mapLV = new LiberacaoViagemBO().obterTodosValidoByTipo(TipoLiberacao.RETROATIVO);
            ArrayList<IFuncionario> IFList = new ArrayList<IFuncionario>();
            List<IColaborador> colaboradores = new EntidadeDAO().obterColaboradores();

            for (IFuncionario iFunc : mapLista.values()) {
                //se o funcionário já possui uma liberação, seu nome não é mostrado para solicitar uma nova liberação
                //if (mapLV.get(iFunc.getMatricula()) == null) {
                    IFList.add(iFunc);
                //}
            }

            Collections.sort(IFList, new Comparator<IFuncionario>() {
                public int compare(IFuncionario arg0, IFuncionario arg1) {
                    if (arg0.getNome().compareTo(arg1.getNome()) != 0) {
                        return arg0.getNome().compareTo(arg1.getNome());
                    } else {
                        return arg0.getNome().compareTo(arg1.getNome());
                    }
                }
            });
            request.setAttribute("listaFuncionario", IFList);
            request.setAttribute("listaConsultor", colaboradores);
            request.setAttribute("tipoLiberacao", TipoLiberacao.LIMITE_50.getId());

        } catch (Exception ex) {
            Logger.getLogger(LiberacaoViagemAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mapping.findForward("novoLimite50");
    }

    public ActionForward adicionar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        LiberacaoViagem liberacaoViagem = null;
        try {
            IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
            IFuncionario Ifunc = new RHServico().obterFuncionarioPorMatricula(usuario.getCodigoDominio());
            //SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = Calendar.getInstance().getTime();

            String tipoLiberacao = (String) dyna.get("tipoLiberacao");
            String tipoPessoa = (String) dyna.get("tipoPessoa");

            liberacaoViagem = (LiberacaoViagem) dyna.get("liberacaoViagem");

            liberacaoViagem.setCodigoDominioUsuarioSolicitante(Ifunc.getCodigoDominio());
            liberacaoViagem.setMatriculaHRUsuarioSolicitante(Ifunc.getMatriculaHR());

            if(tipoPessoa.equals("0")){
                // se for funcionário
                liberacaoViagem.setCodigoConsultorViajante(null);
            }else{
                // se for consultor
                liberacaoViagem.setCodigoDominioUsuarioViajante(null);
            }
            
            liberacaoViagem.setTipoLiberacao(TipoLiberacao.getById(Integer.valueOf(tipoLiberacao)));
            liberacaoViagem.setTotalViagensLiberadas(1);
            liberacaoViagem.setDataLiberacao(date);
            liberacaoViagem.setValido(true);

            HibernateUtil.beginTransaction();

            new LiberacaoViagemBO().inserir(liberacaoViagem);
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Liberacao inserida com sucesso.");

            HibernateUtil.commitTransaction();

        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao inserir a Liberacao.");
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(LiberacaoViagemAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        if(liberacaoViagem.getTipoLiberacao().equals(TipoLiberacao.RETROATIVO)){
            return mapping.findForward("redirectRetroativo");
        }else{
            if(liberacaoViagem.getTipoLiberacao().equals(TipoLiberacao.LIMITE_50)){
                return mapping.findForward("redirectLimite50");
            }
        }
        return mapping.findForward("redirect");
    }

    public ActionForward excluir(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String[] id = new String[0];
            boolean erro = false;
            if (request.getParameterValues("ids_exclusao") != null) {
                id = request.getParameterValues("ids_exclusao");
            }
            HibernateUtil.beginTransaction();
            for (int i = 0; i < id.length; i++) {
                LiberacaoViagemBO liberacaoViagemBO = new LiberacaoViagemBO();
                LiberacaoViagem liberacaoViagem = liberacaoViagemBO.obterPorPk(Integer.valueOf(id[i]));
                
                try {
                    liberacaoViagemBO.excluir(liberacaoViagem);
                } catch (Exception ex) {
                    erro = true;
                    ex.printStackTrace();
                    HibernateUtil.rollbackTransaction();
                    MensagemTagUtil.adicionarMensagem(request.getSession(), "A Liberação de Viagem \"" + liberacaoViagem.getDescricao() + "\" está associada. Não pode ser excluída!");
                }
            }
            if(!erro){
                MensagemTagUtil.adicionarMensagem(request.getSession(), "Liberação excluída com sucesso.");
            }

            HibernateUtil.commitTransaction();
        } catch (AplicacaoException ex) {
            Logger.getLogger(CompanhiaAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return unspecified(mapping, form, request, response);
    }
}
