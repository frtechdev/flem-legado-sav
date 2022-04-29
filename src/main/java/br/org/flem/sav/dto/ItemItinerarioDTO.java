package br.org.flem.sav.dto; 

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author mccosta
 */
public class ItemItinerarioDTO {

    private Integer id;
    private Date data;
    private String origem;
    private String destino;
    private String observacoes;

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }
    public String getDataFormatada()
    {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(this.data);
    }
}
