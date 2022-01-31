package com.increff.pos.service;

import java.util.List;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;

@Service
public class ProductService {
	
	@Autowired
	private ProductDao dao;
	
	@Transactional
	public void add(ProductPojo p) throws ApiException {
		normalize(p);
		dao.insert(p);
	}
	
	@Transactional
	public void delete(int id) {
			dao.delete(id);
		}
	
	@Transactional
	public ProductPojo get(int id) throws ApiException {
		ProductPojo p = getCheck(id);
		return p;
	}
	
	@Transactional
	public List<ProductPojo> getBrand_Category(String brand,String category) throws ApiException {
		List<ProductPojo> p = getCheckBrand_Category(brand,category);
		return p;
	}
	
	@Transactional
	public List<ProductPojo> getAll() {
			return dao.selectAll();
		}
	
	@Transactional(rollbackFor = ApiException.class)
	public void update(int id, ProductPojo p) throws ApiException {
		normalize(p);
		ProductPojo ex = getCheck(id);
		ex.setBarcode(p.getBarcode());
		ex.setBrand(p.getBrand());
		ex.setCategory(p.getCategory());
		ex.setMrp(p.getMrp());
		ex.setName(p.getName());
		dao.update(id,p);
	}
	
	@Transactional
	public ProductPojo getCheck(int id) throws ApiException {
		ProductPojo p = dao.select(id);
		if(p==null) {
			throw new ApiException("Product with given ID does not exist, id:" + id);
		}
		return p;
	}
	
	@Transactional
	public List<ProductPojo> getCheckBrand_Category(String brand,String category) throws ApiException {
		List<ProductPojo> p = dao.selectBrand_Category(brand,category);
		if(p==null) {
			throw new ApiException("Product with given brand and category combo does not exist, brand:" + brand + " and category:"+category);
		}
		return p;
	}
	
	@Transactional
	public ProductPojo getFromBarcode(String barcode) throws ApiException {
		ProductPojo p = dao.selectfrombarcode(barcode);
		return p;
	}
	
	@Transactional
	public static void normalize(ProductPojo p) {
		p.setBarcode(p.getBarcode().toLowerCase().trim());
		p.setName(p.getName().toLowerCase().trim());
	}
	
	@Transactional
	public void deleteBrand_Category(String brand,String category) {
		
		dao.deleteBrandCategory(brand,category);
	}
		
}
