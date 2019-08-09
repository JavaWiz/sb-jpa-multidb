package com.javawiz.secondary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javawiz.secondary.entity.Product;

public interface ProductRepository
  extends JpaRepository<Product, Integer> { }