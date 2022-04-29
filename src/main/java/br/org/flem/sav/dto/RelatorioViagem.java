package br.org.flem.sav.dto; 

import br.org.flem.sav.negocio.Viagem;

/**
 *
 * @author mccosta
 */
public class RelatorioViagem {
    Viagem viagem = null;
    String centroCusto = "não informado";
    String centroResponsabilidade = "";
    String nomeSolicitante = "";
    String nomeViajante = "";
    String cargoViajante = "";
    String departamentoViajante = "";
    String cpfViajante = "";
    String telViajante = "";
    String bancoViajante = "";
    String agViajante = "";
    String ccViajante = "";
    String dvAgenciaViajante = "";
    String dvContaViajante = "";
    String natureza = "";
    String dataSaidaPrevista = "";
    String horaSaidaPrevista = "";
    String dataRetornoPrevista = "";
    String horaRetornoPrevista = "";
    String temReserva = "";
    String nomeCompanhia = "";
    String codigoReserva = "";
    String descricaoServico = "";
    Double diariaPadrao = 0.0;
    Double diariaEspecial = 0.0;
    Double totalDiariaPadrao = 0.0;
    Double totalDiariaEfetivo = 0.0;
    Double totalDiariaSaldo = 0.0;
    Double totalSaldo = 0.0;
    Double totalPrevisto = 0.0;
    Double totalEfetivo = 0.0;
    Double adiantamentoSaldo = 0.0;
    Double qtdDiarias = 0.0;
    String tipoDiaria = "";//(PADRAO,ESPECIAL,DETALHADA)
    String obsDiariaEspecial = "";
    Double valorAdiantamento = 0.0;
    Double totalDiariaAdiantamento = 0.0;
    String dataPrestacao = "";
    String dataEfetivo = "";
    Double qtdDiariasEfetivo = 0.0;
    String dataSolicitacao = "";

    Double passagem = 0.0;
    Double translado = 0.0;
    Double transporte = 0.0;
    Double telefone = 0.0;
    Integer qtdDoc = 0;
    String textoResumo = "";



    public String getAgViajante() {
        return agViajante;
    }

    public void setAgViajante(String agViajante) {
        this.agViajante = agViajante;
    }

    public String getBancoViajante() {
        return bancoViajante;
    }

    public void setBancoViajante(String bancoViajante) {
        this.bancoViajante = bancoViajante;
    }

    public String getCargoViajante() {
        return cargoViajante;
    }

    public void setCargoViajante(String cargoViajante) {
        this.cargoViajante = cargoViajante;
    }

    public String getCcViajante() {
        return ccViajante;
    }

    public void setCcViajante(String ccViajante) {
        this.ccViajante = ccViajante;
    }

    public String getDvAgenciaViajante() {
        return dvAgenciaViajante;
    }
    
    public void setDvAgenciaViajante(String dvAgenciaViajante) {
        this.dvAgenciaViajante = dvAgenciaViajante;
    }

    public String getDvContaViajante() {
        return dvContaViajante;
    }
    
    public void setDvContaViajante(String dvContaViajante) {
        this.dvContaViajante = dvContaViajante;
    }

    public String getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(String centroCusto) {
        this.centroCusto = centroCusto;
    }

    public String getCentroResponsabilidade() {
        return centroResponsabilidade;
    }

    public void setCentroResponsabilidade(String centroResponsabilidade) {
        this.centroResponsabilidade = centroResponsabilidade;
    }

    public String getTemReserva() {
        return temReserva;
    }

    public void setTemReserva(String temReserva) {
        this.temReserva = temReserva;
    }

    public String getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public String getCpfViajante() {
        return cpfViajante;
    }

    public void setCpfViajante(String cpfViajante) {
        this.cpfViajante = cpfViajante;
    }

    public String getDataPrestacao() {
        return dataPrestacao;
    }

    public void setDataPrestacao(String dataPrestacao) {
        this.dataPrestacao = dataPrestacao;
    }

    public String getDataEfetivo() {
        return dataEfetivo;
    }

    public void setDataEfetivo(String dataEfetivo) {
        this.dataEfetivo = dataEfetivo;
    }

    public String getDataRetornoPrevista() {
        return dataRetornoPrevista;
    }

    public void setDataRetornoPrevista(String dataRetornoPrevista) {
        this.dataRetornoPrevista = dataRetornoPrevista;
    }

    public String getDataSaidaPrevista() {
        return dataSaidaPrevista;
    }

    public void setDataSaidaPrevista(String dataSaidaPrevista) {
        this.dataSaidaPrevista = dataSaidaPrevista;
    }

    public String getDepartamentoViajante() {
        return departamentoViajante;
    }

    public void setDepartamentoViajante(String departamentoViajante) {
        this.departamentoViajante = departamentoViajante;
    }

    public String getDescricaoServico() {
        return descricaoServico;
    }

    public void setDescricaoServico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }

    public Double getDiariaEspecial() {
        return diariaEspecial;
    }

    public void setDiariaEspecial(Double diariaEspecial) {
        this.diariaEspecial = diariaEspecial;
    }

    public Double getDiariaPadrao() {
        return diariaPadrao;
    }

    public void setDiariaPadrao(Double diariaPadrao) {
        this.diariaPadrao = diariaPadrao;
    }

    public String getHoraRetornoPrevista() {
        return horaRetornoPrevista;
    }

    public void setHoraRetornoPrevista(String horaRetornoPrevista) {
        this.horaRetornoPrevista = horaRetornoPrevista;
    }

    public String getHoraSaidaPrevista() {
        return horaSaidaPrevista;
    }

    public void setHoraSaidaPrevista(String horaSaidaPrevista) {
        this.horaSaidaPrevista = horaSaidaPrevista;
    }

    public String getNatureza() {
        return natureza;
    }

    public void setNatureza(String natureza) {
        this.natureza = natureza;
    }

    public String getNomeCompanhia() {
        return nomeCompanhia;
    }

    public void setNomeCompanhia(String nomeCompanhia) {
        this.nomeCompanhia = nomeCompanhia;
    }

    public String getNomeSolicitante() {
        return nomeSolicitante;
    }

    public void setNomeSolicitante(String nomeSolicitante) {
        this.nomeSolicitante = nomeSolicitante;
    }

    public String getNomeViajante() {
        return nomeViajante;
    }

    public void setNomeViajante(String nomeViajante) {
        this.nomeViajante = nomeViajante;
    }

    public String getObsDiariaEspecial() {
        return obsDiariaEspecial;
    }

    public void setObsDiariaEspecial(String obsDiariaEspecial) {
        this.obsDiariaEspecial = obsDiariaEspecial;
    }

    public Double getQtdDiarias() {
        return qtdDiarias;
    }

    public void setQtdDiarias(Double qtdDiarias) {
        this.qtdDiarias = qtdDiarias;
    }

    public Double getQtdDiariasEfetivo() {
        return qtdDiariasEfetivo;
    }

    public void setQtdDiariasEfetivo(Double qtdDiariasEfetivo) {
        this.qtdDiariasEfetivo = qtdDiariasEfetivo;
    }

    public String getTelViajante() {
        return telViajante;
    }

    public void setTelViajante(String telViajante) {
        this.telViajante = telViajante;
    }

    public String getTipoDiaria() {
        return tipoDiaria;
    }

    public void setTipoDiaria(String tipoDiaria) {
        this.tipoDiaria = tipoDiaria;
    }

    public Double getTotalDiariaAdiantamento() {
        return totalDiariaAdiantamento;
    }

    public void setTotalDiariaAdiantamento(Double totalDiariaAdiantamento) {
        this.totalDiariaAdiantamento = totalDiariaAdiantamento;
    }

    public Double getTotalDiariaEfetivo() {
        return totalDiariaEfetivo;
    }

    public void setTotalDiariaEfetivo(Double totalDiariaEfetivo) {
        this.totalDiariaEfetivo = totalDiariaEfetivo;
    }

    public Double getTotalDiariaPadrao() {
        return totalDiariaPadrao;
    }

    public void setTotalDiariaPadrao(Double totalDiariaPadrao) {
        this.totalDiariaPadrao = totalDiariaPadrao;
    }

    public Double getValorAdiantamento() {
        return valorAdiantamento;
    }

    public void setValorAdiantamento(Double valorAdiantamento) {
        this.valorAdiantamento = valorAdiantamento;
    }

    public Viagem getViagem() {
        return viagem;
    }

    public void setViagem(Viagem viagem) {
        this.viagem = viagem;
    }

    public String getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(String dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    /*public String getDataRetornoEfetiva() {
        return dataRetornoEfetiva;
    }

    public void setDataRetornoEfetiva(String dataRetornoEfetiva) {
        this.dataRetornoEfetiva = dataRetornoEfetiva;
    }

    public String getDataSaidaEfetiva() {
        return dataSaidaEfetiva;
    }

    public void setDataSaidaEfetiva(String dataSaidaEfetiva) {
        this.dataSaidaEfetiva = dataSaidaEfetiva;
    }

    public String getHoraRetornoEfetiva() {
        return horaRetornoEfetiva;
    }

    public void setHoraRetornoEfetiva(String horaRetornoEfetiva) {
        this.horaRetornoEfetiva = horaRetornoEfetiva;
    }

    public String getHoraSaidaEfetiva() {
        return horaSaidaEfetiva;
    }

    public void setHoraSaidaEfetiva(String horaSaidaEfetiva) {
        this.horaSaidaEfetiva = horaSaidaEfetiva;
    }*/

    public Double getPassagem() {
        return passagem;
    }

    public void setPassagem(Double passagem) {
        this.passagem = passagem;
    }

    public Integer getQtdDoc() {
        return qtdDoc;
    }

    public void setQtdDoc(Integer qtdDoc) {
        this.qtdDoc = qtdDoc;
    }

    public Double getTelefone() {
        return telefone;
    }

    public void setTelefone(Double telefone) {
        this.telefone = telefone;
    }

    public String getTextoResumo() {
        return textoResumo;
    }

    public void setTextoResumo(String textoResumo) {
        this.textoResumo = textoResumo;
    }

    public Double getTranslado() {
        return translado;
    }

    public void setTranslado(Double translado) {
        this.translado = translado;
    }

    public Double getTransporte() {
        return transporte;
    }

    public void setTransporte(Double transporte) {
        this.transporte = transporte;
    }

    public Double getTotalDiariaSaldo() {
        return totalDiariaSaldo;
    }

    public void setTotalDiariaSaldo(Double totalDiariaSaldo) {
        this.totalDiariaSaldo = totalDiariaSaldo;
    }

    public Double getTotalSaldo() {
        return totalSaldo;
    }

    public void setTotalSaldo(Double totalSaldo) {
        this.totalSaldo = totalSaldo;
    }

    public Double getAdiantamentoSaldo() {
        return adiantamentoSaldo;
    }

    public void setAdiantamentoSaldo(Double adiantamentoSaldo) {
        this.adiantamentoSaldo = adiantamentoSaldo;
    }

    public Double getTotalEfetivo() {
        return totalEfetivo;
    }

    public void setTotalEfetivo(Double totalEfetivo) {
        this.totalEfetivo = totalEfetivo;
    }

    public Double getTotalPrevisto() {
        return totalPrevisto;
    }

    public void setTotalPrevisto(Double totalPrevisto) {
        this.totalPrevisto = totalPrevisto;
    }
}
