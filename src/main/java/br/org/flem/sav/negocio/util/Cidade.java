package br.org.flem.sav.negocio.util;

import br.org.flem.fwe.hibernate.dto.base.BaseDTOAb;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author fcsilva
 */
@Entity
@Table(name="cidade")
public class Cidade extends BaseDTOAb implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cidade")  
    private Long id;
    
    private String nome;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_estado", nullable=true)
    private Estado estado = new Estado();
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_pais")
    private Pais pais = new Pais();
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Serializable getPk() {
        return id;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
