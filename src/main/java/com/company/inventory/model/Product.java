package com.company.inventory.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private int price;
	
	private int quantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIncludeProperties({"hibernateLazyInitializer", "handler"})
	private Category category;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column( name = "picture")
	private byte[] picture;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
