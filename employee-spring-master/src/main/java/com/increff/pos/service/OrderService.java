package com.increff.pos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderPojo;


@Service
public class OrderService {
	
	@Autowired
	private OrderDao dao;
	
	@Transactional
	public int add(OrderPojo p) {
		int user_id = dao.insert(p);
		return user_id;
	}
	
	@Transactional
	public void delete(int id) {
			dao.delete(id);
		}
	
	@Transactional
	public OrderPojo get(int id) throws ApiException {
		OrderPojo p = getCheck(id);
		return p;
	}
	
	@Transactional
	public List<OrderPojo> getAll() {
			return dao.selectAll();
		}
	
	@Transactional(rollbackFor = ApiException.class)
	public void update(int id, OrderPojo p) throws ApiException {
		OrderPojo ex = getCheck(id);
		ex.setCreateDate(p.getCreateDate());
		dao.update(id,p);
	}
	
	@Transactional
	public OrderPojo getCheck(int id) throws ApiException {
		OrderPojo p = dao.select(id);
		if(p==null) {
			throw new ApiException("Order with given ID does not exist, id:" + id);
		}
		return p;
	}
	
	@Transactional
	public List<OrderPojo> getDurationOrders(String sdate,String edate) {
		return dao.selectDurationOrders(sdate,edate);
	}
	
}
