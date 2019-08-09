package com.javawiz.primary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javawiz.primary.entity.User;

public interface UserRepository
  extends JpaRepository<User, Integer> { }