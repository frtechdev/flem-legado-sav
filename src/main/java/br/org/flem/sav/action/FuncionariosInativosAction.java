package br.org.flem.sav.action;

import br.org.flem.fw.persistencia.dao.legado.control.DepartamentoUsuarioDAO;
import br.org.flem.fw.service.IFuncionario;
import br.org.flem.fw.service.IUsuario;
import br.org.flem.fw.util.Constante;
import br.org.flem.fwe.service.IUsuarioExterno;
import br.org.flem.fwe.web.action.SecurityDispatchAction;
import br.org.flem.fwe.web.annotation.Funcionalidade;
import br.org.flem.fwe.web.util.MensagemTagUtil;
import br.org.flem.sav.bo.ViagemBO;
import br.org.flem.sav.dto.ViagemDTO;
import br.org.flem.sav.negocio.StatusPrestacaoContas;
import br.org.flem.sav.negocio.StatusViagem;
import br.org.flem.sav.negocio.Viagem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 *
 * @author ILFernandes
 */
public class FuncionariosInativosAction extends SecurityDispatchAction {

    @Override
    @Funcionalidade(nomeCurto="prestFuncInativos")
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        IUsuario usuario = (IUsuario) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
        IFuncionario func_sessao = null;
        IFuncionario func_atual = null;

        Map<Integer, IFuncionario> mapFunc = new DepartamentoUsuarioDAO().obterFuncionariosPorDepartamentoSituacao(usuario, false);

        // início da lista de usuários
        Collection<IFuncionario> IFList = new ArrayList<IFuncionario>();
        boolean exibir = false;
        String matriculaUsuario = (String) request.getSession().getAttribute("matriculaUsuario");

        // func_atual é o usuário logado ou o usuário selecionado na combo
        if (matriculaUsuario != null && !matriculaUsuario.isEmpty()) {
            func_atual = (IFuncionario) mapFunc.get(Integer.parseInt(matriculaUsuario));
            exibir = true;
        }
        try {
            Collection<ViagemDTO> viagensDTO = new ArrayList<ViagemDTO>();
            ViagemBO viagemBO = new ViagemBO();
            Collection<Viagem> viagens = viagemBO.obterTodos();
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            IUsuarioExterno usuarioExterno = (IUsuarioExterno) request.getSession().getAttribute(Constante.USUARIO_LOGADO);
            if (usuarioExterno.getPermissoes().contains("solicEditarViagFuncionario")) {
                request.setAttribute("solicEditarViagFuncionario", "1");
            }
            if (usuarioExterno.getPermissoes().contains("solicCriarEditarPrestFuncionario")) {
                request.setAttribute("solicCriarEditarPrestFuncionario", "1");
            }
            for (Viagem viagem : viagens) {
                if (viagem.getStatusViagem() != StatusViagem.VIAGEM_RECEBIDA
                        || (viagem.getPrestacaoContas() != null && viagem.getPrestacaoContas().getStatusPrestacaoContas() != StatusPrestacaoContas.PRESTACAO_INFORMADA)) {
                    continue;
                }
                if (mapFunc.containsKey(viagem.getCodigoDominioUsuarioViajante())) {
                    ViagemDTO vdto = new ViagemDTO();
                    vdto.setViagem(viagem);

                    if (viagem.getPrestacaoContas() == null) {
                        vdto.setPrestouConta("0");
                        vdto.setPeriodo(simpleDate.format(viagem.getDataSaidaPrevista()) + " " + simpleDate.format(viagem.getDataRetornoPrevista()));
                    } else {
                        vdto.setPrestouConta("1");
                        vdto.setPeriodo(simpleDate.format(viagem.getPrestacaoContas().getDataSaidaEfetiva()) + " " + simpleDate.format(viagem.getPrestacaoContas().getDataRetornoEfetiva()));
                    }

                    IFList.add(mapFunc.get(viagem.getCodigoDominioUsuarioViajante()));
                    func_sessao = mapFunc.get(viagem.getCodigoDominioUsuarioViajante());
                    vdto.setNomeViajante(func_sessao.getNome());
                    viagensDTO.add(vdto);
                }
            }

            request.setAttribute("nmeUsuarioLogado", usuario.getNome());
            request.setAttribute("matriculaUsuarioLogado", usuario.getCodigoDominio());

            request.setAttribute("lista", viagensDTO);
        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemTagUtil.adicionarMensagem(request.getSession(), ex.getMessage(), "erro", this.getClass().getName(), ex);
        }
        request.setAttribute("listaFuncionario", IFList);
        return mapping.findForward("lista");
    }

    @Funcionalidade(nomeCurto="prestFuncInativos")
    public ActionForward filtrar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm dyna = (DynaActionForm) form;
        Viagem filtro = (Viagem) dyna.get("viagem");
        if (filtro.getCodigoDominioUsuarioViajante() != null) {
            request.getSession().setAttribute("matriculaUsuario", filtro.getCodigoDominioUsuarioViajante().toString());
        }
        return unspecified(mapping, form, request, response);
    }
}
