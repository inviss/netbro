package kr.co.d2net.xml.adapter;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class UrlEncodeAdapter extends XmlAdapter<String, String> {
	
    @Override
    public String marshal(String v) throws Exception {
        return URLEncoder.encode(v, "utf-8");
    }

    @Override
    public String unmarshal(String v) throws Exception {
        return URLDecoder.decode(v, "utf-8");
    }
    
}
