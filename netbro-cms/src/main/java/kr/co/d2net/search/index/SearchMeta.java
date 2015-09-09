package kr.co.d2net.search.index;

import kr.co.d2net.search.annotation.Entity;
import kr.co.d2net.search.annotation.Id;
import kr.co.d2net.search.annotation.Indexed;
import kr.co.d2net.search.annotation.Settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(indexName="cms", indexType="metadata")
@Settings(
	shards=10, replicas=1, refresh=10, analysis=true, 
	analyzer={"korean_index", "korean_query"}, analyzerType="custom", analyzerToken={"mecab_ko_standard_tokenizer", "korean_query_tokenizer"},
	tokenizer="korean_query_tokenizer", tokenizerType="mecab_ko_standard_tokenizer", compundLength=100,
	routing=true, routPath="metadata.ct_id"
)
public class SearchMeta {
	
	@Expose
	@SerializedName("category_id")
	@Indexed(name="category_id", type="integer", index="not_analyzed", store=true, includeInAll=false)
	private Integer categoryId;
	
	@Expose
	@SerializedName("episode_id")
	@Indexed(name="episode_id", type="integer", index="not_analyzed", store=true, includeInAll=false)
	private Integer episodeId;
	
	@Expose
	@SerializedName("segment_id")
	@Indexed(name="segment_id", type="integer", index="not_analyzed", store=true, includeInAll=false)
	private Integer segmentId;
	
	@Expose
	@SerializedName("category_nm")
	@Indexed(name="category_nm", type="string", index="analyzed", analyzer="korean_index", termVector=true, store=true, includeInAll=false)
	private String categoryNm;
	
	@Expose
	@SerializedName("episode_nm")
	@Indexed(name="episode_nm", type="string", index="analyzed", analyzer="korean_index", termVector=true, store=true, includeInAll=false)
	private String episodeNm;
	
	@Expose
	@SerializedName("segment_nm")
	@Indexed(name="segment_nm", type="string", index="analyzed", analyzer="korean_index", termVector=true, store=true, includeInAll=false)
	private String segmentNm;
	
	@Id
	@Expose
	@SerializedName("ct_id")
	@Indexed(name="ct_id", type="long", index="not_analyzed", store=true, includeInAll=false)
	private Long ctId;
	
	@Expose
	@SerializedName("vd_hresol")
	@Indexed(name="vd_hresol", type="integer", index="no", store=true, includeInAll=false)
	private Integer vdHresol;
	
	@Expose
	@SerializedName("vd_vresol")
	@Indexed(name="vd_vresol", type="integer", index="no", store=true, includeInAll=false)
	private Integer vdVresol;
	
	@Expose
	@SerializedName("rp_img_kfrm_seq")
	@Indexed(name="rp_img_kfrm_seq", type="long", index="no", store=true, includeInAll=false)
	private Long rpImgKfrmSeq;
	
	@Expose
	@SerializedName("reg_dt")
	//@Indexed(name="reg_dt", type="date", index="not_analyzed", format="yyyy-MM-dd HH:mm:ss", store=true, includeInAll=false)
	@Indexed(name="reg_dt", type="long", index="not_analyzed", store=true, includeInAll=false)
	private long regDt;
	
	@Expose
	@SerializedName("duration")
	@Indexed(name="duration", type="long", index="no", store=true, includeInAll=false)
	private Long duration;
	
	@Expose
	@SerializedName("fl_path")
	@Indexed(name="fl_path", type="string", index="no", store=true, includeInAll=false)
	private String flPath;
	
	@Expose
	@SerializedName("prod_route")
	@Indexed(name="prod_route", type="string", index="not_analyzed", store=true, includeInAll=false)
	private String prodRoute;
	
	@Expose
	@SerializedName("ct_typ")
	@Indexed(name="ct_typ", type="string", index="not_analyzed", store=true, includeInAll=false)
	private String ctTyp;
	
	@Expose
	@SerializedName("ct_cla")
	@Indexed(name="ct_cla", type="string", index="not_analyzed", store=true, includeInAll=false)
	private String ctCla;
	
	@Expose
	@SerializedName("cti_fmt")
	@Indexed(name="cti_fmt", type="string", index="not_analyzed", store=true, includeInAll=false)
	private String ctiFmt;
	
	@Expose
	@SerializedName("ct_nm")
	@Indexed(name="ct_nm", type="string", index="analyzed", analyzer="korean_index", termVector=true, store=true, includeInAll=false)
	private String ctNm;
	
	@Expose
	@SerializedName("key_words")
	@Indexed(name="key_words", type="string", index="analyzed", analyzer="korean_index", termVector=true, store=true, includeInAll=false)
	private String keyWords;
	
	@Expose
	@SerializedName("brd_dd")
	//@Indexed(name="brd_dd", type="date", index="not_analyzed", format="yyyy-MM-dd", store=true, includeInAll=false)
	@Indexed(name="brd_dd", type="long", index="not_analyzed", store=true, includeInAll=false)
	private long brdDd;
	
	@Expose
	@SerializedName("cont")
	@Indexed(name="cont", type="string", index="analyzed", analyzer="korean_index", termVector=true, store=true, includeInAll=false)
	private String cont;
	
	@Expose
	@SerializedName("regrid")
	@Indexed(name="regrid", type="string", index="not_analyzed", store=true, includeInAll=false)
	private String regrid;
	
	@Expose
	@SerializedName("asp_rtp_cd")
	@Indexed(name="asp_rtp_cd", type="string", index="not_analyzed", store=true, includeInAll=false)
	private String aspRtpCd;
	
	@Expose
	@SerializedName("vd_qlty")
	@Indexed(name="vd_qlty", type="string", index="not_analyzed", store=true, includeInAll=false)
	private String vdQlty;
	
	@Expose
	@SerializedName("nodes")
	@Indexed(name="nodes", type="string", index="not_analyzed", store=true, includeInAll=false)
	private String nodes;

	@Expose
	@SerializedName("ct_leng")
	@Indexed(name="ct_leng", type="string", index="no", store=true, includeInAll=false)
	private String ctLeng;
	
	@Expose
	@SerializedName("svr_fl_nm")
	@Indexed(name="svr_fl_nm", type="string", index="no", store=true, includeInAll=false)
	private String svrFlNm;
	
	@Expose
	@SerializedName("rist_clf_cd")
	@Indexed(name="rist_clf_cd", type="string", index="not_analyzed", store=true, includeInAll=false)
	private String ristClfCd;
	
	@Expose
	@SerializedName("lock_stat_cd")
	@Indexed(name="lock_stat_cd", type="string", index="not_analyzed", store=true, includeInAll=false)
	private String lockStatCd;
/*
	@Expose
	@SerializedName("file")
	@SubEntity(indexType="file")
	private AttatchFile attatchFile;

	
	public AttatchFile getAttatchFile() {
		return attatchFile;
	}
	public void setAttatchFile(AttatchFile attatchFile) {
		this.attatchFile = attatchFile;
	}
*/
	public String getLockStatCd() {
		return lockStatCd;
	}

	public void setLockStatCd(String lockStatCd) {
		this.lockStatCd = lockStatCd;
	}

	public String getRistClfCd() {
		return ristClfCd;
	}

	public void setRistClfCd(String ristClfCd) {
		this.ristClfCd = ristClfCd;
	}

	public String getSvrFlNm() {
		return svrFlNm;
	}

	public void setSvrFlNm(String svrFlNm) {
		this.svrFlNm = svrFlNm;
	}

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

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

	public long getRegDt() {
		return regDt;
	}

	public void setRegDt(long regDt) {
		this.regDt = regDt;
	}

	public long getBrdDd() {
		return brdDd;
	}

	public void setBrdDd(long brdDd) {
		this.brdDd = brdDd;
	}

	public String getCtLeng() {
		return ctLeng;
	}

	public void setCtLeng(String ctLeng) {
		this.ctLeng = ctLeng;
	}

	
	/*
	@Expose
	@SerializedName("content")
	@SubEntity(indexType="content")
	private ContentView contentView;

	public ContentView getContentView() {
		return contentView;
	}
	public void setContentView(ContentView contentView) {
		this.contentView = contentView;
	}
	*/
}
