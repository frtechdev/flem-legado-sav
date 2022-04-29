package br.org.flem.sav.negocio; 

import br.org.flem.fwe.hibernate.dto.base.BaseDTOAb;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author mccosta
 */
@Entity
@Table(name="cargodiaria")
public class CargoDiaria extends BaseDTOAb {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_cargodiaria")
    private Integer id;

    private Integer codigo;

    private Integer cargoDominio;

    private String cargoNome;
    private String descricao;
    private Double valorBahia;
    private Double valorBrasil;
    private Double valorExterior;

    @Override
    public Serializable getPk() {
        return getId();
    }

    public String getCargoNome() {
        return cargoNome;
    }

    public void setCargoNome(String cargoNome) {
        this.cargoNome = cargoNome;
    }

    public Integer getCodigoHR() {
        return codigo;
    }

    public void setCodigoHR(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCargoDominio() {
        return cargoDominio;
    }

    public void setCargoDominio(Integer cargoDominio) {
        this.cargoDominio = cargoDominio;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValorBahia() {
        return valorBahia;
    }

    public void setValorBahia(Double valorBahia) {
        this.valorBahia = valorBahia;
    }

    public Double getValorBrasil() {
        return valorBrasil;
    }

    public void setValorBrasil(Double valorBrasil) {
        this.valorBrasil = valorBrasil;
    }

    public Double getValorExterior() {
        return valorExterior;
    }

    public void setValorExterior(Double valorExterior) {
        this.valorExterior = valorExterior;
    }

}