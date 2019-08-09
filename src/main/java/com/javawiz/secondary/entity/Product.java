package com.javawiz.secondary.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Product {
	@Version
	private Integer version;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
	@SequenceGenerator(name = "product_id_seq", sequenceName = "PRODUCT_SEQUENCE_ID", allocationSize = 100)
	private Integer id;
	
	private String productId;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "IMAGE_URL")
	private String imageUrl;

	private BigDecimal price;
}