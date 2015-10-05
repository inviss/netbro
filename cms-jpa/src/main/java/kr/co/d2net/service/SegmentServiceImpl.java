package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.List;

import kr.co.d2net.dao.SegmentDao;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.dto.vo.Segment;
import kr.co.d2net.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("segmentService")
@Transactional(readOnly=true)
public class SegmentServiceImpl implements SegmentService {
	@Autowired
	SegmentDao segmentDao;
	
	@Override
	public void insertSegment(SegmentTbl segmentTbl) throws ServiceException {
		segmentDao.save(segmentTbl);
	}

	@Override
	public List<Segment> findSegmentList(Search search) throws ServiceException {
		List<SegmentTbl>segmentTbls = segmentDao.findSegmentList(search);
		List<Segment> segments = new ArrayList<Segment>();
		
		for(SegmentTbl segmentTbl : segmentTbls){
			Segment segment = new Segment();
			
			segment.setCategoryId(segmentTbl.getCategoryId());
			segment.setEpisodeId(segmentTbl.getEpisodeId());
			segment.setSegmentId(segmentTbl.getSegmentId());
			segment.setSegmentNm(segmentTbl.getSegmentNm());
			segment.setModDt(segmentTbl.getModDt());
			segment.setModrId(segmentTbl.getModrId());
			segment.setUseYn(segmentTbl.getUseYn().toString());
			segment.setRegDt(segmentTbl.getRegDt());
			segment.setRegrId(segmentTbl.getRegrId());
			
			segments.add(segment);
		}
		return segments;
	}}
