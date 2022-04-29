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
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author fcsilva
 */
@Entity
@Table(name="pais")
public class Pais extends BaseDTOAb implements java.io.Serializable {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pais")  
    private Long id;
    private String sigla;
    private String nome;
    
    @Column(name="nome_completo")
    private String nomeCompleto;
    
    @Column(name="nome_ingles")
    private String nomeIngles;
    
    @Column(name="nome_nacionalidade")
    private String nacionalidade;
    
    @OneToOne
    @JoinColumn(name="id_idioma")
    private Idioma idioma;
    
    @OneToMany(mappedBy = "pais")
    private List<Cidade> cidades;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_continente")
    private Continente continente;    

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

    public List<Cidade> getCidades() {
        return cidades;
    }

    public void setCidades(List<Cidade> cidades) {
        this.cidades = cidades;
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

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getNomeIngles() {
        return nomeIngles;
    }

    public void setNomeIngles(String nomeIngles) {
        this.nomeIngles = nomeIngles;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Continente getContinente() {
        return continente;
    }

    public void setContinente(Continente continente) {
        this.continente = continente;
    }
}
