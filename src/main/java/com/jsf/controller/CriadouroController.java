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

import com.jsf.dao.CriadouroDao;
import com.jsf.model.Criadouro;
import com.jsf.model.Suino;
import com.jsf.util.FiltroPaginacao;

/**
 * 1) Linha 32 - Antes utilizavamos o @RequestScoped. Tive que auterar no
 * pom.xml o glassfish da versão 2.1.17 para 2.2.7 que permite a utilização
 * do @ViewScoped do pacote javax.faces.view.ViewScoped. (obs: no livro tem a
 * diferença entre as anotaçõe)
 * 
 * 2) Linhas 47 a 51 - São declarados os novos atributos
 * 
 * 3) As demais auterações estão com comentário!
 * 
 * @author Rafhael em: 26/01/2019
 *
 */

@Named
@ViewScoped
public class CriadouroController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Criadouro criadouro;
	@Inject
	private Suino suino;

	@Inject
	private CriadouroDao dao;

	private FiltroPaginacao filtroPaginacao = new FiltroPaginacao();
	private LazyDataModel<Criadouro> model;

	/**
	 * Construtor foi declarado para poder carregar LazyDataModel quando o
	 * objeto for criado
	 */
	public CriadouroController() {
		carregargridcriadouro();
	}

	/********************* Métodos *********************/

	/**
	 * Adiciona ou Edita um criadouro
	 */
	public void salvar() {

		if (criadouro.getIdCriadouro() == null) { // Adiciona um novo criadouro
			boolean salvo = dao.inserir(criadouro);

			if (salvo == true) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO,
						"ciradouro cadastrado com sucesso!", "Criadouro cadastrado com sucesso!"));
				resetar();
			} else {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Criadouro não cadastrada!", "Criadouro não cadastrada!"));
			}
		} else { // Atualiza um usuário
			dao.edita(criadouro);
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Suino atualizado com sucesso!", "Suino atualizado com sucesso!"));
			resetar();
		}
	}

	/**
	 * Reseta os atributos do objeto
	 */
	public void resetar() {
		criadouro = new Criadouro();
		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
				.findComponent("dataForm:tabela");
		dataTable.resetValue();
		PrimeFaces.current().resetInputs("form:panelGrid");
	}

	/**
	 * Seleção do objeto no dataTable e coloca no formulário para edição
	 * 
	 * @param event
	 */
	public void onRowSelect(SelectEvent event) {
		criadouro = (Criadouro) event.getObject();
		PrimeFaces.current().resetInputs("form:panelGrid");
	}

	/**
	 * Carrega as Criadouros no dataTable e trata o número de linas, quantidade
	 * total, linha selecionada, etc.
	 */
	public void carregargridcriadouro() {

		/**
		 * Popula o LazyDataModel
		 */
		model = new LazyDataModel<Criadouro>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Criadouro> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {

				filtroPaginacao.setPrimeiroRegistro(first);
				filtroPaginacao.setQuantidadeRegistros(pageSize);
				filtroPaginacao.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtroPaginacao.setPropriedadeOrdenacao(sortField);

				setRowCount(dao.quantidadeFiltrados(filtroPaginacao));

				return dao.filtrados(filtroPaginacao);
			}

			/**
			 * Os dois metodos abaixo servem para seleciona uma linha no
			 * dataTable
			 */
			@Override
			public Criadouro getRowData(String rowKey) {
				for (Criadouro criadouro : dao.filtrados(filtroPaginacao)) {
					if (criadouro.getIdCriadouro() == (Long.parseLong(rowKey))) {
						return criadouro;
					}
				}
				return null;
			}

			@Override
			public Object getRowKey(Criadouro criadouro) {

				return criadouro.getIdCriadouro();
			}
		};
	}

	/**
	 * Carrega os suinos no campo selct
	 * 
	 * @return suinos
	 */
	public List<Suino> todosSuinos() {

		List<Suino> todosSuinos = dao.todosSuinos();

		if (todosSuinos.size() != 0) {

			todosSuinos.sort((a, b) -> a.getRaca().compareTo(b.getRaca()));

			return todosSuinos;
		} else {
			return null;

		}

	}

	/********************* Getter e Setter *********************/

	public FiltroPaginacao getFiltroPaginacao() {
		return filtroPaginacao;
	}

	public Criadouro getCriadouro() {
		return criadouro;
	}

	public void setCriadouro(Criadouro criadouro) {
		this.criadouro = criadouro;
	}

	public LazyDataModel<Criadouro> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Criadouro> model) {
		this.model = model;
	}

	public void setFiltroPaginacao(FiltroPaginacao filtroPaginacao) {
		this.filtroPaginacao = filtroPaginacao;
	}

	public Suino getSuino() {
		return suino;
	}

	public void setSuino(Suino suino) {
		this.suino = suino;
	}
	
	

}
