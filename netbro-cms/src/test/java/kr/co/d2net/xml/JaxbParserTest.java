package kr.co.d2net.xml;

import java.io.File;

import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.xml.MetaInfo;
import kr.co.d2net.dto.xml.Metadata;
import kr.co.d2net.dto.xml.Workflow;
import kr.co.d2net.service.XmlConvertorService;
import kr.co.d2net.service.XmlConvertorServiceImpl;
import kr.co.d2net.utils.DateUtils;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

public class JaxbParserTest {

	@Ignore
	@Test
	public void xmlParsing() {
		try {
			XmlConvertorService<Workflow> convertorService = new XmlConvertorServiceImpl<Workflow>();
			/*
			StringBuffer buffer = new StringBuffer()
			.append("<workflow>                                                ")
			.append("    <info>                                                ")
			.append("        <eq_id>LING_01</eq_id>                            ")
			.append("        <eq_channel>01</eq_channel>                       ")
			.append("        <status>200</status>                              ")
			.append("        <prgrs>0</prgrs>                                  ")
			.append("        <regrid></regrid>                                 ")
			.append("    </info>                                               ")
			.append("    <metadata>                                            ")
			.append("        <contents>                                        ")
			.append("            <cate_id></cate_id>                           ")
			.append("            <episode_id></episode_id>                     ")
			.append("            <segment_id></segment_id>                     ")
			.append("            <ct_id></ct_id>                               ")
			.append("            <ct_nm>test</ct_nm>                           ")
			.append("            <vd_qlty>HD</vd_qlty>                         ")
			.append("            <brd_dd>2013-09-02</brd_dd>                   ")
			.append("            <duration>503</duration>                      ")
			.append("            <key_words></key_words>                       ")
			.append("            <asp_rto_cd>16:9</asp_rto_cd>                 ")
			.append("            <audio_type_cd>02</audio_type_cd>             ")
			.append("        </contents>                                       ")
			.append("        <contents_inst>                                   ")
			.append("            <cti_id></cti_id>                             ")
			.append("            <cti_fmt></cti_fmt>                           ")
			.append("            <bit_rt>50000000</bit_rt>                     ")
			.append("            <frm_per_sec>29.97</frm_per_sec>              ")
			.append("            <drp_frm_yn>y</drp_frm_yn>                    ")
			.append("            <vd_hresol>1080</vd_hresol>                   ")
			.append("            <vd_vresol>1920</vd_vresol>                   ")
			.append("            <audio_samp_frq>48000</audio_samp_frq>        ")
			.append("            <audio_bdwt>384000</audio_bdwt>               ")
			.append("            <file_path>201309/02/</file_path>             ")
			.append("            <org_file_nm>20130902050045</org_file_nm>     ")
			.append("            <file_ext>mxf</file_ext>                      ")
			.append("            <fl_sz>125412424</fl_sz>                      ")
			.append("        </contents_inst>                                  ")
			.append("        <contents_inst>                                   ")
			.append("            <cti_id></cti_id>                             ")
			.append("            <cti_fmt></cti_fmt>                           ")
			.append("            <bit_rt>700000</bit_rt>                       ")
			.append("            <frm_per_sec>29.97</frm_per_sec>              ")
			.append("            <vd_hresol>360</vd_hresol>                    ")
			.append("            <vd_vresol>640</vd_vresol>                    ")
			.append("            <audio_samp_frq>48000</audio_samp_frq>        ")
			.append("            <audio_bdwt>96000</audio_bdwt>                ")
			.append("            <file_path>201309/02/</file_path>             ")
			.append("            <org_file_nm>20130902050045</org_file_nm>     ")
			.append("            <file_ext>wmv</file_ext>                      ")
			.append("            <fl_sz>3223256</fl_sz>                        ")
			.append("        </contents_inst>                                  ")
			.append("    </metadata>                                           ")
			.append("</workflow>                                               ");
			*/
			String xml = FileUtils.readFileToString(new File("D:/test.xml"), "utf-8");
			Workflow workflow = convertorService.unMarshaller(xml);
			Metadata metadata = workflow.getMetadata();
			System.out.println("categoryId: "+metadata.getContentsTbl().getCategoryId());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
