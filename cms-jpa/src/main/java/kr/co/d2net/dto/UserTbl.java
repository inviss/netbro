package kr.co.d2net.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name="USER_TBL")
public class UserTbl extends BaseObject {

	public UserTbl() {}

	public UserTbl(String userId,Integer authId) {
		this.userId = userId;
		this.authId = authId;
	}
	
	public UserTbl(String controlGubunPath) {
		this.controlGubun = controlGubunPath;
	}

	@Id
	@BusinessKey
	@Column(name="USER_ID", length=30)
	private String  userId;	//사용자ID

	@Column(name="USER_NM", length=50, nullable=true)
	private String userNm;	//사용자명

	@Column(name="REGRID", length=30, nullable=true)
	private String regrId;	//등록자ID

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date regDt; //등록일시

	@Column(name="MODRID", length=30, nullable=true)
	private String modrId;	//수정자ID

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT", nullable=true)
	private Date modDt;	//수정일시

	@Column(name="USE_YN", columnDefinition="char(1) default 'Y'", nullable=true)
	private String useYn;		//사용여부

	@Column(name="USER_PASS", length=32, nullable=true)
	private String userPass;	//비밀번호

	@Column(name="USER_PHONE", length=13, nullable=true)
	private String userPhone;	//전화번호

	@Transient
	private Integer[] userAuths;

	@Transient
	private Integer authId; // 권한 ID

	@Transient
	private String authNm;	//	권한명
	
	@Transient
	private String controlGubun;	//	권한명



	public String getControlGubun() {
		return controlGubun;
	}

	public void setControlGubun(String controlGubun) {
		this.controlGubun = controlGubun;
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
	public Integer[] getUserAuths() {
		return userAuths;
	}
	public void setUserAuths(Integer[] userAuths) {
		this.userAuths = userAuths;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	public String getRegrId() {
		return regrId;
	}

	public void setRegrId(String regrId) {
		this.regrId = regrId;
	}

	public Date getRegDt() {
		return regDt;
	}

	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}

	public String getModrId() {
		return modrId;
	}

	public void setModrId(String modrId) {
		this.modrId = modrId;
	}

	public Date getModDt() {
		return modDt;
	}

	public void setModDt(Date modDt) {
		this.modDt = modDt;
	}


	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}



	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OneToMany(targetEntity=UserAuthTbl.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="userTbl")
	private Set<UserAuthTbl> userAuth = new HashSet<UserAuthTbl>();

	public Set<UserAuthTbl> getUserAuth() {
		return userAuth;
	}

	public void setUserAuth(Set<UserAuthTbl> userAuth) {
		this.userAuth = userAuth;
	}

	public void addUserAuth(UserAuthTbl userAuth) {
		this.userAuth.add(userAuth);
	}


}
