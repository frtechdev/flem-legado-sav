package br.org.flem.sav.negocio;

import br.org.flem.fwe.hibernate.dto.base.BaseDTOAb;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.CascadeType;

/**
 *
 * @author fcsilva
 */
@Entity
@Table(name="itinerario")
public class Itinerario extends BaseDTOAb {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_itinerario")    
    private Integer id;

    @Column(length=3000)
    private String observacao;
    

    @OneToMany(mappedBy = "itinerario", cascade=CascadeType.REMOVE)
    private List<ItemItinerario> itensItinerario;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ItemItinerario> getItensItinerario() {
        return itensItinerario;
    }

    public void setItensItinerario(List<ItemItinerario> itensItinerario) {
        this.itensItinerario = itensItinerario;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public Serializable getPk() {
        return getId();
    }
}
