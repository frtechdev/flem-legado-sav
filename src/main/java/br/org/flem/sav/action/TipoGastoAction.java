package br.org.flem.sav.action; 

import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fwe.hibernate.util.HibernateUtil;
import br.org.flem.fwe.web.action.SecurityDispatchAction;
import br.org.flem.fwe.web.annotation.Funcionalidade;
import br.org.flem.fwe.web.util.MensagemTagUtil;
import br.org.flem.sav.bo.TipoGastoBO;
import br.org.flem.sav.negocio.TipoGasto;
import java.util.ArrayList;
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
public class TipoGastoAction extends SecurityDispatchAction {

    @Funcionalidade(nomeCurto="admTipoGasto")
    public ActionForward filtrar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm dyna = (DynaActionForm) form;
        TipoGasto filtro = (TipoGasto) dyna.get("tipoGasto");
        request.setAttribute("tipoGasto_filtro", filtro);
        return unspecified(mapping, form, request, response);
    }

    @Override
    @Funcionalidade(nomeCurto="admTipoGasto")
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            TipoGasto filtro = (TipoGasto) request.getAttribute("tipoGasto_filtro");
            if (filtro != null) {
                request.setAttribute("lista", new TipoGastoBO().obterPorFiltro(filtro));
            } else {
                request.setAttribute("lista", new TipoGastoBO().obterTodos());
            }
        } catch (AplicacaoException ex) {
            Logger.getLogger(CompanhiaAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapping.findForward("lista");
    }

    @Funcionalidade(nomeCurto="admNovoTipoGasto")
    public ActionForward novo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("novo");
    }

    public ActionForward selecionar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        String id = request.getParameter("id");
        TipoGasto tipoGasto = null;
        try {
            if (GenericValidator.isInt(id)) {
                tipoGasto = new TipoGastoBO().obterPorPk(Integer.valueOf(id));
                dyna.set("tipoGasto", tipoGasto);

                return mapping.findForward("editar");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar selecionar o Tipo de gasto.");
        }
        return unspecified(mapping, form, request, response);
    }

    public ActionForward alterar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        try {
            TipoGasto tipoGasto = (TipoGasto) dyna.get("tipoGasto");
            HibernateUtil.beginTransaction();
            new TipoGastoBO().alterar(tipoGasto);
            HibernateUtil.commitTransaction();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Tipo de gasto salvo com sucesso.");
        } catch (Exception ex) {
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(TipoGastoAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
            if (ex.getMessage().contains("ConstraintViolationException")) {
                MensagemTagUtil.adicionarMensagem(request.getSession(), "Já existe um tipo de gasto com esta descrição");
            } else {
                MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", TipoGastoAction.class.getName(), ex);
            }
        }
        return mapping.findForward("redirect");
    }

    public ActionForward adicionar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        try {
            TipoGasto tipoGasto = (TipoGasto) dyna.get("tipoGasto");
            HibernateUtil.beginTransaction();
            new TipoGastoBO().inserir(tipoGasto);
            HibernateUtil.commitTransaction();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Tipo de gasto salvo com sucesso.");
        } catch (Exception ex) {
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(TipoGastoAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
            if (ex.getMessage().contains("ConstraintViolationException")) {
                MensagemTagUtil.adicionarMensagem(request.getSession(), "Já existe um tipo de gasto com esta descrição");
            } else {
                MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", TipoGastoAction.class.getName(), ex);
            }

        }
        return mapping.findForward("redirect");
    }

    @Funcionalidade(nomeCurto="admExcluirTipoGasto")
    public ActionForward excluir(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ArrayList erros = new ArrayList();
        try {
            String[] id = new String[0];
            if (request.getParameterValues("ids_exclusao") != null) {
                id = request.getParameterValues("ids_exclusao");
            }
            HibernateUtil.beginTransaction();
            for (int i = 0; i < id.length; i++) {
                TipoGastoBO tipoGastoBO = new TipoGastoBO();
                TipoGasto tipoGasto = tipoGastoBO.obterPorPk(Integer.valueOf(id[i]));
                try {
                    tipoGastoBO.excluir(tipoGasto);
                } catch (Exception ex) {
                    HibernateUtil.rollbackTransaction();
                    MensagemTagUtil.adicionarMensagem(request.getSession(), "O Tipo de Gasto \"" + tipoGasto.getDescricao() + "\" está associado. Não pode ser excluído!");
                    erros.add("");
                    break;
                }
            }
            HibernateUtil.commitTransaction();
        } catch (AplicacaoException ex) {
            Logger.getLogger(CompanhiaAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (erros.size() <= 0) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Exclusão realizada com sucesso!");
        }
        return mapping.findForward("redirect");
    }
}
