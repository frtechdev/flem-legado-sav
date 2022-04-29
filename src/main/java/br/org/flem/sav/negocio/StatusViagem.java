package br.org.flem.sav.negocio; 

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public enum StatusViagem {

    VIAGEM_ABERTA(0, "Viagem Aberta"),
    VIAGEM_RECEBIDA(1, "Viagem Recebida"),
    VIAGEM_CANCELADA(2, "Viagem Cancelada"),
    VIAGEM_FINALIZADA(3, "Viagem Finalizada");

    private int id;
    private String descricao;

    private StatusViagem(int id, String descricao) {
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

    /*public static StatusViagem getById(int id) {
        StatusViagem status = null;
        switch (id) {
            case 0: {
                status = StatusViagem.VIAGEM_ABERTA;
                break;
            }
            case 1: {
                status = StatusViagem.VIAGEM_RECEBIDA;
                break;
            }
            case 2: {
                status = StatusViagem.VIAGEM_CANCELADA;
                break;
            }
             case 3: {
                status = StatusViagem.VIAGEM_FINALIZADA;
                break;
            }
        }
        return status;
    }*/
    
    public static StatusViagem getById(int id){
        StatusViagem status = null;
        for (StatusViagem statusViagem : StatusViagem.values()) {
            if(statusViagem.getId() == id){
                status = statusViagem;
            }
        }
        return status;
    }

    public static Collection<StatusViagem> toCollection() {
        List<StatusViagem> lista = new ArrayList<StatusViagem>();
        for (int i =0; i < StatusViagem.values().length; i++) {
            if(i !=  StatusViagem.VIAGEM_CANCELADA.ordinal()) {
                lista.add(StatusViagem.values()[i]);
            }
        }
        return lista;
    }
}