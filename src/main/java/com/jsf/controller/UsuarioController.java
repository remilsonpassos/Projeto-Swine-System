package com.jsf.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.jsf.dao.UsuarioDao;
import com.jsf.model.Perfil;
import com.jsf.model.Usuario;
import com.jsf.util.FiltroPaginacao;

/**
 * 1) Linha 32 - Antes utilizavamos o @RequestScoped. Tive que auterar no pom.xml o glassfish da versão 2.1.17 para 2.2.7
 * que permite a utilização do @ViewScoped do pacote javax.faces.view.ViewScoped. (obs: no livro tem a diferença entre as anotaçõe)
 * 
 * 2) Linhas 47 a 51 - São declarados os novos atributos 
 * 
 * 3) As demais auterações estão com comentário!
 * 
 * @author joelcio em: 20/01/2019
 *
 */

@Named
@ViewScoped 
public class UsuarioController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Usuario usuario;
	@Inject
	private Perfil perfil;
	@Inject
	private UsuarioDao dao;

	private FiltroPaginacao filtroPaginacao = new FiltroPaginacao();
	private LazyDataModel<Usuario> model;

	private boolean trocarSenha = false;
	private String tituloSenha;

	/**
	 * Construtor foi declarado para poder carregar LazyDataModel quando o objeto for criado 
	 */
	public UsuarioController() {
		carregargridusuario();
	}

	/********************* Métodos *********************/

	/**
	 * Adiciona ou Edita um usuário
	 */
	public void salvar() {

		Long id = usuario.getIdUsuario();

		if (id == 0) { // Adiciona um novo usuário
			boolean salvo = dao.inserir(usuario);

			if (salvo == true) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Usuário cadastrado com sucesso!", "Usuário cadastrado com sucesso!"));
				resetar();
			} else {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Usuário não cadastrado!", "Usuário não cadastrado!"));
			}
		} else { // Atualiza um usuário
			dao.edita(usuario);
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Usuário atualizado com sucesso!", "Usuário atualizado com sucesso!"));
			resetar();
		}
	}

	/**
	 * Reseta os atributos do objeto 
	 */
	public void resetar() {
		usuario = new Usuario();
		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
				.findComponent("dataForm:tabela");
		dataTable.resetValue();
		PrimeFaces.current().resetInputs("form:panelGrid");
		trocarSenha = false;
	}

	/**
	 * Habilita e desabilita o botão de auteração de senha trocando a nomeclatura dinamicamente 
	 */
	public void habilitaCampoSenha() {
		
		if(trocarSenha == false){
			tituloSenha = "Cancela Alterar Senha";
			trocarSenha = true;	
		} else {
			tituloSenha = "Alterar Senha";
			trocarSenha = false;		
		}
		
		PrimeFaces.current().resetInputs("form:senha");
	}

	/**
	 * Seleção do objeto no dataTable e coloca no formulário para edição
	 * 
	 * @param event
	 */
	public void onRowSelect(SelectEvent event) {
		usuario = (Usuario) event.getObject();
		tituloSenha = "Alterar Senha";
	}

	/**
	 * Carrega os usuários no dataTable e trata o número de linas, quantidade total, linha selecionada, etc.
	 */
	public void carregargridusuario() {
		
		/**
		 * Popula o LazyDataModel
		 */
		model = new LazyDataModel<Usuario>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Usuario> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {

				filtroPaginacao.setPrimeiroRegistro(first);
				filtroPaginacao.setQuantidadeRegistros(pageSize);
				filtroPaginacao.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtroPaginacao.setPropriedadeOrdenacao(sortField);

				setRowCount(dao.quantidadeFiltrados(filtroPaginacao));

				return dao.filtrados(filtroPaginacao);
			}

			/**
			 * Os doi medos abaixo servem para seleciona uma lina no dataTable
			 */
			@Override
			public Usuario getRowData(String rowKey) {
				for (Usuario usuario : dao.filtrados(filtroPaginacao)) {
					if (usuario.getIdUsuario() == (Long.parseLong(rowKey))) {
						return usuario;
					}
				}
				return null;
			}

			@Override
			public Object getRowKey(Usuario usuario) {

				return usuario.getIdUsuario();
			}
		};
	}

	/**
	 * Carrega os perfis no campo selct
	 * 
	 * @return perfis
	 */
	public List<Perfil> todosPerfis() {

		List<Perfil> perfis = dao.todosPerfis();

		if (perfis.size() != 0) {

			perfis.sort((a, b) -> a.getNome().compareTo(b.getNome()));

			return perfis;
		} else {
			return null;

		}

	}

	/********************* Getter e Setter *********************/
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public FiltroPaginacao getFiltroPaginacao() {
		return filtroPaginacao;
	}

	public LazyDataModel<Usuario> getModel() {
		return model;
	}

	public boolean isTrocarSenha() {
		return trocarSenha;
	}

	public void setTrocarSenha(boolean trocarSenha) {
		this.trocarSenha = trocarSenha;
	}

	public String getTituloSenha() {
		return tituloSenha;
	}

	public void setTituloSenha(String tituloSenha) {
		this.tituloSenha = tituloSenha;
	}

}
