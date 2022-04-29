package br.org.flem.sav.bo;

import br.org.flem.fw.service.IFuncionario;
import br.org.flem.fw.service.impl.RHServico;
import br.org.flem.fwe.bo.BaseBOAb;
import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.sav.dao.ViagemDAO;
import br.org.flem.sav.dto.RelatorioDiariaSalarioColaboradoresDTO;
import br.org.flem.sav.dto.ViagemDTO;
import br.org.flem.sav.negocio.DestinoViagem;
import br.org.flem.sav.negocio.Viagem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mccosta
 */
public class ViagemBO extends BaseBOAb<Viagem> {

    public ViagemBO() throws AplicacaoException {
        super(new ViagemDAO());
    }

    public Collection<Viagem> obterTodosFuncionario(IFuncionario f) {
        return ((ViagemDAO) dao).obterTodosFuncionario(f);
    }

    public Collection<Integer> obterFuncMatriculaSemPrestacaoConta() {
        return ((ViagemDAO) dao).obterFuncMatriculaSemPrestacaoConta();
    }

    public Collection<Viagem> obterTodosConsultor() {
        return ((ViagemDAO) dao).obterTodosConsultor();
    }

    public Collection<Viagem> obterTodosConsultor(String cod_consultor) {
        return ((ViagemDAO) dao).obterTodosConsultor(cod_consultor);
    }

    public Collection<Viagem> obterTodosFuncionario() {
        return ((ViagemDAO) dao).obterTodosFuncionario();
    }

    public Collection<Viagem> obterTodosByStatusViagem(Integer status) {
        return ((ViagemDAO) dao).obterTodosByStatusViagem(status);
    }

    public Collection<Viagem> obterTodosByStatusViagemAbertoRecebido() {
        return ((ViagemDAO) dao).obterTodosByStatusViagemAbertoRecebido();
    }

    public Collection<Viagem> obterTodosByStatusViagemAbertoRecebidoSemPrestacao() {
        return ((ViagemDAO) dao).obterTodosByStatusViagemAbertoRecebidoSemPrestacao();
    }

    public Collection<Viagem> obterTodosByStatusViagemRecebidoSemPrestacao() {
        return ((ViagemDAO) dao).obterTodosByStatusViagemRecebidoSemPrestacao();
    }

    public Collection<Viagem> obterTodosByStatusPrestacaoConta(Integer status) {
        return ((ViagemDAO) dao).obterTodosByStatusPrestacaoConta(status);
    }

    public Collection<Viagem> obterSemPrestacaoContaPorViajante(Integer codigoDominio) {
        List<Viagem> viagens = (List<Viagem>) ((ViagemDAO) dao).obterSemPrestacaoContaPorViajante(codigoDominio);
        Collection<Viagem> viagensComPrestacaoAberta = ((ViagemDAO) dao).obterPrestacaoContaAbertaPorViajante(codigoDominio);
        Collection<Viagem> viagensCanceladasComPrestacaoAberta = ((ViagemDAO) dao).obterCancelamentoRecebimentoPrestacaoContaPorViajante(codigoDominio);

        for (Viagem viagem : viagensComPrestacaoAberta) {
            viagens.add(viagem);
        }

        for (Viagem viagem : viagensCanceladasComPrestacaoAberta) {
            viagens.add(viagem);
        }

        //Ordena em ordem decrescente
        Collections.sort(viagens, new Comparator<Viagem>() {

            public int compare(Viagem v1, Viagem v2) {
                return v2.getId().compareTo(v1.getId());
            }
        });

        return viagens;
    }

    public Collection<Viagem> obterSemPrestacaoContaPorConsultor(String codigo) {
        List<Viagem> viagens = (List<Viagem>) ((ViagemDAO) dao).obterSemPrestacaoContaPorConsultor(codigo);
        Collection<Viagem> viagensComPrestacaoAberta = ((ViagemDAO) dao).obterPrestacaoContaAbertaPorConsultor(codigo);
        Collection<Viagem> viagensCanceladasComPrestacaoAberta = ((ViagemDAO) dao).obterCancelamentoRecebimentoPrestacaoContaPorConsultor(codigo);

        for (Viagem viagem : viagensComPrestacaoAberta) {
            viagens.add(viagem);
        }
        for (Viagem viagem : viagensCanceladasComPrestacaoAberta) {
            viagens.add(viagem);
        }

        //Ordena em ordem decrescente
        Collections.sort(viagens, new Comparator<Viagem>() {

            public int compare(Viagem v1, Viagem v2) {
                return v2.getId().compareTo(v1.getId());
            }
        });

        return viagens;
    }

    public Collection<Viagem> obterTodosByStatusPrestacaoContaEmAberto() {
        return ((ViagemDAO) dao).obterTodosByStatusPrestacaoContaEmAberto();
    }

    public Collection<Viagem> obterTodosByStatusPrestacaoContaRecebidoFinalizado() {
        return ((ViagemDAO) dao).obterTodosByStatusPrestacaoContaRecebidoFinalizado();
    }

    public Collection<Viagem> obterTodosComPrestacaoConta() {
        return ((ViagemDAO) dao).obterTodosComPrestacaoConta();
    }

    public Collection<Viagem> obterTodasPorMesDoFuncionario(IFuncionario funcionario, int mes, int ano) {
        return ((ViagemDAO) dao).obterTodasPorMesDoFuncionario(funcionario, mes, ano);
    }

    public Collection<Viagem> obterTodasPorMesAnoComDiarias(int mes, int ano, Collection<String> centroCustos) {
        return ((ViagemDAO) dao).obterTodasPorMesAnoComDiarias(mes, ano, centroCustos);
    }

    public Collection<Viagem> obterTodasDoFuncionarioPorPeriodoMesAnoComDiarias(Integer codigoDominio, Date dataSaida, Date dataRetorno) {
        return ((ViagemDAO) dao).obterTodasDoFuncionarioPorPeriodoMesAnoComDiarias(codigoDominio, dataSaida, dataRetorno);
    }

    public Collection<Viagem> obterTodosOrdenadoDataSolicitacao() {
        return ((ViagemDAO) dao).obterTodosOrdenadoDataSolicitacao();
    }

    public Collection<Viagem> obterPorFiltros(Viagem viagem, Collection<Integer> matriculas) {
        return ((ViagemDAO) dao).obterPorFiltros(viagem, matriculas);
    }

    public Collection<Viagem> obterPorFiltros(Viagem viagem, Collection<Integer> matriculas, Collection<String> consultores) {
        return ((ViagemDAO) dao).obterPorFiltros(viagem, matriculas, consultores);
    }

    public Map<Integer, Integer> obterMatriculaQtViagensPrestacaoAberta(Collection<String> codigosCentroCusto) {
        return ((ViagemDAO) dao).obterMatriculaQtViagensPrestacaoAberta(codigosCentroCusto);
    }

    public Map<String, Integer> obterCodConsultorQtViagensPrestacaoAberta(Collection<String> codigosCentroCusto) {
        return ((ViagemDAO) dao).obterCodConsultorQtViagensPrestacaoAberta(codigosCentroCusto);
    }

    public Collection<Viagem> obterTodosPorPeriodo(Viagem viagem) {
        Collection<Viagem> viagemLista = ((ViagemDAO) dao).obterTodosPorPeriodo(viagem);
        Collection<Viagem> viagens = new ArrayList<Viagem>();

        for (Viagem v : viagemLista) {
            if (v.getPrestacaoContas() == null) {
                viagens.add(v);
            } else {
                if (viagem.getDataSaidaPrevista().before(v.getPrestacaoContas().getDataRetornoEfetiva())
                        || viagem.getDataSaidaPrevista().equals(v.getPrestacaoContas().getDataRetornoEfetiva())) {
                    viagens.add(v);
                }
            }
        }

        return viagens;
    }

    /**
     * <b>OBS.:</b> No relatorio SAV015 a dataRetornoPrevista é entendida como a
     * data final do período porém na querie o valor da data retorno ´é passado
     * para o banco como sendo a da de saida. data saida >= dataInicial e data
     * saida <= dataFinal
     *
     * @param viagemFiltro
     * @param codigosCentroCusto
     * @return Coleção de viagens
     *
     *
     */
    public Collection<Viagem> obterPorFiltroFaixaCentroCusto(Viagem viagemFiltro, Collection<String> codigosCentroCusto) {
        return ((ViagemDAO) dao).obterPorFiltroFaixaCentroCusto(viagemFiltro, codigosCentroCusto);
    }

    /**
     * <b>OBS.:</b> No relatorio SAV015 a dataRetornoPrevista é entendida como a
     * data final do período porém na querie o valor da data retorno ´é passado
     * para o banco como sendo a da de saida. data saida >= dataInicial e data
     * saida <= dataFinal
     *
     * @param viagemFiltro
     * @param codigosCentroCusto
     * @return Coleção de viagens
     *
     *
     */
    public Collection<Viagem> obterTodasPorFiltroFaixaCentroCusto(Viagem viagemFiltro, Collection<String> codigosCentroCusto) {
        return ((ViagemDAO) dao).obterTodasPorFiltroFaixaCentroCusto(viagemFiltro, codigosCentroCusto);
    }

    public Collection<Viagem> relatorioAnalíticoPrestacaoPendente(Viagem viagemFiltro, Collection<String> codigosCentroCusto) {
        return ((ViagemDAO) dao).relatorioAnalíticoPrestacaoPendente(viagemFiltro, codigosCentroCusto);
    }

    public Collection<Viagem> relatorioAnalíticoPrestacaoInformada(Viagem viagemFiltro, Collection<String> codigosCentroCusto) {
        return ((ViagemDAO) dao).relatorioAnalíticoPrestacaoInformada(viagemFiltro, codigosCentroCusto);
    }

    public Date calcularDataPagamentoPrevisto(Date dataSaidaPrevista, DestinoViagem destinoViagem) {
        Calendar dataPagamentoPrevisto = Calendar.getInstance();
        dataPagamentoPrevisto.setTime(dataSaidaPrevista);
        Calendar hoje = Calendar.getInstance();
        if (destinoViagem != DestinoViagem.EXTERIOR) {

            // Pega a data de 2 dias (48 horas) atrás.
            dataPagamentoPrevisto.add(Calendar.DAY_OF_MONTH, -2);

            // Trata se a data de vencimento não cai em final de semana, nesse caso busca o dia útil anterior.
            while (dataPagamentoPrevisto.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || dataPagamentoPrevisto.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                dataPagamentoPrevisto.add(Calendar.DAY_OF_MONTH, -1);
            }

            // Trata se a data de vencimento é a data de hoje, nesse caso busca o próximo dia útil.
            if (hoje.getTimeInMillis() > dataPagamentoPrevisto.getTimeInMillis()) {
                dataPagamentoPrevisto.setTimeInMillis(hoje.getTimeInMillis());
                dataPagamentoPrevisto.add(Calendar.DAY_OF_MONTH, 1);
            }

            // Trata se a data de vencimento não cai em final de semana, nesse caso busca o próximo dia útil.
            while (dataPagamentoPrevisto.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || dataPagamentoPrevisto.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                dataPagamentoPrevisto.add(Calendar.DAY_OF_MONTH, 1);
            }

        } else {

            // Pega a data de 4 dias (96 horas) atrás.
            dataPagamentoPrevisto.add(Calendar.DAY_OF_MONTH, -4);

            // Trata se a data de vencimento não cai em final de semana, nesse caso busca o dia útil anterior.
            while (dataPagamentoPrevisto.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || dataPagamentoPrevisto.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                dataPagamentoPrevisto.add(Calendar.DAY_OF_MONTH, -1);
            }

            // Trata se a data de vencimento é a data de hoje, nesse caso busca o próximo dia útil.
            if (hoje.getTimeInMillis() > dataPagamentoPrevisto.getTimeInMillis()) {
                dataPagamentoPrevisto.setTimeInMillis(hoje.getTimeInMillis());
                dataPagamentoPrevisto.add(Calendar.DAY_OF_MONTH, 1);
            }

            // Trata se a data de vencimento não cai em final de semana, nesse caso busca o próximo dia útil.
            while (dataPagamentoPrevisto.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || dataPagamentoPrevisto.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                dataPagamentoPrevisto.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        return dataPagamentoPrevisto.getTime();
    }

    public Collection<Viagem> obterViagensPorIds(Collection<Integer> ids) {
        return ((ViagemDAO) this.dao).obterViagensPorIds(ids);
    }

    public Collection<Viagem> obterViagensPorDataPagamentoViajante(Date date, Integer matricula) {
        return ((ViagemDAO) this.dao).obterViagensPorDataPagamentoViajante(date, matricula);
    }

    public Collection<Viagem> obterViagensComPrestacaoContasPorDataPagamentoViajante(Date date, Integer matricula) {
        return ((ViagemDAO) this.dao).obterViagensComPrestacaoContasPorDataPagamentoViajante(date, matricula);
    }

    /**
     * Obtém a lista de viajens por Centro de custos, e depois as viagens pelas
     * matriculas.
     *
     * @param dataPagamento
     * @param codigoCentrosCustos
     * @return
     */
    public Collection<Viagem> obterViagensPorDataPagamentoViajantes(Date dataPagamento, Collection<String> codigoCentrosCustos) {
        Collection<Integer> listaMatriculas = new ArrayList<Integer>();
        Collection<Viagem> viagensPorCC = ((ViagemDAO) this.dao).obterViagensPorDataDePagamentoCentroCusto(dataPagamento, codigoCentrosCustos);
        for (Viagem viagem : viagensPorCC) {
            if (viagem.getCodigoDominioUsuarioViajante() == null) {
                continue;
            }
            listaMatriculas.add(viagem.getCodigoDominioUsuarioViajante());
        }

        return ((ViagemDAO) this.dao).obterViagensPorDataDePagamentoUsuarios(dataPagamento, listaMatriculas);
    }

    /**
     * Obtém a lista de viajens por Centro de custos, e depois as viagens pelas
     * matriculas.
     *
     * @param dataSaida
     * @param dataRetorno
     * @param codigoCentrosCustos
     * @return
     */
    public Collection<Viagem> obterViagensPorDataPagamentoViajantes(String dataSaida, String dataRetorno, Collection<String> codigoCentrosCustos) {
        //Solução para quando o cc não estiver mais disponível no SAV
        //{
        /*
            codigoCentrosCustos = new ArrayList<String>();
            codigoCentrosCustos.add("202860000");
            codigoCentrosCustos.add("202861000");
            codigoCentrosCustos.add("2028610036");
            codigoCentrosCustos.add("2028610039");
            codigoCentrosCustos.add("2028610136");
            codigoCentrosCustos.add("2028610236");
            codigoCentrosCustos.add("2028611130");
            codigoCentrosCustos.add("2028611133");
            codigoCentrosCustos.add("2028611136");
            codigoCentrosCustos.add("2028611139");
            codigoCentrosCustos.add("2028611214");
            codigoCentrosCustos.add("2028611230");
            codigoCentrosCustos.add("2028611233");
            codigoCentrosCustos.add("2028611239");
            codigoCentrosCustos.add("2028611414");
            codigoCentrosCustos.add("2028611433");
            codigoCentrosCustos.add("2028611439");
            codigoCentrosCustos.add("2028611536");
            codigoCentrosCustos.add("2028611539");
            codigoCentrosCustos.add("2028612314");
            codigoCentrosCustos.add("2028612333");
            codigoCentrosCustos.add("2028612339");
            codigoCentrosCustos.add("2028612514");
            codigoCentrosCustos.add("2028612533");
            codigoCentrosCustos.add("2028612539");
            codigoCentrosCustos.add("2028613114");
            codigoCentrosCustos.add("2028613130");
            codigoCentrosCustos.add("2028613133");
            codigoCentrosCustos.add("2028613214");
            codigoCentrosCustos.add("2028613230");
            codigoCentrosCustos.add("2028613233");
            codigoCentrosCustos.add("2028613239");
            codigoCentrosCustos.add("202862000");
            codigoCentrosCustos.add("2028620036");
            codigoCentrosCustos.add("2028621136");
            codigoCentrosCustos.add("202863000");
            codigoCentrosCustos.add("2028630020");
            codigoCentrosCustos.add("2028630030");
            codigoCentrosCustos.add("2028630039");
            codigoCentrosCustos.add("2028630040");
        */
        //}
        Collection<Viagem> viagensPorCC = ((ViagemDAO) this.dao).obterViagensPorDataDePagamentoCentroCusto(dataSaida, dataRetorno, codigoCentrosCustos);

        return viagensPorCC;
    }

    /*
     * public static void main(String[] args) throws AplicacaoException {
     * Calendar cal = Calendar.getInstance(); cal.set(2010, 11, 22);
     * System.out.println(new
     * ViagemBO().calcularDataPagamentoPrevisto(cal.getTime(),
     * DestinoViagem.BAHIA));
    }
     */
    public boolean verificarCentroCustoFonteDeRecurso(Viagem viagem, String codigo) {
        boolean retorno = true;
        if (viagem.getCodigoCentroCusto().charAt(0) != codigo.charAt(0)) {
            //recursos proprios nao paga viagem de projeto nem projeto paga viagem interna
            retorno = false;
        }
        else if (viagem.getCodigoCentroCusto().startsWith("2")) {
            //projeto só paga viagem de centros de custo do projeto
            if (!codigo.substring(0, 5).equals(viagem.getCodigoCentroCusto().substring(0, 5))) {
                retorno = false;
            }
        }
        return true;
    }
    
    public List<ViagemDTO> obterViagensParaConferencia(String dataSaida, String dataRetorno, Collection<String> listaCodigos) throws Exception {
        List<ViagemDTO> viagensDTO = new ArrayList<ViagemDTO>();
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
        RHServico rh = new RHServico();
        Map<Integer, IFuncionario> mapIFuncionarios = rh.obterMapTodos();
        ViagemBO viagemBO = new ViagemBO();
        Collection<Viagem> viagens = viagemBO.obterViagensPorDataPagamentoViajantes(dataSaida, dataRetorno, listaCodigos);
        Integer matricula = 0;
        ViagemDTO viagemDTO = null;
        
        for (Viagem viagem : viagens) {
            matricula = viagem.getCodigoDominioUsuarioViajante();
            if (mapIFuncionarios.containsKey(matricula)) {
                viagemDTO = new ViagemDTO();
                IFuncionario funcionario = mapIFuncionarios.get(matricula);
                
                viagemDTO.setNomeViajante(funcionario.getNome().replace("  ", ""));
                viagemDTO.setViagem(viagem);
                viagemDTO.setPeriodo(simpleDate.format(viagem.getDataSaidaPrevista()) + " - " + simpleDate.format(viagem.getDataRetornoPrevista()));
                viagemDTO.setStatusViagem(viagem.getStatusViagem().getDescricao());
                viagemDTO.setStatusPrestacao("-");
                viagemDTO.setObservacao("-");
                viagemDTO.setObservacao(viagem.getItinerario().getObservacao());
                viagemDTO.setCentroCusto(viagem.getCodigoCentroCusto());
                viagemDTO.setTotalDiarias(viagem.getTotalDiarias());
                viagemDTO.setItensItinerario(viagem.getItinerario().getItensItinerario());
                if(viagem.getPrestacaoContas() != null){
                    viagemDTO.setStatusPrestacao(viagem.getPrestacaoContas().getStatusPrestacaoContas().getDescricao());
                    viagemDTO.setObservacao("");
                    viagemDTO.setPeriodo(simpleDate.format(viagem.getPrestacaoContas().getDataSaidaEfetiva()) + " - " + simpleDate.format(viagem.getPrestacaoContas().getDataRetornoEfetiva()));
                    if(viagem.getPrestacaoContas().getItinerario() != null){
                        if(!viagem.getPrestacaoContas().getItinerario().getObservacao().isEmpty()){
                            viagemDTO.setObservacao(viagem.getPrestacaoContas().getItinerario().getObservacao());
                        }
                        viagemDTO.setItensItinerario(viagem.getPrestacaoContas().getItinerario().getItensItinerario());
                    }else{
                        //caso 1: viagem cancelada
                        viagemDTO.setItensItinerario(viagem.getItinerario().getItensItinerario());
                    }
                    viagemDTO.setTotalDiarias(viagem.getPrestacaoContas().getTotalDiarias());
                }
                viagensDTO.add(viagemDTO);
            }
        }
        
        //Ordena em ordem crescente de data de pagamento, nome e id da viagem
        Collections.sort(viagensDTO, new Comparator<ViagemDTO>() {
            @Override
            public int compare(ViagemDTO o1, ViagemDTO o2) {
                //Se a data de pagamento for diferentes, ordena pela data de pagamento
                int dtPagamento = o1.getViagem().getDataPagamento().compareTo(o2.getViagem().getDataPagamento());
                if (dtPagamento != 0) return dtPagamento; 

                //Se forem iguais, ordenamos pelo nome.
                int nome = o1.getNomeViajante().compareTo(o2.getNomeViajante());  
                if (nome != 0) return nome;   

                //Se os nomes forem iguais, ordenamos pelo ID da viagem.
                return o1.getViagem().getId().compareTo(o2.getViagem().getId());  
            }
        });
        
        return viagensDTO;
    }
    
     public List<RelatorioDiariaSalarioColaboradoresDTO> relatorioDiariaSalarioColaboradores (String mes, String ano) throws AcessoDadosException{
          return ((ViagemDAO) dao).relatorioDiariaSalarioColaboradores(mes, ano);
     }
     
     



}