package com.increff.employee.service;

import static org.junit.Assert.assertEquals;



import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;


@Transactional
public class InventoryServiceTest extends AbstractUnitTest {
	
	@Autowired
	private InventoryService service;
	
	@Test
	public void testAdd() throws ApiException {
		InventoryPojo p = new InventoryPojo();
		p.setBarcode(" 1234");
		p.setQuantity(100);
		service.add(p);
	}
	
	@Test
	public void testNormalize() {
		InventoryPojo p = new InventoryPojo();
		p.setBarcode(" 1234 ");
		InventoryService.normalize(p);
		assertEquals("1234",p.getBarcode());
	}
}
