package br.org.flem.sav.negocio; 

import java.util.ArrayList;
import java.util.Collection;

public enum TipoDiaria {
    PADRAO(0,"Padrão"),
    ESPECIAL(1,"Especial"),
    DETALHADA(2,"Detalhada"),
    ESPECIAL_SUB(3,"Especial_sub");

    private final int id;
    private final String nome;

    private TipoDiaria(int id, String nome) {
     this.id = id;
     this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public int getId(){
       return id;
   }

    public static Collection<TipoDiaria> toCollection() {
        ArrayList<TipoDiaria> tipos = new ArrayList<TipoDiaria>();

        for (int i = 0; i < TipoDiaria.values().length; i++) {
            if(i != 3){
                tipos.add(TipoDiaria.values()[i]);
            }
        }
        return tipos;
    }

    public static Collection<TipoDiaria> toAgendamentoCollection() {
        ArrayList<TipoDiaria> tipos = new ArrayList<TipoDiaria>();

        for (int i = 0; i < TipoDiaria.values().length; i++) {
            if(i != 1 && i != 3){
                tipos.add(TipoDiaria.values()[i]);
            }
        }
        return tipos;
    }
    
    public static TipoDiaria getById(int id){
        TipoDiaria tipo = null;
        for (TipoDiaria tipoDiaria : TipoDiaria.values()) {
            if(tipoDiaria.getId() == id){
                tipo = tipoDiaria;
            }
        }
        return tipo;
    }

    /*@Deprecated
    public static TipoDiaria getById(Integer id) {
        if (id != null) {
            if(TipoDiaria.PADRAO.getId() == id){
                 return TipoDiaria.PADRAO;
            }
            else if (TipoDiaria.ESPECIAL.getId() == id) {
                return TipoDiaria.ESPECIAL;
            }
            else if (TipoDiaria.DETALHADA.getId() == id) {
                return TipoDiaria.DETALHADA;
            }
        }
        return null;
    }*/
}