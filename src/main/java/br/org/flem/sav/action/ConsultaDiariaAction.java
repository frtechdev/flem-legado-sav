package br.org.flem.sav.action;

import br.org.flem.fw.service.IFuncionario;
import br.org.flem.fw.service.INivel;
import br.org.flem.fw.service.impl.RHServico;
import br.org.flem.fwe.web.action.SecurityDispatchAction;
import br.org.flem.fwe.web.annotation.Funcionalidade;
import br.org.flem.sav.negocio.util.Diarias;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author mgsilva
 */
public class ConsultaDiariaAction extends SecurityDispatchAction {
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    
    
    @Override
    @Funcionalidade(nomeCurto="consulDiaria")
    public ActionForward unspecified(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response){

        RHServico rh = new RHServico();

       Map<Integer,IFuncionario> map = rh.obterMapIdFuncionarioAtivo();
       List<IFuncionario> lista = new ArrayList<IFuncionario>(map.values());

       Collections.sort(lista, new Comparator<IFuncionario>() {

            public int compare(IFuncionario t, IFuncionario t1) {
               return t.getNome().compareTo(t1.getNome());
            }
        });

       request.setAttribute("listaFuncionarios", lista);



        return mapping.findForward("diaria");
    }

    @Funcionalidade(nomeCurto="consulDiaria")
    public ActionForward filtrar(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response){
        try {
            String matricula = request.getParameter("id");
            //GregorianCalendar gc = new GregorianCalendar();
            RHServico rh = new RHServico();
            IFuncionario func = rh.obterFuncionarioPorMatricula(Integer.valueOf(matricula));
            double salario = rh.obterSalarioPorFuncionario(func);

            Double porcentagem = new Diarias().calcularPorcentagem(func);

            request.getSession().setAttribute("func_ConsultaDiaria_matricula", func.getCodigoDominio());

            request.setAttribute("porcentagem", porcentagem);
            request.setAttribute("valorRestante", salario*((50-porcentagem)/100));

        } catch (Exception ex) {
            Logger.getLogger(ConsultaDiariaAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mapping.findForward(SUCCESS);
    }
}