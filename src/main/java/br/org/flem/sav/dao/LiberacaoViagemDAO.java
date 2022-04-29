package br.org.flem.sav.dao; 

import br.org.flem.fw.service.IFuncionario;
import br.org.flem.fwe.exception.AcessoDadosException;
import br.org.flem.fwe.hibernate.dao.base.BaseDAOAb;
import br.org.flem.sav.negocio.LiberacaoViagem;
import br.org.flem.sav.negocio.TipoLiberacao;
import br.org.flem.sav.negocio.Viagem;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mccosta
 */
public class LiberacaoViagemDAO extends BaseDAOAb <LiberacaoViagem>{

    public LiberacaoViagemDAO() throws AcessoDadosException{
    }

    @Override
    protected Class<LiberacaoViagem> getClasseDto() {
        return LiberacaoViagem.class;
    }

    public LiberacaoViagem obterByViagemTipo(Viagem viagem, TipoLiberacao tipoLiberacao) {
        return (LiberacaoViagem) session.createQuery("FROM LiberacaoViagem L WHERE L.tipoLiberacao = :tipoLiberacao AND L.viagem = :viagem").setInteger("tipoLiberacao", tipoLiberacao.getId()).setEntity("viagem", viagem).uniqueResult();
    }

    public LiberacaoViagem obterByIFuncionario(IFuncionario iFuncionario) {
        return (LiberacaoViagem) session.createQuery("FROM LiberacaoViagem L WHERE L.valido = '1' AND L.codigoDominioUsuarioViajante = :mat AND L.tipoLiberacao = 0 ORDER BY L.id").setInteger("mat", iFuncionario.getCodigoDominio()).setMaxResults(1).uniqueResult();
    }

    public LiberacaoViagem obterLimite50ByIFuncionario(IFuncionario iFuncionario) {
        return (LiberacaoViagem) session.createQuery("FROM LiberacaoViagem L WHERE L.valido = '1' AND L.codigoDominioUsuarioViajante = :mat AND L.tipoLiberacao = 2 ORDER BY L.id").setInteger("mat", iFuncionario.getCodigoDominio()).setMaxResults(1).uniqueResult();
    }

    public LiberacaoViagem obterByCodigoConsultor(String codigo) {
        return (LiberacaoViagem) session.createQuery("FROM LiberacaoViagem L WHERE L.valido = '1' AND L.codigoConsultorViajante = :codigo AND L.tipoLiberacao = 0 ORDER BY L.id").setString("codigo", codigo).setMaxResults(1).uniqueResult();
    }

    public LiberacaoViagem obterByIFuncionarioTipo(IFuncionario iFuncionario, TipoLiberacao tipoLiberacao) {
        return (LiberacaoViagem) session.createQuery("FROM LiberacaoViagem L WHERE L.valido = '1' AND L.codigoDominioUsuarioViajante = :mat AND L.tipoLiberacao = :tipoLiberacao ").setInteger("mat", iFuncionario.getCodigoDominio()).setInteger("tipoLiberacao", tipoLiberacao.getId()).setMaxResults(1).uniqueResult();
    }

    public LiberacaoViagem obterByCodigoConsultorTipo(String codigo, TipoLiberacao tipoLiberacao) {
        return (LiberacaoViagem) session.createQuery("FROM LiberacaoViagem L WHERE L.valido = '1' AND L.codigoConsultorViajante = :codigo AND L.tipoLiberacao = :tipoLiberacao ").setString("codigo", codigo).setInteger("tipoLiberacao", tipoLiberacao.getId()).setMaxResults(1).uniqueResult();
    }

    public Map<Integer,LiberacaoViagem> obterTodosValido() {
        Map<Integer,LiberacaoViagem> mapLV = new HashMap<Integer,LiberacaoViagem>();
        Collection<LiberacaoViagem> liberacaoList = (Collection<LiberacaoViagem>) session.createQuery("FROM LiberacaoViagem L WHERE  L.valido = '1' AND L.tipoLiberacao = 0 ").list();

        for (LiberacaoViagem lv : liberacaoList) {
            mapLV.put(lv.getCodigoDominioUsuarioViajante(), lv);
        }

        return mapLV;
    }

    public Map<Integer,LiberacaoViagem> obterTodosValidoByTipo(TipoLiberacao tipoLiberacao) {
        Map<Integer,LiberacaoViagem> mapLV = new HashMap<Integer,LiberacaoViagem>();
        Collection<LiberacaoViagem> liberacaoList = (Collection<LiberacaoViagem>) session.createQuery("FROM LiberacaoViagem L WHERE  L.valido = '1' AND L.tipoLiberacao = :tipoLiberacao ").setInteger("tipoLiberacao", tipoLiberacao.getId()).list();

        for (LiberacaoViagem lv : liberacaoList) {
            mapLV.put(lv.getCodigoDominioUsuarioViajante(), lv);
        }

        return mapLV;
    }

    public Collection<LiberacaoViagem> obterTodosByTipo(TipoLiberacao tipoLiberacao) {
        return (Collection<LiberacaoViagem>) session.createQuery("FROM LiberacaoViagem L WHERE  L.tipoLiberacao = :tipoLiberacao ORDER BY L.id DESC ").setInteger("tipoLiberacao", tipoLiberacao.getId()).list();
    }
}
