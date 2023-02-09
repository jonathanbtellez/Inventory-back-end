package com.company.inventory.dao;

import org.springframework.data.repository.CrudRepository;

import com.company.inventory.model.Category;

public interface ICategoryDao extends CrudRepository<Category, Long>{
//	This interface contain the methods to do CRUD operations
}
