package kr.co.d2net.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name="TEST_DB_TBL")
public class TestDbTbl extends AbstractPersistable<Integer> {
	private static final long serialVersionUID = 8043949665831948354L;
	
	@Override
	public void setId(Integer id) {
		super.setId(id);
	}
	
	@Column(name="test_nm", length=100, nullable=true)
	private int testNm;

	public int getTestNm() {
		return testNm;
	}

	public void setTestNm(int testNm) {
		this.testNm = testNm;
	}
	@Column(name="test_hibernate", length=100, nullable=true)
	private long test_hibernate;

	public long getTest_hibernate() {
		return test_hibernate;
	}

	public void setTest_hibernate(long test_hibernate) {
		this.test_hibernate = test_hibernate;
	}
	

	
}
