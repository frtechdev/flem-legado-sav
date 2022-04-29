/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.flem.sav.dto;

/**
 *
 * @author AJLima
 */
public class RelatorioDiariaSalarioColaboradoresDTO {
    
    
    private String matricula;
    private String nome;
    private String ano;
    private String mes;
    private String vlTotal;
    private String vlRecebido;
    private String bSalario;
    private String percentualDiariaSalario;

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getVlTotal() {
        return vlTotal;
    }

    public void setVlTotal(String vlTotal) {
        this.vlTotal = vlTotal;
    }

    public String getVlRecebido() {
        return vlRecebido;
    }

    public void setVlRecebido(String vlRecebido) {
        this.vlRecebido = vlRecebido;
    }

    public String getbSalario() {
        return bSalario;
    }

    public void setbSalario(String bSalario) {
        this.bSalario = bSalario;
    }

    public String getPercentualDiariaSalario() {
        return percentualDiariaSalario;
    }

    public void setPercentualDiariaSalario(String percentualDiariaSalario) {
        this.percentualDiariaSalario = percentualDiariaSalario;
    }

    
    
}
