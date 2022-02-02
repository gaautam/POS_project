package com.increff.pos.pojo;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "Brand", "Category" }) })
public class BrandPojo {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "Brand_ID")
	private int id;
	
	@Column(name = "Brand",nullable = false)
	private String brand;
	
	@Column(name = "Category",nullable = false)
	private  String category;
	
//	@OneToMany(mappedBy = "b")
//	private Set<ProductPojo> p;
	
	public BrandPojo() {
		super();
	}
	
//	public BrandPojo(int id,String brand,String category,Set<ProductPojo> p) {
//		super();
//		this.id = id;
//		this.brand = brand;
//		this.category = category;
//		this.p = p;
//	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	
//	public Set<ProductPojo> getProductPojo(){
//		return p;
//	}
//	
//	public void setProductPojo(Set<ProductPojo> p) {
//		this.p = p;
//	}
	
}
