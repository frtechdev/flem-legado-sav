package br.org.flem.sav.negocio.util;

import br.org.flem.fwe.hibernate.dto.base.BaseDTOAb;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

/**
 *
 * @author fcsilva
 */
@DataTransferObject
@Entity
@Table(name="estado")
public class Estado extends BaseDTOAb implements java.io.Serializable {

    @RemoteProperty
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")  
    private Long id;
    private String sigla;
    @RemoteProperty
    private String nome;

    @OneToMany(mappedBy = "estado")
    private List<Cidade> cidades;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_pais")
    private Pais pais;

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

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

    public List<Cidade> getCidades() {
        return cidades;
    }

    public void setCidades(List<Cidade> cidades) {
        this.cidades = cidades;
    }

    @Override
    public Serializable getPk() {
        return id;
    }
}
