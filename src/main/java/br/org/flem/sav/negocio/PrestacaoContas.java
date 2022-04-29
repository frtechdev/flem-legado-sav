package br.org.flem.sav.negocio; 

import br.org.flem.fwe.hibernate.dto.base.BaseDTOAb;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="prestacaocontas")
public class PrestacaoContas extends BaseDTOAb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestacaocontas")
    private Integer id;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSaidaEfetiva;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date DataRetornoEfetiva;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPrestacao;

    private Integer matriculaUsuarioPrestacao;
    
    private Integer codigoDominioUsuarioPrestacao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPagamento;
    
    @OneToOne(cascade=CascadeType.REMOVE)
    private Itinerario itinerario;
    
    @OneToMany(mappedBy="prestacaoContas", cascade=CascadeType.REMOVE)
    private List<Gasto> gastos;

    @Enumerated(EnumType.ORDINAL)
    private SituacaoDataPagamentoEnum situacaoDataPagamento;

    private Integer quantidadeDocumentos;

    private Double diariaEfetiva;

    private Double qtDiariaEfetiva;

    private String descricaoE;

    private Double totalDiarias;
    private Double totalAdiantamento;

    @Enumerated
    private TipoDiaria tipoDiaria;

    private StatusPrestacaoContas statusPrestacaoContas;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFinalizacao;

    @Column(length=500)
    private String observacao;

    @OneToMany(mappedBy = "prestacaoContas", cascade=CascadeType.REMOVE)
    private List<TrechoEfetivo> trechosEfetivo;

    public Double getQtDiariaEfetiva() {
        return qtDiariaEfetiva;
    }

    public void setQtDiariaEfetiva(Double qtDiaria) {
        this.qtDiariaEfetiva = qtDiaria;
    }


    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }


    public Date getDataSaidaEfetiva() {
        return dataSaidaEfetiva;
    }

    public void setDataSaidaEfetiva(Date dataSaidaEfetiva) {
        this.dataSaidaEfetiva = dataSaidaEfetiva;
    }

    public Date getDataRetornoEfetiva() {
        return DataRetornoEfetiva;
    }

    public void setDataRetornoEfetiva(Date DataRetornoEfetiva) {
        this.DataRetornoEfetiva = DataRetornoEfetiva;
    }

    public Date getDataPrestacao() {
        return dataPrestacao;
    }

    public void setDataPrestacao(Date dataSolicitacao) {
        this.dataPrestacao = dataSolicitacao;
    }

    public List<Gasto> getGastos() {
        return gastos;
    }

    public void setGastos(List<Gasto> gastos) {
        this.gastos = gastos;
    }

    public Integer getQuantidadeDocumentos() {
        return quantidadeDocumentos;
    }

    public void setQuantidadeDocumentos(Integer quantidadeDocumentos) {
        this.quantidadeDocumentos = quantidadeDocumentos;
    }

    public StatusPrestacaoContas getStatusPrestacaoContas() {
        return statusPrestacaoContas;
    }

    public void setStatusPrestacaoContas(StatusPrestacaoContas statusPrestacaoContas) {
        this.statusPrestacaoContas = statusPrestacaoContas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Itinerario getItinerario() {
        return itinerario;
    }

    public void setItinerario(Itinerario itinerario) {
        this.itinerario = itinerario;
    }

    public List<TrechoEfetivo> getTrechosEfetivo() {
        return trechosEfetivo;
    }

    public void setTrechosEfetivo(List<TrechoEfetivo> trechosEfetivo) {
        this.trechosEfetivo = trechosEfetivo;
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

    public TipoDiaria getTipoDiaria() {
        return tipoDiaria;
    }

    public void setTipoDiaria(TipoDiaria tipoDiaria) {
        this.tipoDiaria = tipoDiaria;
    }

    public Double getDiariaEfetiva() {
        return diariaEfetiva;
    }

    public void setDiariaEfetiva(Double diariaEfetiva) {
        this.diariaEfetiva = diariaEfetiva;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public SituacaoDataPagamentoEnum getSituacaoDataPagamento() {
        return situacaoDataPagamento;
    }

    public void setSituacaoDataPagamento(SituacaoDataPagamentoEnum situacaoDataPagamento) {
        this.situacaoDataPagamento = situacaoDataPagamento;
    }

    public String getDescricaoE() {
        return descricaoE;
    }

    public void setDescricaoE(String descricaoE) {
        this.descricaoE = descricaoE;
    }

    public Date getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setDataFinalizacao(Date dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

    @Override
    public Serializable getPk() {
        return getId();
    }

    public Integer getMatriculaHRUsuarioPrestacao() {
        return matriculaUsuarioPrestacao;
    }

    public void setMatriculaHRUsuarioPrestacao(Integer matriculaUsuarioPrestacao) {
        this.matriculaUsuarioPrestacao = matriculaUsuarioPrestacao;
    }

    public Integer getCodigoDominioUsuarioPrestacao() {
        return codigoDominioUsuarioPrestacao;
    }

    public void setCodigoDominioUsuarioPrestacao(Integer codigoDominioUsuarioPrestacao) {
        this.codigoDominioUsuarioPrestacao = codigoDominioUsuarioPrestacao;
    }

}