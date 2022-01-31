package com.increff.employee.service;

import static org.junit.Assert.assertEquals;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemsService;


@Transactional
public class OrderItemServiceTest extends AbstractUnitTest {
	
	@Autowired
	private OrderItemsService service;
	
	@Test
	public void testAdd() throws ApiException {
		OrderItemsPojo p = new OrderItemsPojo();
		p.setBarcode("123");
		p.setOrderId(5);
		p.setQuantity(100);
		p.setSelling_price(200);
		service.add(p);
	}
	
	@Test
	public void testNormalize() {
		OrderItemsPojo p = new OrderItemsPojo();
		p.setBarcode(" 123aBc ");
		OrderItemsService.normalize(p);
		assertEquals("123abc",p.getBarcode());
	}
}
