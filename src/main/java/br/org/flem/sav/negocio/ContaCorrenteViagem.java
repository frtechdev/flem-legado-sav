package br.org.flem.sav.negocio; 

import br.org.flem.fwe.hibernate.dto.base.BaseDTOAb;
import br.org.flem.sav.negocio.util.Banco;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author mgsilva
 */
@Entity
public class ContaCorrenteViagem extends BaseDTOAb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String nomeFuncionario;
    String matriculaFuncionarioViajante;
    String codigoDominioFuncionarioViajante;

    @ManyToOne
    @JoinColumn(name="id_banco")
    Banco banco;
    String agencia;
    String conta;

    public String getMatriculaHRFuncionarioViajante() {
        return matriculaFuncionarioViajante;
    }

    public void setMatriculaHRFuncionarioViajante(String matriculaFuncionarioViajante) {
        this.matriculaFuncionarioViajante = matriculaFuncionarioViajante;
    }

    public String getCodigoDominioFuncionarioViajante() {
        return codigoDominioFuncionarioViajante;
    }

    public void setCodigoDominioFuncionarioViajante(String codigoDominioFuncionarioViajante) {
        this.codigoDominioFuncionarioViajante = codigoDominioFuncionarioViajante;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }
    

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Serializable getPk() {
        return getId();
    }
}
