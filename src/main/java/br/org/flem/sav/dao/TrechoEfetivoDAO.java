package br.org.flem.sav.dao; 

import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.hibernate.dao.base.BaseDAOAb;
import br.org.flem.sav.negocio.PrestacaoContas;
import br.org.flem.sav.negocio.TrechoEfetivo;
import java.util.Collection;

/**
 *
 * @author mccosta
 */
public class TrechoEfetivoDAO extends BaseDAOAb <TrechoEfetivo>{

    public TrechoEfetivoDAO() throws AcessoDadosException{
    }

    @Override
    protected Class<TrechoEfetivo> getClasseDto() {
        return TrechoEfetivo.class;
    }

    public Collection<TrechoEfetivo> obterPorPrestacao(PrestacaoContas prestacaoContas) {
        return (Collection<TrechoEfetivo>) session.createQuery("FROM TrechoEfetivo t WHERE t.prestacaoContas = :prestacaoContas ORDER BY t.id ASC").setEntity("prestacaoContas", prestacaoContas).list();
    }
}