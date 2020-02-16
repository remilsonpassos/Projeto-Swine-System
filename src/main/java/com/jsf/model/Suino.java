package com.jsf.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.jsf.interfaces.SampleEntity;

/**
 * The persistent class for the suino database table.
 * 
 */
@Entity
public class Suino implements Serializable, SampleEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private Long idSuino;

	@Column(length = 100)
	private String raca;

	@Column(length = 10, precision = 2)
	private double peso;

	/**
	 * Terá como M para macho e F para fêmea
	 */
	@Column(length = 1)
	private String sexo;

	@Column
	private boolean ativo;

	@OneToOne
	@JoinColumn(name = "idIdade", nullable = false)
	private IdadeSuino idadeSuino;

	@OneToMany(mappedBy = "suino", fetch = FetchType.EAGER)
	private Set<Vacina> vacina;

	@OneToMany(mappedBy = "suino", fetch = FetchType.EAGER)
	private Set<Criadouro> criadouro;

	@OneToMany(mappedBy = "suino", fetch = FetchType.EAGER)
	private Set<Alimento> alimento;

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Long getIdSuino() {
		return idSuino;
	}

	public void setIdSuino(Long idSuino) {
		this.idSuino = idSuino;
	}

	public String getRaca() {
		return raca;
	}

	public void setRaca(String raca) {
		this.raca = raca;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public IdadeSuino getIdadeSuino() {
		return idadeSuino;
	}

	public void setIdadeSuino(IdadeSuino idadeSuino) {
		this.idadeSuino = idadeSuino;
	}

	public Set<Vacina> getVacina() {
		return vacina;
	}

	public void setVacina(Set<Vacina> vacina) {
		this.vacina = vacina;
	}

	public Set<Criadouro> getCriadouro() {
		return criadouro;
	}

	public void setCriadouro(Set<Criadouro> criadouro) {
		this.criadouro = criadouro;
	}

	public Set<Alimento> getAlimento() {
		return alimento;
	}

	public void setAlimento(Set<Alimento> alimento) {
		this.alimento = alimento;
	}

	@Override
	public Long getId() {
		return idSuino;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Suino))
			return false;
		Suino other = (Suino) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

}