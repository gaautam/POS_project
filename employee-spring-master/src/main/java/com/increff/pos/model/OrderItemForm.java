package com.increff.pos.model;

public class OrderItemForm {
	private  int quantity;
    private  String barcode;
	private double selling_price;
	private int order_id;
	
    public double getSelling_price() {
		return selling_price;
	}

	public void setSelling_price(double selling_price) {
		this.selling_price = selling_price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

    public OrderItemForm () {}

    public OrderItemForm(int order_id, double selling_price, int quantity,  String barcode) {
    	this.order_id = order_id;
        this.selling_price = selling_price;
        this.quantity = quantity;
        this.barcode = barcode;
    }

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
}
