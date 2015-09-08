package kr.co.d2net.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.CornerDao;
import kr.co.d2net.dao.filter.CornerSpecifications;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.CornerTbl;
import kr.co.d2net.dto.vo.StoryBoard;
import kr.co.d2net.exception.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class CornerServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CornerDao cornerDao;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private ContentsServices contentsServices;

	@Autowired
	private ContentsInstServices contentsInstServices;

	/**
	 * 코너의 정보를 수정한다. 
	 * 코너를 수정하기에 앞서 기존에 등록된 코드의 정보를 모두 삭제 처리하고, 다시 코너정보를 등록한다.
	 * 코너의 s_duration은 img 파일의 파일 명과동일하다
	 * @param cornerInfo
	 * @param storyBoard
	 * @throws RuntimeException
	 */
	@Modifying
	@Transactional
	public void updateConrnerInfo(List<CornerTbl> cornerInfo ,StoryBoard storyBoard) throws ServiceException{

		String[] array = storyBoard.getDividImgs().split(",");//기존 저장되어있는 코너의 시작 duration
		String[] tempArray = storyBoard.getDividImgs().split(",");//
		String[] cnNm = storyBoard.getCnNm().split(",");
		String[] cnCont = storyBoard.getCnCont().split(",");

		List<CornerTbl> deleteCorners = new ArrayList<CornerTbl>();

		
		//신규로 등록하고자하는 코너정보와 기존에 등록되어있는 코너정보의 sDuration의 값을 비교한다. 
		//저장한 이후에는 해당 array의 값을 none으로 치환하여 이후 이루워질 삭제로직에서 제외시킨다.
		for(int i = 0; i < array.length; i++){
			for(CornerTbl info : cornerInfo){
				if(info.getsDuration() == Long.parseLong(array[i])){

					info.setCnCont(cnCont[i]);
					info.setCnNm(cnNm[i]);
					info.setRegId(storyBoard.getModrId());
					
					cornerDao.save(info);
					
					tempArray[i]="none";
					deleteCorners.add(info);
				}
			}
		}

		cornerInfo.removeAll(deleteCorners);

		//조회된 정보중 일치하지 않은 정보가 존재한다면 코너합치기로 사라진 코너이므로 삭제조치한다.
		if(cornerInfo.size() != 0){
			for(CornerTbl deleteInfo : cornerInfo){
				delete(deleteInfo);
			}
		} 
		String dividImgs = Arrays.toString(tempArray);			
		dividImgs = dividImgs.replace("[", "").replace("]", "");
		storyBoard.setDividImgs(dividImgs);
		//db에 저장요청을 한다
		add(storyBoard);
	}

	/**
	 * 코너의 정보를 실제 db에 저장요청하다
	 * @param storyBoard
	 * @throws RuntimeException
	 */
	@Modifying
	@Transactional
	public void add(StoryBoard storyBoard) throws ServiceException{
		String[] array = storyBoard.getDividImgs().split(",");
		String[] cnNm = storyBoard.getCnNm().split(",");
		String[] cnCont = storyBoard.getCnCont().split(",");
		for(int i = 0; i < array.length; i++){

			if(!array[i].trim().equals("none")){
				
				if(logger.isDebugEnabled()){
					logger.debug("array["+i+"] : "+array[i]);
				}
				
				CornerTbl corner = new CornerTbl();
				
				corner.setCtId(storyBoard.getCtId());
				corner.setRegDt(new Date());
				corner.setsDuration(Long.parseLong(array[i].trim()));
				corner.setCnCont(cnCont[i]);
				corner.setCnNm(cnNm[i]);
				corner.setsDuration(Long.parseLong(array[i].trim()));
				corner.setRegId(storyBoard.getModrId());
				
				cornerDao.save(corner);
			}
		}
	}


	/**
	 * 코너정보를 삭제한다
	 * @param corner
	 */
	@Modifying
	@Transactional
	public void delete(CornerTbl corner) throws ServiceException{
		cornerDao.delete(corner.getCnId());
	}

	/**
	 * ctId에 소속된 코너의 총갯수를 조회한다
	 * @param corner
	 * @return
	 */
	@Modifying	
	public long count(CornerTbl corner) throws ServiceException{
		return cornerDao.count(CornerSpecifications.CtIdEqual(corner.getCtId()));
	}

	/**
	 * ctId에 소속된 코너의 모든 정보를 조회한다 s_duration기준으로 오름차순 조회를 한다
	 * @param ctId
	 * @return
	 */
	@Modifying	
	public List<CornerTbl> findCornerList(long ctId) throws ServiceException{
		return cornerDao.findAll(CornerSpecifications.CtIdEqual(ctId), new Sort(
				new Order(Direction.ASC, "sDuration")));
	}


	/**
	 * 스토리 보드 정보를 저장한다.
	 * @param cornerInfo
	 * @param storyBoard
	 * @throws IOException 
	 * @throws RuntimeException
	 */
	@Modifying
	@Transactional
	public void updateAllStoryBoardInfo(StoryBoard storyBoard) throws ServiceException, IOException{


			/*
			 * 1. 코너 나누기, 합치기 한 결과를 저장한다.
			 * 저장을 하는 정보중에서 기존 조회 데이터와 비교했을때 차이가 있다면 insert하거나 delete한다.
			 * 최초로 코너를 생성하는경우 비교작업없이 바로 저장하도록한다.			 
			 */

			CornerTbl cornerTbl = new CornerTbl();
			cornerTbl.setCtId(storyBoard.getCtId());

			Long count = count(cornerTbl);		

			if(count > 0 && StringUtils.isBlank(storyBoard.getDividImgs())){
				//코너테이블에는 값이 있지만 dividImgs 가 공백인경우는 코너 전체 삭제인경우 이므로 cornerTbl의 값을 모두 지운다.
				List<CornerTbl> dels =  findCornerList(cornerTbl.getCtId());
				for(CornerTbl del : dels){
					cornerDao.delete(del);
				}
			}else if(count > 0){
				List<CornerTbl> cornerInfo = findCornerList(storyBoard.getCtId());
				updateConrnerInfo(cornerInfo,storyBoard);
			}else if(StringUtils.isNotBlank(storyBoard.getDividImgs())){
				add(storyBoard);
			}


		 

			/*
			 * 2. 영상 삭제 
			 * 	UI에서 샷합치기로 삭제등록된 이미지에대해서 실제로 파일을 삭제하고, 
			 *  txt파일에 삭제된 duration 정보를 지운뒤 다지 저장한다.		 
			 */
			if(StringUtils.isNotBlank(storyBoard.getShowImgs())){
				ContentsTbl contentsTbl = contentsServices.getContentObj(storyBoard.getCtId());

				ContentsInstTbl contentsInstTbl = contentsInstServices.getContentInstObj(storyBoard.getCtId());

				String filePath = contentsInstTbl.getFlPath(); //contentsInstTbl.getFlPath(); // /201309/05
				String fileNm = contentsInstTbl.getWrkFileNm(); //contentsInstTbl.getFlPath(); // /201309/05
				String substrFilepath = fileNm.substring(fileNm.lastIndexOf('_')+1);

				if(logger.isDebugEnabled()){
					logger.debug("filePath     "+filePath);
					logger.debug("fileNm       "+fileNm);
					logger.debug("substrFilepath "+substrFilepath);
				}

				String folder = null;
				String targetDrive = messageSource.getMessage("row.drive", null, Locale.KOREA);

				folder = targetDrive + filePath + File.separator + substrFilepath;

				if(logger.isDebugEnabled()){
					logger.debug(folder);
					logger.debug("text   "+storyBoard.getShowImgs());
				}
				

				File outDir = new File(folder);

				//스토리보드 txt파일data
				//새로 txt 파일을 생성한다
					BufferedWriter out = new BufferedWriter(new FileWriter(folder+ File.separator + substrFilepath+".txt"));
					out.write(storyBoard.getShowImgs());
					out.close();

			}

	}

}
