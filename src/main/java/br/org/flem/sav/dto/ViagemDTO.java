package br.org.flem.sav.dto;

import br.org.flem.fw.service.IFonteRecurso;
import br.org.flem.sav.negocio.ItemItinerario;
import br.org.flem.sav.negocio.LiberacaoViagem;
import br.org.flem.sav.negocio.Viagem;
import br.org.flem.sav.negocio.ViagemComAgendamento;
import br.org.flem.sav.negocio.util.Cidade;
import java.util.List;

/**
 *
 * @author mccosta
 */
public class ViagemDTO {
    Viagem viagem;
    ViagemComAgendamento viagemComAgendamento;
    LiberacaoViagem liberacaoViagem;
    IFonteRecurso fonteRecurso;
    String nomeViajante = "";
    String nomeSolicitante = "";
    String prestouConta = "";
    String periodo = "";
    String statusViagem = "";
    String statusPrestacao = "";
    String observacao = "";
    String centroCusto = "";
    Double porcentagem;
    Integer qtPrestacaoIrregular;
    Double totalDiarias;
    private Double totalDiariasPrevista;
    Integer processo;
    String agendamentoOK = "Não";
    private String codigoViajante;
    private Integer ordem;
    List<ItemItinerario> itensItinerario;
    
    public Integer getQtPrestacaoIrregular() {
        return qtPrestacaoIrregular;
    }

    public void setQtPrestacaoIrregular(Integer qtPrestacaoIrregular) {
        this.qtPrestacaoIrregular = qtPrestacaoIrregular;
    }
    
    public Double getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(Double porcentagem) {
        this.porcentagem = porcentagem;
    }


    public String getNomeViajante() {
        return nomeViajante;
    }

    public void setNomeViajante(String nomeViajante) {
        this.nomeViajante = nomeViajante;
    }

    public String getNomeSolicitante() {
        return nomeSolicitante;
    }

    public void setNomeSolicitante(String nomeSolicitante) {
        this.nomeSolicitante = nomeSolicitante;
    }

    public Viagem getViagem() {
        return viagem;
    }

    public void setViagem(Viagem viagem) {
        this.viagem = viagem;
    }

    public ViagemComAgendamento getViagemComAgendamento() {
        return viagemComAgendamento;
    }

    public void setViagemComAgendamento(ViagemComAgendamento viagemComAgendamento) {
        this.viagemComAgendamento = viagemComAgendamento;
    }

    public LiberacaoViagem getLiberacaoViagem() {
        return liberacaoViagem;
    }

    public void setLiberacaoViagem(LiberacaoViagem liberacaoViagem) {
        this.liberacaoViagem = liberacaoViagem;
    }

    public IFonteRecurso getFonteRecurso() {
        return fonteRecurso;
    }

    public void setFonteRecurso(IFonteRecurso fonterecurso) {
        this.fonteRecurso = fonterecurso;
    }

    public String getPrestouConta() {
        return prestouConta;
    }

    public void setPrestouConta(String prestouConta) {
        this.prestouConta = prestouConta;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getStatusViagem() {
        return statusViagem;
    }

    public void setStatusViagem(String statusViagem) {
        this.statusViagem = statusViagem;
    }

    public Double getTotalDiarias() {
        return totalDiarias;
    }

    public void setTotalDiarias(Double totalDiarias) {
        this.totalDiarias = totalDiarias;
    }

    public Integer getProcesso() {
        return processo;
    }

    public void setProcesso(Integer processo) {
        this.processo = processo;
    }

    public String getAgendamentoOK() {
        return agendamentoOK;
    }

    public void setAgendamentoOK(String agendamentoOK) {
        this.agendamentoOK = agendamentoOK;
    }

    public String getCodigoViajante() {
        return codigoViajante;
    }

    public void setCodigoViajante(String codigoViajante) {
        this.codigoViajante = codigoViajante;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Double getTotalDiariasPrevista() {
        return totalDiariasPrevista;
    }

    public void setTotalDiariasPrevista(Double totalDiariasPrevista) {
        this.totalDiariasPrevista = totalDiariasPrevista;
    }

    public String getStatusPrestacao() {
        return statusPrestacao;
    }

    public void setStatusPrestacao(String statusPrestacao) {
        this.statusPrestacao = statusPrestacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(String centroCusto) {
        this.centroCusto = centroCusto;
    }

    public List<ItemItinerario> getItensItinerario() {
        return itensItinerario;
    }

    public void setItensItinerario(List<ItemItinerario> itensItinerario) {
        this.itensItinerario = itensItinerario;
    }

    @Override
    public String toString() {
        return "ViagemDTO{" + "viagem=" + viagem + ", viagemComAgendamento=" + viagemComAgendamento + ", liberacaoViagem=" + liberacaoViagem + ", fonteRecurso=" + fonteRecurso + ", nomeViajante=" + nomeViajante + ", nomeSolicitante=" + nomeSolicitante + ", prestouConta=" + prestouConta + ", periodo=" + periodo + ", statusViagem=" + statusViagem + ", statusPrestacao=" + statusPrestacao + ", observacao=" + observacao + ", centroCusto=" + centroCusto + ", porcentagem=" + porcentagem + ", qtPrestacaoIrregular=" + qtPrestacaoIrregular + ", totalDiarias=" + totalDiarias + ", totalDiariasPrevista=" + totalDiariasPrevista + ", processo=" + processo + ", agendamentoOK=" + agendamentoOK + ", codigoViajante=" + codigoViajante + ", ordem=" + ordem + '}';
    }
}
