package br.org.flem.sav.regras;

import br.org.flem.fw.service.IFuncionario;
import br.org.flem.sav.bo.LiberacaoViagemBO;
import br.org.flem.sav.bo.ViagemBO;
import br.org.flem.sav.interfaces.ISolicitarViagem;
import br.org.flem.sav.negocio.LiberacaoViagem;
import br.org.flem.sav.negocio.StatusAgendamento;
import br.org.flem.sav.negocio.Viagem;
import br.org.flem.sav.negocio.ViagemComAgendamento;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author emsilva
 */
public class SolicitarViagemSeturStrategy implements ISolicitarViagem {

    @Override
    public ArrayList<String> funcionarioPodeViajar(IFuncionario iFunc) {
        ArrayList<String> resposta = new ArrayList<String>();
        try {

            //testa se o funcionário possui uma liberação
            LiberacaoViagem liberacaoViagem = new LiberacaoViagemBO().obterByIFuncionario(iFunc);
            if (liberacaoViagem != null && liberacaoViagem.isValido()) {
                resposta.add("true");
                resposta.add("O Funcionário \"" + iFunc.getNome().replace("  ", "") + "\" pode solicitar uma viagem porque possui uma liberação!");
                resposta.add("true");
            } else {
                //O funcionário não possui uma liberação, então seguimos com os testes!

                //testa se tem prestação em aberto
                Collection<Viagem> ausencia_prestacao = new ViagemBO().obterSemPrestacaoContaPorViajante(iFunc.getCodigoDominio());
                if (!ausencia_prestacao.isEmpty()) {
                    String ids = "";
                    boolean foiAgendada = true;

                    //Não será autorizado qualquer viagem que estiver com 2 ou mais prestações de contas de viagem em atraso
                    Integer limitePermitido = 2;

                    for (Viagem viagem : ausencia_prestacao) {
                        ViagemComAgendamento vca = null;
                        if (viagem instanceof ViagemComAgendamento) {
                            vca = (ViagemComAgendamento) viagem;//new ViagemComAgendamentoBO().obterPorPk(viagem.getId());
                        }
                        if (vca == null || vca.getStatusAgendamento() == StatusAgendamento.AGENDAMENTO_APROVADO) {
                            // se esta viagem não foi agendada ou se o agendamento foi aprovado
                            ids += viagem.getId() + ", ";
                            foiAgendada = false;
                            limitePermitido--;
                        }
                    }
                    if (!foiAgendada) {
                        if (limitePermitido <= 0) {
                            ids = ids.substring(0, ids.length() - 2);
                            resposta.add("false");
                            resposta.add("O Funcionário \"" + iFunc.getNome().replace("  ", "") + "\" não pode solicitar uma viagem porque ainda não prestou conta da(s) seguinte(s) viagem(ns), ID(s) ref.: \"" + ids + "\"! ");
                            resposta.add("false");
                        } else {
                            resposta.add("true");
                            resposta.add("O Funcionário \"" + iFunc.getNome().replace("  ", "") + "\" pode solicitar uma viagem!");
                            resposta.add("false");
                        }
                    } else {
                        resposta.add("true");
                        resposta.add("O Funcionário \"" + iFunc.getNome().replace("  ", "") + "\" pode solicitar uma viagem!");
                        resposta.add("false");
                    }
                } else {
                    //testa outros bloqueios de viagem
                    resposta.add("true");
                    resposta.add("O Funcionário \"" + iFunc.getNome().replace("  ", "") + "\" pode solicitar uma viagem!");
                    resposta.add("false");
                }
            }
            //}
        } catch (Exception ex) {
            Logger.getLogger(SolicitarViagemSeturStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resposta;
    }
}
