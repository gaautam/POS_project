package com.increff.pos.controller;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandController {

	@Autowired
	private BrandService service;
	
	@Autowired
	private ProductService pservice;
	
	@Autowired
	private InventoryService iservice;

	@ApiOperation(value = "Adds a brand")
	@RequestMapping(path = "/api/brand", method = RequestMethod.POST)
	public void add(@RequestBody BrandForm form) {
		BrandPojo p = convert(form);
		service.add(p);
	}

	
	@ApiOperation(value = "Deletes a Brand")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.DELETE)
	// /api/1
	public String delete(@PathVariable int id) throws ApiException {
		try {
			BrandPojo p1 = service.get(id);
			String brand = p1.getBrand();
			String category = p1.getCategory();
			List<ProductPojo> p2 = pservice.getBrand_Category(brand,category);
			service.delete(id);
			pservice.deleteBrand_Category(p1.getBrand(),p1.getCategory());
			for (ProductPojo item : p2) {
				iservice.deleteBarcode(item.getBarcode());
			}
			return "Successfull";
		} catch (Exception e) {
			return e.toString();
		}
	}

	@ApiOperation(value = "Gets a brand by ID")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
	public BrandData get(@PathVariable int id) throws ApiException {
		BrandPojo p = service.get(id);
		return convert(p);
	}

	@ApiOperation(value = "Gets list of all brands")
	@RequestMapping(path = "/api/brand", method = RequestMethod.GET)
	public List<BrandData> getAll() {
		List<BrandPojo> list = service.getAll();
		List<BrandData> list2 = new ArrayList<BrandData>();
		for (BrandPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates a brand info")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody BrandForm f) throws ApiException {
		BrandPojo p = convert(f);
		service.update(id, p);
	}
	

	private static BrandData convert(BrandPojo p) {
		BrandData d = new BrandData();
		d.setBrand(p.getBrand());
		d.setCategory(p.getCategory());
		d.setId(p.getId());
		return d;
	}

	private static BrandPojo convert(BrandForm f) {
		BrandPojo p = new BrandPojo();
		p.setBrand(f.getBrand());
		p.setCategory(f.getCategory());
		return p;
	}

}
