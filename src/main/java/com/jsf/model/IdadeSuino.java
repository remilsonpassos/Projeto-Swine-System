package com.jsf.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.jsf.interfaces.SampleEntity;

@Entity
public class IdadeSuino implements Serializable, SampleEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private Long idIdade;

	@Column(length=20)
	private String nome;
	
	@Column(length=50)
	private String descricao;

	public Long getIdIdade() {
		return idIdade;
	}

	public void setIdIdade(Long idIdade) {
		this.idIdade = idIdade;
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
	
	@Override
	public Long getId() {		
		return idIdade;
	}
	
	@Override
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IdadeSuino))
			return false;
		IdadeSuino other = (IdadeSuino) obj;
		if (getId() == null){
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

}
