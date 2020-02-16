package com.jsf.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 
 * 
 */
@Entity
public class Criadouro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private Long idCriadouro;

	@Column(length = 200)
	private String nome;

	@Column(columnDefinition = "TEXT") 
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "idSuino", referencedColumnName = "idSuino")
	private Suino suino;

	@Column
	private boolean ativo;

	public Long getIdCriadouro() {
		return idCriadouro;
	}

	public void setIdCriadouro(Long idCriadouro) {
		this.idCriadouro = idCriadouro;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Suino getSuino() {
		return suino;
	}

	public void setSuino(Suino suino) {
		this.suino = suino;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
}