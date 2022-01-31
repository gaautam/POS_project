package com.increff.pos.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.increff.pos.dao.OrderItemsDao;
import com.increff.pos.pojo.OrderItemsPojo;

@Service
public class OrderItemsService {
	
	@Autowired
	private OrderItemsDao dao;
	
	@Transactional
	public void add(OrderItemsPojo p) {
		normalize(p);
		dao.insert(p);
	}
	
	@Transactional
	public void delete(int id) {
			dao.delete(id);
		}
	
	@Transactional
	public OrderItemsPojo get(int id) throws ApiException {
		OrderItemsPojo p = getCheck(id);
		return p;
	}
	
	@Transactional
	public List<OrderItemsPojo> getAll() {
			return dao.selectAll();
		}
	
	@Transactional(rollbackFor = ApiException.class)
	public void update(int id, OrderItemsPojo p) throws ApiException {
		normalize(p);
		OrderItemsPojo ex = getCheck(id);
		ex.setOrderId(p.getOrderId());
		ex.setBarcode(p.getBarcode());
		ex.setQuantity(p.getQuantity());
		ex.setSelling_price(p.getSelling_price());
		dao.update(id,p);
	}
	
	@Transactional
	public OrderItemsPojo getCheck(int id) throws ApiException {
		OrderItemsPojo p = dao.select(id);
		if(p==null) {
			throw new ApiException("Order with given ID does not exist, id:" + id);
		}
		return p;
	}
	
	@Transactional
	public List<OrderItemsPojo> getCheckOrderItems(int order_id) throws ApiException {
		List<OrderItemsPojo> p = dao.selectOrderItems(order_id);
		if(p==null) {
			throw new ApiException("Order with given order ID does not exist, order_id:" + order_id);
		}
		return p;
	}
	
	@Transactional
	public List<OrderItemsPojo> getOrderItems(int order_id) throws ApiException {
		List<OrderItemsPojo> p = getCheckOrderItems(order_id);
		return p;
	}
	
	public static void normalize(OrderItemsPojo p) {
		p.setBarcode(p.getBarcode().toLowerCase().trim());

	}

	public int getItemID(int id, String barcode) throws ApiException {
		int item_id = getCheckOrderID(id,barcode);
		return item_id;	
	}

	private int getCheckOrderID(int id, String barcode) throws ApiException {
		int item_id= dao.selectOrderID(id,barcode);
		if(item_id==0) {
			throw new ApiException("OrderItem ID with given order_ID does not exist, id:" + id);
		}
		return item_id;
	}

	
	
}
