package kr.co.d2net.dto;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;



@Entity
@NamedNativeQueries({
	@NamedNativeQuery(name="Menu.findMenuById", query = ""
			+"select   																																																				"
			+"	tm.menu_id as menuId, tm.menu_nm as menuNm, tm.depth                                      "
			+"from (	                                                                                                        "
			+"	select                                                                                                        "
			+"	    node.menu_id, node.menu_nm, (count(parent.menu_id) - (sub.depth + 1)) as depth "
			+"	from menu_tbl node, menu_tbl parent, menu_tbl parent2, (                                                      "
			+"	    select                                                                                                    "
			+"	        menu1.menu_id, (count(menu2.menu_id) -1) as depth                                      "
			+"	    from menu_tbl menu1, menu_tbl menu2                                                                       "
			+"	    where menu1.lft between menu2.lft and menu2.rgt                                                           "
			+"	        and menu1.menu_id = 1 and menu1.use_yn = 'Y' and menu2.use_yn = 'Y'                                   "
			+"	    group by menu1.menu_id, menu1.lft                                                          "
			+"	) sub                                                                                                         "
			+"	where node.lft between parent.lft and parent.rgt and node.lft between parent2.lft and parent2.rgt             "
			+"	    and parent2.menu_id = sub.menu_id and node.use_yn = 'Y' and parent.use_yn = 'Y'                           "
			+"	group by node.menu_id, node.menu_nm, sub.depth, node.lft                          "
			+"	having (count(parent.menu_id) - (sub.depth + 1)) <= 2                                                         "
			+"	order by node.lft                                                                                             "
			+") tm                                                                                                            "
			+"	inner JOIN (SELECT * FROM ROLE_AUTH_TBL WHERE menu_id > 1) mp ON tm.menu_id = mp.menu_id                      "
			+"	inner join (                                                                                                  "
			+"		select tu.user_id, nvl(pu.auth_id, 9) auth_id                                                               "
			+"		from user_tbl tu                                                                                            "
			+"			left outer join user_auth_tbl pu on tu.user_id = pu.user_id                                               "
			+"		where tu.user_id = :userId                                                                                 "
			+"	) up on mp.auth_id = up.auth_id                                                                               "
			+"where tm.depth > 0   ", resultClass = MenuTbl.class),
			@NamedNativeQuery(name = "Menu.getFirstMenuById", query = ""
					+"select                 																																		"
					+"	tm.menu_id  as menuid,                                                                  "
					+"	tm.use_yn   as useyn,                                                                   "
					+"	tm.regrid   as regrid,                                                                  "
					+"	tm.reg_dt   as regdt,                                                                   "
					+"	tm.modrid   as modrid,                                                                  "
					+"	tm.mod_dt   as moddt,                                                                   "
					+"	tm.menu_nm  as menunm,                                                                  "
					+"	tm.lft      as lft,                                                                     "
					+"	tm.rgt      as rgt                                                                      "
					+"from (select * from menu_tbl order by lft asc) tm                                         "
					+"inner join (select * from role_auth_tbl where menu_id > 1) mp on tm.menu_id = mp.menu_id  "
					+"	inner join (                                                                            "
					+"		select tu.user_id, nvl(pu.auth_id, 9) auth_id                                       "
					+"		from user_tbl tu                                                                    "
					+"			left outer join user_auth_tbl pu on tu.user_id = pu.user_id                     "
					+"		where tu.user_id = :userId                                                          "
					+"	) up on mp.auth_id = up.auth_id                                                         "
					+"where tm.use_yn = 'Y' and (tm.rgt - tm.lft) = 1 AND ROWNUM = 1", resultClass = MenuTbl.class)
})
@Table(name="MENU_TBL")
public class MenuTbl extends BaseObject {
	private static final long serialVersionUID = 8043949665831948354L;

	@Id
	@TableGenerator(name = "MENU_ID_SEQ", table = "ID_GEN_TBL", 
	pkColumnName = "ENTITY_NAME", pkColumnValue = "MENU_ID_SEQ", valueColumnName = "VALUE", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "MENU_ID_SEQ")
	@BusinessKey
	@Column(name="MENU_ID", length=4)
	private Integer menuId;	//메뉴ID

	@Column(name="USE_YN", columnDefinition="char(1) default 'Y'", nullable=true)
	private String useYn;	//사용여부

	@Column(name="REGRID", length=30, nullable=true)
	private String regrid;	//등록자ID

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REG_DT", nullable=true)
	private Date regDt;	//등록일시

	@Column(name="MODRID", length=30, nullable=true)
	private String modrid;	//수정자ID

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MOD_DT", nullable=true)
	private Date modDt;	//수정일시

	@BusinessKey
	@Column(name="MENU_NM", length=30, nullable=true)
	private String menuNm;	//메뉴명


	@Column(name="LFT", length=2, nullable=true)
	private Integer lft;	//좌번호

	@Column(name="RGT", length=2, nullable=true)
	private Integer rgt;	//우번호

	@Column(name="URL", length=100, nullable=true)
	private String url;
	
	//UI Header에 image명을 가져올때 사용.
	@Column(name="MENU_EN_NM", length=20, nullable=true)
	private String menuEnNm;


	@Transient
	private Integer depth;
	@Transient
	private String controlGubun;


	public String getControlGubun() {
		return controlGubun;
	}

	public void setControlGubun(String controlGubun) {
		this.controlGubun = controlGubun;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public String getRegrid() {
		return regrid;
	}

	public void setRegrid(String regrid) {
		this.regrid = regrid;
	}

	public Date getRegDt() {
		return regDt;
	}

	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}

	public String getModrid() {
		return modrid;
	}

	public void setModrid(String modrid) {
		this.modrid = modrid;
	}

	public Date getModDt() {
		return modDt;
	}

	public void setModDt(Date modDt) {
		this.modDt = modDt;
	}

	public String getMenuNm() {
		return menuNm;
	}

	public void setMenuNm(String menuNm) {
		this.menuNm = menuNm;
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

	public String getMenuEnNm() {
		return menuEnNm;
	}

	public void setMenuEnNm(String menuEnNm) {
		this.menuEnNm = menuEnNm;
	}


	@OneToMany(targetEntity=RoleAuthTbl.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="menuTbl")
	private Set<RoleAuthTbl> roleAuth;

	public Set<RoleAuthTbl> getRoleAuth() {
		return roleAuth;
	}

	public void setRoleAuth(Set<RoleAuthTbl> roleAuth) {
		this.roleAuth = roleAuth;
	}

}
