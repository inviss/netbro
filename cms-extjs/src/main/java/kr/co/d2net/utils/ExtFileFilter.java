package kr.co.d2net.utils;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.lang.StringUtils;

public class ExtFileFilter implements FileFilter {

	private String[] exts = new String[]{"xml"};

	public ExtFileFilter() {}
	public ExtFileFilter(String exts) {
		if(StringUtils.isNotBlank(exts)) {
			if(exts.indexOf(",") > -1) {
				this.exts = exts.split("\\,");
			} else {
				this.exts[0] = exts;
			}
		}
	}

	public boolean accept(File file) {
		for(String extension : exts) {
			if (file.getName().toLowerCase().indexOf(extension) > -1) {
				return true;
			}
		}
		return false;
	}

}
