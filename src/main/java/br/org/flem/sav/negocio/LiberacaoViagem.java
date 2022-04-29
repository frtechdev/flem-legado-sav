package br.org.flem.sav.negocio; 

import br.org.flem.fwe.hibernate.dto.base.BaseDTOAb;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author mccosta
 */
@Entity
@Table(name="liberacaoviagem")
public class LiberacaoViagem extends BaseDTOAb {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_liberacaoviagem")
    private Integer id;

    private Integer matriculaUsuarioViajante;
    private Integer matriculaUsuarioSolicitante;
    
    private Integer codigoDominioUsuarioViajante;
    private Integer codigoDominioUsuarioSolicitante;

    private String codigoConsultorViajante;
    private Integer totalViagensLiberadas;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataLiberacao;
    private String descricao;
    private boolean valido = false;
    private TipoLiberacao tipoLiberacao = TipoLiberacao.PENDENCIA;

    @OneToOne
    private Viagem viagem;

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

    public Integer getMatriculaHRUsuarioSolicitante() {
        return matriculaUsuarioSolicitante;
    }

    public void setMatriculaHRUsuarioSolicitante(Integer matriculaUsuarioSolicitante) {
        this.matriculaUsuarioSolicitante = matriculaUsuarioSolicitante;
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

    public Integer getCodigoDominioUsuarioSolicitante() {
        return codigoDominioUsuarioSolicitante;
    }

    public void setCodigoDominioUsuarioSolicitante(Integer codigoDominioUsuarioSolicitante) {
        this.codigoDominioUsuarioSolicitante = codigoDominioUsuarioSolicitante;
    }

    public String getCodigoConsultorViajante() {
        return codigoConsultorViajante;
    }

    public void setCodigoConsultorViajante(String codigoConsultorViajante) {
        this.codigoConsultorViajante = codigoConsultorViajante;
    }

    public Integer getTotalViagensLiberadas() {
        return totalViagensLiberadas;
    }

    public void setTotalViagensLiberadas(Integer totalViagensLiberadas) {
        this.totalViagensLiberadas = totalViagensLiberadas;
    }

    public Date getData() {
        return dataLiberacao;
    }

    public void setData(Date dataLiberacao) {
        this.dataLiberacao = dataLiberacao;
    }

    public Date getDataLiberacao() {
        return dataLiberacao;
    }

    public void setDataLiberacao(Date dataLiberacao) {
        this.dataLiberacao = dataLiberacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public TipoLiberacao getTipoLiberacao() {
        return tipoLiberacao;
    }

    public void setTipoLiberacao(TipoLiberacao tipoLiberacao) {
        this.tipoLiberacao = tipoLiberacao;
    }

    public Viagem getViagem() {
        return viagem;
    }

    public void setViagem(Viagem viagem) {
        this.viagem = viagem;
    }
}

