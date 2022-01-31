package com.increff.pos.dao;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.ProductPojo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Repository
public class ProductDao {
	
	private static String delete_id = "delete from ProductPojo p where id=:id";
	private static String delete_brand_category = "delete from ProductPojo p where brand=:brand and category=:category";
	private static String select_id  = "select p from ProductPojo p where id=:id";
	private static String select_all = "select p from ProductPojo p";
	private static String select_brand_category  = "select p from ProductPojo p where brand=:brand and category=:category";
	private static String select_from_barcode = "select p from ProductPojo p where barcode=:barcode";
	
	@PersistenceContext
	EntityManager em;
	
	public void insert(ProductPojo p) {
			em.persist(p);
		}
	
	public int delete(int id) {
			Query query = em.createQuery(delete_id);
			query.setParameter("id", id);
			return query.executeUpdate();
		
	}
	
	public ProductPojo select(int id) {
		TypedQuery<ProductPojo> query = getQuery(select_id);
		query.setParameter("id", id);
		return query.getSingleResult();
	}
	
	public void update(int id, ProductPojo p) {
	}

	public List<ProductPojo> selectAll() {
		TypedQuery<ProductPojo> query = getQuery(select_all);
		return query.getResultList();
	}
	
	TypedQuery<ProductPojo> getQuery(String jpql){
		return em.createQuery(jpql,ProductPojo.class);
	}

	public int deleteBrandCategory(String brand,String category) {
		Query query = em.createQuery(delete_brand_category);
		query.setParameter("brand", brand);
		query.setParameter("category",category);
		return query.executeUpdate();	
	}

	public List<ProductPojo> selectBrand_Category(String brand,String category) {
		TypedQuery<ProductPojo> query = getQuery(select_brand_category);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return query.getResultList();
	}

	public ProductPojo selectfrombarcode(String barcode) {
		TypedQuery<ProductPojo> query = getQuery(select_from_barcode);
		query.setParameter("barcode", barcode);
		return query.getSingleResult();
	}

}
