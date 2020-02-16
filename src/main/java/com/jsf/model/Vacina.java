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
public class Vacina implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private Long idVacina;

	@Column(length = 200)
	private String nome;

	@Column(length = 250)
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "idSuino", referencedColumnName = "idSuino")
	private Suino suino;

	@Column
	private boolean ativo;

	public boolean isAtivo() {
		return ativo;
	}

	public Suino getSuino() {
		return suino;
	}

	public void setSuino(Suino suino) {
		this.suino = suino;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Long getIdVacina() {
		return idVacina;
	}

	public void setIdVacina(Long idVacina) {
		this.idVacina = idVacina;
	}

	public Vacina() {
	}

	public String getNome() {
		return this.nome;
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
	/*
	@Override
	public Long getId() {		
		return idVacina;
	}
	
	@Override
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Vacina))
			return false;
		Vacina other = (Vacina) obj;
		if (getId() == null){
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}*/

}