package br.org.flem.sav.negocio;

import br.org.flem.fwe.hibernate.dto.base.BaseDTOAb;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author MCCosta
 */
@Entity
public class PercentualDiaria extends BaseDTOAb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    Integer minQuebraDiariasUm;
    Integer maxQuebraDiariasUm;
    Double quebraDiariasUm;

    Integer minQuebraDiariasDois;
    Integer maxQuebraDiariasDois;
    Double quebraDiariasDois;

    String lotacao;
    
    Integer departamentoDominio;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getQuebraDiarias(Double horas){
        if(horas >= minQuebraDiariasUm && horas <= maxQuebraDiariasUm){
            return quebraDiariasUm;
        }
        if(horas >= minQuebraDiariasDois && horas <= maxQuebraDiariasDois){
            return quebraDiariasDois;
        }
        return 0.0;
    }

    public String getLotacaoHR() {
        return lotacao;
    }

    public void setLotacaoHR(String lotacao) {
        this.lotacao = lotacao;
    }

    public Integer getDepartamentoDominio() {
        return departamentoDominio;
    }

    public void setDepartamentoDominio(Integer departamentoDominio) {
        this.departamentoDominio = departamentoDominio;
    }

    @Override
    public Serializable getPk() {
        return getId();
    }

    public Integer getMinQuebraDiariasUm() {
        return minQuebraDiariasUm;
    }

    public void setMinQuebraDiariasUm(Integer minQuebraDiariasUm) {
        this.minQuebraDiariasUm = minQuebraDiariasUm;
    }

    public Integer getMaxQuebraDiariasUm() {
        return maxQuebraDiariasUm;
    }

    public void setMaxQuebraDiariasUm(Integer maxQuebraDiariasUm) {
        this.maxQuebraDiariasUm = maxQuebraDiariasUm;
    }

    public Double getQuebraDiariasUm() {
        return quebraDiariasUm;
    }

    public void setQuebraDiariasUm(Double quebraDiariasUm) {
        this.quebraDiariasUm = quebraDiariasUm;
    }

    public Integer getMinQuebraDiariasDois() {
        return minQuebraDiariasDois;
    }

    public void setMinQuebraDiariasDois(Integer minQuebraDiariasDois) {
        this.minQuebraDiariasDois = minQuebraDiariasDois;
    }

    public Integer getMaxQuebraDiariasDois() {
        return maxQuebraDiariasDois;
    }

    public void setMaxQuebraDiariasDois(Integer maxQuebraDiariasDois) {
        this.maxQuebraDiariasDois = maxQuebraDiariasDois;
    }

    public Double getQuebraDiariasDois() {
        return quebraDiariasDois;
    }

    public void setQuebraDiariasDois(Double quebraDiariasDois) {
        this.quebraDiariasDois = quebraDiariasDois;
    }

}