package br.org.flem.sav.negocio;

import br.org.flem.fwe.hibernate.dto.base.BaseDTOAb;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author ILFernandes
 */
@Entity
public class Gasto extends BaseDTOAb {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_gasto")
    private Integer id;
    private Double valor;

    @OneToOne
    @JoinColumn(name="id_tipogasto")
    private TipoGasto tipoGasto = new TipoGasto();

    @ManyToOne
    @JoinColumn(name="id_prestacaocontas")
    private PrestacaoContas prestacaoContas;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public TipoGasto getTipoGasto() {
        return tipoGasto;
    }

    public void setTipoGasto(TipoGasto tipoGasto) {
        this.tipoGasto = tipoGasto;
    }

    public PrestacaoContas getPrestacaoContas() {
        return prestacaoContas;
    }

    public void setPrestacaoContas(PrestacaoContas prestacaoContas) {
        this.prestacaoContas = prestacaoContas;
    }

    @Override
    public Serializable getPk() {
        return this.getId();
    }

}