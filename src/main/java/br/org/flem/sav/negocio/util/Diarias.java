package br.org.flem.sav.negocio.util;

import br.org.flem.fw.persistencia.dto.SituacaoFuncionarioEnum;
import br.org.flem.fw.persistencia.dto.Usuario;
import br.org.flem.fw.service.IFuncionario;
import br.org.flem.fw.service.INivel;
import br.org.flem.fw.service.impl.RHServico;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fwe.util.Data;
import br.org.flem.sav.bo.PercentualDiariaBO;
import br.org.flem.sav.bo.UsuarioBO;
import br.org.flem.sav.bo.ViagemBO;
import br.org.flem.sav.dto.FuncionarioDiariaDTO;
import br.org.flem.sav.negocio.DestinoViagem;
import br.org.flem.sav.negocio.PercentualDiaria;
import br.org.flem.sav.negocio.TipoDiaria;
import br.org.flem.sav.negocio.Trecho;
import br.org.flem.sav.negocio.Viagem;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

/**
 *
 * @author JLGuenna
 */
public class Diarias {
    /*
    public static final String SVG = "SVG";
    public static final String PVG = "PVG";

    private Collection<Viagem> obterViagens(Date data, String matricula, String tipo) throws Exception {
    ViagemBO viagemBO = new ViagemBO();
    Collection<Integer> idsViagens = new CompromissoDAO().obterIdsViagensPagasNoMesAno(data, matricula, tipo);
    Collection<Viagem> viagens = viagemBO.obterViagensPorIds(idsViagens);
    return viagens;
    }*/

    private Double calcularTotalDiarias(Date data, Integer matricula) throws Exception {
        Double totalDiarias = 0d;
        ViagemBO viagemBO = new ViagemBO();
        for (Viagem viagem : viagemBO.obterViagensPorDataPagamentoViajante(data, matricula)/*this.obterViagens(data, GEMEntidadeUtil.completarComZerosCodigoFuncionario(codEntidade), SVG)*/) {
            //verifica se houve acrécimo de diária.
            totalDiarias += viagem.getTotalDiarias();
        }

        for (Viagem viagem : viagemBO.obterViagensComPrestacaoContasPorDataPagamentoViajante(data, matricula)/*this.obterViagens(data, GEMEntidadeUtil.completarComZerosCodigoFuncionario(codEntidade), PVG)*/) {
            if (viagem.getPrestacaoContas() != null && viagem.getTotalDiarias() < viagem.getPrestacaoContas().getTotalDiarias()) {
                totalDiarias += viagem.getPrestacaoContas().getTotalDiarias() - viagem.getTotalDiarias();
            }
        }
        return totalDiarias;
    }

    public Boolean verificar(Integer matriculaUsuario, Date dataSaidaPrevista, DestinoViagem destinoViagem, double qtdeDiariaViagem, double valorDiaria, Integer id_viagem, String tipoDiaria, double totalDiariasSomadasDouble) throws Exception {
        Boolean retorno = false;
        RHServico rh = new RHServico();
        ViagemBO viagemBO = new ViagemBO();
        IFuncionario Ifunc = rh.obterFuncionarioPorMatricula(matriculaUsuario);
        Date dataDePagamento = viagemBO.calcularDataPagamentoPrevisto(dataSaidaPrevista, destinoViagem);        
        Double totalDiarias = qtdeDiariaViagem * valorDiaria;
        Double salario50 = (rh.obterSalarioPorFuncionario(Ifunc) / 2);
        
        //System.out.println("\n\ndataDePagamento 01 ->>> " + dataDePagamento);
        
        //System.out.println("salario50 = " + salario50);
        
        if(tipoDiaria.equals("2")){
            totalDiarias = totalDiariasSomadasDouble;
        }
        
        //System.out.println("totalDiarias = " + totalDiarias);
        
        for (Viagem viagem : viagemBO.obterViagensPorDataPagamentoViajante(dataDePagamento, matriculaUsuario) /*this.obterViagens(dataDePagamento, GEMEntidadeUtil.completarComZerosCodigoFuncionario(matriculaUsuario.toString()), SVG)*/) {
            //System.out.println("for1 -> viagem = " + viagem.getiNivel());
            //Evita somar viagem atual
            if (viagem.getId().equals(id_viagem)) {
                continue;
            }
            //verifica se houve acrécimo de diária.
            totalDiarias += viagem.getTotalDiarias();
        }
        //System.out.println("totalDiarias2 = " + totalDiarias);
        //System.out.println("dataDePagamento 02 ->>> " + dataDePagamento);
        for (Viagem viagem : viagemBO.obterViagensComPrestacaoContasPorDataPagamentoViajante(dataDePagamento, matriculaUsuario) /*this.obterViagens(dataDePagamento, GEMEntidadeUtil.completarComZerosCodigoFuncionario(matriculaUsuario.toString()), PVG)*/) {
            //System.out.println("for2 -> viagem = " + viagem.getiNivel());
            if (viagem.getPrestacaoContas() != null && viagem.getTotalDiarias() < viagem.getPrestacaoContas().getTotalDiarias()) {
                //System.out.println("if 01");
                //System.out.println(viagem.getiNivel() + " == " + id_viagem);
                if (viagem.getId().equals(id_viagem)) {
                    //System.out.println("if 02");
                    //System.out.println("totalDiarias3 = " + totalDiarias);
                    totalDiarias += viagem.getPrestacaoContas().getTotalDiarias() - (qtdeDiariaViagem * valorDiaria);
                    //System.out.println("totalDiarias4 = " + totalDiarias);
                    continue;
                }
                //System.out.println("totalDiarias5 = " + totalDiarias);
                totalDiarias += viagem.getPrestacaoContas().getTotalDiarias() - viagem.getTotalDiarias();
                //System.out.println("totalDiarias6 = " + totalDiarias);
            }
        }
        
        //System.out.println(totalDiarias + " <= " + salario50);
        
        if (totalDiarias <= salario50) {
            retorno = true;
        }
        return retorno;
    }

    // verifica se alcançou os 50% do salário, mês corrente
    public Boolean verificar(IFuncionario Ifunc) throws Exception {
        Boolean retorno = false;
        if (this.calcularTotalDiarias(new Date(), Ifunc.getCodigoDominio()) <= (new RHServico().obterSalarioPorFuncionario(Ifunc) / 2)) {
            retorno = true;
        }
        return retorno;
    }

    public Boolean verificar(Date dataPagamento, IFuncionario funcionario) throws Exception {
        Boolean retorno = false;
        if (this.calcularTotalDiarias(dataPagamento, funcionario.getCodigoDominio()) <= (new RHServico().obterSalarioPorFuncionario(funcionario) / 2)) {
            retorno = true;
        }
        return retorno;
    }

    public Double calcularPorcentagem(IFuncionario Ifunc) throws Exception {
        return this.calcularTotalDiarias(new Date(), Ifunc.getCodigoDominio()) / new RHServico().obterSalarioPorFuncionario(Ifunc) * 100;
    }

    // Retorna porcentagem do salário relativa ao valor de uso de diárias de viagem
    public Double calcularPorcentagem(IFuncionario ifunc, INivel nivel, Date saida, Date retorno) throws Exception {
        return this.calcularTotalDiarias(saida, ifunc.getCodigoDominio()) / nivel.getSalario() * 100;
    }

    public Collection<FuncionarioDiariaDTO> calcularPorcentagem(Date dataFiltro, Collection<String> listaCodigos) throws Exception {
        RHServico rh = new RHServico();
        Map<Integer, IFuncionario> mapIFuncionarios = rh.obterMapTodos();
        Map<Integer, INivel> mapNiveis = rh.obterNiveis();
        Collection<FuncionarioDiariaDTO> funcionariosDiarias = new ArrayList<FuncionarioDiariaDTO>();
        if (listaCodigos == null || listaCodigos.isEmpty()) {
            return funcionariosDiarias;
        }
        ViagemBO viagemBO = new ViagemBO();
        Collection<Viagem> viagens = viagemBO.obterViagensPorDataPagamentoViajantes(dataFiltro, listaCodigos);
        Map<Integer, Double> totaisDiarias = new HashMap<Integer, Double>();
        for (Viagem viagem : viagens) {
            Integer matricula = viagem.getCodigoDominioUsuarioViajante();
            if (mapIFuncionarios.containsKey(matricula)) {
                if (!totaisDiarias.containsKey(matricula)) {
                    totaisDiarias.put(matricula, viagem.getTotalDiarias());
                } else {
                    double tmpTotalDiaria = totaisDiarias.get(matricula);
                    totaisDiarias.put(matricula, tmpTotalDiaria + viagem.getTotalDiarias());
                }
            }
        }
        for (Integer matricula : totaisDiarias.keySet()) {
            if (mapIFuncionarios.containsKey(matricula) && totaisDiarias.get(matricula) > 0d) {
                IFuncionario funcionario = mapIFuncionarios.get(matricula);
                FuncionarioDiariaDTO funcionarioDiariaDTO = new FuncionarioDiariaDTO();
                double salarioFuncionario = mapNiveis.get(funcionario.getNivel().getiNivel()).getSalario();
                double percentual = totaisDiarias.get(matricula) / salarioFuncionario * 100;
                funcionarioDiariaDTO.setNomeFuncionario(funcionario.getNome());
                funcionarioDiariaDTO.setValorTotalDiarias(totaisDiarias.get(matricula));
                funcionarioDiariaDTO.setPercentual(percentual);
                funcionariosDiarias.add(funcionarioDiariaDTO);
            }
        }
        return funcionariosDiarias;

    }
    
    public Collection<FuncionarioDiariaDTO> calcularPorcentagem(String dataSaida, String dataRetorno, Collection<String> listaCodigos, Integer situacaoFuncionario) throws Exception {
        RHServico rh = new RHServico();
        Map<Integer, IFuncionario> mapIFuncionarios = null;
        if(situacaoFuncionario != 2){
            mapIFuncionarios = rh.obterMapIdFuncionarioPorSituacao(SituacaoFuncionarioEnum.obterPorCodigoHR(situacaoFuncionario));
        }else{
            mapIFuncionarios = rh.obterMapTodos();
        }
        Map<Integer, INivel> mapNiveis = rh.obterNiveis();
        List<FuncionarioDiariaDTO> funcionariosDiarias = new ArrayList<FuncionarioDiariaDTO>();
        String matriculaMesRef = "";
        Integer matricula = 0;
        if (listaCodigos == null || listaCodigos.isEmpty()) {
            return funcionariosDiarias;
        }
        ViagemBO viagemBO = new ViagemBO();
        Collection<Viagem> viagens = viagemBO.obterViagensPorDataPagamentoViajantes(dataSaida, dataRetorno, listaCodigos);
        Map<Integer, Double> totaisDiarias = null;//new HashMap<String, Double>();
        
        Map<String, Map<Integer, Double>> totaisDiariasDetalhado = new HashMap<String, Map<Integer, Double>>();
        
        for (Viagem viagem : viagens) {
            matricula = viagem.getCodigoDominioUsuarioViajante();
            if (mapIFuncionarios.containsKey(matricula)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(viagem.getDataPagamento());
                matriculaMesRef = matricula+"-";
                if(calendar.get(Calendar.MONTH)+1 < 10){
                    matriculaMesRef += "0"+(calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR);
                }else{
                    matriculaMesRef += (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR);
                }
                
                if (!totaisDiariasDetalhado.containsKey(matriculaMesRef)) {
                    totaisDiarias = new LinkedHashMap<Integer, Double>();
                    if(viagem.getPrestacaoContas() != null && viagem.getPrestacaoContas().getTotalDiarias() != null){
                        totaisDiarias.put(viagem.getId(), viagem.getPrestacaoContas().getTotalDiarias());
                    }else{
                        if(viagem.getTotalDiarias() != null){
                            totaisDiarias.put(viagem.getId(), viagem.getTotalDiarias());
                        }else{
                            totaisDiarias.put(viagem.getId(), 0d);
                        }
                    }
                    totaisDiariasDetalhado.put(matriculaMesRef, totaisDiarias);
                } else {
                    totaisDiarias = totaisDiariasDetalhado.get(matriculaMesRef);
                    if(viagem.getPrestacaoContas() != null && viagem.getPrestacaoContas().getTotalDiarias() != null){
                        totaisDiarias.put(viagem.getId(), viagem.getPrestacaoContas().getTotalDiarias());
                    }else{
                        if(viagem.getTotalDiarias() != null){
                            totaisDiarias.put(viagem.getId(), viagem.getTotalDiarias());
                        }else{
                            totaisDiarias.put(viagem.getId(), 0d);
                        }
                    }
                }
            }
        }
        
        for (String matMesRef : totaisDiariasDetalhado.keySet()) {
            String[] obj = matMesRef.split("-");
            matricula = Integer.valueOf(obj[0]);
            //1 -> mes/anos ref
            
            if (mapIFuncionarios.containsKey(matricula)) {
                Integer idViagem = 0;
                double tmpTotalDiaria = 0d;
                double tmpDiaria = 0d;
                Viagem viagem = null;
                
                for (Map.Entry<Integer, Double> mapMesRef : totaisDiariasDetalhado.get(matMesRef).entrySet()) {
                    idViagem = mapMesRef.getKey();
                    tmpDiaria = mapMesRef.getValue();
                    tmpTotalDiaria = tmpTotalDiaria + tmpDiaria;
                    viagem = viagemBO.obterPorPk(idViagem);

                    IFuncionario funcionario = mapIFuncionarios.get(matricula);
                    FuncionarioDiariaDTO funcionarioDiariaDTO = new FuncionarioDiariaDTO();
                    double salarioFuncionario = mapNiveis.get(funcionario.getNivel().getiNivel()).getSalario();
                    double percentual = 0d;
                    if(tmpTotalDiaria > 0d) {
                        percentual = tmpTotalDiaria / salarioFuncionario * 100;
                    }
                    funcionarioDiariaDTO.setNomeFuncionario(funcionario.getNome().replace("  ", ""));
                    funcionarioDiariaDTO.setIdViagem(idViagem);
                    funcionarioDiariaDTO.setValorDiaria(tmpDiaria);
                    funcionarioDiariaDTO.setValorTotalDiarias(tmpTotalDiaria);
                    funcionarioDiariaDTO.setPercentual(percentual);
                    funcionarioDiariaDTO.setAnoMesRef(Integer.valueOf(obj[1].substring(3, 7) + obj[1].substring(0, 2)));
                    
                    /***********************/
                    int intSituacao = 0;
                    if (funcionario.getSituacao().getDescricao().equals("FUNCIONARIO ATIVO"))
                    {
                        intSituacao = 1;
                    }
                    /************************/
                    
                    funcionarioDiariaDTO.setSituacao(intSituacao);// 1-Ativo 0-Inativo
                    funcionarioDiariaDTO.setStatusViagem(viagem.getStatusViagem().name());
                    funcionarioDiariaDTO.setCodProcesso(viagem.getProcesso());
                    funcionarioDiariaDTO.setCargo(funcionario.getCargo().getNome().replace("  ", ""));
                    
                    if(viagem.getPrestacaoContas() != null){
                        if(viagem.getPrestacaoContas().getItinerario() != null){
                            funcionarioDiariaDTO.setItensItinerario(viagem.getPrestacaoContas().getItinerario().getItensItinerario());
                        }else{
                            //caso 1: viagem cancelada
                            funcionarioDiariaDTO.setItensItinerario(viagem.getItinerario().getItensItinerario());
                        }
                        funcionarioDiariaDTO.setPeriodo(Data.formataData(viagem.getPrestacaoContas().getDataSaidaEfetiva())+" a "+Data.formataData(viagem.getPrestacaoContas().getDataRetornoEfetiva()));
                    }else{
                        funcionarioDiariaDTO.setItensItinerario(viagem.getItinerario().getItensItinerario());
                        funcionarioDiariaDTO.setPeriodo(Data.formataData(viagem.getDataSaidaPrevista())+" a "+Data.formataData(viagem.getDataRetornoPrevista()));
                    }
                    
                    
                    funcionariosDiarias.add(funcionarioDiariaDTO);
                }
            }
        }
        
        //Ordena em ordem crescente de anoMesRef, nome e id da viagem
        Collections.sort(funcionariosDiarias, new Comparator<FuncionarioDiariaDTO>() {

            @Override
            public int compare(FuncionarioDiariaDTO o1, FuncionarioDiariaDTO o2) {
                //return o1.getAnoMesRef().compareTo(o2.getAnoMesRef());
                
                //Caso os anoMesRef's sejam diferentes, ordena pelo anoMesRef.
                int anoMesRef = o1.getAnoMesRef().compareTo(o2.getAnoMesRef());  
                if (anoMesRef != 0) return anoMesRef; 

                //Se forem iguais, ordenamos pelo nome.
                int nome = o1.getNomeFuncionario().compareTo(o2.getNomeFuncionario());  
                if (nome != 0) return nome;   

                //Se os nomes forem iguais, ordenamos pelo ID da viagem.
                return o1.getIdViagem().compareTo(o2.getIdViagem());  
            }
        });
        
        return funcionariosDiarias;

    }
    
    public Integer calcularQuantidadeDias(Date dataSaida, Date dataRetorno) {
        DateTime saida = new DateTime(dataSaida.getTime());
        DateTime retorno = new DateTime(dataRetorno.getTime());
        Integer dias = JodaTimeUtil.calculaQuantidadeDiasSemHoras(saida, retorno);
        //Conta o dia da saida
        if (!JodaTimeUtil.compararDiaMesAno(saida, retorno) && dias == 0) {
            dias++;
        }
        return dias;
    }

    /**
     *
     * @param dataSaida
     * @param dataRetorno
     * @return Quantidade de diária (double)
     * @throws AplicacaoException if dataSaida > dataRetorno
     */
    public Double calculaQuantidadeDiaria(Date dataSaida, Date dataRetorno, Integer matriculaFuncionario) throws AplicacaoException {
        Double qtdDiaria = 0d;
        Double qtdHoras;
        if (dataSaida.after(dataRetorno)) {
            throw new AplicacaoException("Data de retorno inferior a data de saida");
        }
        DateTime saida = new DateTime(dataSaida);
        DateTime retorno = new DateTime(dataRetorno);
        qtdDiaria = this.calcularQuantidadeDias(dataSaida, dataRetorno).doubleValue();
        qtdHoras = JodaTimeUtil.diferencaApenasHoras(saida, retorno);
        
        //OBTER O PERCENTUAL DE DIÁRIA DO VIAJANTE COM BASE EM SUA LOTAÇÃO
        Usuario usr = (Usuario) new UsuarioBO().obterPorCodigoDominio(matriculaFuncionario);
        PercentualDiariaBO percentualDiariaBO = new PercentualDiariaBO();
        PercentualDiaria percentualDiaria = percentualDiariaBO.obterPorDepartamentoDominio(usr.getLotacaoDominio());
        if(percentualDiaria == null){
            percentualDiaria = percentualDiariaBO.obterPorDepartamentoDominio(0);//"PADRAO"
        }

        qtdDiaria += percentualDiaria.getQuebraDiarias(qtdHoras);
        return qtdDiaria;
    }

    public void separarDiariaPorMesAno(Date dataSaida, Date dataRetorno, Double valorDiaria, Map<Integer, Double> map, Integer matriculaFuncionario) throws AplicacaoException {
        DateTime saida = new DateTime(dataSaida);
        DateTime retorno = new DateTime(dataRetorno);
        DateTime tmpRetorno = new DateTime();
        while (retorno.isAfter(saida)) {
            int indice = Integer.valueOf(saida.getMonthOfYear() + "" + saida.getYear());
            double tmpValorDiaria = 0d;
            if (map.get(indice) != null) {
                tmpValorDiaria = map.get(indice);
            }
            if (saida.getMonthOfYear() == retorno.getMonthOfYear()
                    && saida.getYear() == retorno.getYear()) {
                tmpRetorno = retorno;
                map.put(indice, tmpValorDiaria + (this.calculaQuantidadeDiaria(saida.toDate(), tmpRetorno.toDate(), matriculaFuncionario) * valorDiaria));
                saida = retorno;
            } else {
                tmpRetorno = saida.plusDays(1);
                map.put(indice, tmpValorDiaria + (this.calculaQuantidadeDiaria(saida.toDate(), tmpRetorno.toDate(), matriculaFuncionario) * valorDiaria));
            }
            saida = saida.plusDays(1);
        }
    }

    public Boolean verificar(IFuncionario funcionario, Date dataSaidaPrevista, Date dataRetornoPrevista, double valorDiaria, Integer idViagem, Map<Integer, Double> map) throws AplicacaoException {
        //TODO ALTERAR NOME DO METODO
        Boolean retorno = true;

        Collection<Viagem> viagens = new ViagemBO().obterTodasDoFuncionarioPorPeriodoMesAnoComDiarias(funcionario.getCodigoDominio(), JodaTimeUtil.obterPrimeiroDiaHoraDoMes(dataSaidaPrevista), JodaTimeUtil.obterUltimoDiaHoraDoMes(dataRetornoPrevista));
        Double metadeSalario = new RHServico().obterSalarioPorFuncionario(funcionario) / 2;

        if (map == null) {
            map = new HashMap<Integer, Double>();
        }

        //Faz o calculo do periodo selecionado na viagem que deseja salvar/editar
        this.separarDiariaPorMesAno(dataSaidaPrevista, dataRetornoPrevista, valorDiaria, map, funcionario.getCodigoDominio());
        
        //Acrescenta os valors contidos no banco.
        for (Viagem viagem : viagens) {
            //Em caso de edição pula para o próximo laço sem considerar a viagem.
            if (viagem.getId().equals(idViagem)) {
                continue;
            }
            if (viagem.getTipoDiaria() == TipoDiaria.DETALHADA) {
                for (Trecho trecho : viagem.getTrechos()) {
                    this.separarDiariaPorMesAno(trecho.getDataInicio(), trecho.getDataFim(), trecho.getDiaria(), map, funcionario.getCodigoDominio());
                }
            } else {
                this.separarDiariaPorMesAno(viagem.getDataSaidaPrevista(), viagem.getDataRetornoPrevista(), viagem.getDiaria(), map, funcionario.getCodigoDominio());
            }
        }

        for (double valorComparar : map.values()) {
            if (valorComparar > metadeSalario) {
                retorno = false;
                break;
            }
        }
        return retorno;
    }

}
