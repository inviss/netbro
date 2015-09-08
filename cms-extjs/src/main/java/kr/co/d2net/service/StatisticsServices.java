package kr.co.d2net.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

import kr.co.d2net.dao.IdGenDao;
import kr.co.d2net.dao.StatisticsDao;
import kr.co.d2net.dao.filter.StatisticsSpecifications;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CategoryTbl_;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.ContentsTbl_;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.IdGenTbl;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.StatisticsTbl;
import kr.co.d2net.dto.StatisticsTbl_;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Statistic;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.ObjectUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 카테고리 정보를 조회하기위한함수
 * @author asura
 *
 */
@Service
@Transactional(readOnly=true)
public class StatisticsServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private StatisticsDao statisticsDao;
	@Autowired
	private IdGenDao  idGenDao;
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private MessageSource messageSource;

	/**
	 * 전체 통계쿼리를 조회한다. 스케쥴러에서 statisticsTbl에 데이터를 넣어줄때 사용하는 함수
	 * 한번에 모든 정볼르 조합할수 없으므로 
	 * 카테고리정보조회, 데이터 사애별 조회를 각기하여 리스트를 합친다
	 * @return List<StatisticsTbl>
	 * @throws ParseException 
	 */
	@Modifying
	@Transactional	
	public List<StatisticsTbl> findTotalStatistics(Statistic statistic) throws ServiceException, ParseException{
		Search search = new Search();
		statistic.setCategoryId(0);

		//기간별 카테고리 정보를 조회한다
		List<Statistic> statisticInfos = findCategoryListForPeriod(statistic,search);
		List<StatisticsTbl> returnLists = new ArrayList<StatisticsTbl>();

		//등록건 조회
		search.setDataStatCd("regist");
		List<Statistic> regists = findStatisticsListForSchedulerInDataStatCd(search);

		if(logger.isDebugEnabled()){
			logger.debug("registssize  "+regists.size());
		}

		//폐기건 조회
		search.setDataStatCd("discard");
		List<Statistic> discards = findStatisticsListForSchedulerInDataStatCd(search);

		if(logger.isDebugEnabled()){
			logger.debug("discards ssize  "+discards.size());
		}

		//정리전건 조회
		search.setDataStatCd("beforeArrange");
		List<Statistic> beforeArranges = findStatisticsListForSchedulerInDataStatCd(search);

		if(logger.isDebugEnabled()){
			logger.debug("beforeArranges size  "+beforeArranges.size());
		}

		//정리 완료건 조회
		search.setDataStatCd("completeArrange");
		List<Statistic> completeArranges = findStatisticsListForSchedulerInDataStatCd(search);

		if(logger.isDebugEnabled()){
			logger.debug("completeArranges size  "+completeArranges.size());
		}

		//에러건 조회
		search.setDataStatCd("error");
		List<Statistic> errors = findStatisticsListForSchedulerInDataStatCd(search);

		if(logger.isDebugEnabled()){
			logger.debug("errors ssize  "+errors.size());
		}

		//카테고리 정보를 db에 저장하고 등록건 정보를 업데이트한다
		for(Statistic regist : regists){

			Date date  = new Date();

			SimpleDateFormat format2 = new SimpleDateFormat("yyyyMM");
			date = format2.parse(regist.getRegDd());

			String dateString = format2.format(date);
			regist.setRegDd(dateString);

			for(Statistic categorys : statisticInfos){
				if(categorys.getCategoryId() == regist.getCategoryId()){
					StatisticsTbl newInfo = new StatisticsTbl();

					newInfo.setRegist(regist.getCount());					
					newInfo.setRegDd(regist.getRegDd());
					newInfo.setCategoryId(regist.getCategoryId());				
					newInfo.setDepth(categorys.getDepth());
					newInfo.setGroupId(categorys.getGroupId());

					statisticsDao.save(newInfo);	
				} 
			}
		}

		//통계테이블에 등록되어있는 모든 정보를 조회한다
		List<StatisticsTbl> infos = statisticsDao.findAll();

		//폐기,  정리전, 정리완료의 통계정보를 db에 저장한다
		if(infos != null){
			for(StatisticsTbl info : infos){
				//폐기등록
				for(Statistic discard : discards){
					Date date  = new Date();

					SimpleDateFormat format2 = new SimpleDateFormat("yyyyMM");
					date = format2.parse(discard.getRegDd());
					String dateString = format2.format(date);
					discard.setRegDd(dateString);

					if(discard.getCategoryId() == info.getCategoryId() && discard.getRegDd().equals(info.getRegDd())){
						info.setDiscard(discard.getCount());
						statisticsDao.save(info);			
					} 
				}
			}	

			//정리전 등록
			for(StatisticsTbl info : infos){
				for(Statistic beforeArrange :  beforeArranges){
					Date date  = new Date();

					SimpleDateFormat format2 = new SimpleDateFormat("yyyyMM");
					date = format2.parse(beforeArrange.getRegDd());
					String dateString = format2.format(date);
					beforeArrange.setRegDd(dateString);

					if(beforeArrange.getCategoryId() == info.getCategoryId() && beforeArrange.getRegDd().equals(info.getRegDd())){
						info.setBeforeArrange(beforeArrange.getCount());	
						statisticsDao.save(info);		
					}
				}
			}

			//정리완료 등록
			for(StatisticsTbl info : infos){
				for(Statistic completeArrange : completeArranges){
					Date date  = new Date();

					SimpleDateFormat format2 = new SimpleDateFormat("yyyyMM");
					date = format2.parse(completeArrange.getRegDd());
					String dateString = format2.format(date);
					completeArrange.setRegDd(dateString);
					if(completeArrange.getCategoryId() == info.getCategoryId() && completeArrange.getRegDd().equals(info.getRegDd())){
						info.setCompleteArrange(completeArrange.getCount());
						statisticsDao.save(info);			
					}
				}
			}


			//에러등록
			for(StatisticsTbl info : infos){
				for(Statistic error : errors){
					Date date  = new Date();

					SimpleDateFormat format2 = new SimpleDateFormat("yyyyMM");
					date = format2.parse(error.getRegDd());
					String dateString = format2.format(date);
					error.setRegDd(dateString);
					if(error.getCategoryId() == info.getCategoryId() && error.getRegDd().equals(info.getRegDd())){
						info.setError(error.getCount());
						statisticsDao.save(info);			
					}
				}
			}

			//중복을 막기위해 사용된 카테고리 정보는 리스트에서 제거한다
			for(StatisticsTbl useCategory : infos){

				for(Iterator<Statistic> it = statisticInfos.iterator() ; it.hasNext(); ){
					Statistic value = it.next();

					if(value.getCategoryId() == useCategory.getCategoryId()){
						it.remove();
					}
				}

			}
		}

		for(Statistic category : statisticInfos){

			StatisticsTbl categoryInfo = new StatisticsTbl();
			categoryInfo.setCategoryId(category.getCategoryId());
			categoryInfo.setDepth(category.getDepth());
			categoryInfo.setGroupId(category.getGroupId());
			statisticsDao.save(categoryInfo);

		}
		return returnLists;

	}	

	/**
	 * 년별 통계쿼리를 일괄로 DB에 저장한다.
	 * @param StatisticsTbl
	 * @return List<CategoryTbl>
	 */

	@Modifying
	@Transactional	
	public String InsertTotalStatistc(StatisticsTbl infos) throws ServiceException{
		statisticsDao.save(infos);
		return "yes";
	}

	/**
	 * StatisticTbl의 모든 데이터를 삭제한다.
	 */
	@Modifying
	@Transactional	
	public void deleteTotalStatistcs() throws ServiceException {
		statisticsDao.deleteAll();
	}


	/**
	 * StatisticTbl의 SEQ의 값을 초기화한다.
	 */
	@Modifying
	@Transactional	
	public void initSeq() throws ServiceException {
		IdGenTbl idGendTbl = new IdGenTbl();
		idGendTbl.setEntityName("STATISTICS_SEQ");
		idGendTbl.setValue(1l);

		idGenDao.save(idGendTbl);
	}

	/**
	 * 기간별 통계쿼리를 구한다.
	 * @param categoryId
	 * @param depth
	 * @return List<Statistic>
	 */
	public List<Statistic> findStatisticsListForPeriod(Statistic statistic,Search search) throws ServiceException{

		List<Statistic> statisticInfos = findCategoryListForPeriod(statistic,search);
		List<Statistic> tempesults = new ArrayList<Statistic>();
		List<Statistic> results = new ArrayList<Statistic>();


		//등록건 조회
		search.setDataStatCd("regist");
		List<Statistic> regists = findStatisticsListForPeriodInDataStatCd(statistic,search);

		//폐기건 조회
		search.setDataStatCd("discard");
		List<Statistic> discards = findStatisticsListForPeriodInDataStatCd(statistic,search);

		//정리전건 조회
		search.setDataStatCd("beforeArrange");
		List<Statistic> beforeArranges = findStatisticsListForPeriodInDataStatCd(statistic,search);

		//정리 완료건 조회
		search.setDataStatCd("completeArrange");
		List<Statistic> completeArranges = findStatisticsListForPeriodInDataStatCd(statistic,search);

		//에러건 조회
		search.setDataStatCd("error");
		List<Statistic> errors = findStatisticsListForPeriodInDataStatCd(statistic,search);

		if(statisticInfos != null){
			for(Statistic info : statisticInfos){

				for(Statistic regist : regists){
					if(regist.getCategoryId() == info.getCategoryId()){
						info.setRegist(regist.getCount());
					}
				};

				for(Statistic discard : discards){
					if(discard.getCategoryId() == info.getCategoryId()){
						info.setDiscard(discard.getCount());
					}
				};

				for(Statistic beforeArrange :  beforeArranges){
					if(beforeArrange.getCategoryId() == info.getCategoryId()){
						info.setBeforeArrange(beforeArrange.getCount());
					}
				};

				for(Statistic completeArrange : completeArranges){
					if(completeArrange.getCategoryId() == info.getCategoryId()){
						info.setCompleteArrange(completeArrange.getCount());
					}
				};

				for(Statistic error : errors){
					if(error.getCategoryId() == info.getCategoryId()){
						info.setError(error.getCount());
					}
				};

				tempesults.add(info);
			}

			//그룹id의 명칭을 넣어준다.
			for(Statistic temp : statisticInfos){
				for(Statistic tempGroup : tempesults){
					if(temp.getCategoryId() == tempGroup.getGroupId()){
						tempGroup.setGroupNm(temp.getCategoryNm());

						results.add(tempGroup);
					}
				}
			}
		}

		return results;
	}	


	/**
	 * 기간별 통계쿼리를 구하기위해서 전체 카테고리의 정보를 nodes,그룹id,depth,orderNum으로 정렬하며 뽑아낸다.
	 * @param statistic
	 * @param search
	 * @return List<Statistic>
	 */
	public List<Statistic> findCategoryListForPeriod(Statistic statistic,Search search)throws ServiceException {
		String[] categoryFields = {"categoryId","categoryNm","depth","nodes","orderNum","groupId"};
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<CategoryTbl> from = criteriaQuery.from(CategoryTbl.class);

		Selection[] s = new Selection[categoryFields.length];

		int i=0;

		for(int j = 0; j < categoryFields.length; j++) {
			s[i] = from.get(categoryFields[j]);
			i++;
		}

		if(statistic.getCategoryId() != 0){
			criteriaQuery.where(criteriaBuilder.equal(from.get(CategoryTbl_.categoryId), statistic.getCategoryId()));
		}
		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				).orderBy(criteriaBuilder.asc(from.get(CategoryTbl_.orderNum)));


		TypedQuery<Object[]> typedQuery = em.createQuery(select);


		/**
		 * 페이징 시작. 한페이지에 13개씩
		 * 2014.06.19 페이징 임시 해제 추후 구현
		 */
		if(search.getPageNo() != null && search.getPageNo() != 0){/*

				int	startPage = (search.getPageNo()-1) * SearchControls.STATISTIC_GRAPH_COUNT;

				if(logger.isDebugEnabled()){

					logger.debug("startPage :"+startPage);

				}

				typedQuery.setMaxResults(SearchControls.STATISTIC_GRAPH_COUNT);
				typedQuery.setFirstResult(startPage);
		 */}
		List<Object[]> list2 = typedQuery.getResultList();

		List<Statistic> statistics = new ArrayList<Statistic>();

		for(Object[] list : list2) {

			Statistic info = new Statistic();

			i=0;

			for(int j = 0; j < categoryFields.length; j++) {
				ObjectUtils.setProperty(info, categoryFields[j], list[i]);
				i++;
			}
			logger.debug("info    "+info.getCategoryNm());
			statistics.add(info);
		}

		return statistics;
	}	



	/**
	 * 각카테고리별 루트 카테고리 정보를 구한다.
	 * @param statistic
	 * @param search
	 * @return List<Statistic>
	 */
	public List<Statistic> findCategoryRoots(Statistic statistic,Search search)throws ServiceException {
		String[] categoryFields = {"categoryId","categoryNm","depth","nodes","orderNum","groupId"};
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<CategoryTbl> from = criteriaQuery.from(CategoryTbl.class);


		Selection[] s = new Selection[categoryFields.length];

		int i=0;

		for(int j = 0; j < categoryFields.length; j++) {
			s[i] = from.get(categoryFields[j]);
			i++;
		}


		criteriaQuery.where(criteriaBuilder.equal(from.get(CategoryTbl_.depth), 0));

		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				).orderBy(criteriaBuilder.asc(from.get(CategoryTbl_.orderNum)));


		TypedQuery<Object[]> typedQuery = em.createQuery(select);


		/**
		 * 페이징 시작. 한페이지에 13개씩
		 * 2014.06.19 페이징 임시 해제 추후 구현
		 */
		if(search.getPageNo() != null && search.getPageNo() != 0){/*

				int	startPage = (search.getPageNo()-1) * SearchControls.STATISTIC_GRAPH_COUNT;

				if(logger.isDebugEnabled()){

					logger.debug("startPage :"+startPage);

				}

				typedQuery.setMaxResults(SearchControls.STATISTIC_GRAPH_COUNT);
				typedQuery.setFirstResult(startPage);
		 */}
		List<Object[]> list2 = typedQuery.getResultList();

		if(logger.isDebugEnabled()){
			logger.debug("list2 :"+list2.size());
		}

		List<Statistic> statistics = new ArrayList<Statistic>();

		for(Object[] list : list2) {
			Statistic info = new Statistic();
			i=0;
			for(int j = 0; j < categoryFields.length; j++) {
				ObjectUtils.setProperty(info, categoryFields[j], list[i]);
				i++;
			}
			statistics.add(info);
		}

		return statistics;
	}	


	/**
	 * 기간별 통계쿼리를 구하기위해서 contents_tbl의 data_stat_cd의 구분자 값별로 데이터를 뽑아낸다.
	 * @param statistic
	 * @param search
	 * @return List<Statistic>
	 */
	public List<Statistic> findStatisticsListForPeriodInDataStatCd(Statistic statistic,Search search) throws ServiceException{
		String[] categoryFields = {"categoryId","count"};
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
		Root<ContentsTbl> from = criteriaQuery.from(ContentsTbl.class);

		Selection[] s = new Selection[categoryFields.length];

		int i=0;

		for(int j = 0; j < categoryFields.length; j++) {

			if(categoryFields[j].equals("count")){
				s[i] = criteriaBuilder.count(from.get(ContentsTbl_.categoryId));
			}else{
				s[i] = from.get(categoryFields[j]);
			}
			i++;
		}

		if(search.getDataStatCd().equals("regist")){
			criteriaQuery.where(
					criteriaBuilder.and(
							criteriaBuilder.isNull(from.get(ContentsTbl_.delDd)),
							criteriaBuilder.notEqual(from.get(ContentsTbl_.dataStatCd), "003"),
							criteriaBuilder.greaterThanOrEqualTo(from.get(ContentsTbl_.regDt), statistic.getStartDD()),
							criteriaBuilder.lessThanOrEqualTo(from.get(ContentsTbl_.regDt), statistic.getEndDD())
							));

		}else if(search.getDataStatCd().equals("discard")){
			criteriaQuery.where(
					criteriaBuilder.and(			
							criteriaBuilder.isNotNull(from.get("delDd")),
							criteriaBuilder.greaterThanOrEqualTo(from.get(ContentsTbl_.delDd), statistic.getStartDD()),
							criteriaBuilder.lessThanOrEqualTo(from.get(ContentsTbl_.delDd), statistic.getEndDD())
							));

		}else if(search.getDataStatCd().equals("beforeArrange")){
			criteriaQuery.where(
					criteriaBuilder.and(
							criteriaBuilder.isNull(from.get(ContentsTbl_.delDd)),
							criteriaBuilder.or(criteriaBuilder.equal(from.get(ContentsTbl_.dataStatCd), "000"),criteriaBuilder.equal(from.get(ContentsTbl_.dataStatCd), "001")),
							criteriaBuilder.greaterThanOrEqualTo(from.get(ContentsTbl_.regDt), statistic.getStartDD()),
							criteriaBuilder.lessThanOrEqualTo(from.get(ContentsTbl_.regDt), statistic.getEndDD())
							));

		}else if(search.getDataStatCd().equals("completeArrange")){
			criteriaQuery.where(
					criteriaBuilder.and(			
							criteriaBuilder.isNull(from.get(ContentsTbl_.delDd)),
							criteriaBuilder.equal(from.get(ContentsTbl_.dataStatCd), "002"),
							criteriaBuilder.greaterThanOrEqualTo(from.get(ContentsTbl_.arrangeDt), statistic.getStartDD()),
							criteriaBuilder.lessThanOrEqualTo(from.get(ContentsTbl_.arrangeDt), statistic.getEndDD())
							));

		}else if(search.getDataStatCd().equals("error")){
			criteriaQuery.where(
					criteriaBuilder.and(			
							criteriaBuilder.isNull(from.get(ContentsTbl_.delDd)),
							criteriaBuilder.equal(from.get(ContentsTbl_.dataStatCd), "003"),
							criteriaBuilder.greaterThanOrEqualTo(from.get(ContentsTbl_.arrangeDt), statistic.getStartDD()),
							criteriaBuilder.lessThanOrEqualTo(from.get(ContentsTbl_.arrangeDt), statistic.getEndDD())
							));

		}

		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				).groupBy(from.get(ContentsTbl_.categoryId)).orderBy(criteriaBuilder.asc(from.get(ContentsTbl_.categoryId)));

		TypedQuery<Object[]> typedQuery = em.createQuery(select);

		List<Object[]> list2 = typedQuery.getResultList();

		List<Statistic> statistics = new ArrayList<Statistic>();

		int k=0;
		for(Object[] list : list2) {
			Statistic info = new Statistic();
			i=0;

			for(int j = 0; j < categoryFields.length; j++) {
				ObjectUtils.setProperty(info, categoryFields[j], list[i]);
				i++;
			}

			statistics.add(info);
		}

		return statistics;
	}	



	/**
	 * 각 컬럼별 상세 내역을 조회한다(등록건,정리전,정리완료,폐기건 각각에 대한 상세 정보 조회).
	 * @param statisticsTbl
	 * @param search
	 * @return List<CategoryTbl>
	 */
	public List<Statistic> findStatisticDetailInfo(Statistic statistic,Search search) throws ServiceException{
		String[] ctFields = {"regDt","ctNm","ctLeng","brdDd","vdQlty"};
		String[] EpisodeFields = {"episodeNm"};
		String[] contentsInstFields = {"vdHresol","vdVresol"};
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<ContentsTbl> from = criteriaQuery.from(ContentsTbl.class);
		Join<ContentsTbl, SegmentTbl> segment = from.join("segmentTbl", JoinType.INNER);
		Join<SegmentTbl, EpisodeTbl> episdoe = segment.join("episodeTbl", JoinType.INNER);
		Join<ContentsTbl, ContentsInstTbl> contents = from.join("contentsInst", JoinType.INNER);

		criteriaQuery.where(StatisticsSpecifications.StatisticsDetailSearch(criteriaBuilder,criteriaQuery,from,segment,contents, statistic));

		Selection[] s = new Selection[ctFields.length + EpisodeFields.length+contentsInstFields.length];

		int i=0;

		for(int j = 0; j < ctFields.length; j++) {
			s[i] = from.get(ctFields[j]);
			i++;
		}

		for(int j = 0; j < EpisodeFields.length; j++) {	
			s[i] = episdoe.get(EpisodeFields[j]);				
			i++;
		}

		for(int j = 0; j < contentsInstFields.length; j++) {	
			s[i] = contents.get(contentsInstFields[j]);				
			i++;
		}

		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				).orderBy(criteriaBuilder.desc(from.get("regDt")));


		TypedQuery<Object[]> typedQuery = em.createQuery(select);

		/**
		 * 페이징 시작. 한페이지에 25개씩
		 */
		int startPage = (statistic.getPageNum()) * SearchControls.STATISTIC_LIST_COUNT;
		int endPage = startPage+SearchControls.STATISTIC_LIST_COUNT;

		if(logger.isDebugEnabled()){
			logger.debug("startPage :"+startPage);
			logger.debug("endPage :"+endPage);
		}

		typedQuery.setFirstResult(startPage);
		typedQuery.setMaxResults(endPage);

		List<Object[]> list2 = typedQuery.getResultList();

		List<Statistic> statistics = new ArrayList<Statistic>();

		int k=0;
		for(Object[] list : list2) {

			Statistic info = new Statistic();
			i=0;

			for(int j = 0; j < ctFields.length; j++) {
				ObjectUtils.setProperty(info, ctFields[j], list[i]);
				i++;
			}

			for(int j = 0; j < EpisodeFields.length; j++) {
				ObjectUtils.setProperty(info, EpisodeFields[j], list[i]);
				i++;
			}

			for(int j = 0; j < contentsInstFields.length; j++) {
				ObjectUtils.setProperty(info, contentsInstFields[j], list[i]);
				i++;
			}
			//20131218 페이지 값 1이 넘어오면 30건 조회하는 현상발생. 원인 분석필요
			if(k < 20){
				statistics.add(info);
				k++;
			}

		}

		return statistics;
	}	


	/**
	 * 각 컬럼별 상세 내역의 총건수를 구한다(등록건,정리전,정리완료,폐기건 각각에 대한 상세 정보 조회).
	 * @param statisticsTbl
	 * @param search
	 * @return List<CategoryTbl>
	 */
	public int totalCountStatisticDetailInfo(Statistic statistic) throws ServiceException{
		String message = "";
		String[] ctFields = {"ctNm"};

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<ContentsTbl> from = criteriaQuery.from(ContentsTbl.class);
		Join<ContentsTbl, SegmentTbl> segment = from.join("segmentTbl", JoinType.INNER);
		Join<SegmentTbl, EpisodeTbl> episdoe = segment.join("episodeTbl", JoinType.INNER);
		Join<ContentsTbl, ContentsInstTbl> contents = from.join("contentsInst", JoinType.INNER);

		criteriaQuery.where(StatisticsSpecifications.StatisticsDetailSearch(criteriaBuilder,criteriaQuery,from,segment,contents, statistic));

		Selection[] s = new Selection[ctFields.length];

		int i=0;
		for(int j = 0; j < ctFields.length; j++) {
			s[i] = from.get(ctFields[j]);
			i++;
		}

		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				);

		TypedQuery<Object[]> typedQuery = em.createQuery(select);

		List<Object[]> list2 = typedQuery.getResultList();

		int count = list2.size();

		return count;

	}	

	/**
	 *  통계테이블에 존재하는 연도리스트를 조회한다
	 *
	 * @return List<Statistic>
	 */
	public List<Statistic> findYearList() throws ServiceException{
		String[] statisticFields = {"regDd"};

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<StatisticsTbl> from = criteriaQuery.from(StatisticsTbl.class);

		criteriaQuery.where(criteriaBuilder.isNotNull(from.get(StatisticsTbl_.regDd)));
		criteriaQuery.groupBy(criteriaBuilder.substring(from.get(StatisticsTbl_.regDd), 1,4));

		Selection[] s = new Selection[statisticFields.length ];

		int i=0;
		for(int j = 0; j < statisticFields.length; j++) {
			s[i] = criteriaBuilder.substring(from.<String>get(statisticFields[j]), 1,4);
			i++;
		}

		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				);

		TypedQuery<Object[]> typedQuery = em.createQuery(select);
		List<Object[]> list2 = typedQuery.getResultList();
		
		int count = list2.size();

		List<Statistic> statistics = new ArrayList<Statistic>();

		//통계테이블에 등록된 등록일 값을 연도벌로  group by하여 연도리스트를 반환한다
		for(int k = 0; k < count; k++){
			Statistic info = new Statistic();
			info.setYearList(list2.get(k)+"");

			statistics.add(info);
		}

		return statistics;
	}	

	/**
	 * 연별 통계쿼리를 구한다.
	 * @param categoryId
	 * @param depth
	 * @return List<Statistic>
	 */

	public List<Statistic> findStatisticsListForYear(Statistic statistic) throws ServiceException{
		Search search = new Search();
		statistic.setCategoryId(0);

		List<Statistic>  categorys = findCategoryListForPeriod(statistic,search);
		List<Statistic>  rootCategorys = findCategoryRoots(statistic,search);
		List<Statistic> statisticInfos = findStatisticsListForYearInStatisticTbl(statistic);
		List<Statistic> tempesults = new ArrayList<Statistic>();
		List<Statistic> result = new ArrayList<Statistic>();

		for(Statistic info : categorys){

			for(Statistic statis : statisticInfos){
				int categoryTbl = info.getCategoryId();
				int statisticsTbl =statis.getCategoryId();
				if(categoryTbl == statisticsTbl){
					info.setRegist(statis.getRegist());
					info.setBeforeArrange(statis.getBeforeArrange());
					info.setCompleteArrange(statis.getCompleteArrange());
					info.setDiscard(statis.getDiscard());
					info.setError(statis.getError());
				}
			}

			tempesults.add(info);
		}

		//그룹id의 명칭을 넣어준다.
		for(Statistic temp : rootCategorys){

			for(Statistic tempGroup : tempesults){
				if(temp.getCategoryId() == tempGroup.getGroupId()){
					tempGroup.setGroupNm(temp.getCategoryNm());

					result.add(tempGroup);
				}

			}
		}
		return result;	
	}	


	/**
	 * 연별 통계쿼리를 구하기 해서 statisticTbl에 존재하는 통계값을 조회한다
	 * @param statistic
	 * @return List<Statistic>
	 */

	public List<Statistic> findStatisticsListForYearInStatisticTbl(Statistic statistic) throws ServiceException{
		String[] statisticFields = {"regist","beforeArrange","completeArrange","discard","error","depth","groupId","categoryId"};
		String[] categoryFields = {"categoryNm"};

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<StatisticsTbl> from = criteriaQuery.from(StatisticsTbl.class);
		Root<CategoryTbl> join = criteriaQuery.from(CategoryTbl.class);

		criteriaQuery.where(StatisticsSpecifications.YearEqual(criteriaBuilder,criteriaQuery,from,statistic),criteriaBuilder.and(criteriaBuilder.equal(from.get(StatisticsTbl_.categoryId), join.get(CategoryTbl_.categoryId))));
		criteriaQuery.groupBy(from.get(StatisticsTbl_.categoryId),join.get(CategoryTbl_.categoryNm),from.get(StatisticsTbl_.depth),from.get(StatisticsTbl_.groupId));
		criteriaQuery.orderBy(criteriaBuilder.asc(from.get(StatisticsTbl_.groupId)));

		Selection[] s = new Selection[statisticFields.length + categoryFields.length];

		int i=0;
		
		//컬럼명이 depth, groupid, categoryid가 아니면 sum을 통해 그 수를 합한다.
		for(int j = 0; j < statisticFields.length; j++) {
			if(statisticFields[j] == "depth" || statisticFields[j] == "groupId" || statisticFields[j] == "categoryId"){
				s[i] = from.get(statisticFields[j]);
			}else{
				s[i] = criteriaBuilder.sum(from.<Long>get(statisticFields[j]));
			}
			i++;
		}

		for(int q = 0; q < categoryFields.length; q++) {
			s[i] = join.get(categoryFields[q]);
			i++;
		}

		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				);


		TypedQuery<Object[]> typedQuery = em.createQuery(select);
		List<Object[]> list2 = typedQuery.getResultList();
		int count = list2.size();
		List<Statistic> statistics = new ArrayList<Statistic>();

		int k=0;
		for(Object[] list : list2) {
			Statistic info = new Statistic();
			i=0;
			for(int j = 0; j < statisticFields.length; j++) {
				ObjectUtils.setProperty(info, statisticFields[j], list[i]);
				i++;
			}

			//20131218 페이지 값 1이 넘어오면 30건 조회하는 현상발생. 원인 분석필요
			if(k < 13){
				statistics.add(info);
				k++;
			}
		}
		
		return statistics;
	}	
	
	/**
	 * 연간 통계용 그래프 데이터를 조회한다
	 * @param Statistic

	 * @return List<Statistic>
	 */


	public List<Statistic> findStatisticsListForYearForGraph(Statistic statistic) throws ServiceException{
		String[] statisticFields = {"regist","beforeArrange","completeArrange","discard","error","regDd"};


		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<StatisticsTbl> from = criteriaQuery.from(StatisticsTbl.class);

		criteriaQuery.where(StatisticsSpecifications.YearEqual(criteriaBuilder,criteriaQuery,from,statistic));
		criteriaQuery.groupBy(from.get(StatisticsTbl_.regDd));
		criteriaQuery.orderBy(criteriaBuilder.asc(from.get(StatisticsTbl_.regDd)));

		Selection[] s = new Selection[statisticFields.length];

		int i=0;
		for(int j = 0; j < statisticFields.length; j++) {
			if(statisticFields[j] != "regDd"){
				s[i] = criteriaBuilder.sum(from.<Long>get(statisticFields[j]));
			}else{
				s[i] = from.get(statisticFields[j]);
			}
			i++;
		}

		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				);


		TypedQuery<Object[]> typedQuery = em.createQuery(select);
		List<Object[]> list2 = typedQuery.getResultList();
		List<Statistic> statistics = new ArrayList<Statistic>();
		
		int k=0;

		//통계테이터가 없는 달을 표현하기위해서 달을 배열로 만들고 값이 없는 달에는 공백값을 강제로 넣어준다.
		String[] yearMonth={"01","02","03","04","05","06","07","08","09","10","11","12"};			 

		for(int m =0;m<yearMonth.length;m++){
			Statistic empty = new Statistic(); 
			empty.setBeforeArrange(0l);
			empty.setCompleteArrange(0l);
			empty.setRegist(0l);
			empty.setError(0l);
			empty.setDiscard(0l);
			empty.setRegDd(yearMonth[m]);
			statistics.add(empty);
		}
		
		for(Object[] list : list2) {
			Statistic info = new Statistic();
			i=0;

			for(int j = 0; j < statisticFields.length; j++) {
				ObjectUtils.setProperty(info, statisticFields[j], list[i]);
				i++;
			}

			//년도를 빼고 월만 집어넣음
			info.setRegDd(info.getRegDd().substring(4));
			//이미 들어가있는 달의 정보를 삭제하고 값이 있는 beans로 치환한다.
			statistics.remove(Integer.parseInt(info.getRegDd())-1);
			statistics.add(Integer.parseInt(info.getRegDd())-1,info);

		}

		return statistics;
	}	



	/**
	 * 전체 통계쿼리를 구하기위해서 contents_tbl의 data_stat_cd의 구분자 값별로 데이터를 뽑아낸다.
	 * @param statistic
	 * @param search
	 * @return List<Statistic>
	 */
	public List<Statistic> findStatisticsListForSchedulerInDataStatCd(Search search) throws ServiceException {
		String[] contentsFields = {"categoryId","count","regDd"};
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
		Root<ContentsTbl> from = criteriaQuery.from(ContentsTbl.class);


		Selection[] s = new Selection[contentsFields.length];

		int i=0;

		for(int j = 0; j < contentsFields.length; j++) {

			if(contentsFields[j].equals("count")){
				s[i] = criteriaBuilder.count(from.get(ContentsTbl_.categoryId));
			}else if(contentsFields[j].equals("regDd")){
				if(search.getDataStatCd().equals("regist")||search.getDataStatCd().equals("beforeArrange")){					
					s[i] = criteriaBuilder.concat(criteriaBuilder.function("year", String.class, from.get(ContentsTbl_.regDt)), criteriaBuilder.function("month", String.class, from.get(ContentsTbl_.regDt)));
				}else if(search.getDataStatCd().equals("completeArrange")||search.getDataStatCd().equals("error")){
					s[i] = criteriaBuilder.concat(criteriaBuilder.function("year", String.class, from.get(ContentsTbl_.arrangeDt)), criteriaBuilder.function("month", String.class, from.get(ContentsTbl_.arrangeDt)));
				}else{
					s[i] = criteriaBuilder.concat(criteriaBuilder.function("year", String.class, from.get(ContentsTbl_.delDd)), criteriaBuilder.function("month", String.class, from.get(ContentsTbl_.delDd)));
				}

			}else{
				s[i] = from.get(contentsFields[j]);
			}
			i++;
		}

		if(search.getDataStatCd().equals("regist")){
			criteriaQuery.where(
					criteriaBuilder.and(
							criteriaBuilder.isNull(from.get(ContentsTbl_.delDd)),
							criteriaBuilder.notEqual(from.get(ContentsTbl_.dataStatCd), "003")
							));

		}else if(search.getDataStatCd().equals("discard")){
			criteriaQuery.where(
					criteriaBuilder.and(			
							criteriaBuilder.isNotNull(from.get(ContentsTbl_.delDd))
							));

		}else if(search.getDataStatCd().equals("beforeArrange")){
			criteriaQuery.where(
					criteriaBuilder.and(
							criteriaBuilder.isNull(from.get(ContentsTbl_.delDd)),
							criteriaBuilder.or(criteriaBuilder.equal(from.get(ContentsTbl_.dataStatCd), "000"),criteriaBuilder.equal(from.get(ContentsTbl_.dataStatCd), "001"))
							));

		}else if(search.getDataStatCd().equals("completeArrange")){
			criteriaQuery.where(
					criteriaBuilder.and(			
							criteriaBuilder.isNull(from.get(ContentsTbl_.delDd)),
							criteriaBuilder.equal(from.get(ContentsTbl_.dataStatCd), "002")
							));

		}else if(search.getDataStatCd().equals("error")){
			criteriaQuery.where(
					criteriaBuilder.and(			
							criteriaBuilder.isNull(from.get(ContentsTbl_.delDd)),
							criteriaBuilder.equal(from.get(ContentsTbl_.dataStatCd), "003")
							));

		}
		
		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				);

		if(search.getDataStatCd().equals("regist")||search.getDataStatCd().equals("beforeArrange")){					
			select.groupBy(from.get(ContentsTbl_.categoryId),criteriaBuilder.concat(criteriaBuilder.function("year", String.class, from.get(ContentsTbl_.regDt)), criteriaBuilder.function("month", String.class, from.get(ContentsTbl_.regDt))));
		}else if(search.getDataStatCd().equals("discard")){
			select.groupBy(from.get(ContentsTbl_.categoryId),criteriaBuilder.concat(criteriaBuilder.function("year", String.class, from.get(ContentsTbl_.delDd)), criteriaBuilder.function("month", String.class, from.get(ContentsTbl_.delDd))));

		}else if(search.getDataStatCd().equals("completeArrange")||search.getDataStatCd().equals("error")){
			select.groupBy(from.get(ContentsTbl_.categoryId),criteriaBuilder.concat(criteriaBuilder.function("year", String.class, from.get(ContentsTbl_.arrangeDt)), criteriaBuilder.function("month", String.class, from.get(ContentsTbl_.arrangeDt))));

		}

		TypedQuery<Object[]> typedQuery = em.createQuery(select);

		List<Object[]> list2 = typedQuery.getResultList();

		List<Statistic> statistics = new ArrayList<Statistic>();

		int k=0;
		for(Object[] list : list2) {
			Statistic info = new Statistic();
			i=0;

			for(int j = 0; j < contentsFields.length; j++) {
				ObjectUtils.setProperty(info, contentsFields[j], list[i]);
				i++;
			}

			statistics.add(info);

		}

		return statistics;
	}	

}
