package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderPojo;

@Repository
public class OrderDao{
	
	private static String delete_id = "delete from OrderPojo p where id=:id";
	private static String select_id  = "select p from OrderPojo p where id=:id";
	private static String select_all = "select p from OrderPojo p";
	private static String select_duration_orders = "select p from OrderPojo p where createDate>= :sdate and createDate<=:edate";
	//private static String select_same_duration = "select p from OrderPojo p where createDate>:sdate and createDate<=:edate";
	
	@PersistenceContext
	EntityManager em;
	
	public int insert(OrderPojo p) {
			em.persist(p);
			em.flush();
			return p.getId();
		}
	
	public int delete(int id) {
			Query query = em.createQuery(delete_id);
			query.setParameter("id", id);
			return query.executeUpdate();
		
	}
	
	public OrderPojo select(int id) {
		TypedQuery<OrderPojo> query = getQuery(select_id);
		query.setParameter("id", id);
		return query.getSingleResult();
	}
	
	public void update(int id, OrderPojo p) {
	}

	public List<OrderPojo> selectAll() {
		TypedQuery<OrderPojo> query = getQuery(select_all);
		return query.getResultList();
	}
	
	TypedQuery<OrderPojo> getQuery(String jpql){
		return em.createQuery(jpql,OrderPojo.class);
	}

//	public List<OrderPojo> selectDurationOrders(String sdate,String edate) {
//		TypedQuery<OrderPojo> query = getQuery(select_duration_orders);
//		System.out.println(select_duration_orders);
//		System.out.println(select_same_duration);
//		System.out.println(sdate);
//		System.out.println(edate);
//		if(sdate.equals(edate)) {
//		TypedQuery<OrderPojo> query2 = getQuery(select_same_duration);
//		query2.setParameter("sdate", sdate);
//		String removal[] = sdate.split("-");
//		
//		String end = String.valueOf(Integer.parseInt(removal[0])+1)+"-"+removal[1]+"-"+removal[2] ;
//		query2.setParameter("edate", end);
//		return query2.getResultList();
//		}
//		query.setParameter("sdate", sdate);
//		query.setParameter("edate", edate);
//		return query.getResultList();
//	}
	
	public List<OrderPojo> selectDurationOrders(String sdate,String edate) {
		TypedQuery<OrderPojo> query = getQuery(select_duration_orders);
		
		sdate = sdate + " @ 00:00:00";
		edate = edate + " @ 23:59:00";

		query.setParameter("sdate", sdate);
		query.setParameter("edate", edate);
		return query.getResultList();
	}
}
