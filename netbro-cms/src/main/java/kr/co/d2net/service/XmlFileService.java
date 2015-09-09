package kr.co.d2net.service;

public interface XmlFileService {
	
	/**
	 * 신규로 파일을 생성할때 해당 폴더를 모니터링하는 장비가 있다면 파일 쓰기를 완성하기 이전에
	 * 파일의 내용을 읽어 가려는 시도를 할 우려가 많다.
	 * 임시로 .tmp 확장자로 파일을 생성한 후 원하는 파일명으로 rename을 하면 된다.
	 * @param xml
	 * @param filePath
	 * @param fileNm
	 * @throws Exception
	 */
	public void StringToFile(String xml, String filePath, String fileNm) throws Exception;
	
	
	/**
	 * 입력된 파일경로에서 파일의 내용을 읽어온다.
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public String FileToString(String filePath) throws Exception;
	
}
