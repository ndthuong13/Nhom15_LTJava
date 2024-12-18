package controller;

import java.io.IOException;
import java.util.List;

import dao.CategoryDAO;
import model.Category;
import view.CategoryManagementView;

public class CategoryController {
	private CategoryDAO categoryDAO;
	private CategoryManagementView cmv;

	public CategoryController(CategoryDAO categoryDAO, CategoryManagementView cmv) {
		this.categoryDAO = categoryDAO;
		this.cmv = cmv;
	}

	public List<Category> getAllCategories() throws ClassNotFoundException, IOException {
		return categoryDAO.getAll();
	}

	public Category getCategoryById(int id) throws ClassNotFoundException, IOException {
		return categoryDAO.get(category -> category.getId() == id);
	}

	public boolean addCategory(Category category) throws ClassNotFoundException, IOException {
		if (!categoryDAO.add(category)) {
			return false;
		}
		cmv.updateCategoryTable(getAllCategories());
		return true;
	}

	public boolean updateCategory(Category category) throws ClassNotFoundException, IOException {
		if (!categoryDAO.update(category)) {
			return false;
		}
		cmv.updateCategoryTable(getAllCategories());
		return true;
	}

	public boolean deleteCategory(Category category) throws ClassNotFoundException, IOException {
		if (!categoryDAO.delete(category)) {
			return false;
		}
		cmv.updateCategoryTable(getAllCategories());
		return true;
	}

	public void searchCategories(String name) throws ClassNotFoundException, IOException {
		List<Category> categories = categoryDAO.searchByName(name);
		cmv.updateCategoryTable(categories);
	}
}
