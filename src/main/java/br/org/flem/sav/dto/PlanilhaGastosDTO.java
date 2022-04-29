/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.flem.sav.dto;

/**
 *
 * @author ilfernandes
 */
public class PlanilhaGastosDTO {

    private String codigoViajante;
    private String nomeViajante;
    private String tipoGasto;
    private Double valor;

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

    public String getTipoGasto() {
        return tipoGasto;
    }

    public void setTipoGasto(String tipoGasto) {
        this.tipoGasto = tipoGasto;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
