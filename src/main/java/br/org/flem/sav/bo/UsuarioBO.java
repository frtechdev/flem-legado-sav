package br.org.flem.sav.bo; 

import br.org.flem.fw.persistencia.dao.UsuarioDAO;
import br.org.flem.fw.persistencia.dto.Usuario;
import br.org.flem.fw.service.IUsuario;

/**
 *
 * @author fcsilva
 */
public class UsuarioBO {
    
    UsuarioDAO  usuarioDAO = new UsuarioDAO();

    public IUsuario obterPorCodigoDominio(Integer matriculaUsuario) {
        return usuarioDAO.obterPorCodigoDominio(matriculaUsuario);
    }

    public Usuario atualizarStatusFerias(Usuario usuario) {
        return usuarioDAO.atualizarStatusFerias(usuario);
    }
}
