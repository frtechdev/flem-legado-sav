package br.org.flem.sav.negocio;

import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author ilfernandes
 */
public enum SituacaoDataPagamentoEnum {
    PREVISTA(0,"Prevista"),
    EFETIVA(1,"Efetiva");

    private final int id;
    private final String descricao;

    private SituacaoDataPagamentoEnum(int id, String descricao) {
     this.id = id;
     this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getId(){
       return id;
   }

   public Collection<SituacaoDataPagamentoEnum> getLista() {
       return  Arrays.asList(SituacaoDataPagamentoEnum.values());
   }

   public static SituacaoDataPagamentoEnum getById(Integer id) {
        if (id != null) {
            if(PREVISTA.getId() == id){
                 return PREVISTA;
            }
            else if (EFETIVA.getId() == id) {
                return EFETIVA;
            }
        }
        return null;
    }
}