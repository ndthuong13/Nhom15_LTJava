package controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dao.ProductDAO;
import model.Product;
import view.ProductManagementView;

public class ProductController {
	private ProductDAO productDAO;
	private ProductManagementView pmv;

	public ProductController(ProductDAO productDAO, ProductManagementView pmv) {
		this.productDAO = productDAO;
		this.pmv = pmv;
	}

	public List<Product> getAllProducts() throws ClassNotFoundException, IOException {
		return productDAO.getAll();
	}

	public Product getProductById(int id) throws ClassNotFoundException, IOException {
		return productDAO.get(Product -> Product.getId() == id);
	}

	public boolean addProduct(Product Product) throws ClassNotFoundException, IOException {
		if (!productDAO.add(Product)) {
			return false;
		}
		if (pmv != null)
			pmv.updateProductTable(getAllProducts());
		return true;
	}

	public boolean updateProduct(Product Product) throws ClassNotFoundException, IOException {
		if (!productDAO.update(Product)) {
			return false;
		}
		if (pmv != null)
			pmv.updateProductTable(getAllProducts());
		return true;
	}

	public boolean deleteProduct(Product Product) throws ClassNotFoundException, IOException {
		if (!productDAO.delete(Product)) {
			return false;
		}
		if (pmv != null)
			pmv.updateProductTable(getAllProducts());
		return true;
	}

	public void searchProducts(String name) throws ClassNotFoundException, IOException {
		List<Product> products = productDAO.searchByName(name);
		pmv.updateProductTable(products);
	}

	public void sortProductsByPrice(List<Product> products, boolean isINC) {
		Collections.sort(products, new Comparator<Product>() {
			@Override
			public int compare(Product p1, Product p2) {
				if (isINC) {
					return Double.compare(p1.getPrice(), p2.getPrice());
				} else {
					return Double.compare(p2.getPrice(), p1.getPrice());
				}
			}
		});
		pmv.updateProductTable(products);
	}
}
