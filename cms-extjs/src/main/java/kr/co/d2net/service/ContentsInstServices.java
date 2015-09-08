package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.ContentsDao;
import kr.co.d2net.dao.ContentsInstDao;
import kr.co.d2net.dao.filter.ContentInstSpecifications;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.vo.ContentsInst;
import kr.co.d2net.dto.vo.Media;
import kr.co.d2net.exception.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 컨텐츠 inst 관련정보를 조회하기위한 함수
 * @author asura
 *
 */
@Service
@Transactional(readOnly=true)
public class ContentsInstServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ContentsInstDao contentsInstDao;


	@Autowired
	private ContentsDao contentsDao;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private MessageSource messageSource;


	@Modifying
	@Transactional
	public Long add(ContentsInstTbl contentsInstTbl)  throws ServiceException {
		ContentsInstTbl contentsInstTbl2 = contentsInstDao.save(contentsInstTbl);

		return contentsInstTbl2.getCtiId();

	}

	/**
	 * 고해상도 영상으로 등록되어잇는 정보를 한건 조회한다.
	 * @param ctId
	 * @return ContentsInstTbl
	 */
	public ContentsInstTbl getContentInstObj(Long ctId)  throws ServiceException {
		return contentsInstDao.findOne(ContentInstSpecifications.formatLike(ctId, "10"));	
	}
	
	
	/**
	 * ctiId의 값으로 contents_inst_Tbl의 정보를 조회한다
	 * @param ctiId
	 * @return ContentsInstTbl
	 * @throws ServiceException
	 */
	public ContentsInstTbl getContentInstInfoByCtiId(Long ctiId)  throws ServiceException {
		return contentsInstDao.findOne(ctiId);	
	}
	
	
	/**
	 * ctId와 ctiFmt의 값으로  contents_inst_Tbl의 정보를 조회한다
	 * @param ctId, ctiFmt
	 * @return ContentsInstTbl
	 */
	public ContentsInstTbl getContentInstInfoByCtId(Long ctId,String ctiFmt)  throws ServiceException {
		return contentsInstDao.findOne(ContentInstSpecifications.formatLike(ctId, ctiFmt));	
	}
	
	/**
	 * ctid에 소속되어있는 프로파일 정보를 읽어들인다
	 * @param ctId
	 * @return List<Media>
	 */
	public List<Media> findMediaList(Long ctId)  throws ServiceException {
		
		List<ContentsInstTbl> contentsInsts = contentsInstDao.findAll(ContentInstSpecifications.mediaInfo(ctId, "10"));	
		List<Media> medias = new ArrayList<Media>();
		
		ContentsTbl contentsTbl = contentsDao.findOne(ctId);
		
		for(ContentsInstTbl info : contentsInsts){
			Media media = new Media();
			media.setCtiFmt(info.getCtiFmt());
			media.setCtLeng(contentsTbl.getCtLeng());
			media.setAspRtoCd(contentsTbl.getAspRtoCd());
			media.setFlExt(info.getFlExt());
			media.setFlPath(info.getFlPath());
			media.setVdHresol(info.getVdHresol());
			media.setVdQlty(contentsTbl.getVdQlty());
			media.setVdVresol(info.getVdVersol());
			media.setFrmPerSec(info.getFrmPerSec());
			media.setCtiId(info.getCtiId());
			media.setCtId(info.getCtId());
			medias.add(media);
		}
		
		return medias;
	}


	/**
	 * ctid에 소속되어있는 한건의 프로파일 정보를 읽어 들인다.
	 * @param ctId
	 * @return Media
	 */
	public Media getMediaInfo(Media media)  throws ServiceException {
		
		ContentsInstTbl contentsInst = contentsInstDao.findOne(ContentInstSpecifications.formatLike(media.getCtId(), media.getCtiFmt()));
		
		ContentsTbl contentsTbl = contentsDao.findOne(media.getCtId());
		 	
			Media result = new Media();
			result.setCtiFmt(contentsInst.getCtiFmt());
			result.setCtLeng(contentsTbl.getCtLeng());
			result.setAspRtoCd(contentsTbl.getAspRtoCd());
			result.setFlExt(contentsInst.getFlExt());
			result.setFlPath(contentsInst.getFlPath());
			result.setVdHresol(contentsInst.getVdHresol());
			result.setVdQlty(contentsTbl.getVdQlty());
			result.setVdVresol(contentsInst.getVdVersol());
			result.setFrmPerSec(contentsInst.getFrmPerSec());
			result.setCtId(media.getCtId());
		return result;
	}
	
	
	/**
	 * ctid에 소속되어있는   프로파일 갯수를 읽는다.
	 * @param ctId
	 * @return Media
	 */
	public long countMedia(long ctId)  throws ServiceException {
		
		long count =  contentsInstDao.count(ContentInstSpecifications.formatNotLike(ctId, "10"));
		
		return count;
	}

	

	/**
	 * 대표화면 컨첸츠를 지정한다.
	 * @param ctId
	 * @return Media
	 */
	public String updateRpContent(Media media)  throws ServiceException {
		
		List<ContentsInstTbl> contentsInsts = contentsInstDao.findAll(ContentInstSpecifications.formatNotLike(media.getCtId(), "10"));
		
		if(logger.isDebugEnabled()){
			logger.debug("contentsInsts.size()   : "+contentsInsts.size());
		}

		//기존에 대표화면으로 지정된 영상을 해지한다
			for(ContentsInstTbl contentsInstTbl : contentsInsts){
				if(contentsInstTbl.getRpYn() == "Y"){
					contentsInstTbl.setRpYn("N");
					contentsInstDao.save(contentsInstTbl);
				}
			} 	
			
			//새로 등록된 영상의 대표 영상을 등록한다.
			for(ContentsInstTbl contentsInstTbl : contentsInsts){
				if(contentsInstTbl.getCtiId() == media.getCtiId()){
					contentsInstTbl.setRpYn("Y");
					contentsInstDao.save(contentsInstTbl);
				}
			} 	
		 
		return "Y";
	}
	
	

}
