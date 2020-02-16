package com.jsf.dao;

import java.io.Serializable;
import java.util.Date;
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

import com.jsf.model.Perfil;
import com.jsf.model.Usuario;
import com.jsf.util.FiltroPaginacao;
import com.jsf.util.SHA1Util;


public class UsuarioDao implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	@Inject
	private EntityManager em;

	public Usuario getUsuario(String login, String senha) {

		try {

			StringBuffer hql = new StringBuffer();
			hql.append("SELECT u FROM Usuario u ");
			hql.append("WHERE u.login = :login AND u.senha = :senha");

			Query query = this.em.createQuery(hql.toString());
			query.setParameter("login", login);
			query.setParameter("senha", SHA1Util.shaPassword(senha));

			return (Usuario) query.getSingleResult();

		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional
	public boolean inserir(Usuario usuario) {
		try {

			usuario.setAtivo(true);
			usuario.setDataRegistro(new Date());
			usuario.setSenha(SHA1Util.shaPassword(usuario.getSenha()));
			usuario.setCpf(usuario.getCpf().replaceAll("[.-]", ""));

			em.persist(usuario);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Transactional
	public boolean edita(Usuario usuario) {
		try {
			Usuario usuarioBase = em.find(Usuario.class, usuario.getIdUsuario());
			
			if(usuarioBase.getSenha() != usuario.getSenha()){
				usuario.setSenha(SHA1Util.shaPassword(usuario.getSenha()));
			} 			
			usuario.setDataRegistro(new Date());			
			usuario.setCpf(usuario.getCpf().replaceAll("[.-]", ""));

			em.merge(usuario);

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
	public List<Usuario> filtrados(FiltroPaginacao filtro) {
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
		Criteria criteria = session.createCriteria(Usuario.class);
		
		if (StringUtils.isNotEmpty(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}
		
		return criteria;
	}
	// Fim da Paginação Lazy Model	 

	@SuppressWarnings("unchecked")
	public List<Perfil> todosPerfis() {
		try {

			StringBuffer hql = new StringBuffer();
			hql.append("SELECT p FROM Perfil p ");

			Query query = this.em.createQuery(hql.toString());

			return query.getResultList();

		} catch (NoResultException e) {
			return null;
		}
	}
	
	
}
