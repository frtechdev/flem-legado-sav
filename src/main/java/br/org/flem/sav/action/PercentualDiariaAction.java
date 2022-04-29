package br.org.flem.sav.action;

import br.org.flem.fw.persistencia.dto.Departamento;
import br.org.flem.fw.service.impl.RHServico;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fwe.hibernate.util.HibernateUtil;
import br.org.flem.fwe.web.action.SecurityDispatchAction;
import br.org.flem.fwe.web.annotation.Funcionalidade;
import br.org.flem.fwe.web.util.MensagemTagUtil;
import br.org.flem.sav.bo.PercentualDiariaBO;
import br.org.flem.sav.negocio.PercentualDiaria;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
 * @author MCCosta
 */
public class PercentualDiariaAction extends SecurityDispatchAction {

    /* forward name="success" path="" */
    private final static String SUCCESS = "success";

    @Override
    @Funcionalidade(nomeCurto="admPercentualDiaria")
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {

            request.setAttribute("lista", new PercentualDiariaBO().obterTodos());
            request.setAttribute("mapdepartamento", new RHServico().obterDepartamentosMapIndexCodigo());

        } catch (AplicacaoException ex) {
            Logger.getLogger(ContaCorrenteViagemAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mapping.findForward("lista");
    }

    @Funcionalidade(nomeCurto="admNovoPercentualDiaria")
    public ActionForward novo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws AplicacaoException {
        List<Departamento> departamentos = new RHServico().obterDepartamentos();
        List<PercentualDiaria> percentuais  = new PercentualDiariaBO().obterTodos();
        Iterator<Departamento> itDep = departamentos.iterator();
        while(itDep.hasNext()){
            Departamento d = itDep.next();
            for(PercentualDiaria p : percentuais){
                if(d.getCodigoDominio().equals(p.getDepartamentoDominio())){
                    itDep.remove();
                }
            }
        }
        request.setAttribute("departamentos", departamentos);
        
        return mapping.findForward("novo");
    }

    public ActionForward adicionar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            DynaActionForm dyna = (DynaActionForm) form;

            PercentualDiaria percentualDiaria = (PercentualDiaria) dyna.get("percentualDiaria");

            percentualDiaria.setQuebraDiariasUm(Double.parseDouble((String) dyna.get("quebraDiariasUm")));
            percentualDiaria.setMinQuebraDiariasUm(Integer.parseInt((String) dyna.get("minQuebraDiariasUm")));
            percentualDiaria.setMaxQuebraDiariasUm(Integer.parseInt((String) dyna.get("maxQuebraDiariasUm")));
            percentualDiaria.setQuebraDiariasDois(Double.parseDouble((String) dyna.get("quebraDiariasDois")));
            percentualDiaria.setMinQuebraDiariasDois(Integer.parseInt((String) dyna.get("minQuebraDiariasDois")));
            percentualDiaria.setMaxQuebraDiariasDois(Integer.parseInt((String) dyna.get("maxQuebraDiariasDois")));
            
            
            HibernateUtil.beginTransaction();
            new PercentualDiariaBO().inserir(percentualDiaria);
            HibernateUtil.commitTransaction();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Percentual adicionado com sucesso");
            
        } catch (AplicacaoException ex) {
            Logger.getLogger(PercentualDiariaAction.class.getName()).log(Level.SEVERE, null, ex);
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao adicionar percentual");
        }

        return mapping.findForward("redirect");
    }

    public ActionForward selecionar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            DynaActionForm dyna = (DynaActionForm) form;

            String id = request.getParameter("id");
            
            PercentualDiaria percentualDiaria = new PercentualDiariaBO().obterPorPk(Integer.valueOf(id));

            dyna.set("percentualDiaria", percentualDiaria);
            dyna.set("quebraDiariasUm", String.valueOf(percentualDiaria.getQuebraDiariasUm()));
            dyna.set("minQuebraDiariasUm", String.valueOf(percentualDiaria.getMinQuebraDiariasUm()));
            dyna.set("maxQuebraDiariasUm", String.valueOf(percentualDiaria.getMaxQuebraDiariasUm()));
            dyna.set("quebraDiariasDois", String.valueOf(percentualDiaria.getQuebraDiariasDois()));
            dyna.set("minQuebraDiariasDois", String.valueOf(percentualDiaria.getMinQuebraDiariasDois()));
            dyna.set("maxQuebraDiariasDois", String.valueOf(percentualDiaria.getMaxQuebraDiariasDois()));

            request.setAttribute("percentualDiaria", percentualDiaria);
            RHServico rh = new RHServico();
            request.setAttribute("departamentos", rh.obterDepartamentos());
            request.setAttribute("mapdepartamento", rh.obterDepartamentosMapIndexCodigo());

        } catch (AplicacaoException ex) {
            Logger.getLogger(PercentualDiariaAction.class.getName()).log(Level.SEVERE, null, ex);
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao selecionar percentual");

        }

        return mapping.findForward("editar");
    }

    public ActionForward alterar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            DynaActionForm dyna = (DynaActionForm) form;
            PercentualDiariaBO percentualDiariaBO = new PercentualDiariaBO();
            PercentualDiaria percentualDiariaForm = (PercentualDiaria) dyna.get("percentualDiaria");

            PercentualDiaria percentualDiaria = percentualDiariaBO.obterPorPk(percentualDiariaForm.getId());

            percentualDiaria.setQuebraDiariasUm(Double.parseDouble((String) dyna.get("quebraDiariasUm")));
            percentualDiaria.setMinQuebraDiariasUm(Integer.parseInt((String) dyna.get("minQuebraDiariasUm")));
            percentualDiaria.setMaxQuebraDiariasUm(Integer.parseInt((String) dyna.get("maxQuebraDiariasUm")));
            percentualDiaria.setQuebraDiariasDois(Double.parseDouble((String) dyna.get("quebraDiariasDois")));
            percentualDiaria.setMinQuebraDiariasDois(Integer.parseInt((String) dyna.get("minQuebraDiariasDois")));
            percentualDiaria.setMaxQuebraDiariasDois(Integer.parseInt((String) dyna.get("maxQuebraDiariasDois")));

            percentualDiaria.setDepartamentoDominio(percentualDiariaForm.getDepartamentoDominio());

            HibernateUtil.beginTransaction();
            percentualDiariaBO.alterar(percentualDiaria);
            HibernateUtil.commitTransaction();
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Percentual alterado com sucesso");

        } catch (AplicacaoException ex) {
            Logger.getLogger(ContaCorrenteViagemAction.class.getName()).log(Level.SEVERE, null, ex);
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao alterar o percentual");
        }

        return mapping.findForward("redirect");
    }

    @Funcionalidade(nomeCurto="admExcluirPercentualDiaria")
    public ActionForward excluir(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArrayList erros = new ArrayList();
        try {
            String[] id = new String[0];
            if (request.getParameterValues("ids_exclusao") != null) {
                id = request.getParameterValues("ids_exclusao");
            }
            PercentualDiariaBO percentualDiariaBO = new PercentualDiariaBO();

            HibernateUtil.beginTransaction();
            for (int i = 0; i < id.length; i++) {
                PercentualDiaria percentualDiaria = percentualDiariaBO.obterPorPk(Integer.valueOf(id[i]));
                percentualDiariaBO.excluir(percentualDiaria);
            }
            HibernateUtil.commitTransaction();

        } catch (AplicacaoException ex) {
            Logger.getLogger(CompanhiaAction.class.getName()).log(Level.SEVERE, null, ex);
            erros.add("Occoreu um erro ao tentar remover o Percentual");
            MensagemTagUtil.adicionarMensagem(request.getSession(), erros.toString());
        }

        if (erros.size() <= 0) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Exclusão realizada com sucesso!");
        }
        return mapping.findForward("redirect");
    }
    
}