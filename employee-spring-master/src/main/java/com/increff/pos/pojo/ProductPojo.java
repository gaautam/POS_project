package com.increff.pos.pojo;

import javax.persistence.*;


@Entity
public class ProductPojo {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "Product_ID")
	private int id;
	
	@Column(name = "Barcode",unique = true)
	private String barcode;
	
	@Column(name = "Brand")
	private String brand;
	
	@Column(name = "Category")
	private String category;
	
	@Column(name = "Name")
	private String name;
	
	@Column(name = "MRP")
	private double mrp;
	
//	@ManyToOne(cascade=CascadeType.ALL)
//	@JoinColumn(name="Brand_Category2",referencedColumnName = "Brand_ID")
//	private BrandPojo b;
	
	public ProductPojo() {
		super();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getMrp() {
		return mrp;
	}
	public void setMrp(double mrp) {
		this.mrp = mrp;
	}
	
//	public BrandPojo getBrandPojo() {
//		return b;
//	}
//	
//	public void setBrandPojo(BrandPojo b) {
//		this.b = b;
//	}
	
}
