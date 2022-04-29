/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.flem.sav.dto;

import br.org.flem.sav.negocio.ItemItinerario;
import java.util.List;

/**
 *
 * @author ilfernandes
 */
public class FuncionarioDiariaDTO {

    private String nomeFuncionario;
    private double valorTotalDiarias;
    private double percentual;
    private Integer anoMesRef;
    private Integer idViagem;
    private double valorDiaria;
    private Integer situacao;
    private String statusViagem;
    private String codProcesso;
    private String periodo;
    private String cargo;
    List<ItemItinerario> itensItinerario;
    


    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public double getPercentual() {
        return percentual;
    }

    public void setPercentual(double percentual) {
        this.percentual = percentual;
    }

    public double getValorTotalDiarias() {
        return valorTotalDiarias;
    }

    public void setValorTotalDiarias(double valorTotalDiarias) {
        this.valorTotalDiarias = valorTotalDiarias;
    }

    public Integer getAnoMesRef() {
        return anoMesRef;
    }

    public void setAnoMesRef(Integer anoMesRef) {
        this.anoMesRef = anoMesRef;
    }

    public Integer getIdViagem() {
        return idViagem;
    }

    public void setIdViagem(Integer idViagem) {
        this.idViagem = idViagem;
    }

    public double getValorDiaria() {
        return valorDiaria;
    }

    public void setValorDiaria(double valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    public String getCodProcesso() {
        return codProcesso;
    }

    public void setCodProcesso(String codProcesso) {
        this.codProcesso = codProcesso;
    }

    public Integer getSituacao() {
        return situacao;
    }

    public void setSituacao(Integer situacao) {
        this.situacao = situacao;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public List<ItemItinerario> getItensItinerario() {
        return itensItinerario;
    }

    public void setItensItinerario(List<ItemItinerario> itensItinerario) {
        this.itensItinerario = itensItinerario;
    }

    public String getStatusViagem() {
        return statusViagem;
    }

    public void setStatusViagem(String statusViagem) {
        this.statusViagem = statusViagem;
    }
    
    @Override
    public String toString() {
        return "[ "+ this.nomeFuncionario +"-"+ this.getValorTotalDiarias()+ "-" + this.percentual +" ]";
    }

}
