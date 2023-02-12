package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.model.Category;
import com.company.inventory.response.CategoryResponseRest;

@Service
public class CategoryServicesImpl implements ICategoryServices {

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
			rest.setMetadata("Respuesta fail", "-1", "Fail in response");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(rest, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CategoryResponseRest>(rest, HttpStatus.OK);
	}

	@Transactional(readOnly = true)
	public ResponseEntity<CategoryResponseRest> searchById(Long id) {

		CategoryResponseRest rest = new CategoryResponseRest();
		List<Category> list = new ArrayList<Category>();

		try {

			Optional<Category> category = categoryDao.findById(id);

			if (category.isPresent()) {
				list.add(category.get());
				rest.getCategoryResponse().setCategory(list);
				rest.setMetadata("Response ok", "00", "Success, Category found");
			} else {
				rest.setMetadata("Respuesta fail", "-1", "Fail, category not found");
				return new ResponseEntity<CategoryResponseRest>(rest, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			rest.setMetadata("Respuesta fail", "-1", "Fail in response to find by id");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(rest, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CategoryResponseRest>(rest, HttpStatus.OK);

	}

	@Transactional
	public ResponseEntity<CategoryResponseRest> save(Category category) {
		CategoryResponseRest rest = new CategoryResponseRest();
		List<Category> list = new ArrayList<Category>();

		try {

			Category categorySaved = categoryDao.save(category);
			if (categorySaved != null) {
				list.add(categorySaved);
				rest.getCategoryResponse().setCategory(list);
				rest.setMetadata("Respuesta success", "00", "Success, category was saved");
			} else {
				rest.setMetadata("Respuesta fail", "-1", "Fail, category not saved");
				return new ResponseEntity<CategoryResponseRest>(rest, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			rest.setMetadata("Respuesta fail", "-1", "Fail to save the category");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(rest, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CategoryResponseRest>(rest, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<CategoryResponseRest> updateCategory(Category category, Long id) {
		CategoryResponseRest rest = new CategoryResponseRest();
		List<Category> list = new ArrayList<Category>();
		
		try {
			
			Optional<Category> categorySearch = categoryDao.findById(id);
			
			if(categorySearch.isPresent()) {
				// If there is a result we going to update this
				categorySearch.get().setName(category.getName());
				categorySearch.get().setDescription(category.getDescription());

				Category categoryUpdated = categoryDao.save(categorySearch.get());
				
				if (categoryUpdated != null) {
					list.add(categoryUpdated);
					rest.getCategoryResponse().setCategory(list);
					rest.setMetadata("Success response", "00", "Success, category update");
				}else {
					rest.setMetadata("Respuesta fail", "-1", "Fail, category not update");
					return new ResponseEntity<CategoryResponseRest>(rest, HttpStatus.BAD_REQUEST);
				}
				
			}else {
				rest.setMetadata("Respuesta fail", "-1", "Fail, category not update");
				return new ResponseEntity<CategoryResponseRest>(rest, HttpStatus.NOT_FOUND);
			}
			
		}catch(Exception e)	{
			rest.setMetadata("Respuesta fail", "-1", "Fail to update the category");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(rest, HttpStatus.INTERNAL_SERVER_ERROR);
		}return new ResponseEntity<CategoryResponseRest>(rest,HttpStatus.OK);

	}
	
	@Transactional
	public ResponseEntity<CategoryResponseRest> delete(Long id) {
		CategoryResponseRest rest = new CategoryResponseRest();

		try {
			categoryDao.deleteById(id);
			rest.setMetadata("Success response", "00", "Success, category deleted");
		} catch (Exception e) {
			rest.setMetadata("Respuesta fail", "-1", "Fail at delete category");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(rest, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CategoryResponseRest>(rest, HttpStatus.OK);	}

}
