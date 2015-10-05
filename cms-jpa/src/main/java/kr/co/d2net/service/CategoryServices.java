package kr.co.d2net.service;

import kr.co.d2net.dao.CategoryDao;
import kr.co.d2net.dto.CategoryTbl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class CategoryServices {

	@Autowired
	private CategoryDao categoryDao;
	
	@Transactional
	public CategoryTbl saveCategory(CategoryTbl categoryTbl) {
		try {
			categoryDao.save(categoryTbl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categoryTbl;
	}
	
}
