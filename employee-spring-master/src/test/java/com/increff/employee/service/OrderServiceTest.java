package com.increff.employee.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;


@Transactional
public class OrderServiceTest extends AbstractUnitTest {
	
	@Autowired
	private OrderService service;
	
	@Test
	public void testAdd() throws ApiException {
		OrderPojo p = new OrderPojo();
		p.setCreateDate("15-1-2022");
		service.add(p);
	}
}
