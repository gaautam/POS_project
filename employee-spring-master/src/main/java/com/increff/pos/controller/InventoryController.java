package com.increff.pos.controller;

import java.util.ArrayList;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InventoryController {

	@Autowired
	private InventoryService service;

	@ApiOperation(value = "Adds a brand")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
	public void add(@RequestBody InventoryForm form) {
		InventoryPojo p = convert(form);
		service.add(p);
	}

	
	@ApiOperation(value = "Deletes an inventory of product")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.DELETE)
	// /api/1
	public String delete(@PathVariable int id) {
		
		try {
			service.delete(id);
			return "Successfull";
		} catch (Exception e) {
			return e.toString();
		}
	}

	@ApiOperation(value = "Gets an inventory by ID")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable int id) throws ApiException {
		InventoryPojo p = service.get(id);
		return convert(p);
	}

	@ApiOperation(value = "Gets list of all inventories")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public List<InventoryData> getAll() {
		List<InventoryPojo> list = service.getAll();
		List<InventoryData> list2 = new ArrayList<InventoryData>();
		for (InventoryPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates an inventory info")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody InventoryForm f) throws ApiException {
		InventoryPojo p = convert(f);
		service.update(id, p);
	}
	
	@ApiOperation(value = "Updates an order info")
	@RequestMapping(path = "/api/order_update/{barcode}", method = RequestMethod.PUT)
	public void orderUpdate(@PathVariable String barcode, @RequestBody InventoryForm f) throws ApiException {
		InventoryPojo p = convert(f);
		service.updateOrder(barcode, p);
	}
	

	private static InventoryData convert(InventoryPojo p) {
		InventoryData d = new InventoryData();
		d.setBarcode(p.getBarcode());
		d.setQuantity(p.getQuantity());
		d.setId(p.getId());
		return d;
	}

	private static InventoryPojo convert(InventoryForm f) {
		InventoryPojo p = new InventoryPojo();
		p.setBarcode(f.getBarcode());
		p.setQuantity(f.getQuantity());
		return p;
	}

}
