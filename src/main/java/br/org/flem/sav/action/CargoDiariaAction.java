package br.org.flem.sav.action; 

import br.org.flem.fw.service.ICargo;
import br.org.flem.fw.service.impl.RHServico;
import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fwe.hibernate.util.HibernateUtil;
import br.org.flem.fwe.util.Valores;
import br.org.flem.fwe.web.action.SecurityDispatchAction;
import br.org.flem.fwe.web.annotation.Funcionalidade;
import br.org.flem.fwe.web.util.MensagemTagUtil;
import br.org.flem.sav.bo.CargoDiariaBO;
import br.org.flem.sav.negocio.CargoDiaria;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
public class CargoDiariaAction extends SecurityDispatchAction {

    @Override
    @Funcionalidade(nomeCurto="admGerDiaria")
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("lista", new CargoDiariaBO().obterTodosOrdenado());
        } catch (AplicacaoException ex) {
            Logger.getLogger(CargoDiariaAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapping.findForward("lista");
    }

    @Funcionalidade(nomeCurto="admNovoGerDiaria")
    public ActionForward novo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<Integer, ICargo> mapLista = new RHServico().obterCargos();
            ArrayList<ICargo> ICargoList = new ArrayList<ICargo>();
            Collection<CargoDiaria> caC = new CargoDiariaBO().obterTodos();
            boolean tem = false;

            for (ICargo iCargo : mapLista.values()) {
                tem = false;
                for (CargoDiaria cD : caC) {
                    if(iCargo.getId().equals(cD.getCargoDominio())){
                        tem = true;
                    }
                }
                if(!tem){
                    ICargoList.add(iCargo);
                }
            }
            
            Collections.sort(ICargoList, new Comparator<ICargo>() {
                public int compare(ICargo arg0, ICargo arg1) {
                    if (arg0.getNome().compareTo(arg1.getNome()) != 0) {
                        return arg0.getNome().compareTo(arg1.getNome());
                    } else {
                        return arg0.getNome().compareTo(arg1.getNome());
                    }
                }
            });
            request.setAttribute("listaCargos", ICargoList);
            
        } catch (AplicacaoException ex) {
            Logger.getLogger(CargoDiariaAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapping.findForward("novo");
    }

    public ActionForward adicionar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws AcessoDadosException {
        DynaActionForm dyna = (DynaActionForm) form;

        try {
            CargoDiaria cargoDiaria = (CargoDiaria) dyna.get("cargoDiaria");

            cargoDiaria.setValorBahia(Valores.desformataValor(request.getParameter("cargoDiaria.valorBahia").toString()));
            cargoDiaria.setValorBrasil(Valores.desformataValor(request.getParameter("cargoDiaria.valorBrasil").toString()));
            cargoDiaria.setValorExterior(Valores.desformataValor(request.getParameter("cargoDiaria.valorExterior").toString()));

            HibernateUtil.beginTransaction();
            new CargoDiariaBO().inserir(cargoDiaria);
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Cargo/Di??ria inserido com sucesso.");
        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao inserir o Cargo/Di??ria.");
            try {
                HibernateUtil.rollbackTransaction();
            } catch (AcessoDadosException ex1) {
                Logger.getLogger(CargoDiariaAction.class.getName()).log(Level.SEVERE, null, ex1);
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

        try {
            if (GenericValidator.isInt(id)) {
                CargoDiaria cargoDiaria = new CargoDiariaBO().obterPorPk(Integer.valueOf(id));
                dyna.set("cargoDiaria", cargoDiaria);
            }
            return mapping.findForward("editar");
        } catch (AplicacaoException ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar selecionar o Cargo/Di??ria.");
        }
        return unspecified(mapping, form, request, response);
    }

    public ActionForward alterar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        try {
            CargoDiaria cargoDiaria = (CargoDiaria) dyna.get("cargoDiaria");

            cargoDiaria.setValorBahia(Valores.desformataValor(request.getParameter("cargoDiaria.valorBahia").toString()));
            cargoDiaria.setValorBrasil(Valores.desformataValor(request.getParameter("cargoDiaria.valorBrasil").toString()));
            cargoDiaria.setValorExterior(Valores.desformataValor(request.getParameter("cargoDiaria.valorExterior").toString()));

            new CargoDiariaBO().alterar(cargoDiaria);
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Cargo/Di??ria alterado com sucesso.");
        } catch (AplicacaoException ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Ocorreu um erro ao tentar alterar o Cargo/Di??ria.");
            if (ex.getMessage().contains("ConstraintViolationException")) {
                MensagemTagUtil.adicionarMensagem(request.getSession(), "J?? existe um Cargo/Di??ria com esta descri????o");
            }
        }
        return mapping.findForward("redirect");
    }

    @Funcionalidade(nomeCurto="admExcluirGerDiaria")
    public ActionForward excluir(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ArrayList erros = new ArrayList();
        try {
            String[] id = new String[0];
            if (request.getParameterValues("ids_exclusao") != null) {
                id = request.getParameterValues("ids_exclusao");
            }
            HibernateUtil.beginTransaction();
            for (int i = 0; i < id.length; i++) {
                CargoDiariaBO CargoDiariaBO = new CargoDiariaBO();
                CargoDiaria cargoDiaria = CargoDiariaBO.obterPorPk(Integer.valueOf(id[i]));
                try {
                    CargoDiariaBO.excluir(cargoDiaria);
                } catch (Exception ex) {
                    HibernateUtil.rollbackTransaction();
                    MensagemTagUtil.adicionarMensagem(request.getSession(), "O Cargo/Di??ria \"" + cargoDiaria.getDescricao() + "\" est?? associado. N??o pode ser exclu??do!");
                    erros.add("O Cargo/Di??ria \"" + cargoDiaria.getDescricao() + "\" est?? associado. N??o pode ser exclu??do!");
                    break;
                }
            }
            HibernateUtil.commitTransaction();
        } catch (AplicacaoException ex) {
            Logger.getLogger(CargoDiariaAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (erros.size() <= 0) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Exclus??o realizada com sucesso!");
        }
        return unspecified(mapping, form, request, response);
    }
}