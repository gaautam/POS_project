package com.increff.pos.service;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;

@Service
public class BrandService {
	
	@Autowired
	private BrandDao dao;
	
	@Transactional
	public void add(BrandPojo p) {
		normalize(p);
		dao.insert(p);
	}
	
	@Transactional
	public void delete(int id) {
			dao.delete(id);
		}
	
	@Transactional
	public BrandPojo get(int id) throws ApiException {
		BrandPojo p = getCheck(id);
		return p;
	}
	
	@Transactional
	public List<BrandPojo> getAll() {
			return dao.selectAll();
		}
	
	@Transactional(rollbackFor = ApiException.class)
	public void update(int id, BrandPojo p) throws ApiException {
		normalize(p);
		BrandPojo ex = getCheck(id);
		ex.setBrand(p.getBrand());
		ex.setCategory(p.getCategory());
		dao.update(id,p);
	}
	
	@Transactional
	public BrandPojo getCheck(int id) throws ApiException {
		BrandPojo p = dao.select(id);
		if(p==null) {
			throw new ApiException("Brand with given ID does not exist, id:" + id);
		}
		return p;
	}
	
	@Transactional
	public static void normalize(BrandPojo p) {
		p.setBrand(p.getBrand().toLowerCase().trim());
		p.setCategory(p.getCategory().toLowerCase().trim());

	}
}
