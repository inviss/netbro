package kr.co.d2net.service;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.ContentsModDao;
import kr.co.d2net.dao.filter.ContentsModSpecifications;
import kr.co.d2net.dto.ContentsModTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.exception.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class ContentsModServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ContentsModDao contentsModDao;

	@PersistenceContext
	private EntityManager em;
	@Autowired
	private MessageSource messageSource;

 

	@Modifying
	@Transactional
	public void add(ContentsModTbl content)throws ServiceException {
		contentsModDao.save(content);
	}

/**
 * 최근 수정현황을 조회한다.
 * @param search
 * @return contentsModTbl
 * */
	public ContentsModTbl getLastModInfo(Search search) throws ServiceException {
		List<ContentsModTbl> lists = contentsModDao.findAll(ContentsModSpecifications.whereCodition(search),new Sort(Sort.Direction.DESC,"modDt"));
		ContentsModTbl contentsModTbl = new ContentsModTbl();
	
		//수정자id정보를  beans에 넣어서 반환하며 없다면  not으로 표기하여 넘긴다.
		if(lists.size() != 0){
			for(ContentsModTbl info : lists){
				contentsModTbl.setModId(info.getModId());
				break;
			}
		}else{
			contentsModTbl.setModId("not");
		}
		return contentsModTbl;
	}
}
