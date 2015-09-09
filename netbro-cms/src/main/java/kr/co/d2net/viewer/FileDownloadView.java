package kr.co.d2net.viewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.d2net.dto.Attatch;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

public class FileDownloadView extends AbstractView {
	
	private String getBrowser(HttpServletRequest request) {
		String agentType = request.getHeader("User-Agent");
		if(agentType.indexOf("MSIE") > -1) {
			return "MSIE";
		} else if(agentType.indexOf("Chrome") > -1) {
			return "Chrome";
		} else if(agentType.indexOf("Opera") > -1) {
			return "Opera";
		}
		return "Firefox";
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		File file = (File)model.get("file");
		if(logger.isDebugEnabled()) {
			logger.debug("down path: "+file.getAbsolutePath());
		}
		Attatch attatch = (Attatch)model.get("attatch");

		String agentType = getBrowser(request);
		
		response.setContentType(super.getContentType());
		response.setContentLength((int) file.length());
		if(agentType.equals("MSIE")) {
			response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(attatch.getRealNm(), "UTF-8").replaceAll("\\+", " ")+";");
		} else if(agentType.equals("Firefox")) {
			response.setHeader("Content-Disposition", "attachment;filename="+new String(attatch.getRealNm().getBytes("UTF-8"), "8859_1")+";");
		} else if(agentType.equals("Opera")) {
			response.setHeader("Content-Disposition", "attachment;filename="+new String(attatch.getRealNm().getBytes("UTF-8"), "8859_1")+";");
		} else if(agentType.equals("Chrome")) {
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<attatch.getRealNm().length(); i++) {
				char c = attatch.getRealNm().charAt(i);
				if(c > '~') {
					sb.append(URLEncoder.encode(""+c, "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			response.setHeader("Content-Disposition", "attachment;filename="+sb.toString()+";");
		}
		response.setHeader("Content-Transfer-Encoding", "binary;");
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");

		OutputStream out = response.getOutputStream();

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis,out);
		} catch(java.io.IOException ioe) {
			logger.error("renderMergedOutputModel",ioe);
		} finally {
			if(fis != null) fis.close();
		}
	}
}
