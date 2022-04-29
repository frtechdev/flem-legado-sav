/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.flem.sav.dto;

import java.util.Date;

/**
 *
 * @author ilfernandes
 */
public class RelatorioAPPDTO {

    private String codigoViajante;
    private Integer idViagem;
    private String nomeViajante;
    private Date dataSaida;
    private Date dataRetorno;
    private String fonteRecurso;
    private Double valorDiarias;
    private Double adiantamento;
    private Double valorTotal;
    private String processo;

    public Double getAdiantamento() {
        return adiantamento;
    }

    public void setAdiantamento(Double adiantamento) {
        this.adiantamento = adiantamento;
    }

    public Date getDataRetorno() {
        return dataRetorno;
    }

    public void setDataRetorno(Date dataRetorno) {
        this.dataRetorno = dataRetorno;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
    }

    public String getFonteRecurso() {
        return fonteRecurso;
    }

    public void setFonteRecurso(String fonteRecurso) {
        this.fonteRecurso = fonteRecurso;
    }

    public Integer getIdViagem() {
        return idViagem;
    }

    public void setIdViagem(Integer idViagem) {
        this.idViagem = idViagem;
    }

    public String getNomeViajante() {
        return nomeViajante;
    }

    public void setNomeViajante(String nomeViajante) {
        this.nomeViajante = nomeViajante;
    }

    public Double getValorDiarias() {
        return valorDiarias;
    }

    public void setValorDiarias(Double valorDiarias) {
        this.valorDiarias = valorDiarias;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getProcesso() {
        return processo;
    }

    public void setProcesso(String processo) {
        this.processo = processo;
    }

    public String getCodigoViajante() {
        return codigoViajante;
    }

    public void setCodigoViajante(String codigoViajante) {
        this.codigoViajante = codigoViajante;
    }
    
}
