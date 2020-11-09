package com.deadlybeat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deadlybeat.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

}
