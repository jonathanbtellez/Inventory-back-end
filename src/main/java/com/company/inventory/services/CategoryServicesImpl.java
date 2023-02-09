package com.company.inventory.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.model.Category;
import com.company.inventory.response.CategoryResponseRest;

@Service
public class CategoryServicesImpl implements ICategoryServices{
	
	@Autowired
	private ICategoryDao categoryDao;
	
	@Transactional(readOnly = true)
	public ResponseEntity<CategoryResponseRest> search() {
		CategoryResponseRest rest = new CategoryResponseRest();
		
		try {
			List<Category> category = (List<Category>) categoryDao.findAll();
			rest.getCategoryResponse().setCategory(category);
			
			rest.setMetadata("Respuesta ok", "00", "Success response");
		} catch (Exception e) {
			rest.setMetadata("Respuesta fail", "01", "Fail in response");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(rest, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CategoryResponseRest>(rest, HttpStatus.OK);
	}
	

}
