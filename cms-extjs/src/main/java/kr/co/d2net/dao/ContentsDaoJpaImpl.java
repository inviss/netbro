package kr.co.d2net.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Content;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.utils.ObjectUtils;

import org.springframework.stereotype.Repository;

@Repository(value="contentsDaoJpa")
public class ContentsDaoJpaImpl  {

	@PersistenceContext
	private EntityManager entityManager;


	public List<Content> findContents(Search search)
			throws DaoNonRollbackException {
		/*
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Content> criteriaQuery = criteriaBuilder.createQuery(Content.class);
		Root<ContentsTbl> from = criteriaQuery.from(ContentsTbl.class);
		//Join<ContentsTbl, ContentsInstTbl> inst = from.join("contentsInst", JoinType.INNER);

		CriteriaQuery<Content> select = criteriaQuery.select(criteriaBuilder.construct(
				Content.class, 
				from.get(ContentsTbl_.ctId), from.get(ContentsTbl_.ctNm))
		);

		TypedQuery<Content> typedQuery = entityManager.createQuery(select);
		List<Content> list2 = typedQuery.getResultList();

		for(Content ct : list2) {
			System.out.print(ct.getCtId()+", ");
			System.out.println(ct.getCtNm());
		}
		 */

		/*
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
		Root<ContentsTbl> from = criteriaQuery.from(ContentsTbl.class);
		//Join<ContentsTbl, ContentsInstTbl> inst = from.join("contentsInst", JoinType.INNER);

		CriteriaQuery<Tuple> select = criteriaQuery.select(criteriaBuilder.tuple(
				from.get(ContentsTbl_.ctId).alias("ctId"), from.get(ContentsTbl_.ctNm).alias("ctNm"))
		);

		TypedQuery<Tuple> typedQuery = entityManager.createQuery(select);
		List<Tuple> list2 = typedQuery.getResultList();

		for(Tuple ct : list2) {
			System.out.print(ct.get("ctId", Long.class)+", ");
			System.out.println(ct.get("ctNm", String.class));
		}
		 */

		String[] ctFields = {"ctId", "ctNm", "categoryId","brdDd","regDt","vdQlty","delDd","rpimgKfrmSeq"};
		String[] ctiFields = {"flPath", "wrkFileNm","vdHresol","vdVresol"};


		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
		Root<ContentsTbl> from = criteriaQuery.from(ContentsTbl.class);
		Join<ContentsTbl, ContentsInstTbl> inst = from.join("contentsInst", JoinType.INNER);

		criteriaQuery.where(criteriaBuilder.equal(inst.get("ctiFmt"), "102"));

		Selection[] s = new Selection[ctFields.length + ctiFields.length];

		int i=0;

		for(int j=0; j<ctFields.length; j++) {

			s[i] = from.get(ctFields[j]);
			i++;

		}

		for(int j=0; j<ctiFields.length; j++) {

			s[i] = inst.get(ctiFields[j]);
			i++;

		}

		CriteriaQuery<Object[]> select = criteriaQuery.select(

				criteriaBuilder.array(s)

				);

		TypedQuery<Object[]> typedQuery = entityManager.createQuery(select);

		typedQuery.setFirstResult(0);
		typedQuery.setMaxResults(5);
		// query cache 설정
		typedQuery.setHint("org.hibernate.cacheable", true);
		
		List<Object[]> list2 = typedQuery.getResultList();

		List<Content> contents = new ArrayList<Content>();

		for(Object[] list : list2) {

			Content content = new Content();

			i=0;
			for(int j = 0; j<ctFields.length; j++) {

				ObjectUtils.setProperty(content, ctFields[j], list[i]);
				i++;

			}

			for(int j = 0; j<ctiFields.length; j++) {

				ObjectUtils.setProperty(content, ctiFields[j], list[i]);
				i++;

			}

			contents.add(content);

		}

		return contents;

	}

}
