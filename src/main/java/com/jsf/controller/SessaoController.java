package com.jsf.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.jsf.dao.UsuarioDao;
import com.jsf.model.Usuario;

@Named
@SessionScoped
public class SessaoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Usuario usuario;
	@Inject
	private UsuarioDao dao;

	public void logar() throws IOException {

		usuario = dao.getUsuario(usuario.getLogin(), usuario.getSenha());

		if (usuario != null) {			
			
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            if (session != null) {
                session.setAttribute("usuario", usuario);
            }
            
			FacesContext.getCurrentInstance().getExternalContext().redirect("pagina/consulta.xhtml");

		} else {
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Login / Senha inválido!", "Login / Senha inválido!"));
			usuario = new Usuario();
		}

	}

	public void logout() throws IOException {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.invalidate();

		FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
	}

	/********************* Getter e Setter *********************/

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
