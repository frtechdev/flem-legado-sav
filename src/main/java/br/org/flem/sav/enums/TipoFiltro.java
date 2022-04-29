/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.flem.sav.enums;

import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author ilfernandes
 */
public enum TipoFiltro {

    CODIGO_VIAGEM(0, "Código da viagem"),
    CODIGO_VIAJANTE(1, "Matricula/Código Viajante"),
    NOME_VIAJANTE(2, "Nome do viajante");
    private Integer id;
    private String descricao;

    private TipoFiltro(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public static TipoFiltro getById(Integer id) {
        TipoFiltro tipo = null;
        switch (id) {
            case 0: {
                tipo = CODIGO_VIAGEM;
                break;
            }
            case 1: {
                tipo = CODIGO_VIAJANTE;
                break;
            }
            case 2: {
                tipo = NOME_VIAJANTE;
                break;
            }

        }
        return tipo;
    }

    public static TipoFiltro getById(String id) {
        return getById(Integer.valueOf(id));
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getId() {
        return id;
    }

    public static Collection<TipoFiltro> getLista() {
        return Arrays.asList(TipoFiltro.values());
    }
}
