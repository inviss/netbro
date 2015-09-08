package kr.co.d2net.dto.vo;

import java.util.Date;

public class Profile {
	private Long proFlId;	//프로파일ID
	private String regrId;	//등록자ID
	private String servBit;	//서비스BIT
	private String modrId;	//수정자ID
	private String ext;	//확장자
	private String vdoCodec;	//비디오코덱
	private String vdoBitRate;	//비디오비트레이트
	private String vdohori;	//비디오가로
	private String vdoVert;	//비디오세로
	private String vdoFS;	//비디오FS
	private String vdoSync;	//비디오종회맞춤
	private String audCodec;	//오디오코덱
	private String audBitRate;	//오디오비트레이트
	private String audChan;	//오디오채널
	private String audSRate;	//오디오샘플레이트
	private String keyFrame;	//키프레임
	private Integer chanPriority;	//변경순위
	private Integer priority;	//순위
	private String proFlnm;	//프로파일명
	private Date modDt;	//수정일시
	private Date regDt;	//등록일시
	private String picKind;	//그림종류
	private String useYn;	//사용여부
	private String flNameRule;	//파일이름규칙
	
	

	public String getRegrId() {
		return regrId;
	}
	public void setRegrId(String regrId) {
		this.regrId = regrId;
	}
	public String getServBit() {
		return servBit;
	}
	public void setServBit(String servBit) {
		this.servBit = servBit;
	}
	public String getModrId() {
		return modrId;
	}
	public void setModrId(String modrId) {
		this.modrId = modrId;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getVdoCodec() {
		return vdoCodec;
	}
	public void setVdoCodec(String vdoCodec) {
		this.vdoCodec = vdoCodec;
	}
	public String getVdoBitRate() {
		return vdoBitRate;
	}
	public void setVdoBitRate(String vdoBitRate) {
		this.vdoBitRate = vdoBitRate;
	}
	public String getVdohori() {
		return vdohori;
	}
	public void setVdohori(String vdohori) {
		this.vdohori = vdohori;
	}
	public String getVdoVert() {
		return vdoVert;
	}
	public void setVdoVert(String vdoVert) {
		this.vdoVert = vdoVert;
	}
	public String getVdoFS() {
		return vdoFS;
	}
	public void setVdoFS(String vdoFS) {
		this.vdoFS = vdoFS;
	}
	public String getVdoSync() {
		return vdoSync;
	}
	public void setVdoSync(String vdoSync) {
		this.vdoSync = vdoSync;
	}
	public String getAudCodec() {
		return audCodec;
	}
	public void setAudCodec(String audCodec) {
		this.audCodec = audCodec;
	}
	public String getAudBitRate() {
		return audBitRate;
	}
	public void setAudBitRate(String audBitRate) {
		this.audBitRate = audBitRate;
	}
	public String getAudChan() {
		return audChan;
	}
	public void setAudChan(String audChan) {
		this.audChan = audChan;
	}
	public String getAudSRate() {
		return audSRate;
	}
	public void setAudSRate(String audSRate) {
		this.audSRate = audSRate;
	}
	public String getKeyFrame() {
		return keyFrame;
	}
	public void setKeyFrame(String keyFrame) {
		this.keyFrame = keyFrame;
	}
	public Integer getChanPriority() {
		return chanPriority;
	}
	public void setChanPriority(Integer chanPriority) {
		this.chanPriority = chanPriority;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getProFlnm() {
		return proFlnm;
	}
	public void setProFlnm(String proFlnm) {
		this.proFlnm = proFlnm;
	}
	public Date getModDt() {
		return modDt;
	}
	public void setModDt(Date modDt) {
		this.modDt = modDt;
	}
	public Date getRegDt() {
		return regDt;
	}
	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}
	public String getPicKind() {
		return picKind;
	}
	public void setPicKind(String picKind) {
		this.picKind = picKind;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getFlNameRule() {
		return flNameRule;
	}
	public void setFlNameRule(String flNameRule) {
		this.flNameRule = flNameRule;
	}
	public Long getProFlId() {
		return proFlId;
	}
	public void setProFlId(Long proFlId) {
		this.proFlId = proFlId;
	}
	

}
