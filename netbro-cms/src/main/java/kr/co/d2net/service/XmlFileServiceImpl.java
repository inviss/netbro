package kr.co.d2net.service;

import java.io.File;

import kr.co.d2net.exception.RequiredFieldException;
import kr.co.d2net.exception.XmlParsingException;
import kr.co.d2net.exception.XmlWriteException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class XmlFileServiceImpl implements XmlFileService {

	public String FileToString(String filePath) throws Exception {
		String xml = "";
		try {
			if(StringUtils.isBlank(filePath)){
				throw new RequiredFieldException("파일관련 정보(path)가 없습니다.");
			}
			File f = new File(filePath);
			xml = FileUtils.readFileToString(f, "UTF-8");
		}catch(Exception e){
			throw new XmlParsingException("XML File을 생성중 에러가 발생했습니다. File을 생성할 수 없습니다.", e);
		}
		return xml;
	}

	public void StringToFile(String xml, String filePath, String fileNm) throws Exception {
		try {
			if(StringUtils.isBlank(filePath) || StringUtils.isBlank(fileNm)){
				throw new RequiredFieldException("파일관련 정보(path or name)가 없습니다.");
			}
			File tmp = new File(filePath, fileNm.substring(0, fileNm.indexOf("."))+".tmp");
			tmp.createNewFile();
			FileUtils.writeStringToFile(tmp, xml, "UTF-8");
			
			File f = new File(filePath, fileNm);
			tmp.renameTo(f);
		}catch(Exception e){
			throw new XmlWriteException("XML File을 생성중 에러가 발생했습니다. File을 생성할 수 없습니다.", e);
		}
	}

}
