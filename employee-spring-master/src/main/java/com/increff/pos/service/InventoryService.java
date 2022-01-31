package com.increff.pos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;

@Service
public class InventoryService {
	
	@Autowired
	private InventoryDao dao;
	
	@Transactional
	public void add(InventoryPojo p) {
		normalize(p);
		dao.insert(p);
	}
	
	@Transactional
	public void delete(int id) {
			dao.delete(id);
		}
	
	@Transactional
	public void deleteBarcode(String barcode) {
			dao.deleteBarcode(barcode);
		}
	
	@Transactional
	public InventoryPojo get(int id) throws ApiException {
		InventoryPojo p = getCheck(id);
		return p;
	}
	
	@Transactional
	public List<InventoryPojo> getAll() {
			return dao.selectAll();
		}
	
	@Transactional(rollbackFor = ApiException.class)
	public void update(int id, InventoryPojo p) throws ApiException {
		normalize(p);
		InventoryPojo ex = getCheck(id);
		ex.setBarcode(p.getBarcode());
		ex.setQuantity(p.getQuantity());
		dao.update(id,p);
	}
	
	@Transactional(rollbackFor = ApiException.class)
	public void updateOrder(String barcode, InventoryPojo p) throws ApiException {
		normalize(p);
		InventoryPojo ex = getCheckBarcode(barcode);
		ex.setBarcode(p.getBarcode());
		ex.setQuantity(p.getQuantity());
		dao.updateorder(barcode,p);
	}
	
	@Transactional
	public InventoryPojo getCheck(int id) throws ApiException {
		InventoryPojo p = dao.select(id);
		if(p==null) {
			throw new ApiException("Product in Inventory with given ID does not exist, id:" + id);
		}
		return p;
	}
	
	@Transactional
	public InventoryPojo getCheckBarcode(String barcode) throws ApiException {
		InventoryPojo p = dao.selectBarcode(barcode);
		if(p==null) {
			throw new ApiException("Product in Inventory with given barcode does not exist, barcode:" + barcode);
		}
		return p;
	}
	
	@Transactional
	public static void normalize(InventoryPojo p) {
		p.setBarcode(p.getBarcode().toLowerCase().trim());
	}
}
