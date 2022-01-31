package com.increff.pos.dao;

import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderItemsPojo;

@Repository
public class OrderItemsDao{
	
	private static String delete_id = "delete from OrderItemsPojo p where id=:id";
	private static String select_id  = "select p from OrderItemsPojo p where id=:id";
	private static String select_order_id  = "select p from OrderItemsPojo p where order_id=:order_id";
	private static String select_all = "select p from OrderItemsPojo p";
	private static String search_itemID = "select p.orderItemId from OrderItemsPojo p where order_id=:order_id and barcode=:barcode";
	
	@PersistenceContext
	EntityManager em;
	
	public void insert(OrderItemsPojo p) {
			em.persist(p);
		}
	
	public int delete(int id) {
			Query query = em.createQuery(delete_id);
			query.setParameter("id", id);
			return query.executeUpdate();
		
	}
	
	public OrderItemsPojo select(int id) {
		TypedQuery<OrderItemsPojo> query = getQuery(select_id);
		query.setParameter("id", id);
		return query.getSingleResult();
	}
	
	public void update(int id, OrderItemsPojo p) {
	}

	public List<OrderItemsPojo> selectAll() {
		TypedQuery<OrderItemsPojo> query = getQuery(select_all);
		return query.getResultList();
	}
	
	TypedQuery<OrderItemsPojo> getQuery(String jpql){
		return em.createQuery(jpql,OrderItemsPojo.class);
	}

	public List<OrderItemsPojo> selectOrderItems(int order_id) {
		TypedQuery<OrderItemsPojo> query = getQuery(select_order_id);
		query.setParameter("order_id", order_id);
		return query.getResultList();
	}

	public int selectOrderID(int id, String barcode) {
		Query query = em.createQuery(search_itemID);
		query.setParameter("order_id", id);
		query.setParameter("barcode", barcode);
		return (int) query.getSingleResult();
	}
}
