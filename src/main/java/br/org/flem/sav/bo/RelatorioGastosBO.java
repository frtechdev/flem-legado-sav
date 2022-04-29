package br.org.flem.sav.bo;

import br.org.flem.fw.service.IColaborador;
import br.org.flem.fw.service.IFuncionario;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.sav.dto.PlanilhaGastosDTO;
import br.org.flem.sav.dto.RelatorioGastoDTO;
import br.org.flem.sav.negocio.Gasto;
import br.org.flem.sav.negocio.StatusViagem;
import br.org.flem.sav.negocio.Viagem;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ilfernandes
 */
public class RelatorioGastosBO {

    public Collection<RelatorioGastoDTO> prepararColecaoDTO(Collection<String> codigosCentroCusto, String dataInicial, String dataFinal, Map<Integer, IFuncionario> funcionarios, Map<String, IColaborador> consultores) throws AplicacaoException, ParseException {
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Map<String, RelatorioGastoDTO> mapDto = new HashMap<String, RelatorioGastoDTO>();
        Viagem viagemFiltro = new Viagem();
        //viagemFiltro.setStatusViagem(StatusViagem.VIAGEM_FINALIZADA);
        if (dataInicial != null && !dataInicial.isEmpty()) {
            viagemFiltro.setDataSaidaPrevista(simpleDate.parse(dataInicial + " 00:00"));
        }
        if (dataFinal != null && !dataFinal.isEmpty()) {
            viagemFiltro.setDataRetornoPrevista(simpleDate.parse(dataFinal + " 23:59"));
        }

        //Collection<Viagem> viagens = new ViagemBO().obterPorFiltroFaixaCentroCusto(viagemFiltro, codigosCentroCusto);
        Collection<Viagem> viagens = new ViagemBO().obterTodasPorFiltroFaixaCentroCusto(viagemFiltro, codigosCentroCusto);
        //Collection<Gasto> gastos = new GastoBO().obterTodos();
        for (Viagem viagem : viagens) {
            //verifica se  o viajante é funcionario ou consultor.
            String codigoViajante = (viagem.getCodigoDominioUsuarioViajante() != null ? viagem.getCodigoDominioUsuarioViajante().toString() : viagem.getCodigoConsultorViajante());
            RelatorioGastoDTO relatorioGastoDTO = null;
            if (mapDto.containsKey(codigoViajante)) {
                relatorioGastoDTO = mapDto.get(codigoViajante);
                relatorioGastoDTO.getGastos().addAll(viagem.getPrestacaoContas().getGastos());
            } else {
                relatorioGastoDTO = new RelatorioGastoDTO();
                relatorioGastoDTO.setGastos(viagem.getPrestacaoContas().getGastos());
                if (viagem.getCodigoDominioUsuarioViajante() != null && funcionarios.containsKey(Integer.valueOf(codigoViajante))) {
                    relatorioGastoDTO.setNomeViajante(funcionarios.get(Integer.valueOf(codigoViajante)).getNome());
                    relatorioGastoDTO.setCodigoViajante(codigoViajante);
                }

                if (consultores.containsKey(codigoViajante)) {
                    relatorioGastoDTO.setNomeViajante(consultores.get(codigoViajante).getNome());
                    relatorioGastoDTO.setCodigoViajante(codigoViajante);
                }
            }
            relatorioGastoDTO.incQtdViagens();

            //faz o somatorio do adiantamento e diarias
            if (viagem.getPrestacaoContas().getTotalDiarias() != null) {
                relatorioGastoDTO.setTotalAdiantamentoEfetivo((viagem.getPrestacaoContas().getTotalAdiantamento() - viagem.getPrestacaoContas().getTotalDiarias()) + relatorioGastoDTO.getTotalAdiantamentoEfetivo());
                relatorioGastoDTO.setTotalDiarias(viagem.getPrestacaoContas().getTotalDiarias() + relatorioGastoDTO.getTotalDiarias());
                if (viagem.getTotalDiarias() != null) {
                    relatorioGastoDTO.setTotalDiariasDiferencial((viagem.getPrestacaoContas().getTotalDiarias() - viagem.getTotalDiarias()) + relatorioGastoDTO.getTotalDiariasDiferencial());
                }
            } else {
                relatorioGastoDTO.setTotalAdiantamentoEfetivo(viagem.getPrestacaoContas().getTotalAdiantamento() + relatorioGastoDTO.getTotalAdiantamentoEfetivo());
            }
            if (viagem.getTotalDiarias() != null) {
                relatorioGastoDTO.setTotalAdiantamento((viagem.getTotalAdiantamento() - viagem.getTotalDiarias()) + relatorioGastoDTO.getTotalAdiantamento());
            } else {
                relatorioGastoDTO.setTotalAdiantamento(viagem.getPrestacaoContas().getTotalAdiantamento() + relatorioGastoDTO.getTotalAdiantamento());
            }

            //relatorioGastoDTO.setDevolucao((relatorioGastoDTO.getTotalDiarias() - relatorioGastoDTO.getTotalDiarias()));
            relatorioGastoDTO.setDevolucao((relatorioGastoDTO.getTotalAdiantamento() - relatorioGastoDTO.getTotalAdiantamentoEfetivo()) - relatorioGastoDTO.getTotalDiariasDiferencial());
            if (relatorioGastoDTO.getCodigoViajante() != null && !relatorioGastoDTO.getCodigoViajante().isEmpty()) {
                mapDto.put(codigoViajante, relatorioGastoDTO);
            }
        }
        return mapDto.values();
    }

    public Collection<PlanilhaGastosDTO> prepararRelatorioPlanilha(Collection<String> codigosCentroCusto, String dataInicial, String dataFinal, Map<Integer, IFuncionario> funcionarios, Map<String, IColaborador> consultores, Set<String> tiposGastos) throws AplicacaoException, ParseException {
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Collection<PlanilhaGastosDTO> retorno = new ArrayList<PlanilhaGastosDTO>();
        Viagem viagemFiltro = new Viagem();
        viagemFiltro.setStatusViagem(StatusViagem.VIAGEM_FINALIZADA);
        if (dataInicial != null && !dataInicial.isEmpty()) {
            viagemFiltro.setDataSaidaPrevista(simpleDate.parse(dataInicial + " 00:00"));
        }
        if (dataFinal != null && !dataFinal.isEmpty()) {
            viagemFiltro.setDataRetornoPrevista(simpleDate.parse(dataFinal + " 23:59"));
        }

        Collection<Viagem> viagens = new ViagemBO().obterPorFiltroFaixaCentroCusto(viagemFiltro, codigosCentroCusto);

        for (Viagem viagem : viagens) {
            PlanilhaGastosDTO diaria = new PlanilhaGastosDTO();
            String codigoViajante = (viagem.getCodigoDominioUsuarioViajante() != null ? viagem.getCodigoDominioUsuarioViajante().toString() : viagem.getCodigoConsultorViajante());
            String nomeViajante = "";
            if (viagem.getCodigoDominioUsuarioViajante() != null && funcionarios.containsKey(Integer.valueOf(codigoViajante))) {
                nomeViajante = funcionarios.get(Integer.valueOf(codigoViajante)).getNome();
                diaria.setCodigoViajante(codigoViajante);
            } else if (consultores.containsKey(codigoViajante)) {
                nomeViajante = consultores.get(codigoViajante).getNome();
                diaria.setCodigoViajante(codigoViajante);
            }
            diaria.setNomeViajante(nomeViajante);
            //Colocado . antes para para poder aparecer em primeiro no relatório
            diaria.setTipoGasto(".Diárias");
            diaria.setValor(viagem.getPrestacaoContas().getTotalDiarias());
            if (diaria.getCodigoViajante() != null && !diaria.getCodigoViajante().isEmpty()) {
                retorno.add(diaria);
            }
            for (Gasto gasto : viagem.getPrestacaoContas().getGastos()) {
                //gera Novo Gasto
                if (tiposGastos.contains(gasto.getTipoGasto().getDescricao())) {
                    PlanilhaGastosDTO gastoPlanilha = new PlanilhaGastosDTO();
                    gastoPlanilha.setCodigoViajante(codigoViajante);
                    gastoPlanilha.setNomeViajante(nomeViajante);
                    gastoPlanilha.setTipoGasto(gasto.getTipoGasto().getDescricao());
                    gastoPlanilha.setValor(gasto.getValor());
                    retorno.add(gastoPlanilha);
                   // System.out.println(codigoViajante + " - " + nomeViajante);
                } else {
                    PlanilhaGastosDTO gastoPlanilha = new PlanilhaGastosDTO();
                    gastoPlanilha.setCodigoViajante(codigoViajante);
                    gastoPlanilha.setNomeViajante(nomeViajante);
                    gastoPlanilha.setTipoGasto("Outros");
                    gastoPlanilha.setValor(gasto.getValor());
                    if (gastoPlanilha.getCodigoViajante() != null && !gastoPlanilha.getCodigoViajante().isEmpty()) {

                        retorno.add(gastoPlanilha);
                    }
                }
            }
        }
        return retorno;
    }
}
