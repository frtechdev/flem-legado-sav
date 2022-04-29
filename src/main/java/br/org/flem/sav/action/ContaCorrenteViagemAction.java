package br.org.flem.sav.action;

import br.org.flem.fw.service.IFuncionario;
import br.org.flem.fw.service.impl.RHServico;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fwe.hibernate.util.HibernateUtil;
import br.org.flem.fwe.web.action.SecurityDispatchAction;
import br.org.flem.fwe.web.annotation.Funcionalidade;
import br.org.flem.fwe.web.util.MensagemTagUtil;
import br.org.flem.sav.bo.BancoBO;
import br.org.flem.sav.bo.ContaCorrenteViagemBO;
import br.org.flem.sav.negocio.ContaCorrenteViagem;
import br.org.flem.sav.negocio.util.Banco;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
 * @author mgsilva
 */
public class ContaCorrenteViagemAction extends SecurityDispatchAction {

    /* forward name="success" path="" */
    private final static String SUCCESS = "success";

    @Override
    @Funcionalidade(nomeCurto="admCadCc")
    public ActionForward unspecified(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) {
        try {

            request.setAttribute("lista", new ContaCorrenteViagemBO().obterTodos());

        } catch (AplicacaoException ex) {
            Logger.getLogger(ContaCorrenteViagemAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mapping.findForward("lista");
    }

    @Funcionalidade(nomeCurto="admNovoCadCc")
    public ActionForward novo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws AplicacaoException {

        RHServico rh = new RHServico();
        ContaCorrenteViagemBO contaCorrenteViagemBO = new ContaCorrenteViagemBO();
        
        Map<Integer, IFuncionario> mapLista = rh.obterMapIdFuncionarioAtivo();
        List<IFuncionario> IFList = new ArrayList<IFuncionario>(mapLista.values());

        Collections.sort(IFList, new Comparator<IFuncionario>() {

            @Override
            public int compare(IFuncionario arg0, IFuncionario arg1) {
                return arg0.getNome().compareTo(arg1.getNome());
            }
        });

        List<ContaCorrenteViagem> listaContaCorrenteViagem = contaCorrenteViagemBO.obterTodos();

        Iterator itFuncionario = IFList.iterator();
        while(itFuncionario.hasNext()){
            IFuncionario func = (IFuncionario)itFuncionario.next();
            for(ContaCorrenteViagem contaCorrente : listaContaCorrenteViagem ){
                if(contaCorrente.getCodigoDominioFuncionarioViajante().equals(func.getCodigoDominio().toString())){
                    itFuncionario.remove();
                }
            }
        }

        request.setAttribute("listaFuncionarios", IFList);
        request.setAttribute("listaBancos", new BancoBO().obterTodosOrdenado());
        request.setAttribute("matriculaFuncionarioViajante",request.getParameter("matricula"));

        return mapping.findForward("novo");
    }

    public ActionForward adicionar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            DynaActionForm dyna = (DynaActionForm) form;
            RHServico rh = new RHServico();
            ContaCorrenteViagemBO ccBO = new ContaCorrenteViagemBO();
            ContaCorrenteViagem ccv = (ContaCorrenteViagem) dyna.get("contaCorrenteViagem");
            IFuncionario ifunc = rh.obterFuncionarioPorMatricula(Integer.valueOf(ccv.getCodigoDominioFuncionarioViajante()));

            ccv.setNomeFuncionario(ifunc.getNome());

            Banco bancoForm = (Banco) dyna.get("banco");
            Banco banco = new BancoBO().obterPorCodigo(bancoForm.getCodigo());
            ccv.setBanco(banco);

            if (ccBO.obterPorMatriculaFuncionario(ccv.getCodigoDominioFuncionarioViajante()) == null) {

                HibernateUtil.beginTransaction();
                ccBO.inserir(ccv);
                HibernateUtil.commitTransaction();

                MensagemTagUtil.adicionarMensagem(request.getSession(), "Conta adicionada com sucesso");
            
            } else {
                MensagemTagUtil.adicionarMensagem(request.getSession(),"A conta não foi adicionada");
                MensagemTagUtil.adicionarMensagem(request.getSession(), "Já existe uma conta cadastrada para o usuário: " + ccv.getNomeFuncionario());
            }

        } catch (AplicacaoException ex) {
            Logger.getLogger(ContaCorrenteViagemAction.class.getName()).log(Level.SEVERE, null, ex);
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao adicionar conta");
        }

        return mapping.findForward("redirect");
    }

    public ActionForward selecionar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            DynaActionForm dyna = (DynaActionForm) form;

            String idConta = request.getParameter("id");
            ContaCorrenteViagem ccv = new ContaCorrenteViagemBO().obterPorPk(Integer.valueOf(idConta));

            RHServico rh = new RHServico();

            int matriculaFuncionario = Integer.valueOf(ccv.getCodigoDominioFuncionarioViajante());
            IFuncionario funcionario = (IFuncionario) rh.obterFuncionarioPorMatricula(matriculaFuncionario);

            dyna.set("contaCorrenteViagem", ccv);
            dyna.set("banco", ccv.getBanco());

            request.setAttribute("viajante", funcionario);
            request.setAttribute("listaBancos", new BancoBO().obterTodosOrdenado());

        } catch (AplicacaoException ex) {
            Logger.getLogger(ContaCorrenteViagemAction.class.getName()).log(Level.SEVERE, null, ex);
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao selecionar conta");

        }

        return mapping.findForward("editar");
    }

    public ActionForward alterar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            DynaActionForm dyna = (DynaActionForm) form;
            RHServico rh = new RHServico();
            ContaCorrenteViagem ccv = (ContaCorrenteViagem) dyna.get("contaCorrenteViagem");
            IFuncionario ifunc = (IFuncionario)rh.obterFuncionarioPorMatricula(Integer.valueOf(ccv.getCodigoDominioFuncionarioViajante()));

            ccv.setNomeFuncionario(ifunc.getNome());

            Banco bancoForm = (Banco) dyna.get("banco");
            Banco banco = new BancoBO().obterPorCodigo(bancoForm.getCodigo());
            ccv.setBanco(banco);

            HibernateUtil.beginTransaction();
            new ContaCorrenteViagemBO().alterar(ccv);
            HibernateUtil.commitTransaction();

            MensagemTagUtil.adicionarMensagem(request.getSession(), "Conta alterada com sucesso");

        } catch (AplicacaoException ex) {
            Logger.getLogger(ContaCorrenteViagemAction.class.getName()).log(Level.SEVERE, null, ex);
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Erro ao adicionar conta");
        }

        return mapping.findForward("redirect");
    }

    @Funcionalidade(nomeCurto="admExcluirCadCc")
    public ActionForward excluir(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArrayList erros = new ArrayList();
        try {
            String[] id = new String[0];
            if (request.getParameterValues("ids_exclusao") != null) {
                id = request.getParameterValues("ids_exclusao");
            }
            ContaCorrenteViagemBO ccvBO = new ContaCorrenteViagemBO();

            HibernateUtil.beginTransaction();
            for (int i = 0; i < id.length; i++) {
                ContaCorrenteViagem ccv = ccvBO.obterPorPk(Integer.valueOf(id[i]));
                ccvBO.excluir(ccv);
            }
            HibernateUtil.commitTransaction();

        } catch (AplicacaoException ex) {
            Logger.getLogger(CompanhiaAction.class.getName()).log(Level.SEVERE, null, ex);
            erros.add("Occoreu um erro ao tentar remover Conta Corrente Viagem");
            MensagemTagUtil.adicionarMensagem(request.getSession(), erros.toString());
        }

        if (erros.size() <= 0) {
            MensagemTagUtil.adicionarMensagem(request.getSession(), "Exclusão realizada com sucesso!");
        }
        return mapping.findForward("redirect");
    }
    
    
    public ActionForward filtrar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm dyna = (DynaActionForm) form;
        String busca = (String) dyna.get("busca");
        try {
            List<ContaCorrenteViagem> Nome = new ContaCorrenteViagemBO().obterPorMatriculaOuNome(busca);
            ContaCorrenteViagem Matricula =  new ContaCorrenteViagemBO().obterPorMatriculaFuncionario(busca);
            
            if(Nome.isEmpty()){
                request.setAttribute("lista", new ContaCorrenteViagemBO().obterPorMatriculaFuncionario(busca));
            }  else {
                request.setAttribute("lista", new ContaCorrenteViagemBO().obterPorMatriculaOuNome(busca));
            }


        } catch (AplicacaoException ex) {
            Logger.getLogger(ContaCorrenteViagemAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapping.findForward("lista");
    }
    
}