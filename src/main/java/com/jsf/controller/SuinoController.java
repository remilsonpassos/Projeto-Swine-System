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

import com.jsf.dao.SuinoDao;
import com.jsf.model.IdadeSuino;
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
public class SuinoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Suino suino;
	@Inject
	private IdadeSuino idadeSuino;
	@Inject
	private SuinoDao dao;

	private FiltroPaginacao filtroPaginacao = new FiltroPaginacao();
	private LazyDataModel<Suino> model;

	/**
	 * Construtor foi declarado para poder carregar LazyDataModel quando o
	 * objeto for criado
	 */
	public SuinoController() {
		carregargridsuino();
	}

	/********************* Métodos *********************/

	/**
	 * Adiciona ou Edita um suino
	 */
	public void salvar() {

		if (suino.getIdSuino() == null) { // Adiciona um novo suino
			boolean salvo = dao.inserir(suino);

			if (salvo == true) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Suino cadastrado com sucesso!", "Suino cadastrado com sucesso!"));
				resetar();
			} else {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Suino não cadastrado!", "Suino não cadastrado!"));
			}
		} else { // Atualiza um usuário
			dao.edita(suino);
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Suino atualizado com sucesso!", "Suino atualizado com sucesso!"));
			resetar();
		}
	}

	/**
	 * Reseta os atributos do objeto
	 */
	public void resetar() {
		suino = new Suino();
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
		suino = (Suino) event.getObject();
	}

	/**
	 * Carrega os Suino no dataTable e trata o número de linas, quantidade
	 * total, linha selecionada, etc.
	 */
	public void carregargridsuino() {

		/**
		 * Popula o LazyDataModel
		 */
		model = new LazyDataModel<Suino>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Suino> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {

				filtroPaginacao.setPrimeiroRegistro(first);
				filtroPaginacao.setQuantidadeRegistros(pageSize);
				filtroPaginacao.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtroPaginacao.setPropriedadeOrdenacao(sortField);

				setRowCount(dao.quantidadeFiltrados(filtroPaginacao));

				return dao.filtrados(filtroPaginacao);
			}

			/**
			 * Os dois metodos abaixo servem para seleciona uma lina no
			 * dataTable
			 */
			@Override
			public Suino getRowData(String rowKey) {
				for (Suino suino : dao.filtrados(filtroPaginacao)) {
					if (suino.getIdSuino() == (Long.parseLong(rowKey))) {
						return suino;
					}
				}
				return null;
			}

			@Override
			public Object getRowKey(Suino suino) {

				return suino.getIdSuino();
			}
		};
	}

	/**
	 * Carrega as Idades no campo selct
	 * 
	 * @return perfis
	 */
	public List<IdadeSuino> todasIdades() {

		List<IdadeSuino> idadeSuinos = dao.todasIdades();

		if (idadeSuinos.size() != 0) {

			idadeSuinos.sort((a, b) -> a.getNome().compareTo(b.getNome()));

			return idadeSuinos;
		} else {
			return null;

		}

	}	
	

	/********************* Getter e Setter *********************/

	public FiltroPaginacao getFiltroPaginacao() {
		return filtroPaginacao;
	}

	public LazyDataModel<Suino> getModel() {
		return model;
	}

	public Suino getSuino() {
		return suino;
	}

	public void setSuino(Suino suino) {
		this.suino = suino;
	}

	public IdadeSuino getIdadeSuino() {
		return idadeSuino;
	}

	public void setIdadeSuino(IdadeSuino idadeSuino) {
		this.idadeSuino = idadeSuino;
	}

}
