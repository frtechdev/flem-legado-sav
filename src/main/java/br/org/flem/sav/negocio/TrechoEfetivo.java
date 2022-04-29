package br.org.flem.sav.negocio; 

import br.org.flem.fwe.hibernate.dto.base.BaseDTOAb;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author mccosta
 */
@Entity
@Table(name = "trechoefetivo")
public class TrechoEfetivo extends BaseDTOAb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trechoefetivo")
    private Integer id;


    @ManyToOne
    @JoinColumn(name="id_prestacaocontas")
    private PrestacaoContas prestacaoContas;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInicio;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFim;

    private Double diaria;
    private Double qtDiaria;
    private String observacao;

    @Override
    public Serializable getPk() {
        return this.id;
    }

    public PrestacaoContas getPrestacaoContas() {
        return prestacaoContas;
    }

    public void setPrestacaoContas(PrestacaoContas prestacaoContas) {
        this.prestacaoContas = prestacaoContas;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Double getDiaria() {
        return diaria;
    }

    public void setDiaria(Double diaria) {
        this.diaria = diaria;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Double getQtDiaria() {
        return qtDiaria;
    }

    public void setQtDiaria(Double qtDiaria) {
        this.qtDiaria = qtDiaria;
    }
}