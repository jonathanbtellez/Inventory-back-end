package com.company.inventory.dao;

import org.springframework.data.repository.CrudRepository;

import com.company.inventory.model.Product;

public interface IProductDao extends CrudRepository<Product, Long>{
//	This interface contain the methods to do CRUD operations
}
