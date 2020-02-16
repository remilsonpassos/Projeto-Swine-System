package com.jsf.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.jsf.model.IdadeSuino;
import com.jsf.model.Suino;
import com.jsf.util.FiltroPaginacao;


public class SuinoDao implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	@Inject
	private EntityManager em;

	public Suino todos() {

		try {

			StringBuffer hql = new StringBuffer();
			hql.append("SELECT u FROM Suino u ");		

			Query query = this.em.createQuery(hql.toString());			

			return (Suino) query.getSingleResult();

		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional
	public boolean inserir(Suino suino) {
		try {

			em.persist(suino);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Transactional
	public boolean edita(Suino suino) {
		try {			
			
			em.merge(suino);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Paginação Lazy Model	
	 * Seleciona os usuário para o data table limitando o seu tamanho
	 * por paginação.
	 * A cada paginação é realizada a consulta no banco que traz a quantidade exata de registros delimitados. 
	 *
	 */
	@SuppressWarnings("unchecked")
	public List<Suino> filtrados(FiltroPaginacao filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		
		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());
		
		if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.asc(filtro.getPropriedadeOrdenacao()));
		} else if (filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.desc(filtro.getPropriedadeOrdenacao()));
		}
		
		return criteria.list();
	}
	
	/**
	 * Traz a quantidade de registros a serem exibidas no dataTable
	 */
	public int quantidadeFiltrados(FiltroPaginacao filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		
		criteria.setProjection(Projections.rowCount());
		
		return ((Number) criteria.uniqueResult()).intValue();
	}
	
	/**
	 * Cria um critério do primeiro registro até o seu máximo
	 */
	private Criteria criarCriteriaParaFiltro(FiltroPaginacao filtro) {
		Session session = em.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Suino.class);
		
		if (StringUtils.isNotEmpty(filtro.getNome())) {
			criteria.add(Restrictions.ilike("raca", filtro.getNome(), MatchMode.ANYWHERE));
		}
		
		return criteria;
	}
	// Fim da Paginação Lazy Model	 

	/**
	 * Popula o select
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<IdadeSuino> todasIdades() {
		try {

			StringBuffer hql = new StringBuffer();
			hql.append("SELECT p FROM IdadeSuino p ");

			Query query = this.em.createQuery(hql.toString());

			return query.getResultList();

		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Suino> todosSuinos(){
		
		try {	
		StringBuffer hql = new StringBuffer();
		hql.append("SELECT p FROM Suino p ");
		
		Query query = this.em.createQuery(hql.toString());

		return query.getResultList();
		
		} catch (NoResultException e) {
			return null;
		}
	}
	
}
