package br.org.flem.sav.negocio;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author mgsilva
 */
public enum DestinoViagem {

    BAHIA(0), BRASIL(1), EXTERIOR(2);

    Integer id;

     DestinoViagem(Integer id){
        this.id = id;
    }


    public static Collection<String> toCollection(){
        Collection<String> nomes = new ArrayList<String>();

        for (int i = 0; i < DestinoViagem.values().length; i++) {
            nomes.add(DestinoViagem.values()[i].name());

        }
       return nomes;
    }
    

}
