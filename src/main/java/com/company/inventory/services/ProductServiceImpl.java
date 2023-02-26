package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.util.Util;

@Service
public class ProductServiceImpl implements IProductService {

	private ICategoryDao categoryDao;
	
	private IProductDao productDao;
	
	public ProductServiceImpl(ICategoryDao categoryDao, IProductDao productDao) {
		
		super();
		this.categoryDao = categoryDao;
		this.productDao = productDao;
	}

	@Transactional
	public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId) {
		
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
	
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> searchById(Long id) {
		
		ProductResponseRest responseRest = new ProductResponseRest();
		List<Product> list = new ArrayList<Product>();
		
		try {
			// Search product by id
			Optional<Product> product = productDao.findById(id);
			if(product.isPresent()) {
			
				byte[] imagenDescompressed = Util.decompressZLib(product.get().getPicture());
				product.get().setPicture(imagenDescompressed);
				list.add(product.get());
				responseRest.getProduct().setProducts(list);
				System.out.println(responseRest.getProduct());
				responseRest.setMetadata("Success Response", "00", "Product was found");
				return new ResponseEntity<ProductResponseRest>(responseRest, HttpStatus.OK);
				
			}else {
			
				responseRest.setMetadata("Fail Response", "-1", "Product not found");
				return new ResponseEntity<ProductResponseRest>(responseRest, HttpStatus.NOT_FOUND);				
			
			}
		
		} catch (Exception e) {
			
			e.getStackTrace();
			responseRest.setMetadata("Fail Response", "-1", "Fail to search product");
			return new ResponseEntity<ProductResponseRest>(responseRest, HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
		
	}
	
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> searchByName(String name) {
		ProductResponseRest responseRest = new ProductResponseRest();
		List<Product> list = new ArrayList<Product>();
		List<Product> listAux = new ArrayList<Product>();
		
		try {
			// Search product by name
			listAux = productDao.findByNameContainingIgnoreCase(name);
			if(listAux.size() > 0) {
				
				listAux.stream().forEach( p ->{
					byte[] imagenDescompressed = Util.decompressZLib(p.getPicture());
					p.setPicture(imagenDescompressed);
					list.add(p);
					responseRest.getProduct().setProducts(list);
				});
				

				responseRest.setMetadata("Success Response", "00", "Products were found");
				return new ResponseEntity<ProductResponseRest>(responseRest, HttpStatus.OK);
				
			}else {
			
				responseRest.setMetadata("Fail Response", "-1", "Product not found");
				return new ResponseEntity<ProductResponseRest>(responseRest, HttpStatus.NOT_FOUND);				
			
			}
		
		} catch (Exception e) {
			
			e.getStackTrace();
			responseRest.setMetadata("Fail Response", "-1", "Fail to search product");
			return new ResponseEntity<ProductResponseRest>(responseRest, HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
	}

	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> deleteById(Long id) {
		
		ProductResponseRest responseRest = new ProductResponseRest();
		
		try {
			// Delete product by id
			productDao.deleteById(id);
			responseRest.setMetadata("Success Response", "00", "Product was delete");
			return new ResponseEntity<ProductResponseRest>(responseRest, HttpStatus.OK);
		
		} catch (Exception e) {
			
			e.getStackTrace();
			responseRest.setMetadata("Fail Response", "-1", "Fail to delete product");
			return new ResponseEntity<ProductResponseRest>(responseRest, HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> search() {
	
		ProductResponseRest responseRest = new ProductResponseRest();
		List<Product> list = new ArrayList<Product>();
		List<Product> listAux = new ArrayList<Product>();
		
		try {
			// Search product by name
			listAux = (List<Product>) productDao.findAll();
			if(listAux.size() > 0) {
				
				listAux.stream().forEach( p ->{
					byte[] imagenDescompressed = Util.decompressZLib(p.getPicture());
					p.setPicture(imagenDescompressed);
					list.add(p);
					responseRest.getProduct().setProducts(list);
				});
				

				responseRest.setMetadata("Success Response", "00", "Products were found");
				return new ResponseEntity<ProductResponseRest>(responseRest, HttpStatus.OK);
				
			}else {
			
				responseRest.setMetadata("Fail Response", "-1", "Products not found");
				return new ResponseEntity<ProductResponseRest>(responseRest, HttpStatus.NOT_FOUND);				
			
			}
		
		} catch (Exception e) {
			
			e.getStackTrace();
			responseRest.setMetadata("Fail Response", "-1", "Fail to search products");
			return new ResponseEntity<ProductResponseRest>(responseRest, HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
	}

	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> update(Product product, Long categoryId, Long id) {

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
			
			//Search product to update
			Optional<Product> productSearch = productDao.findById(id);
			
			if(productSearch.isPresent()) {
				
				//Update product
				productSearch.get().setName(product.getName());
				productSearch.get().setCategory(product.getCategory());
				productSearch.get().setPicture(product.getPicture());
				productSearch.get().setPrice(product.getPrice());
				productSearch.get().setQuantity(product.getQuantity());
				
				//Save the product in DB
				Product productToUpdate = productDao.save(productSearch.get());
				
				if(productToUpdate != null) {
					list.add(productToUpdate);
					responseRest.getProduct().setProducts(list);
					responseRest.setMetadata("Success Response", "00", "Product update");
				} else {
					responseRest.setMetadata("Fail Response", "-1", "Product not update");
					return new ResponseEntity<ProductResponseRest>(responseRest, HttpStatus.BAD_REQUEST);
				}
				
			}else {
						
				responseRest.setMetadata("Fail Response", "-1", "Product not update");
				return new ResponseEntity<ProductResponseRest>(responseRest, HttpStatus.NOT_FOUND);
			
			}
		
		} catch (Exception e) {
			
			e.getStackTrace();
			responseRest.setMetadata("Fail Response", "-1", "Fail to saved product");
			return new ResponseEntity<ProductResponseRest>(responseRest, HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
		
		return new ResponseEntity<ProductResponseRest>(responseRest, HttpStatus.OK);
	}

}
