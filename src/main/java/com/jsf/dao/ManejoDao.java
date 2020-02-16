package com.jsf.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.jsf.model.Suino;


public class ManejoDao implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	@Inject
	private EntityManager em;
	
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
