package kr.co.d2net.dao.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.CodeTbl.CodeId;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.service.CodeServices;

public class CodeTest extends BaseDaoConfig {
	
	@Autowired
	private CodeServices codeServices;
	
	@Ignore
	@Test
	public void addAll() {
		try {
			Set<CodeTbl> codes = new HashSet<CodeTbl>();
			
			CodeTbl code = new CodeTbl();
			
			CodeId id = code.getId();
			
			id.setClfCD("1");
			id.setSclCd("2");
			codes.add(code);
			

			codeServices.add(code);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void InsertTest() {
		try {
			//codeServices.insertCodeInfo("테스트");
			//CodeTbl code = new CodeTbl();
			//code.
			//codeServices.insertCodeInfo(code);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void getInfoTest() {
		try {
			CodeTbl code = new CodeTbl();
			CodeId id = new CodeId();
			id.setClfCD("U001");
			id.setSclCd("001");
			code.setId(id);
			CodeTbl info = codeServices.getCodeInfo(code);
			System.out.println("#####clfcd "+info.getId().getClfCD());
			System.out.println("#####sclcd "+info.getId().getSclCd());
			System.out.println("#####clfnm "+info.getClfNM());
			System.out.println("#####sclnm "+info.getSclNm());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Ignore
	@Test
	public void getClfList() {
		try {
			CodeTbl code = new CodeTbl();
			Search search = new Search();
			search.setPageNo(1);
			List<CodeTbl> infos  =  codeServices.getCodeInfos( code,  search);
			System.out.println("############"+codeServices.CountCodeInfo(code));
			System.out.println("############"+infos.size());
			for(CodeTbl codeTbl : infos){
				System.out.println("############"+codeTbl.getClfNM());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void groupList() {
		try {
			CodeTbl code = new CodeTbl();
		
			List<CodeTbl> infos  =  codeServices.getClfInfoList();
			
			System.out.println("############"+infos.size());
			for(CodeTbl codeTbl : infos){
				System.out.println("############"+codeTbl.getClfNM());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void getSclCd() {
		
			CodeTbl info;
			try {
				info = codeServices.getClfInfo("CFMT", "mxf");
				System.out.println("############"+info.getId().getSclCd());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	
	}
}
