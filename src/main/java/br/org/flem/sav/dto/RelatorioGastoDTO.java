/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.flem.sav.dto;

import br.org.flem.sav.negocio.Gasto;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author ilfernandes
 */
public class RelatorioGastoDTO {

    private String codigoViajante;
    private String nomeViajante;
    private Double totalDiarias = 0d;
    private Double totalAdiantamento = 0d;
    private Collection<Gasto> gastos = new ArrayList<Gasto>();
    private Integer qtdViagens = 0;
    private Double devolucao = 0d;
    private Double totalAdiantamentoEfetivo = 0d;
    private Double totalDiariasDiferencial = 0d;//guardará a diferença entre a diária solicitada e a efetiva, caso haja diferenças a contabilizar.

    public String getCodigoViajante() {
        return codigoViajante;
    }

    public void setCodigoViajante(String codigoViajante) {
        this.codigoViajante = codigoViajante;
    }

    public String getNomeViajante() {
        return nomeViajante;
    }

    public void setNomeViajante(String nomeViajante) {
        this.nomeViajante = nomeViajante;
    }
    public Double getTotalAdiantamento() {
        return totalAdiantamento;
    }

    public void setTotalAdiantamento(Double totalAdiantamento) {
        this.totalAdiantamento = totalAdiantamento;
    }

    public Double getTotalDiarias() {
        return totalDiarias;
    }

    public void setTotalDiarias(Double totalDiarias) {
        this.totalDiarias = totalDiarias;
    }
    public Collection<Gasto> getGastos() {
        return gastos;
    }

    public void setGastos(Collection<Gasto> gastos) {
        this.gastos = gastos;
    }

    public Integer getQtdViagens() {
        return qtdViagens;
    }

    public void setQtdViagens(Integer qtdViagens) {
        this.qtdViagens = qtdViagens;
    }


     public void incQtdViagens() {
        qtdViagens++;
    }

    public Double getDevolucao() {
        return devolucao;
    }

    public void setDevolucao(Double devolucao) {
        this.devolucao = devolucao;
    }

    public Double getTotalAdiantamentoEfetivo() {
        return totalAdiantamentoEfetivo;
    }

    public void setTotalAdiantamentoEfetivo(Double totalAdiantamentoEfetivo) {
        this.totalAdiantamentoEfetivo = totalAdiantamentoEfetivo;
    }

    public Double getTotalDiariasDiferencial() {
        return totalDiariasDiferencial;
    }

    public void setTotalDiariasDiferencial(Double totalDiariasDiferencial) {
        this.totalDiariasDiferencial = totalDiariasDiferencial;
    }
}

