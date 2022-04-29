 package br.org.flem.sav.action; 

import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fwe.hibernate.util.HibernateUtil;
import br.org.flem.fwe.web.action.SecurityDispatchAction;
import br.org.flem.fwe.web.annotation.Funcionalidade;
import br.org.flem.fwe.web.util.MensagemTagUtil;
import br.org.flem.sav.bo.BancoBO;
import br.org.flem.sav.negocio.util.Banco;
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
public class BancoAction extends SecurityDispatchAction {

    @Override
    @Funcionalidade(nomeCurto="admBanco")
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("lista", new BancoBO().obterTodos());
        } catch (AplicacaoException ex) {
            Logger.getLogger(CompanhiaAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapping.findForward("lista");
    }

    @Funcionalidade(nomeCurto="admNovoBanco")
    public ActionForward novo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("novo");
    }

    public ActionForward selecionar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        String id = request.getParameter("id");
        Banco banco = null;
        try {
            if (GenericValidator.isInt(id)) {
                banco = new BancoBO().obterPorPk(Integer.valueOf(id));
                dyna.set("banco", banco);
            }
            return mapping.findForward("editar");
        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar selecionar o Banco.");
        }
        return unspecified(mapping, form, request, response);
    }

    public ActionForward alterar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        try {
            Banco bancoForm = (Banco) dyna.get("banco");
            Banco banco = new BancoBO().obterPorCodigo(bancoForm.getCodigo());
            if(banco != null && banco.getId().equals(bancoForm.getId())){
                banco = new BancoBO().obterPorNome(bancoForm.getNome());
                if(banco != null && banco.getId().equals(bancoForm.getId())){
                    HibernateUtil.beginTransaction();
                    new BancoBO().alterar(bancoForm);
                    HibernateUtil.commitTransaction();
                    MensagemTagUtil.adicionarMensagem(request.getSession(), "Banco alterado com sucesso.");
                }else{
                    MensagemTagUtil.adicionarMensagem(request.getSession(), "Já existe um Banco com este nome");
                }
            }else{
                MensagemTagUtil.adicionarMensagem(request.getSession(), "Já existe um Banco com este código");
            }
            
        } catch (AplicacaoException ex) {
                ex.printStackTrace();
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(BancoAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar alterar o Banco.");
                if (ex.getMessage().contains("ConstraintViolationException")) {
                    MensagemTagUtil.adicionarMensagem(request.getSession(), "Já existe um Banco com esta descrição");
                }
        }
        try {
            HibernateUtil.commitTransaction();
        } catch (AplicacaoException ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage());
        }
        return mapping.findForward("redirect");
    }

    public ActionForward adicionar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;

        try {
            Banco bancoForm = (Banco) dyna.get("banco");
            Banco banco = new BancoBO().obterPorCodigo(bancoForm.getCodigo());
            if(banco == null){
                banco = new BancoBO().obterPorNome(bancoForm.getNome());
                if(banco == null){
                    HibernateUtil.beginTransaction();
                    new BancoBO().inserir(bancoForm);
                    HibernateUtil.commitTransaction();
                    MensagemTagUtil.adicionarMensagem(request.getSession(), "Banco inserido com sucesso.");
                }else{
                    MensagemTagUtil.adicionarMensagem(request.getSession(), "Já existe um Banco com este nome");
                }
            }else{
                MensagemTagUtil.adicionarMensagem(request.getSession(), "Já existe um Banco com este código");
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(BancoAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar inserir o Banco.");
            if (ex.getMessage().contains("ConstraintViolationException")) {
                MensagemTagUtil.adicionarMensagem(request.getSession(), "Já existe um Banco com esta descrição");
            }
        }
        try {
            HibernateUtil.commitTransaction();
        } catch (AplicacaoException ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage());
        }
        return mapping.findForward("redirect");
    }

    @Funcionalidade(nomeCurto="admExcluirBanco")
    public ActionForward excluir(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ArrayList erros = new ArrayList();
        try {
            String[] id = new String[0];
            if (request.getParameterValues("ids_exclusao") != null) {
                id = request.getParameterValues("ids_exclusao");
            }
            HibernateUtil.beginTransaction();
            for (int i = 0; i < id.length; i++) {
                BancoBO bancoBO = new BancoBO();
                Banco banco = bancoBO.obterPorPk(Integer.valueOf(id[i]));
                try {
                    bancoBO.excluir(banco);
                } catch (Exception ex) {
                    HibernateUtil.rollbackTransaction();
                    MensagemTagUtil.adicionarMensagem(request.getSession(), "O Banco \"" + banco.getNome() + "\" está associado. Não pode ser excluído!");
                    erros.add("O Banco \"" + banco.getNome() + "\" está associado. Não pode ser excluído!");
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
