package kr.co.d2net.rest;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
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


/**
 * 컨텐츠와 코드정보 관련된 외부 interface 와 통신하는 class.
 * @author Administrator
 *
 */
@Path("/client")
@Service("rfClientService")
public class RFClientService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CodeServices codeServices;
	@Autowired
	private ContentsServices contentsServices;



	/**
	 * 
	 * @param clfCd
	 * @return
	 */
	@GET
	@Path("/find/code/{clfCd}")
	@Produces({MediaType.APPLICATION_JSON+";charset=UTF-8", MediaType.APPLICATION_XML})
	public MetaData findCode(@PathParam("clfCd") String clfCd) {

		MetaData metaData = new MetaData();
		List<CodeTbl> codeTbls;
		try {
			codeTbls = codeServices.findCodeInfos(clfCd);
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


	/**
	 * 
	 * @param clfCd
	 * @param rmk1
	 * @return
	 */
	@GET
	@Path("/get/code/rmk1/{clfCd}/{rmk1}")
	@Produces({MediaType.APPLICATION_JSON+";charset=UTF-8", MediaType.APPLICATION_XML})
	public MetaData getCode(@PathParam("clfCd") String clfCd, @PathParam("rmk1") String rmk1) {
		MetaData metaData = new MetaData();
		try {
			CodeTbl info = codeServices.getClfInfo(clfCd, rmk1);

			if(info != null) {
				if(logger.isInfoEnabled()) {
					logger.info("scl_cd : "+info.getId().getSclCd());
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



	/**
	 * 
	 * @param ctId
	 * @return
	 */
	@GET
	@Path("/get/content/{ctId}")
	@Produces({MediaType.APPLICATION_JSON+";charset=UTF-8", MediaType.APPLICATION_XML})
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
			//코드정보를 조회하기위한 조회 beans
			CodeTbl tempCodeTbl = null;
			CodeId codeId = null;
			try {
				codeId.setClfCD("CCLA");
				codeId.setSclCd(contentsTbl.getCtCla());
				tempCodeTbl.setId(codeId);

				codeTbl = codeServices.getCodeInfo(tempCodeTbl);
				ctData.setCtClaNm(codeTbl.getClfNM());
			} catch (Exception e) {
				logger.warn("ctCla get error", e.getCause());
			}
			try {
				codeId.setClfCD("CTYP");
				codeId.setSclCd(contentsTbl.getCtCla());
				tempCodeTbl.setId(codeId);


				codeTbl = codeServices.getCodeInfo(tempCodeTbl);
				ctData.setCtTypNm(codeTbl.getClfNM());
			} catch (Exception e) {
				logger.warn("ctTyp get error", e.getCause());
			}
			try {
				codeId.setClfCD("CTYP");
				codeId.setSclCd(contentsTbl.getCtCla());
				tempCodeTbl.setId(codeId);

				codeTbl = codeServices.getCodeInfo(tempCodeTbl);
				ctData.setCtTypNm(codeTbl.getClfNM());
			} catch (Exception e) {
				logger.warn("ristClfCd get error", e.getCause());
			}

		} catch (Exception e) {
			logger.error("getContent error", e);
		}

		return ctData;
	}



	/**
	 * 
	 * @param ctId
	 * @return
	 */
	@POST
	@Path("/error/content/{ctId}")
	@Produces({MediaType.TEXT_PLAIN})
	public String saveErrorContent(@PathParam("ctId") Long ctId) {
		try {
			ContentsTbl contentsTbl = contentsServices.getContentObj(ctId);
			if(contentsTbl != null) {
				//이미 오류등록된 영상이 아니면 오류등록
				if(contentsTbl.getDataStatCd() != "003"){
					contentsTbl.setDataStatCd("003");
					contentsTbl.setArrangeDt(new Date());

					contentsServices.saveContentObj(contentsTbl);
				}
			}
		} catch (Exception e) {
			logger.error("saveErrorContent error", e);
			return "N";
		}
		return "Y";
	}
}
