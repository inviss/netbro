package kr.co.d2net.dto;

import java.util.Date;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name="AUTH_TBL")
public class AuthTbl extends BaseObject{
	
	public AuthTbl() {}

	public AuthTbl(Integer authId) {
		this.authId = authId;
	}
	

	@Id
	@TableGenerator(name = "AUTH_ID_SEQ", table = "ID_GEN_TBL", 
	pkColumnName = "ENTITY_NAME", pkColumnValue = "AUTH_ID_SEQ", valueColumnName = "VALUE", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "AUTH_ID_SEQ")
	@BusinessKey
	@Column(name="AUTH_ID", length=16)
	private Integer authId;		//권한ID	

	@Column(name="AUTH_NM", length=30, nullable=true)
	private String authNm;	//권한명	
	
	@Column(name="AUTH_SUB_NM", length=200, nullable=true)
	private String authSubNm;	//권한명	

	@Column(name="REGRID", length=30, nullable=true)
	private String regrId;	//등록자ID	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date regDt;//등록일시

	@Column(name="MODRID", length=30, nullable=true)
	private String modrId;	//수정자ID

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT", nullable=true)
	private Date modDt;//수정일시

	@Column(name="USE_YN", columnDefinition="char(1) default 'Y'", nullable=true)
	private String useYn;	//사용여부

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

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	
	public String getAuthSubNm() {
		return authSubNm;
	}

	public void setAuthSubNm(String authSubNm) {
		this.authSubNm = authSubNm;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(targetEntity=RoleAuthTbl.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="authTbl")
	private Set<RoleAuthTbl> roleAuth;

	public Set<RoleAuthTbl> getRoleAuth() {
		return roleAuth;
	}
	public  void setRoleAuth(Set<RoleAuthTbl> roleAuth) {
		this.roleAuth = roleAuth;
	}
	
	
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(targetEntity=UserAuthTbl.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="authTbl")
	private  Set<UserAuthTbl> userAuth;

	public  Set<UserAuthTbl> getUserAuth() {
		return userAuth;
	}
	public  void setUserAuth(Set<UserAuthTbl> userAuth) {
		this.userAuth = userAuth;
	}
	
	

}
