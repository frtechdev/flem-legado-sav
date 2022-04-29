package br.org.flem.sav.negocio; 

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author mccosta
 */

@Entity
@Table(name="viagemagendada")
@PrimaryKeyJoinColumn(name="id_viagem")
public class ViagemComAgendamento extends Viagem{
    
    @Enumerated
    private StatusAgendamento statusAgendamento;

    public StatusAgendamento getStatusAgendamento() {
        return statusAgendamento;
    }

    public void setStatusAgendamento(StatusAgendamento statusAgendamento) {
        this.statusAgendamento = statusAgendamento;
    }
}
