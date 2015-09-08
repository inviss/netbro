package kr.co.d2net.search.index;

import java.util.Date;

import kr.co.d2net.search.annotation.Entity;
import kr.co.d2net.search.annotation.Id;
import kr.co.d2net.search.annotation.Indexed;
import kr.co.d2net.xml.adapter.JsonDateDeSerializer;
import kr.co.d2net.xml.adapter.JsonDateSerializer;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@Entity(indexName="cms", indexType="metadata")
public class SearchMeta {
	
	@JsonProperty("category_id")
	@Indexed(name="category_id", type="integer", index="not_analyzed")
	private Integer categoryId;

	@JsonProperty("episode_id")
	@Indexed(name="episode_id", type="integer", index="not_analyzed")
	private Integer episodeId;

	@JsonProperty("segment_id")
	@Indexed(name="segment_id", type="integer", index="not_analyzed")
	private Integer segmentId;

	@JsonProperty("category_nm")
	@Indexed(name="category_nm", type="string", index="analyzed", analyzer="korean_index", termVector="with_positions_offsets")
	private String categoryNm;

	@JsonProperty("episode_nm")
	@Indexed(name="episode_nm", type="string", index="analyzed", analyzer="korean_index", termVector="with_positions_offsets")
	private String episodeNm;

	@JsonProperty("segment_nm")
	@Indexed(name="segment_nm", type="string", index="analyzed", analyzer="korean_index", termVector="with_positions_offsets")
	private String segmentNm;
	
	@JsonProperty("ct_id")
	@Indexed(name="ct_id", type="long", index="not_analyzed")
	private Long ctId;

	@JsonProperty("vd_hresol")
	@Indexed(name="vd_hresol", type="integer", index="no")
	private Integer vdHresol;

	@JsonProperty("vd_vresol")
	@Indexed(name="vd_vresol", type="integer", index="no")
	private Integer vdVresol;

	@JsonProperty("rp_img_kfrm_seq")
	@Indexed(name="rp_img_kfrm_seq", type="long", index="no")
	private Long rpImgKfrmSeq;

	//@JsonSerialize(using = JsonDateSerializer.class)
	//@JsonDeserialize(using = JsonDateDeSerializer.class)
	@JsonProperty("reg_dt")
	@Indexed(name="reg_dt", type="long", index="not_analyzed")
	private Long regDt;

	@JsonProperty("duration")
	@Indexed(name="duration", type="long", index="no")
	private Long duration;

	@JsonProperty("fl_path")
	@Indexed(name="fl_path", type="string", index="no")
	private String flPath;

	@JsonProperty("prod_route")
	@Indexed(name="prod_route", type="string", index="not_analyzed")
	private String prodRoute;

	@JsonProperty("ct_typ")
	@Indexed(name="ct_typ", type="string", index="not_analyzed")
	private String ctTyp;

	@JsonProperty("ct_cla")
	@Indexed(name="ct_cla", type="string", index="not_analyzed")
	private String ctCla;

	@JsonProperty("cti_fmt")
	@Indexed(name="cti_fmt", type="string", index="not_analyzed")
	private String ctiFmt;

	@JsonProperty("ct_nm")
	@Indexed(name="ct_nm", type="string", index="analyzed", analyzer="korean_index", termVector="with_positions_offsets")
	private String ctNm;

	@JsonProperty("key_words")
	@Indexed(name="key_words", type="string", index="analyzed", analyzer="korean_index", termVector="with_positions_offsets")
	private String keyWords;

	//@JsonSerialize(using = JsonDateSerializer.class)
	//@JsonDeserialize(using = JsonDateDeSerializer.class)
	@JsonProperty("brd_dd")
	@Indexed(name="brd_dd", type="long", index="not_analyzed")
	private Long brdDd;

	@JsonProperty("cont")
	@Indexed(name="cont", type="string", index="analyzed", analyzer="korean_index", termVector="with_positions_offsets")
	private String cont;

	@JsonProperty("regrid")
	@Indexed(name="regrid", type="string", index="not_analyzed")
	private String regrid;

	@JsonProperty("asp_rtp_cd")
	@Indexed(name="asp_rtp_cd", type="string", index="not_analyzed")
	private String aspRtpCd;

	@JsonProperty("vd_qlty")
	@Indexed(name="vd_qlty", type="string", index="not_analyzed")
	private String vdQlty;

	@JsonProperty("nodes")
	@Indexed(name="nodes", type="string", index="not_analyzed")
	private String nodes;

	@JsonProperty("ct_leng")
	@Indexed(name="ct_leng", type="string", index="no")
	private String ctLeng;

	@JsonProperty("svr_fl_nm")
	@Indexed(name="svr_fl_nm", type="string", index="no")
	private String svrFlNm;

	@JsonProperty("rist_clf_cd")
	@Indexed(name="rist_clf_cd", type="string", index="not_analyzed")
	private String ristClfCd;

	@JsonProperty("lock_stat_cd")
	@Indexed(name="lock_stat_cd", type="string", index="not_analyzed")
	private String lockStatCd;

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getEpisodeId() {
		return episodeId;
	}

	public void setEpisodeId(Integer episodeId) {
		this.episodeId = episodeId;
	}

	public Integer getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(Integer segmentId) {
		this.segmentId = segmentId;
	}

	public String getCategoryNm() {
		return categoryNm;
	}

	public void setCategoryNm(String categoryNm) {
		this.categoryNm = categoryNm;
	}

	public String getEpisodeNm() {
		return episodeNm;
	}

	public void setEpisodeNm(String episodeNm) {
		this.episodeNm = episodeNm;
	}

	public String getSegmentNm() {
		return segmentNm;
	}

	public void setSegmentNm(String segmentNm) {
		this.segmentNm = segmentNm;
	}

	@Id
	public Long getCtId() {
		return ctId;
	}

	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}

	public Integer getVdHresol() {
		return vdHresol;
	}

	public void setVdHresol(Integer vdHresol) {
		this.vdHresol = vdHresol;
	}

	public Integer getVdVresol() {
		return vdVresol;
	}

	public void setVdVresol(Integer vdVresol) {
		this.vdVresol = vdVresol;
	}

	public Long getRpImgKfrmSeq() {
		return rpImgKfrmSeq;
	}

	public void setRpImgKfrmSeq(Long rpImgKfrmSeq) {
		this.rpImgKfrmSeq = rpImgKfrmSeq;
	}

	public Long getRegDt() {
		return regDt;
	}

	public void setRegDt(Long regDt) {
		this.regDt = regDt;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getFlPath() {
		return flPath;
	}

	public void setFlPath(String flPath) {
		this.flPath = flPath;
	}

	public String getProdRoute() {
		return prodRoute;
	}

	public void setProdRoute(String prodRoute) {
		this.prodRoute = prodRoute;
	}

	public String getCtTyp() {
		return ctTyp;
	}

	public void setCtTyp(String ctTyp) {
		this.ctTyp = ctTyp;
	}

	public String getCtCla() {
		return ctCla;
	}

	public void setCtCla(String ctCla) {
		this.ctCla = ctCla;
	}

	public String getCtiFmt() {
		return ctiFmt;
	}

	public void setCtiFmt(String ctiFmt) {
		this.ctiFmt = ctiFmt;
	}

	public String getCtNm() {
		return ctNm;
	}

	public void setCtNm(String ctNm) {
		this.ctNm = ctNm;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public Long getBrdDd() {
		return brdDd;
	}

	public void setBrdDd(Long brdDd) {
		this.brdDd = brdDd;
	}

	public String getCont() {
		return cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
	}

	public String getRegrid() {
		return regrid;
	}

	public void setRegrid(String regrid) {
		this.regrid = regrid;
	}

	public String getAspRtpCd() {
		return aspRtpCd;
	}

	public void setAspRtpCd(String aspRtpCd) {
		this.aspRtpCd = aspRtpCd;
	}

	public String getVdQlty() {
		return vdQlty;
	}

	public void setVdQlty(String vdQlty) {
		this.vdQlty = vdQlty;
	}

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public String getCtLeng() {
		return ctLeng;
	}

	public void setCtLeng(String ctLeng) {
		this.ctLeng = ctLeng;
	}

	public String getSvrFlNm() {
		return svrFlNm;
	}

	public void setSvrFlNm(String svrFlNm) {
		this.svrFlNm = svrFlNm;
	}

	public String getRistClfCd() {
		return ristClfCd;
	}

	public void setRistClfCd(String ristClfCd) {
		this.ristClfCd = ristClfCd;
	}

	public String getLockStatCd() {
		return lockStatCd;
	}

	public void setLockStatCd(String lockStatCd) {
		this.lockStatCd = lockStatCd;
	}
	
}
