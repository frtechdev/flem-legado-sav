package br.org.flem.sav.bo;

import br.org.flem.fw.persistencia.dto.Funcionario;
import br.org.flem.fw.service.IColaborador;
import br.org.flem.fw.service.IFuncionario;
import br.org.flem.sav.dto.ViagemDTO;
import br.org.flem.sav.negocio.StatusViagem;
import br.org.flem.sav.negocio.Viagem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author ilfernandes
 */
public class RelatorioTotalSDVBO {

    //private
    public Collection<ViagemDTO> prepararColecaoDTO(Collection<Viagem> viagens, Collection<Funcionario> funcionarios, Collection<IColaborador> consultores) {
        Collection<ViagemDTO> resultado = new ArrayList<ViagemDTO>();
        StringBuilder statusViagem = new StringBuilder();
        for (Viagem viagem : viagens) {
            ViagemDTO viagemDTO = new ViagemDTO();
            viagemDTO.setTotalDiariasPrevista(viagem.getTotalDiarias());
            viagemDTO.setTotalDiarias(0d);
            switch (viagem.getStatusViagem()) {
                case VIAGEM_ABERTA: {
                    if (viagem.getDataRetornoPrevista().after(new Date())) {
                        statusViagem.append("Viagem aberta");
                        viagemDTO.setOrdem(1);
                    } else {
                        statusViagem.append("Viagem aberta não realizada");
                        viagemDTO.setOrdem(2);
                    }
                    
                    break;
                }
                case VIAGEM_RECEBIDA: {
                    if (viagem.getDataRetornoPrevista().after(new Date())) {
                        statusViagem.append("Viagem programada");
                        viagemDTO.setOrdem(3);
                    } else {
                        statusViagem.append("Viagem realizada");
                        viagemDTO.setOrdem(4);
                    }
                    break;
                }
                case VIAGEM_FINALIZADA: {
                    statusViagem.append("Viagem finalizada");
                    viagemDTO.setOrdem(6);
                    break;
                }
                default:
                    statusViagem.append("");
            }
            if (viagem.getPrestacaoContas() != null) {
                viagemDTO.setTotalDiarias(viagem.getPrestacaoContas().getTotalDiarias());
                switch (viagem.getPrestacaoContas().getStatusPrestacaoContas()) {
                    case PRESTACAO_INFORMADA: {
                        statusViagem.append(" com prestação de contas informada");
                        break;
                    }
                    case PRESTACAO_RECEBIDA: {
                        statusViagem.append(" com prestação de contas recebida");
                        viagemDTO.setOrdem(5);
                        break;
                    }
                    case PRESTACAO_FINALIZADA: {
                        //statusViagem.append(" Prestação de Contas Finalizada ");
                        break;
                    }
                }
            } else if (viagem.getStatusViagem() != null && viagem.getStatusViagem() != StatusViagem.VIAGEM_ABERTA
                    && !statusViagem.toString().contains("não realizada") && !statusViagem.toString().contains("Viagem programada")) {
                statusViagem.append(" com prestação de contas em aberto");
            }
            for (IFuncionario funcionario : funcionarios) {
                if (viagem.getCodigoDominioUsuarioViajante() != null && funcionario.getCodigoDominio().equals(viagem.getCodigoDominioUsuarioViajante())) {
                    viagemDTO.setNomeViajante(funcionario.getNome());
                    viagemDTO.setCodigoViajante(viagem.getCodigoDominioUsuarioViajante().toString());
                }
            }
            for (IColaborador colaborador : consultores) {
                if (viagem.getCodigoConsultorViajante() != null && colaborador.getCodigo().equals(viagem.getCodigoConsultorViajante())) {
                    viagemDTO.setNomeViajante(colaborador.getNome());
                    viagemDTO.setCodigoViajante(viagem.getCodigoConsultorViajante());
                }
            }
            viagemDTO.setStatusViagem(statusViagem.toString());
            viagemDTO.setViagem(viagem);
            if (viagemDTO.getCodigoViajante() != null && !viagemDTO.getCodigoViajante().isEmpty()) {
                resultado.add(viagemDTO);
            }
            statusViagem.delete(0, statusViagem.length() + 1);
        }
        return resultado;
    }
}
