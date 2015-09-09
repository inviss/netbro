package kr.co.d2net.rest;

import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.CodeTbl.CodeId;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.json.CommonMeta;
import kr.co.d2net.dto.json.CtData;
import kr.co.d2net.dto.json.MetaData;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.CodeServices;
import kr.co.d2net.service.ContentsServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Path("/client")
@Service("rfClientService")
public class RFClientService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CodeServices codeServices;
	@Autowired
	private ContentsServices contentsServices;
	
	@GET
	@Path("/find/code/{clfCd}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public MetaData findCode(@PathParam("clfCd") String clfCd) {
		
		MetaData metaData = new MetaData();
		List<CodeTbl> codeTbls;
		try {
			codeTbls = codeServices.getCodeInfos(clfCd);
		} catch (ServiceException e) {
			return new MetaData();
		}
		if(logger.isDebugEnabled()) {
			logger.debug("code size: "+codeTbls);
		}
		for(CodeTbl codeTbl : codeTbls) {
			CommonMeta commonMeta = new CommonMeta();
			
			CodeId codeId = codeTbl.getId();
			commonMeta.setClfCd(codeId.getClfCD());
			commonMeta.setSclCd(codeId.getSclCd());
			commonMeta.setClfNm(codeTbl.getClfNM());
			commonMeta.setSclNm(codeTbl.getSclNm());
			
			metaData.addCommonMeta(commonMeta);
		}
		return metaData;
	}
	
	@GET
	@Path("/get/code/slfcd/{clfCd}/{slfNm}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public MetaData getCode(@PathParam("clfCd") String clfCd, @PathParam("slfNm") String slfNm) {
		MetaData metaData = new MetaData();
		try {
			CodeTbl info = codeServices.getClfInfo(clfCd, slfNm);
			
			if(info != null) {
				if(logger.isDebugEnabled()) {
					logger.debug("scl_cd: "+info.getId().getSclCd());
				}
				
				CommonMeta commonMeta = new CommonMeta();
				commonMeta.setClfCd(info.getId().getClfCD());
				commonMeta.setClfNm(info.getClfNM());
				commonMeta.setSclCd(info.getId().getSclCd());
				commonMeta.setSclNm(info.getSclNm());
				metaData.addCommonMeta(commonMeta);
			}
		} catch (ServiceException e) {
			return new MetaData();
		}
		
		return metaData;
	}
	
	@GET
	@Path("/get/content/{ctId}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public CtData getContent(@PathParam("ctId") Long ctId) {
		CtData ctData = null;
		try {
			ContentsTbl contentsTbl = contentsServices.getContentObj(ctId);
			
			ctData = new CtData();
			ctData.setCtId(contentsTbl.getCtId());
			ctData.setCtNm(contentsTbl.getCtNm());
			ctData.setCtLeng(contentsTbl.getCtLeng());
			ctData.setSpcInfo(contentsTbl.getSpcInfo());
			ctData.setDataStatCd(contentsTbl.getDataStatCd());
			
			Set<ContentsInstTbl> contentsInstTbls = contentsTbl.getContentsInst();
			if(contentsInstTbls != null) {
				for(ContentsInstTbl contentsInstTbl : contentsInstTbls) {
					if(contentsInstTbl.getCtiFmt().startsWith("2") && contentsInstTbl.getUseYn().equals("Y")) { // mp4
						ctData.setFilePath(contentsInstTbl.getFlPath());
						ctData.setFileNm(contentsInstTbl.getWrkFileNm());
						ctData.setFileExt(contentsInstTbl.getFlExt());
					}
				}
			}
			
			CodeTbl codeTbl = null;
			try {
				codeTbl = codeServices.getCodeInfo("CCLA", contentsTbl.getCtCla());
				ctData.setCtClaNm(codeTbl.getClfNM());
			} catch (Exception e) {
				logger.warn("ctCla get error", e.getCause());
			}
			try {
				codeTbl = codeServices.getCodeInfo("CTYP", contentsTbl.getCtTyp());
				ctData.setCtTypNm(codeTbl.getClfNM());
			} catch (Exception e) {
				logger.warn("ctTyp get error", e.getCause());
			}
			try {
				codeTbl = codeServices.getCodeInfo("RIST", contentsTbl.getCtCla());
				ctData.setCtTypNm(codeTbl.getClfNM());
			} catch (Exception e) {
				logger.warn("ristClfCd get error", e.getCause());
			}
			
		} catch (Exception e) {
			logger.error("getContent error", e);
		}
		
		return ctData;
	}
}
