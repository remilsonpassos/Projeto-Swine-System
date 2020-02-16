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

import com.jsf.dao.VacinaDao;
import com.jsf.model.Suino;
import com.jsf.model.Vacina;
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
public class VacinaController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Vacina vacina;
	@Inject
	private Suino suino;

	@Inject
	private VacinaDao dao;

	private FiltroPaginacao filtroPaginacao = new FiltroPaginacao();
	private LazyDataModel<Vacina> model;

	/**
	 * Construtor foi declarado para poder carregar LazyDataModel quando o
	 * objeto for criado
	 */
	public VacinaController() {
		carregargridvacina();
	}

	/********************* Métodos *********************/

	/**
	 * Adiciona ou Edita uma vacina
	 */
	public void salvar() {

		if (vacina.getIdVacina() == null) { // Adiciona um novo suino
			boolean salvo = dao.inserir(vacina);

			if (salvo == true) {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Vacina cadastrada com sucesso!", "Vacina cadastrada com sucesso!"));
				resetar();
			} else {
				FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Vacina não cadastrada!", "Vacina não cadastrada!"));
			}
		} else { // Atualiza um usuário
			dao.edita(vacina);
			FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Vacina atualizada com sucesso!", "Vacina atualizada com sucesso!"));
			resetar();
		}
	}

	/**
	 * Reseta os atributos do objeto
	 */
	public void resetar() {
		vacina = new Vacina();
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
		vacina = (Vacina) event.getObject();
	}

	/**
	 * Carrega as Vacinas no dataTable e trata o número de linas, quantidade
	 * total, linha selecionada, etc.
	 */
	public void carregargridvacina() {

		/**
		 * Popula o LazyDataModel
		 */
		model = new LazyDataModel<Vacina>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Vacina> load(int first, int pageSize, String sortField, SortOrder sortOrder,
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
			public Vacina getRowData(String rowKey) {
				for (Vacina vacina : dao.filtrados(filtroPaginacao)) {
					if (vacina.getIdVacina() == (Long.parseLong(rowKey))) {
						return vacina;
					}
				}
				return null;
			}

			@Override
			public Object getRowKey(Vacina vacina) {

				return vacina.getIdVacina();
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

	public Vacina getVacina() {
		return vacina;
	}

	public void setVacina(Vacina vacina) {
		this.vacina = vacina;
	}

	public LazyDataModel<Vacina> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Vacina> model) {
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
