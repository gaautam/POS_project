package com.increff.pos.dao;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.InventoryPojo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Repository
public class InventoryDao {
	
	private static String delete_id = "delete from InventoryPojo p where id=:id";
	private static String select_id  = "select p from InventoryPojo p where id=:id";
	private static String select_barcode  = "select p from InventoryPojo p where barcode=:barcode";
	private static String select_all = "select p from InventoryPojo p";
	private static String delete_barcode = "delete from InventoryPojo p where barcode=:barcode";
	
	
	@PersistenceContext
	EntityManager em;
	
	public void insert(InventoryPojo p) {
			em.persist(p);
		}
	
	public int delete(int id) {
			Query query = em.createQuery(delete_id);
			query.setParameter("id", id);
			return query.executeUpdate();
		
	}
	
	public InventoryPojo select(int id) {
		TypedQuery<InventoryPojo> query = getQuery(select_id);
		query.setParameter("id", id);
		return query.getSingleResult();
	}
	
	public InventoryPojo selectBarcode(String barcode) {
		TypedQuery<InventoryPojo> query = getQuery(select_barcode);
		query.setParameter("barcode", barcode);
		return query.getSingleResult();
	}
	
	public void update(int id, InventoryPojo p) {
	}
	
	public void updateorder(String barcode, InventoryPojo p) {
	}

	public List<InventoryPojo> selectAll() {
		TypedQuery<InventoryPojo> query = getQuery(select_all);
		return query.getResultList();
	}
	
	TypedQuery<InventoryPojo> getQuery(String jpql){
		return em.createQuery(jpql,InventoryPojo.class);
	}

	public int deleteBarcode(String barcode) {
		Query query = em.createQuery(delete_barcode);
		query.setParameter("barcode", barcode);
		return query.executeUpdate();	
	}
}
