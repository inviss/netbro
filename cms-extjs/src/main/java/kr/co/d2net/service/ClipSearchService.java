package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.search.Index;
import kr.co.d2net.search.index.SearchMeta;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClipSearchService extends Index<SearchMeta> {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ContentsInstServices contentsInstServices;
	
	public long findCount(Search search) {
		return count(search);
	}

	public List<SearchMeta> findData(Search search) {
		return query(search);
	}
	
	
	/**
	 * 검색엔진에 데이터를 입력한다. 
	 * contentsTbl과 contentsInstTbl에서 정보를 모아서 검색엔진에 입력요청을 한다.
	 * @param contentsTbl,contentsInstTbl
	 * */
	public void addData(ContentsTbl contentsTbl, ContentsInstTbl contentsInstTbl) {
		SearchMeta meta = new SearchMeta();
		
		meta.setCategoryId(contentsTbl.getCategoryId());
		meta.setEpisodeId(contentsTbl.getEpisodeId());
		meta.setSegmentId(contentsTbl.getSegmentId());
		meta.setCategoryNm(contentsTbl.getCategoryNm());
		meta.setEpisodeNm(StringUtils.defaultIfBlank(contentsTbl.getEpisodeNm(), "기본"));
		meta.setSegmentNm(StringUtils.defaultIfBlank(contentsTbl.getSegmentNm(), "기본"));
		meta.setCtId(contentsTbl.getCtId());
		meta.setCtNm(contentsTbl.getCtNm());
		meta.setCtCla(contentsTbl.getCtCla());
		meta.setCtTyp(contentsTbl.getCtTyp());
		meta.setCont(contentsTbl.getCont());
		meta.setVdQlty(contentsTbl.getVdQlty());
		meta.setAspRtpCd(contentsTbl.getAspRtoCd());
		meta.setKeyWords(contentsTbl.getKeyWords());
		meta.setDuration(Long.valueOf(contentsTbl.getDuration()));
		meta.setRpImgKfrmSeq(contentsTbl.getRpimgKfrmSeq());
		meta.setRegDt(contentsTbl.getRegDt().getTime());
		meta.setBrdDd(contentsTbl.getBrdDd().getTime());
		meta.setRegrid(contentsTbl.getRegrId());
		meta.setVdHresol(contentsInstTbl.getVdHresol());
		meta.setVdVresol(contentsInstTbl.getVdVersol());
		meta.setCtiFmt(contentsInstTbl.getCtiFmt());
		meta.setFlPath(contentsInstTbl.getFlPath()+contentsInstTbl.getWrkFileNm()+"."+contentsInstTbl.getFlExt());//fullpath ex)/201407/14/sample008_인기가요2_20140714104006.mxf
		meta.setProdRoute(contentsTbl.getProdRoute());
		meta.setNodes(contentsTbl.getNodes());
		meta.setRistClfCd(StringUtils.defaultIfBlank(contentsTbl.getRistClfCd(), "000")); // 설정없음
		meta.setLockStatCd(StringUtils.defaultIfBlank(contentsTbl.getLockStatcd(), "000")); // 정리전
		meta.setCtLeng(contentsTbl.getCtLeng());
	
		//검색엔진에 저장요청을 한다
		addData(meta);
	}

	
	/**
	 * 검색엔진에 입력요청을 한다.
	 * @param searchMeta
	 * */
	public void addData(SearchMeta searchMeta) {
		addIndex(searchMeta);
	}
	
	/**
	 * 검색엔진에 등록된 컨텐츠의 정보를 수정하기 위해서 beans에 값을 넣어서 
	 * 검색엔진에 수정요청을 한다.
	 * @param contentsTbl,contentsInstTbl
	 * */
	public void updateData(ContentsTbl contentsTbl, ContentsInstTbl contentsInstTbl) {
		SearchMeta meta = getIndex(contentsTbl.getCtId());
		
		if(meta != null) {
			meta.setCategoryId(contentsTbl.getCategoryId());
			meta.setEpisodeId(contentsTbl.getEpisodeId() == null?1:contentsTbl.getEpisodeId());
			meta.setSegmentId(contentsTbl.getSegmentId() == null?1:contentsTbl.getSegmentId());
			meta.setCategoryNm(contentsTbl.getCategoryNm());
			meta.setEpisodeNm(StringUtils.defaultIfBlank(contentsTbl.getEpisodeNm(), "기본"));
			meta.setSegmentNm(StringUtils.defaultIfBlank(contentsTbl.getSegmentNm(), "기본"));
			meta.setCtNm(contentsTbl.getCtNm());
			meta.setCtCla(contentsTbl.getCtCla());
			meta.setCtTyp(contentsTbl.getCtTyp());
			meta.setCont(contentsTbl.getCont());
			meta.setKeyWords(contentsTbl.getKeyWords());
			meta.setRpImgKfrmSeq(Long.valueOf(contentsTbl.getRpimgKfrmSeq()));
			meta.setBrdDd(contentsTbl.getBrdDd().getTime());
			meta.setRegrid(contentsTbl.getRegrId());
			meta.setNodes(contentsTbl.getNodes());
			meta.setRistClfCd(StringUtils.defaultIfBlank(contentsTbl.getRistClfCd(), "000")); // 설정없음
			meta.setLockStatCd(StringUtils.defaultIfBlank(contentsTbl.getLockStatcd(), "000")); // 정리전
			meta.setVdHresol(contentsInstTbl.getVdHresol());
			meta.setVdVresol(contentsInstTbl.getVdVersol());
			meta.setCtLeng(contentsTbl.getCtLeng()); 
		 
			 updateData(meta);
		}
	}

	/**
	 * 검색엔진에 수정요청을 한다.
	 * @param searchMeta
	 * */
	public void updateData(SearchMeta searchMeta) {
		updateIndex(searchMeta);
	}
	
	
	/**
	 * 대표화면 키프레임 정보를 수정요청을 한다.
	 * @param contentsTbl
	 * */
	public void updateSearchMeta(ContentsTbl contentsTbl) {
		
		if(logger.isDebugEnabled()) {
			logger.debug("updateRpImgKfrm ct_id: "+contentsTbl.getCtId());
		}
		
		SearchMeta meta = getIndex(contentsTbl.getCtId());
		
		if(meta != null) {
			if(logger.isDebugEnabled()) {
				logger.debug("updateRpImgKfrm SearchMeta: "+meta.getCategoryNm());
			}
			meta.setSvrFlNm(contentsTbl.getWrkFileNm());
			meta.setRpImgKfrmSeq(Long.valueOf(contentsTbl.getRpimgKfrmSeq()));
		}
		
		updateData(meta);
	}

	/**
	 * 검색엔진에 등록된 컨넨츠 메타정보를 삭제 요청을 한다.
	 * @param id
	 * */
	public void deleteData(Long id) {
		deleteIndex(id);
	}


	
}
