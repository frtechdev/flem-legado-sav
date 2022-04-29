package br.org.flem.sav.action; 

import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fwe.hibernate.util.HibernateUtil;
import br.org.flem.fwe.web.action.SecurityDispatchAction;
import br.org.flem.fwe.web.annotation.Funcionalidade;
import br.org.flem.fwe.web.util.MensagemTagUtil;
import br.org.flem.sav.bo.CidadeBO;
import br.org.flem.sav.dao.EstadoDAO;
import br.org.flem.sav.dao.PaisDAO;
import br.org.flem.sav.negocio.util.Cidade;
import br.org.flem.sav.negocio.util.Estado;
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
public class CidadeAction extends SecurityDispatchAction {

    @Funcionalidade(nomeCurto="admCidade")
    public ActionForward filtrar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm dyna = (DynaActionForm) form;
        Cidade filtro = (Cidade) dyna.get("cidade");
        request.setAttribute("cidade_filtro", filtro);
        return unspecified(mapping, form, request, response);
    }

    @Funcionalidade(nomeCurto="admCidade")
    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        try {
            Cidade filtro = (Cidade) request.getAttribute("cidade_filtro");
            if (filtro != null) {
                request.setAttribute("lista", new CidadeBO().obterPorFiltro(filtro));
            } else {
                request.setAttribute("lista", new CidadeBO().obterTodos());
            }
            filtro = new Cidade();
            dyna.set("cidade", filtro);
            request.setAttribute("listaPaises", new PaisDAO().obterTodosOrdenado());
        } catch (AplicacaoException ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", this.clazz.getName(), ex);
            Logger.getLogger(CompanhiaAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapping.findForward("lista");
    }

    @Funcionalidade(nomeCurto="admNovoCidade")
    public ActionForward novo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("listaPaises", new PaisDAO().obterTodosOrdenado());
        } catch (AcessoDadosException ex) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", this.clazz.getName(), ex);
            Logger.getLogger(CidadeAction.class.getName()).log(Level.SEVERE, null, ex);
            return unspecified(mapping, form, request, response);
        }
        return mapping.findForward("novo");
    }

    public ActionForward selecionar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        String id = request.getParameter("id");
        Cidade cidade = new Cidade();
        try {
            if (GenericValidator.isInt(id)) {
                cidade.setId(Long.valueOf(id));
                cidade = new CidadeBO().obterPorPk(cidade);
                // Artificio utilizado para não acontecer o erro na hora de editar um país.
                
                if(cidade.getEstado()== null){
                    Estado est = new Estado();
                    est.setId(Long.valueOf("-1"));
                    cidade.setEstado(est);
                }
                // Artificio utilizado para não acontecer o erro na hora de editar um país.
                request.setAttribute("listaPaises", new PaisDAO().obterTodosOrdenado());
                request.setAttribute("listaEstados", new EstadoDAO().obterPorPais(cidade.getPais()));
                dyna.set("cidade", cidade);
                return mapping.findForward("editar");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar selecionar a Cidade.");
        }
        return unspecified(mapping, form, request, response);
    }

    public ActionForward alterar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        try {
            Cidade cidade = (Cidade) dyna.get("cidade");
            if (cidade.getEstado() != null && cidade.getEstado().getId() == 0) {
                cidade.setEstado(null);
            }
            HibernateUtil.beginTransaction();
            new CidadeBO().alterar(cidade);
            HibernateUtil.commitTransaction();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Cidade salvo com sucesso.");
        } catch (Exception ex) {
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(CidadeAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
            if (ex.getMessage().contains("ConstraintViolationException")) {
                MensagemTagUtil.adicionarMensagem(request.getSession(), "Já existe uma cidade com este nome");
            } else {
                MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", CidadeAction.class.getName(), ex);
            }
        }
        return mapping.findForward("redirect");
    }

    public ActionForward adicionar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        try {
            Cidade cidade = (Cidade) dyna.get("cidade");
            if (cidade.getEstado() != null && cidade.getEstado().getId() == 0) {
                cidade.setEstado(null);
            }
            HibernateUtil.beginTransaction();
            new CidadeBO().inserir(cidade);
            HibernateUtil.commitTransaction();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Cidade salva com sucesso.");
        } catch (Exception ex) {
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(CidadeAction.class.getName()).log(Level.SEVERE, null, ex1);
            }
            if (ex.getMessage().contains("ConstraintViolationException")) {
                MensagemTagUtil.adicionarMensagem(request.getSession(), "Já existe uma cidade com este nome");
            } else {
                MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", CidadeAction.class.getName(), ex);
            }

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
                CidadeBO cidadeBO = new CidadeBO();
                Cidade cidade = new Cidade();
                cidade.setEstado(null);
                cidade.setPais(null);
                cidade.setId(Long.valueOf(id[i]));
                try {
                    cidadeBO.excluir(cidade);
                } catch (Exception ex) {
                    HibernateUtil.rollbackTransaction();
                    MensagemTagUtil.adicionarMensagem(request.getSession(), "A Cidade está associada. Não pode ser excluída!");
                    Logger.getLogger(CompanhiaAction.class.getName()).log(Level.SEVERE, null, ex);
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
