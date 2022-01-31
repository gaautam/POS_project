package com.increff.pos.dao;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.BrandPojo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Repository
public class BrandDao {
	
	private static String delete_id = "delete from BrandPojo p where id=:id";
	private static String select_id  = "select p from BrandPojo p where id=:id";
	private static String select_all = "select p from BrandPojo p";
	
	@PersistenceContext
	EntityManager em;
	
	public void insert(BrandPojo p) {
			em.persist(p);
		}
	
	public int delete(int id) {
			Query query = em.createQuery(delete_id);
			query.setParameter("id", id);
			return query.executeUpdate();
		
	}
	
	public BrandPojo select(int id) {
		TypedQuery<BrandPojo> query = getQuery(select_id);
		query.setParameter("id", id);
		return query.getSingleResult();
	}
	
	public void update(int id, BrandPojo p) {
	}

	public List<BrandPojo> selectAll() {
		TypedQuery<BrandPojo> query = getQuery(select_all);
		return query.getResultList();
	}
	
	TypedQuery<BrandPojo> getQuery(String jpql){
		return em.createQuery(jpql,BrandPojo.class);
	}
}
