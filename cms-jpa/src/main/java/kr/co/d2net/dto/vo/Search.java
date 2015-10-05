package kr.co.d2net.dto.vo;

import java.util.Date;

public class Search {
	
	/*
	 * 검색 구분자
	 * Default value : 리스트
	 * 동일한 검색 조건에 결과값을 리스트로 받는지 검색건수를 받는지 선택.
	 * Search().list(), Search().count()
	 * if(search.getOperator() == Operator.LIST) {
	 *   Path<Long> ctId = root.get(ContentsTbl_.ctId);
	 *   .....
	 * }
	 */
	public static enum Operator {
		LIST,
		COUNT;
	}
	private Operator operator = Operator.LIST;
	public Search withOperator(Operator operator) {
		this.operator = operator;
		return this;
	}
	public Operator getOperator() {
		return operator;
	}
	
	public Search list() {
		return withOperator(Operator.LIST);
	}

	public Search count() {
		return withOperator(Operator.COUNT);
	}
	
	/*
	 * 기간 검색 구분자
	 * Default Value: BRD_DD
	 * 기간 검색 조건의 기본값은 등록일이고, 조건에 따라 동시 검색 및
	 * 방송일 검색으로 나눌 수 있다.
	 * Search.all(), Search.brdDd(), Search.regDd()
	 */
	public static enum Separator {
		ALL,
		BRD_DD,
		REG_DD
	}
	private Separator separator = Separator.REG_DD;
	public Search withSeparator(Separator separator) {
		this.separator = separator;
		return this;
	}
	public Separator getSeparator() {
		return separator;
	}
	public Search all() {
		return withSeparator(Separator.ALL);
	}
	public Search brdDd() {
		return withSeparator(Separator.BRD_DD);
	}
	public Search regDd() {
		return withSeparator(Separator.REG_DD);
	}
	
	public final int PAGE_SIZE = 10;
	
	private Integer pageNo;
	private String keyword;
	private Long ctId;
	private Long  ctiId;
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
	private String authId;
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
	private String workStat;
	private String traContentNm;
	
	private String searchUserObj;
	private String userSelectBox;
	private String ctIds;
	private String dataStatCd;
	
	private String searchEquipObj;
	private String equipSelectBox;
	
	private String deviceId;
	private Integer deviceNum;
	
	private String oldPasswd;
    private String newPasswd;
	
    private String rootId;
    private String nodes;
    private String node;
    
    
    private String flPath;
    private String orgFilenm;
    private String transFilenm;
    private String modrId;
    private String ctNm;
    private String clfCd;
    private String sclCd;
    private String ristClfCd;
    private Integer menuId;
    
    private Date to;
    private Date from;
    
    private Date regStartDt;
    private Date regEndDt;
    private Date brdStartDt;
    private Date brdEndDt;
    
    private Long rpImg;
    private String cont;
    
    private Integer storageId;
    
    private Long trsSeq;
    private Long seq;
    private String version;
    
    private String searchGb;
    private String prodRoute;
    private String clfGubun;
    
    //폐기관리검색조건
    private String disuseClf;
    
    //공지사항검색조건
    private Long noticeId;
    
    //카테고리 검색조건
    private Integer orderNum;
    
    
    
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public Long getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}
	public String getDisuseClf() {
		return disuseClf;
	}
	public void setDisuseClf(String disuseClf) {
		this.disuseClf = disuseClf;
	}
	public String getClfGubun() {
		return clfGubun;
	}
	public void setClfGubun(String clfGubun) {
		this.clfGubun = clfGubun;
	}
	public String getSclCd() {
		return sclCd;
	}
	public void setSclCd(String sclCd) {
		this.sclCd = sclCd;
	}
	public String getProdRoute() {
		return prodRoute;
	}
	public void setProdRoute(String prodRoute) {
		this.prodRoute = prodRoute;
	}
	public Long getSeq() {
		return seq;
	}
	public void setSeq(Long seq) {
		this.seq = seq;
	}
	public Long getCtiId() {
		return ctiId;
	}
	public void setCtiId(Long ctiId) {
		this.ctiId = ctiId;
	}
	public String getSearchGb() {
		return searchGb;
	}
	public void setSearchGb(String searchGb) {
		this.searchGb = searchGb;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Integer getDeviceNum() {
		return deviceNum;
	}
	public void setDeviceNum(Integer deviceNum) {
		this.deviceNum = deviceNum;
	}
	public Long getTrsSeq() {
		return trsSeq;
	}
	public void setTrsSeq(Long trsSeq) {
		this.trsSeq = trsSeq;
	}
	public Integer getStorageId() {
		return storageId;
	}
	public void setStorageId(Integer storageId) {
		this.storageId = storageId;
	}
	public String getCont() {
		return cont;
	}
	public void setCont(String cont) {
		this.cont = cont;
	}
	public Long getRpImg() {
		return rpImg;
	}
	public void setRpImg(Long rpImg) {
		this.rpImg = rpImg;
	}
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
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	public Date getRegStartDt() {
		return regStartDt;
	}
	public void setRegStartDt(Date regStartDt) {
		this.regStartDt = regStartDt;
	}
	public Date getRegEndDt() {
		return regEndDt;
	}
	public void setRegEndDt(Date regEndDt) {
		this.regEndDt = regEndDt;
	}
	public Date getBrdStartDt() {
		return brdStartDt;
	}
	public void setBrdStartDt(Date brdStartDt) {
		this.brdStartDt = brdStartDt;
	}
	public Date getBrdEndDt() {
		return brdEndDt;
	}
	public void setBrdEndDt(Date brdEndDt) {
		this.brdEndDt = brdEndDt;
	}
	public Integer getMenuId() {
		return menuId;
	}
	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public String getRistClfCd() {
		return ristClfCd;
	}
	public void setRistClfCd(String ristClfCd) {
		this.ristClfCd = ristClfCd;
	}
	public String getClfCd() {
		return clfCd;
	}
	public void setClfCd(String clfCd) {
		this.clfCd = clfCd;
	}
	public String getCtNm() {
		return ctNm;
	}
	public void setCtNm(String ctNm) {
		this.ctNm = ctNm;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
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

	public String getAuthId() {
		return authId;
	}
	public void setAuthId(String authId) {
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

	public String getWorkStat() {
		return workStat;
	}
	public void setWorkStat(String workStat) {
		this.workStat = workStat;
	}
	public String getTraContentNm() {
		return traContentNm;
	}
	public void setTraContentNm(String traContentNm) {
		this.traContentNm = traContentNm;
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
