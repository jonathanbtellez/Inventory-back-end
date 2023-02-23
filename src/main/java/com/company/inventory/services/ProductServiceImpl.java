package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;

@Service
public class ProductServiceImpl implements IProductService {

	private ICategoryDao categoryDao;
	
	private IProductDao productDao;
	
	public ProductServiceImpl(ICategoryDao categoryDao, IProductDao productDao) {
		
		super();
		this.categoryDao = categoryDao;
		this.productDao = productDao;
	}



	public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId) {
		
		System.out.println(categoryId);
		ProductResponseRest responseRest = new ProductResponseRest();
		List<Product> list = new ArrayList<Product>();
		
		try {
			// Search category to set in the product object
			Optional<Category> category = categoryDao.findById(categoryId);
			
			if(category.isPresent()) {
			
				product.setCategory(category.get());
			
			}else {
			
				responseRest.setMetadata("Fail Response", "-1", "Category not found");
				return new ResponseEntity<ProductResponseRest>(responseRest, HttpStatus.NOT_FOUND);				
			
			}
			//Save product
			Product productSaved = productDao.save(product);
			
			if(productSaved != null) {
			
				list.add(productSaved);
				responseRest.getProduct().setProducts(list);
			
			}else {
		
				responseRest.setMetadata("Fail Response", "-1", "Product not saved");
				return new ResponseEntity<ProductResponseRest>(responseRest, HttpStatus.BAD_REQUEST);
			
			}
		
		} catch (Exception e) {
			
			e.getStackTrace();
			responseRest.setMetadata("Fail Response", "-1", "Fail to saved product");
			return new ResponseEntity<ProductResponseRest>(responseRest, HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
		
		return new ResponseEntity<ProductResponseRest>(responseRest, HttpStatus.OK);
	}

}
