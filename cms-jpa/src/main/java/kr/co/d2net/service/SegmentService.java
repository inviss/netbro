package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dto.ContentsModTbl;
import kr.co.d2net.dto.NoticeTbl;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.vo.Content;
import kr.co.d2net.dto.vo.Disuse;
import kr.co.d2net.dto.vo.Notice;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.dto.vo.Segment;
import kr.co.d2net.exception.ServiceException;

public interface SegmentService {
 
	public void insertSegment(SegmentTbl segmentTbl) throws ServiceException;
	 
	public List<Segment> findSegmentList(Search search) throws ServiceException;
	 
	
}
