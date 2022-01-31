package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.BrandService;

@Transactional
public class BrandServiceTest extends AbstractUnitTest {
	
	@Autowired
	private BrandService service;
	
	@Test
	public void testAdd() {
		BrandPojo p = new BrandPojo();
		p.setBrand(" Flying Machine ");
		p.setCategory(" Shirt ");
		service.add(p);
	}
	
	@Test
	public void testNormalize() {
		BrandPojo p = new BrandPojo();
		p.setBrand(" Flying Machine ");
		p.setCategory(" Shirt ");
		BrandService.normalize(p);
		assertEquals("flying machine",p.getBrand());
		assertEquals("shirt",p.getCategory());
	}
}
