package com.increff.pos.controller;

import java.util.ArrayList;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.ReportForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderItemsService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.service.ReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ReportController {
	
	@Autowired
	private BrandService service;
	
	@Autowired
	private InventoryService iservice;
	
	@Autowired
	private OrderService oservice;
	
	@Autowired
	private ReportService rservice;
	
	@Autowired
	private OrderItemsService oiservice;
	
	@Autowired
	private ProductService pservice;
	
	@ApiOperation(value = "Generates a report of all brands")
	@RequestMapping(path = "/api/brand/report", method = RequestMethod.GET)
	public List<BrandData> getAllBrand() {
		List<BrandPojo> list = service.getAll();
		List<BrandData> list2 = new ArrayList<BrandData>();
		for (BrandPojo p : list) {
			list2.add(convertBrand(p));
		}
		rservice.brandReport(list2);
		return list2;
	}
	
	@ApiOperation(value = "Gets list of all inventories")
	@RequestMapping(path = "/api/inventory/report", method = RequestMethod.GET)
	public List<InventoryData> getAllInventory() {
		List<InventoryPojo> list = iservice.getAll();
		List<InventoryData> list2 = new ArrayList<InventoryData>();
		for (InventoryPojo p : list) {
			list2.add(convertInventory(p));
		}
		rservice.inventoryReport(list2);
		return list2;
	}
	
	@ApiOperation(value = "Generates a report of all Orders within a particular duration with brand and category filters")
	@RequestMapping(path = "/api/order/report/{sdate}/{edate}/{brand}/{category}", method = RequestMethod.GET)
	public List<ReportForm> getAllOrders(@PathVariable String sdate,@PathVariable String edate,@PathVariable String brand,@PathVariable String category) throws ApiException {
		List<OrderPojo> list = oservice.getDurationOrders(sdate,edate);
		List<ReportForm> list2 = new ArrayList<ReportForm>();
		for (OrderPojo p : list) {
			int id = p.getId();
			List<OrderItemsPojo> l = oiservice.getOrderItems(id);
			for (OrderItemsPojo p2 : l) {
				ProductPojo result = pservice.getFromBarcode(p2.getBarcode());
				list2.add(convertedFromBarcode(p2,result));	
			}
		}
		rservice.OrderReport(list2,brand,category,sdate,edate);
		return list2;
	}
	
	@ApiOperation(value = "Generates a report of all Orders within a particular duration")
	@RequestMapping(path = "/api/order/report/{sdate}/{edate}", method = RequestMethod.GET)
	public List<ReportForm> getAllOrderswithout(@PathVariable String sdate,@PathVariable String edate) throws ApiException {
		List<OrderPojo> list = oservice.getDurationOrders(sdate,edate);
		List<ReportForm> list2 = new ArrayList<ReportForm>();
		for (OrderPojo p : list) {
			int id = p.getId();
			List<OrderItemsPojo> l = oiservice.getOrderItems(id);
			for (OrderItemsPojo p2 : l) {
				ProductPojo result = pservice.getFromBarcode(p2.getBarcode());
				list2.add(convertedFromBarcode(p2,result));	
			}
		}
		rservice.AllOrderReport(list2,sdate,edate);
		return list2;
	}

	@ApiOperation(value = "Generates an invoice for an order")
	@RequestMapping(path = "/api/order/report/{id}", method = RequestMethod.GET)
	public List<OrderItemData> getAllOrderItems(@PathVariable int id) throws ApiException {
		List<OrderItemsPojo> list = oiservice.getOrderItems(id);
		OrderPojo order = oservice.get(id);
		OrderData converted_order = convertOrder(order);
		List<OrderItemData> list2 = new ArrayList<OrderItemData>();
		for (OrderItemsPojo p : list) {
			list2.add(convertOrderItems(p));
		}
		rservice.invoiceGenerator(list2,converted_order);
		return list2;
	}
	
	@ApiOperation(value = "Downloads invoice of an order")
	@RequestMapping(path = "/api/order/report/orderitemurl", method = RequestMethod.GET)
	public String downloadOrderItemsUrl(){
		String url = "http://localhost:9000/employee/resources/Order_Invoice.pdf";
		return url;
	}
	
	@ApiOperation(value = "Downloads report of all brands")
	@RequestMapping(path = "/api/brand/report/brandurl", method = RequestMethod.GET)
	public String downloadBrandUrl(){
		String url = "http://localhost:9000/employee/resources/Brand_Report.pdf";
		return url;
	}
	
	@ApiOperation(value = "Downloads report of the current inventory")
	@RequestMapping(path = "/api/inventory/report/inventoryurl", method = RequestMethod.GET)
	public String downloadInventoryUrl(){
		String url = "http://localhost:9000/employee/resources/Inventory_Report.pdf";
		return url;
	}
	
	@ApiOperation(value = "Downloads report of all orders in the given duration with brand and category")
	@RequestMapping(path = "/api/order/report/orderurl", method = RequestMethod.GET)
	public String downloadOrderUrl(){
		String url = "http://localhost:9000/employee/resources/Order_Report.pdf";
		return url;
	}
	
	@ApiOperation(value = "Downloads report of all orders in the given duration")
	@RequestMapping(path = "/api/order/report/consolidated_orderurl", method = RequestMethod.GET)
	public String downloadConsolidatedOrderUrl(){
		String url = "http://localhost:9000/employee/resources/Consolidated_Order_Report.pdf";
		return url;
	}
	
	private static BrandData convertBrand(BrandPojo p) {
		BrandData d = new BrandData();
		d.setBrand(p.getBrand());
		d.setCategory(p.getCategory());
		d.setId(p.getId());
		return d;
	}
	
	private static InventoryData convertInventory(InventoryPojo p) {
		InventoryData d = new InventoryData();
		d.setBarcode(p.getBarcode());
		d.setQuantity(p.getQuantity());
		d.setId(p.getId());
		return d;
	}
	
	private static OrderItemData convertOrderItems(OrderItemsPojo p) {
		OrderItemData d = new OrderItemData();
		d.setBarcode(p.getBarcode());
		d.setQuantity(p.getQuantity());
		d.setSelling_price(p.getSelling_price());
		return d;
	}
	private static OrderData convertOrder(OrderPojo p) {
		OrderData d = new OrderData();
		d.setCreateDate(p.getCreateDate());
		d.setId(p.getId());
		return d;
	}
	
	private ReportForm convertedFromBarcode(OrderItemsPojo p2, ProductPojo result) {
		ReportForm r = new ReportForm();
		r.setBrand(result.getBrand());
		r.setCategory(result.getCategory());
		r.setQuantity(p2.getQuantity());
		r.setSelling_price(p2.getSelling_price());
		return r;
		
	}
}
