package br.org.flem.sav.negocio; 

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author mccosta
 */
public enum TipoLiberacao {
    PENDENCIA(0,"PENDENCIA"),
    RETROATIVO(1,"RETROATIVO"),
    LIMITE_50(2,"LIMITE_50");

    private final int id;
    private final String nome;

    private TipoLiberacao(int id, String nome) {
     this.id = id;
     this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public int getId(){
       return id;
   }

    public static Collection<TipoLiberacao> toCollection() {
        ArrayList<TipoLiberacao> tipos = new ArrayList<TipoLiberacao>();
        tipos.addAll(Arrays.asList(TipoLiberacao.values()));
        return tipos;
    }
    
    public static TipoLiberacao getById(int id){
        TipoLiberacao tipo = null;
        for (TipoLiberacao tipoLiberacao : TipoLiberacao.values()) {
            if(tipoLiberacao.getId() == id){
                tipo = tipoLiberacao;
            }
        }
        return tipo;
    }

    /*public static TipoLiberacao getById(Integer id) {
        TipoLiberacao retorno = null;
        switch(id){
            case 0:
                retorno = TipoLiberacao.PENDENCIA;
            break;

            case 1:
                retorno = TipoLiberacao.RETROATIVO;
            break;

            case 2:
                retorno = TipoLiberacao.LIMITE_50;
            break;
        }
        return retorno;
    }*/
}