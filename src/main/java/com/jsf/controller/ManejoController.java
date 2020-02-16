package com.jsf.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import com.jsf.dao.ManejoDao;
import com.jsf.model.Alimento;
import com.jsf.model.Criadouro;
import com.jsf.model.Suino;
import com.jsf.model.Vacina;

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
public class ManejoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Suino suino;
	@Inject
	private ManejoDao dao;

	private Suino suinoSelecionado;
	private Vacina vacina;
	private Alimento alimento;
	private Criadouro criadouro;
	
	private boolean exibirManejo = false;

	public ManejoController() {
	}

	/********************* Métodos *********************/

	/**
	 * Adiciona ou Edita um suino
	 */
	public void getDados() {

		System.out.println("Modelo: " + suinoSelecionado);

		if (suinoSelecionado != null) {

			
			exibirManejo = true;
		} else {
			resetar();
			FacesContext.getCurrentInstance().addMessage("",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Suino não cadastrado!", "Suino não cadastrado!"));
		}

	}

	/**
	 * Reseta os atributos do objeto
	 */
	public void resetar() {
		exibirManejo = false;
		suino = new Suino();
		//suinoSelecionado = new Suino();
		PrimeFaces.current().resetInputs("formManejoSelect:panelGridManejo");
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

	public Suino getSuino() {
		return suino;
	}

	public void setSuino(Suino suino) {
		this.suino = suino;
	}

	public Suino getSuinoSelecionado() {
		return suinoSelecionado;
	}

	public void setSuinoSelecionado(Suino suinoSelecionado) {
		this.suinoSelecionado = suinoSelecionado;
	}

	public Vacina getVacina() {
		return vacina;
	}

	public void setVacina(Vacina vacina) {
		this.vacina = vacina;
	}

	public Alimento getAlimento() {
		return alimento;
	}

	public void setAlimento(Alimento alimento) {
		this.alimento = alimento;
	}

	public Criadouro getCriadouro() {
		return criadouro;
	}

	public void setCriadouro(Criadouro criadouro) {
		this.criadouro = criadouro;
	}

	public boolean isExibirManejo() {
		return exibirManejo;
	}

	public void setExibirManejo(boolean exibirManejo) {
		this.exibirManejo = exibirManejo;
	}

	
}
