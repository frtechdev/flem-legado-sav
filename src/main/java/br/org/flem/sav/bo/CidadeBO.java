package br.org.flem.sav.bo; 

import br.org.flem.fwe.bo.BaseBOAb;
import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.sav.dao.CidadeDAO;
import br.org.flem.sav.negocio.DestinoViagem;
import br.org.flem.sav.negocio.Itinerario;
import br.org.flem.sav.negocio.util.Cidade;
import br.org.flem.sav.negocio.util.Estado;
import br.org.flem.sav.negocio.util.Pais;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mccosta
 */
public class CidadeBO extends BaseBOAb<Cidade> {
    
    public CidadeBO() throws AplicacaoException{
        super(new CidadeDAO());
    }
    
    public Cidade obterPorNome(String nome) {
        return ((CidadeDAO) dao).obterPorNome(nome, null, null);
    }

    public Cidade obterPorNome(String nome, String uf) {
        Pais pais = null;
        Estado estado = null;
        try {
            if(uf.equals("BR")){
                pais = new PaisBO().obterPorSigla(uf);
            }else{
                pais = new PaisBO().obterPorSigla("BR");
                estado = new EstadoBO().obterPorSigla(uf);
            }
        } catch (AplicacaoException ex) {
            Logger.getLogger(CidadeBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ((CidadeDAO) dao).obterPorNome(nome, estado, pais);
    }

    public Collection<Cidade> obterBahia() {
        return ((CidadeDAO) dao).obterBahia();
    }

    public Collection<Cidade> obterBrasil() {
        return ((CidadeDAO) dao).obterBrasil();
    }

    public Collection<Cidade> obterExterior() {
        return ((CidadeDAO) dao).obterExterior();
    }

    public Collection<Cidade> obterPorDestino(String destino){

        if(DestinoViagem.valueOf(destino).equals(DestinoViagem.BAHIA)){
            return ((CidadeDAO) dao).obterBahia();
        }else if(DestinoViagem.valueOf(destino).equals(DestinoViagem.BRASIL)){
            return ((CidadeDAO) dao).obterBrasil();
        }else{
           return ((CidadeDAO) dao).obterExterior();
        }
    }
    
    public Collection<Cidade> obterListaDestinoPorItemItinerario(Itinerario itinerario) {
        return ((CidadeDAO) dao).obterListaDestinoPorItemItinerario(itinerario);
    }

}
