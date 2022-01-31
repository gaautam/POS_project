package com.increff.pos.controller;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderItemController {


	@Autowired
	private OrderItemsService service;

	@ApiOperation(value = "Adds an orderitem")
	@RequestMapping(path = "/api/orderitem", method = RequestMethod.POST)
	public void add(@RequestBody List<OrderItemForm> form) {
		System.out.println("Hello");
		for (OrderItemForm f : form) {
            OrderItemsPojo p = convert(f);
            service.add(p);
        }
				
	}

	
	@ApiOperation(value = "Deletes an orderitem of product")
	@RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.DELETE)
	// /api/1
	public void delete(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Gets an orderitem by ID")
	@RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.GET)
	public OrderItemData get(@PathVariable int id) throws ApiException {
		OrderItemsPojo p = service.get(id);
		return convert(p);
	}
	
	@ApiOperation(value = "Gets the orderItems from the orderID")
	@RequestMapping(path = "/api/order_item_by_order_id/{order_id}", method = RequestMethod.GET)
	public List<OrderItemsPojo> getOrderItems(@PathVariable int order_id) throws ApiException {
		List<OrderItemsPojo> p = service.getOrderItems(order_id);
		return p;
	}

	@ApiOperation(value = "Gets list of all orderitem")
	@RequestMapping(path = "/api/orderitem", method = RequestMethod.GET)
	public List<OrderItemData> getAll() {
		List<OrderItemsPojo> list = service.getAll();
		List<OrderItemData> list2 = new ArrayList<OrderItemData>();
		for (OrderItemsPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates an orderitem info")
	@RequestMapping(path = "/api/orderitem/{id}/", method = RequestMethod.PUT)
	public void update(@PathVariable int id,@RequestBody OrderItemForm f) throws ApiException {
		OrderItemsPojo p = convert(f);
		service.update(id, p);
	}
	
	
	@ApiOperation(value = "Get an order item ID from barcode and order_id combo")
	@RequestMapping(path = "/api/orderitem/{id}/{barcode}", method = RequestMethod.GET)
	public int getOrderItemID(@PathVariable int id,@PathVariable String barcode) throws ApiException {
		return service.getItemID(id,barcode);
	}

	private static OrderItemData convert(OrderItemsPojo p) {
		OrderItemData d = new OrderItemData();
		d.setBarcode(p.getBarcode());
		d.setQuantity(p.getQuantity());
		d.setSelling_price(p.getSelling_price());
		d.setOrderItemId(p.getOrderItemId());
		d.setOrder_id(p.getOrderId());
		return d;
	}

	private static OrderItemsPojo convert(OrderItemForm f) {
		OrderItemsPojo p = new OrderItemsPojo();
		p.setBarcode(f.getBarcode());
		p.setQuantity(f.getQuantity());
		p.setSelling_price(f.getSelling_price());
		p.setOrderId(f.getOrder_id());
		return p;
	}

}
