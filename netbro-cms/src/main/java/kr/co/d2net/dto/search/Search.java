package kr.co.d2net.dto.search;

import java.util.Date;

public class Search {
	
	public String getFlPath() {
		return flPath;
	}
	public void setFlPath(String flPath) {
		this.flPath = flPath;
	}
	public String getOrgFilenm() {
		return orgFilenm;
	}
	public void setOrgFilenm(String orgFilenm) {
		this.orgFilenm = orgFilenm;
	}
	public String getTransFilenm() {
		return transFilenm;
	}
	public void setTransFilenm(String transFilenm) {
		this.transFilenm = transFilenm;
	}
	public static final int PAGE_SIZE = 10;
	
	private Integer pageNo;
	private String keyword;
	private Long ctId;
	private Integer categoryId;
	private Integer episodeId;
	private Integer segmentId;
	private Date startDt;
	private Date endDt;
	private String contentNm;
	private Integer duration ;
	private String  spcInfo ;
	private Date  brdDd ;
	private String episodeNm;
	private String categoryNm;
	private Integer depth;
	private Integer preParent;
	private String userNm;
	private String userPhone;
	private String userPasswd;
	private String useYn;
	private String  userId;
	private Long  userSeq;
	private Integer authId;
	private String authNm;
	private String authSubNm;
	
	private String controlGubun;
	
	private Date fromDate;
	private Date toDate;
	
	private Integer lft;
	private Integer rgt;
	
	private String ctiFmt;
	private String dateGb;
	private String ctCla;
	private String ctTyp;
	private Integer pageFrom;
	private Integer pageSize;
	
	private String searchTyp;
	private String firstSelectBox;
	private String secondSelectBox;
	private String selectContentNm;
	
	private String searchUserObj;
	private String userSelectBox;
	private String ctIds;
	private String dataStatCd;
	
	private String searchEquipObj;
	private String equipSelectBox;
	
	private String deviceId;
	
	private String oldPasswd;
    private String newPasswd;
	
    private String rootId;
    private String nodes;
    
    
    private String flPath;
    private String orgFilenm;
    private String transFilenm;
    private String modrId;
    
	
    
	public String getModrId() {
		return modrId;
	}
	public void setModrId(String modrId) {
		this.modrId = modrId;
	}
	public String getNodes() {
		return nodes;
	}
	public void setNodes(String nodes) {
		this.nodes = nodes;
	}
	public String getRootId() {
		return rootId;
	}
	public void setRootId(String rootId) {
		this.rootId = rootId;
	}
	public String getOldPasswd() {
		return oldPasswd;
	}
	public void setOldPasswd(String oldPasswd) {
		this.oldPasswd = oldPasswd;
	}
	public String getNewPasswd() {
		return newPasswd;
	}
	public void setNewPasswd(String newPasswd) {
		this.newPasswd = newPasswd;
	}
	public String getSearchEquipObj() {
		return searchEquipObj;
	}
	public void setSearchEquipObj(String searchEquipObj) {
		this.searchEquipObj = searchEquipObj;
	}
	public String getEquipSelectBox() {
		return equipSelectBox;
	}
	public void setEquipSelectBox(String equipSelectBox) {
		this.equipSelectBox = equipSelectBox;
	}
	public String getDataStatCd() {
		return dataStatCd;
	}
	public void setDataStatCd(String dataStatCd) {
		this.dataStatCd = dataStatCd;
	}
	public String getCtIds() {
		return ctIds;
	}
	public void setCtIds(String ctIds) {
		this.ctIds = ctIds;
	}
	public String getSearchTyp() {
		return searchTyp;
	}
	public void setSearchTyp(String searchTyp) {
		this.searchTyp = searchTyp;
	}
	public String getDateGb() {
		return dateGb;
	}
	public void setDateGb(String dateGb) {
		this.dateGb = dateGb;
	}
	public String getCtCla() {
		return ctCla;
	}
	public void setCtCla(String ctCla) {
		this.ctCla = ctCla;
	}
	public String getCtTyp() {
		return ctTyp;
	}
	public void setCtTyp(String ctTyp) {
		this.ctTyp = ctTyp;
	}
	public Integer getPageFrom() {
		return pageFrom;
	}
	public void setPageFrom(Integer pageFrom) {
		this.pageFrom = pageFrom;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getCtiFmt() {
		return ctiFmt;
	}
	public void setCtiFmt(String ctiFmt) {
		this.ctiFmt = ctiFmt;
	}
	public Integer getLft() {
		return lft;
	}
	public void setLft(Integer lft) {
		this.lft = lft;
	}
	public Integer getRgt() {
		return rgt;
	}
	public void setRgt(Integer rgt) {
		this.rgt = rgt;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getUserPasswd() {
		return userPasswd;
	}
	public void setUserPasswd(String userPasswd) {
		this.userPasswd = userPasswd;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getUserSeq() {
		return userSeq;
	}
	public void setUserSeq(Long userSeq) {
		this.userSeq = userSeq;
	}
	public Integer getPreParent() {
		return preParent;
	}
	public void setPreParent(Integer preParent) {
		this.preParent = preParent;
	}
	public Integer getDepth() {
		return depth;
	}
	public void setDepth(Integer depth) {
		this.depth = depth;
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
	public Date getBrdDd() {
		return brdDd;
	}
	public void setBrdDd(Date brdDd) {
		this.brdDd = brdDd;
	}
	public String getContentNm() {
		return contentNm;
	}
	public void setContentNm(String contentNm) {
		this.contentNm = contentNm;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public String getSpcInfo() {
		return spcInfo;
	}
	public void setSpcInfo(String spcInfo) {
		this.spcInfo = spcInfo;
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
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public Long getCtId() {
		return ctId;
	}
	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Date getStartDt() {
		return startDt;
	}
	public void setStartDt(Date startDt) {
		this.startDt = startDt;
	}
	public Date getEndDt() {
		return endDt;
	}
	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}
	public Integer getAuthId() {
		return authId;
	}
	public void setAuthId(Integer authId) {
		this.authId = authId;
	}
	public String getAuthNm() {
		return authNm;
	}
	public void setAuthNm(String authNm) {
		this.authNm = authNm;
	}
	public String getControlGubun() {
		return controlGubun;
	}
	public void setControlGubun(String controlGubun) {
		this.controlGubun = controlGubun;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getSecondSelectBox() {
		return secondSelectBox;
	}
	public void setSecondSelectBox(String secondSelectBox) {
		this.secondSelectBox = secondSelectBox;
	}
	public String getFirstSelectBox() {
		return firstSelectBox;
	}
	public void setFirstSelectBox(String firstSelectBox) {
		this.firstSelectBox = firstSelectBox;
	}
	public String getSelectContentNm() {
		return selectContentNm;
	}
	public void setSelectContentNm(String selectContentNm) {
		this.selectContentNm = selectContentNm;
	}
	public String getSearchUserObj() {
		return searchUserObj;
	}
	public void setSearchUserObj(String searchUserObj) {
		this.searchUserObj = searchUserObj;
	}
	public String getUserSelectBox() {
		return userSelectBox;
	}
	public void setUserSelectBox(String userSelectBox) {
		this.userSelectBox = userSelectBox;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getAuthSubNm() {
		return authSubNm;
	}
	public void setAuthSubNm(String authSubNm) {
		this.authSubNm = authSubNm;
	}
}
