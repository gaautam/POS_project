package com.increff.pos.controller;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductController {

	@Autowired
	private ProductService service;
	
	@Autowired
	private InventoryService iservice;
	

	@ApiOperation(value = "Adds a product")
	@RequestMapping(path = "/api/product", method = RequestMethod.POST)
	public void add(@RequestBody ProductForm form) throws ApiException {
		ProductPojo p = convert(form);
		service.add(p);
	}

	
	@ApiOperation(value = "Deletes a product")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.DELETE)
	// /api/1
	public String delete(@PathVariable int id) throws ApiException {
		try {
			ProductPojo p = service.get(id);
			service.delete(id);
			iservice.deleteBarcode(p.getBarcode());
			return "Successfull";
		} catch (Exception e) {
			return e.toString();
		}
	}
	
	@ApiOperation(value = "Gets a product by ID")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
	public ProductData get(@PathVariable int id) throws ApiException {
		ProductPojo p = service.get(id);
		return convert(p);
	}

	@ApiOperation(value = "Gets list of all products")
	@RequestMapping(path = "/api/product", method = RequestMethod.GET)
	public List<ProductData> getAll() {
		List<ProductPojo> list = service.getAll();
		List<ProductData> list2 = new ArrayList<ProductData>();
		for (ProductPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates a product info")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody ProductForm f) throws ApiException {
		ProductPojo p = convert(f);
		service.update(id, p);
	}
	

	private static ProductData convert(ProductPojo p) {
		ProductData d = new ProductData();
		d.setBarcode(p.getBarcode());
		d.setBrand(p.getBrand());
		d.setCategory(p.getCategory());
		d.setMrp(p.getMrp());
		d.setName(p.getName());
		d.setId(p.getId());
		return d;
	}

	private static ProductPojo convert(ProductForm f) {
		ProductPojo p = new ProductPojo();
		p.setBarcode(f.getBarcode());
		p.setBrand(f.getBrand());
		p.setCategory(f.getCategory());
		p.setMrp(f.getMrp());
		p.setName(f.getName());
		return p;
	}

}
