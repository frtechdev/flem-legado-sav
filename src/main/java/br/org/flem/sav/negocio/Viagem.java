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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="viagem")
@Inheritance(strategy = InheritanceType.JOINED)
public class Viagem extends BaseDTOAb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_viagem")
    private Integer id;
    
    private Integer matriculaUsuarioSolicitante;
    private Integer codigoDominioUsuarioSolicitante;
    
    
    
    private Integer matriculaUsuarioViajante;
    private Integer codigoDominioUsuarioViajante;

    private String codigoConsultorViajante;

    private String codigoDepartamentoViajante;
    private Integer codigoDominioDepartamentoViajante;

    private Integer codigoCargoViajante;
    private Integer codigoDominioCargoViajante;

    private String codigoCentroCusto;
    private String codigoCentroResponsabilidade;
    private String codigoFonteRecurso;

    private Double diaria;
    private Double diariaP;

    private Double qtDiaria;

    private String descricaoE;

    private String processo;

    private String anoProcesso;

    @OneToMany(mappedBy = "viagem", cascade=CascadeType.REMOVE)
    private List<LiberacaoViagem> liberacoesViagem;
    
    @Enumerated
    private Natureza natureza;

    @Column(length=3000)
    private String descricao;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSaidaPrevista;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRetornoPrevista;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSolicitacao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPagamento;

    @OneToOne(cascade=CascadeType.REMOVE)
    private Reserva reserva;
    private Double valorAdiantamento;
    
    @OneToOne(cascade=CascadeType.REMOVE)
    private Itinerario itinerario;

    @Enumerated
    private DestinoViagem destino;
    
    @Enumerated
    private TipoDiaria tipoDiaria;
    
    @Enumerated
    private StatusViagem statusViagem;

    @Enumerated(EnumType.ORDINAL)
    private SituacaoDataPagamentoEnum SituacaoDataPagamento;
    
    @OneToOne
    private PrestacaoContas prestacaoContas;

    private String banco, agencia, conta;

    @OneToMany(mappedBy = "viagem", cascade=CascadeType.REMOVE)
    private List<Trecho> trechos;

    private Double totalDiarias;
    private Double totalAdiantamento;

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }


    public Double getQtDiaria() {
        return qtDiaria;
    }

    public void setQtDiaria(Double qtDiaria) {
        this.qtDiaria = qtDiaria;
    }


    public Double getDiaria() {
        return diaria;
    }

    public void setDiaria(Double diaria) {
        this.diaria = diaria;
    }

    public Double getDiariaP() {
        return diariaP;
    }

    public void setDiariaP(Double diariaP) {
        this.diariaP = diariaP;
    }

    public Integer getMatriculaHRUsuarioSolicitante() {
        return matriculaUsuarioSolicitante;
    }

    public void setMatriculaHRUsuarioSolicitante(Integer matriculaUsuarioSolicitante) {
        this.matriculaUsuarioSolicitante = matriculaUsuarioSolicitante;
    }

    public Integer getCodigoDominioUsuarioSolicitante() {
        return codigoDominioUsuarioSolicitante;
    }

    public void setCodigoDominioUsuarioSolicitante(Integer codigoDominioUsuarioSolicitante) {
        this.codigoDominioUsuarioSolicitante = codigoDominioUsuarioSolicitante;
    }

    public Integer getMatriculaHRUsuarioViajante() {
        return matriculaUsuarioViajante;
    }

    public void setMatriculaHRUsuarioViajante(Integer matriculaUsuarioViajante) {
        this.matriculaUsuarioViajante = matriculaUsuarioViajante;
    }

    public Integer getCodigoDominioUsuarioViajante() {
        return codigoDominioUsuarioViajante;
    }

    public void setCodigoDominioUsuarioViajante(Integer codigoDominioUsuarioViajante) {
        this.codigoDominioUsuarioViajante = codigoDominioUsuarioViajante;
    }

    public Natureza getNatureza() {
        return natureza;
    }

    public void setNatureza(Natureza natureza) {
        this.natureza = natureza;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataSaidaPrevista() {
        return dataSaidaPrevista;
    }

    public void setDataSaidaPrevista(Date dataSaidaPrevista) {
        this.dataSaidaPrevista = dataSaidaPrevista;
    }

    public Date getDataRetornoPrevista() {
        return dataRetornoPrevista;
    }

    public void setDataRetornoPrevista(Date dataRetornoPrevista) {
        this.dataRetornoPrevista = dataRetornoPrevista;
    }

    public Date getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(Date dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public Double getValorAdiantamento() {
        return valorAdiantamento;
    }

    public void setValorAdiantamento(Double valorAdiantamento) {
        this.valorAdiantamento = valorAdiantamento;
    }

    public TipoDiaria getTipoDiaria() {
        return tipoDiaria;
    }

    public void setTipoDiaria(TipoDiaria tipoDiaria) {
        this.tipoDiaria = tipoDiaria;
    }

    public StatusViagem getStatusViagem() {
        return statusViagem;
    }

    public void setStatusViagem(StatusViagem statusViagem) {
        this.statusViagem = statusViagem;
    }

    public PrestacaoContas getPrestacaoContas() {
        return prestacaoContas;
    }

    public void setPrestacaoContas(PrestacaoContas prestacaoContas) {
        this.prestacaoContas = prestacaoContas;
    }

    public Itinerario getItinerario() {
        return itinerario;
    }

    public void setItinerario(Itinerario itinerario) {
        this.itinerario = itinerario;
    }

    @Override
    public Serializable getPk() {
        return getId();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoCentroCusto() {
        return codigoCentroCusto;
    }

    public void setCodigoCentroCusto(String codigoCentroCusto) {
        this.codigoCentroCusto = codigoCentroCusto;
    }

    public String getCodigoCentroResponsabilidade() {
        return codigoCentroResponsabilidade;
    }

    public void setCodigoCentroResponsabilidade(String codigoCentroResponsabilidade) {
        this.codigoCentroResponsabilidade = codigoCentroResponsabilidade;
    }

    public Integer getCodigoCargoHRViajante() {
        return codigoCargoViajante;
    }

    public void setCodigoCargoHRViajante(Integer codigoCargoViajante) {
        this.codigoCargoViajante = codigoCargoViajante;
    }

    public Integer getCodigoDominioCargoViajante() {
        return codigoDominioCargoViajante;
    }

    public void setCodigoDominioCargoViajante(Integer codigoDominioCargoViajante) {
        this.codigoDominioCargoViajante = codigoDominioCargoViajante;
    }

    public String getCodigoConsultorViajante() {
        return codigoConsultorViajante;
    }

    public void setCodigoConsultorViajante(String codigoConsultorViajante) {
        this.codigoConsultorViajante = codigoConsultorViajante;
    }

    public String getCodigoDepartamentoHRViajante() {
        return codigoDepartamentoViajante;
    }

    public void setCodigoDepartamentoHRViajante(String codigoDepartamentoViajante) {
        this.codigoDepartamentoViajante = codigoDepartamentoViajante;
    }

    public Integer getCodigoDominioDepartamentoViajante() {
        return codigoDominioDepartamentoViajante;
    }

    public void setCodigoDominioDepartamentoViajante(Integer codigoDominioDepartamentoViajante) {
        this.codigoDominioDepartamentoViajante = codigoDominioDepartamentoViajante;
    }

    public String getCodigoFonteRecurso() {
        return codigoFonteRecurso;
    }

    public void setCodigoFonteRecurso(String codigoFonteRecurso) {
        this.codigoFonteRecurso = codigoFonteRecurso;
    }

    public List<LiberacaoViagem> getLiberacoesViagem() {
        return liberacoesViagem;
    }

    public void setLiberacoesViagem(List<LiberacaoViagem> liberacoesViagem) {
        this.liberacoesViagem = liberacoesViagem;
    }

    public String getDescricaoE() {
        return descricaoE;
    }

    public void setDescricaoE(String descricaoE) {
        this.descricaoE = descricaoE;
    }

    public DestinoViagem getDestino() {
        return destino;
    }

    public void setDestino(DestinoViagem destino) {
        this.destino = destino;
    }

    public String getProcesso() {
        return processo;
    }

    public void setProcesso(String processo) {
        this.processo = processo;
    }

    public String getAnoProcesso() {
        return anoProcesso;
    }

    public void setAnoProcesso(String anoProcesso) {
        this.anoProcesso = anoProcesso;
    }

    public List<Trecho> getTrechos() {
        return trechos;
    }

    public void setTrechos(List<Trecho> trechos) {
        this.trechos = trechos;
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

    public SituacaoDataPagamentoEnum getSituacaoDataPagamento() {
        return SituacaoDataPagamento;
    }

    public void setSituacaoDataPagamento(SituacaoDataPagamentoEnum SituacaoDataPagamento) {
        this.SituacaoDataPagamento = SituacaoDataPagamento;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

}
 
