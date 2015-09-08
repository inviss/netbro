package kr.co.d2net.test.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import kr.co.d2net.dao.filter.ObjectLikeSpecifications;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Content;
import kr.co.d2net.dto.vo.Download;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.ArchiveServices;
import kr.co.d2net.service.CodeServices;
import kr.co.d2net.service.DownloadServices;
import kr.co.d2net.utils.ObjectUtils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CodeTest extends BaseDaoConfig {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private ArchiveServices archiveServices;
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private CodeServices codeServices;
	
 
	@Test
	public void codeTest() {
		Search search = new Search();
		try {
			
			codeServices.findCodeClfList();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
}
