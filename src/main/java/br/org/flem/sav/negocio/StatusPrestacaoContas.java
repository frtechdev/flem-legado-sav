package br.org.flem.sav.negocio; 

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author mccosta
 */
public enum StatusPrestacaoContas {

    PRESTACAO_INFORMADA(0, "Prestação informada"),
    PRESTACAO_RECEBIDA(1, "Prestação Recebida"),
    PRESTACAO_FINALIZADA(2, "Prestação Finalizada");
    private int id;
    private String descricao;

    private StatusPrestacaoContas(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public static StatusPrestacaoContas getById(int id){
        StatusPrestacaoContas status = null;
        for (StatusPrestacaoContas statusPrestacaoContas : StatusPrestacaoContas.values()) {
            if(statusPrestacaoContas.getId() == id){
                status = statusPrestacaoContas;
            }
        }
        return status;
    }

    /*public static StatusPrestacaoContas getById(int id) {
        StatusPrestacaoContas status = null;
        switch (id) {
            case 0: {
                status = StatusPrestacaoContas.PRESTACAO_INFORMADA;
                break;
            }
            case 1: {
                status = StatusPrestacaoContas.PRESTACAO_RECEBIDA;
                break;
            }
            case 2: {
                status = StatusPrestacaoContas.PRESTACAO_FINALIZADA;
                break;
            }
        }
        return status;
    }*/

    public static Collection<StatusPrestacaoContas> toCollection() {
        List<StatusPrestacaoContas> lista = new ArrayList(Arrays.asList(StatusPrestacaoContas.values()));
        return lista;
    }
}
