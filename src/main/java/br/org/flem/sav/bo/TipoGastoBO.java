package br.org.flem.sav.bo;

import br.org.flem.fwe.exception.AplicacaoException;
import br.org.flem.fwe.bo.BaseBOAb;
import br.org.flem.sav.negocio.TipoGasto;
import br.org.flem.sav.dao.TipoGastoDAO;
import java.util.Collection;

/**
 *
 * @author ILFernandes
 */
public class TipoGastoBO extends BaseBOAb<TipoGasto> {

    public TipoGastoBO() throws AplicacaoException {
        super(new TipoGastoDAO());
    }

    public Collection<TipoGasto> obterTodosAtivos() {
        return ((TipoGastoDAO)this.dao).obterTodosAtivos();
    }

     public static void main(String[] args) throws Exception {
        String descs[] = {"Passagens", "Translado", "Transportes Urbanos", "Telefonemas a Serviço"};
        br.org.flem.fwe.hibernate.util.HibernateUtil.beginTransaction();
        for (String s : descs) {
            TipoGasto tipoGasto = new TipoGasto();
            tipoGasto.setDescricao(s);
            new TipoGastoBO().inserir(tipoGasto);
        }
         br.org.flem.fwe.hibernate.util.HibernateUtil.commitTransaction();
    }
}
