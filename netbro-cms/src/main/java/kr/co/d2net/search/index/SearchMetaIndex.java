package kr.co.d2net.search.index;

import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.search.Cluster;
import kr.co.d2net.search.Index;
import kr.co.d2net.search.adapter.DateTypeAdapter;
import kr.co.d2net.search.adapter.TimestampTypeAdapter;
import kr.co.d2net.service.ContentsInstServices;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.delete.DeleteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SearchMetaIndex extends Index<SearchMeta> {
	final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private MessageSource messageSource;
	static Gson gson;

	static{
		try{
			gson = new GsonBuilder()
			.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
			.excludeFieldsWithoutExposeAnnotation()
			.registerTypeAdapter(java.sql.Timestamp.class, new TimestampTypeAdapter())
			.registerTypeAdapter(java.util.Date.class, new DateTypeAdapter())
			.create();
		}catch(Exception e){
			
		}
	}

	public void addSearchMeta(ContentsTbl contentsTbl, ContentsInstTbl contentsInstTbl) {
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
		meta.setFlPath(contentsInstTbl.getFlPath()+"/"+contentsInstTbl.getWrkFileNm()+"."+contentsInstTbl.getFlExt());
		meta.setProdRoute(contentsTbl.getProdRoute());
		meta.setNodes(contentsTbl.getNodes());
		meta.setRistClfCd(StringUtils.defaultIfBlank(contentsTbl.getRistClfCd(), "000")); // 설정없음
		meta.setLockStatCd(StringUtils.defaultIfBlank(contentsTbl.getLockStatcd(), "000")); // 정리전

		if(logger.isDebugEnabled()) {
			logger.debug("addIndex: "+gson.toJson(meta));
		}
		Cluster.getInstance().getClient().prepareIndex("cms", "metadata")
		.setId(String.valueOf(contentsTbl.getCtId()))
		.setSource(gson.toJson(meta)).execute().actionGet();

		Cluster.getInstance().closeClient();
	}

	public void updateSearchMeta(ContentsTbl contentsTbl,ContentsInstTbl contentsInstTbl) throws ServiceException {
		try {
			SearchMeta meta = get(String.valueOf(contentsTbl.getCtId()));
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
				meta.setCont(contentsTbl.getSpcInfo());
				meta.setKeyWords(contentsTbl.getKeyWords());
				meta.setRpImgKfrmSeq(Long.valueOf(contentsTbl.getRpimgKfrmSeq()));
				meta.setBrdDd(contentsTbl.getBrdDd().getTime());
				meta.setRegrid(contentsTbl.getRegrId());
				meta.setNodes(contentsTbl.getNodes());
				meta.setRistClfCd(StringUtils.defaultIfBlank(contentsTbl.getRistClfCd(), "000")); // 설정없음
				meta.setLockStatCd(StringUtils.defaultIfBlank(contentsTbl.getLockStatcd(), "000")); // 정리전
				meta.setVdHresol(contentsInstTbl.getVdHresol());
				meta.setVdVresol(contentsInstTbl.getVdVersol());
				if(logger.isDebugEnabled()) {
					logger.debug("updateIndex: "+gson.toJson(meta));
				}
				Cluster.getInstance().getClient().prepareIndex("cms", "metadata")
				.setId(String.valueOf(contentsTbl.getCtId()))
				.setSource(gson.toJson(meta)).execute().actionGet();
				
				Cluster.getInstance().closeClient();
			} else {
				logger.info("updateSerchMeta is null - id: "+contentsTbl.getCtId());
			}
		} catch (Exception e) {
			String message = messageSource.getMessage("error.015", null, null);
			throw new ServiceException("error.015",message,null);
		}
	}
	
	public void updateRpImgKfrm(ContentsTbl contentsTbl) {
		try {
			SearchMeta meta = get(String.valueOf(contentsTbl.getCtId()));
			
			if(meta != null) {
				meta.setRpImgKfrmSeq(Long.valueOf(contentsTbl.getRpimgKfrmSeq()));
				
				Cluster.getInstance().getClient().prepareIndex("cms", "metadata")
				.setId(String.valueOf(contentsTbl.getCtId()))
				.setSource(gson.toJson(meta)).execute().actionGet();
				
				Cluster.getInstance().closeClient();
			}
		} catch (Exception e) {
			logger.error("updateRpImgKfrm Error", e);
		}
		
	}
	
	public void deleteSearchMeta(Long ctId) {
		try {
			SearchMeta meta = get(String.valueOf(ctId));
			if(meta != null) {
				DeleteRequest request = new DeleteRequest("cms", "metadata", String.valueOf(ctId));
				Cluster.getInstance().getClient().delete(request).actionGet();
				
				Cluster.getInstance().closeClient();
			}
		} catch (Exception e) {
			logger.error("deleteSearchMeta Error", e);
		}
		
	}
}
