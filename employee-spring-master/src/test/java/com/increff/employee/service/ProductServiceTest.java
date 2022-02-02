package com.increff.employee.service;

import static org.junit.Assert.assertEquals;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ProductService;

@Transactional
public class ProductServiceTest extends AbstractUnitTest {
	
	@Autowired
	private ProductService service;
	
	@Test
	public void testAdd() throws ApiException {
		ProductPojo p = new ProductPojo();
		p.setBarcode(" 12345");
		p.setBrand("Nike");
		p.setCategory("shorts");
		p.setName(" Shorts1 ");
		service.add(p);
	}
	
	@Test
	public void testNormalize() {
		ProductPojo p = new ProductPojo();
		p.setBarcode(" 12345 ");
		p.setBrand("Nike");
		p.setCategory("shorts");
		p.setName(" Shorts1 ");
		ProductService.normalize(p);
		assertEquals("12345",p.getBarcode());
		assertEquals("nike",p.getBrand());
		assertEquals("shorts",p.getCategory());
		assertEquals("shorts1",p.getName());
	}
}
