package com.increff.pos.pojo;

import javax.persistence.*;

@Entity
public class OrderItemsPojo {

	  	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int orderItemId;
	  	
	  	@Column(name = "order_id")
	    private int orderId;

	    @Column(name = "barcode")
	    private String barcode;

	    @Column(name = "quantity")
	    private int quantity;

	    @Column(name = "selling_price")
	    private double selling_price;
	    
	    public OrderItemsPojo(){
	    	
	    }

	    public OrderItemsPojo(int orderId, String barcode, int quantity,double selling_price) {
	        this.barcode = barcode;
	        this.quantity = quantity;
	        this.selling_price = selling_price;
	        this.orderId=orderId;
	    }

		public int getOrderItemId() {
			return orderItemId;
		}

		public void setOrderItemId(int orderItemId) {
			this.orderItemId = orderItemId;
		}

		public String getBarcode() {
			return barcode;
		}

		public void setBarcode(String barcode) {
			this.barcode = barcode;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

		public double getSelling_price() {
			return selling_price;
		}

		public void setSelling_price(double selling_price) {
			this.selling_price = selling_price;
		}

		public int getOrderId() {
			return orderId;
		}

		public void setOrderId(int orderId) {
			this.orderId = orderId;
		}

}
