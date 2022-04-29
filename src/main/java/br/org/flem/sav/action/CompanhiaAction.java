 package br.org.flem.sav.action; 

import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fwe.hibernate.util.HibernateUtil;
import br.org.flem.fwe.web.action.SecurityDispatchAction;
import br.org.flem.fwe.web.annotation.Funcionalidade;
import br.org.flem.fwe.web.util.MensagemTagUtil;
import br.org.flem.sav.bo.CompanhiaBO;
import br.org.flem.sav.negocio.Companhia;
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
public class CompanhiaAction extends SecurityDispatchAction {

    @Override
    @Funcionalidade(nomeCurto="admCompViagem")
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("lista", new CompanhiaBO().obterTodos());
        } catch (AplicacaoException ex) {
            Logger.getLogger(CompanhiaAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapping.findForward("lista");
    }

    @Funcionalidade(nomeCurto="admNovoCompViagem")
    public ActionForward novo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("novo");
    }

    public ActionForward selecionar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        String id = request.getParameter("id");
        try {
            if (GenericValidator.isInt(id)) {
                Companhia companhia = new CompanhiaBO().obterPorPk(Integer.valueOf(id));
                dyna.set("companhia", companhia);
            }
            return mapping.findForward("editar");
        } catch (AplicacaoException ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar selecionar a Companhia.");
        }
        return unspecified(mapping, form, request, response);
    }

    public ActionForward alterar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        try {
            Companhia companhia = (Companhia) dyna.get("companhia");
            new CompanhiaBO().alterar(companhia);
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Companhia alterada com sucesso.");
        } catch (AplicacaoException ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar alterar a Companhia.");
            if (ex.getMessage().contains("ConstraintViolationException")) {
                MensagemTagUtil.adicionarMensagem(request.getSession(), "Já existe uma Companhia com esta descrição");
            }
        }
        return mapping.findForward("redirect");
    }

    public ActionForward adicionar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;

        try {
            Companhia companhia = (Companhia) dyna.get("companhia");
            HibernateUtil.beginTransaction();
            new CompanhiaBO().inserir(companhia);
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Companhia inserida com sucesso.");
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(CompanhiaAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar inserir a Companhia.");
            if (ex.getMessage().contains("ConstraintViolationException")) {
                MensagemTagUtil.adicionarMensagem(request.getSession(), "Já existe uma Companhia com esta descrição");
            }
        }
        try {
            HibernateUtil.commitTransaction();
        } catch (AplicacaoException ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage());
        }
        return mapping.findForward("redirect");
    }

    public ActionForward excluir(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ArrayList erros = new ArrayList();
        try {
            String[] id = new String[0];
            if (request.getParameterValues("ids_exclusao") != null) {
                id = request.getParameterValues("ids_exclusao");
            }
            HibernateUtil.beginTransaction();
            for (int i = 0; i < id.length; i++) {
                CompanhiaBO companhiaBO = new CompanhiaBO();
                Companhia companhia = companhiaBO.obterPorPk(Integer.valueOf(id[i]));
                try {
                    companhiaBO.excluir(companhia);
                } catch (Exception ex) {
                    HibernateUtil.rollbackTransaction();
                    MensagemTagUtil.adicionarMensagem(request.getSession(), "A Companhia \"" + companhia.getDescricao() + "\" está associada. Não pode ser excluída!");
                    erros.add("A Companhia \"" + companhia.getDescricao() + "\" está associada. Não pode ser excluída!");
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
        return unspecified(mapping, form, request, response);
    }
}
