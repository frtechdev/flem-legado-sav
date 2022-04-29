package br.org.flem.sav.interfaces;

import br.org.flem.fw.service.IFuncionario;
import java.util.ArrayList;

/**
 * Interface utilizada para nortear a liberação da solicitação de viagem de usuários do SAV.
 * @author mccosta
 */
public interface ISolicitarViagem {

    /**
     * Método que deve implementar a lógica para saber se o funcionário poderá solicitar
     * a viagem e obter uma resposta (true or false) a partir do IFuncionario passado.
     *
     * @param iFunc IFuncionario contendo o objeto IFuncionario.
     * @return boolean.
     */
    public ArrayList<String> funcionarioPodeViajar(IFuncionario iFunc);

}
