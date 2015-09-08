package kr.co.d2net.utils;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import kr.co.d2net.dto.search.DStatus;

import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.multipart.MultipartFile;

public class Utility {

	final static Logger logger = LoggerFactory.getLogger(Utility.class);

	/**
	 * 값 채우기
	 * 
	 * @param strTarget
	 *            대상 문자열
	 * @param strDest
	 *            채워질 문자열
	 * @param nSize
	 *            총 문자열 길이
	 * @param bLeft
	 *            채워질 문자의 방향이 좌측인지 여부
	 * @return 지정한 길이만큼 채워진 문자열
	 */
	public static String padValue(final String strTarget, final String strDest, final int nSize, final boolean bLeft) {
		if (strTarget == null) {
			return strTarget;
		}

		String retValue = null;
		StringBuffer objSB = new StringBuffer();
		int nLen = strTarget.length();
		int nDiffLen = nSize - nLen;

		for (int i = 0; i < nDiffLen; i++) {
			objSB.append(strDest);
		}

		if (bLeft) { // 채워질 문자열의 방향이 좌측일 경우
			retValue = objSB.toString() + strTarget;
		}
		else { // 채워질 문자열의 방향이 우측일 경우
			retValue = strTarget + objSB.toString();
		}

		return retValue;
	}

	/**
	 * 좌측으로 값 채우기
	 * 
	 * @param strTarget
	 *            대상 문자열
	 * @param strDest
	 *            채워질 문자열
	 * @param nSize
	 *            총 문자열 길이
	 * @return 채워진 문자열
	 */
	public static String padLeft(final String strTarget, final String strDest, final int nSize) {
		return padValue(strTarget, strDest, nSize, true);
	}

	/**
	 * 타임코드 = duration로
	 * @param param frame
	 * @return  String
	 * @throws DataAccessException
	 */
	public static long changeTimeCode(String timecode) {

		int hh, mm, m10, m1, ss, ff;
		long frame;

		hh = Integer.parseInt(timecode.substring(0, 2));     	// 타임코드의 처음 2자리(시간)를 숫자로 변환
		mm = Integer.parseInt(timecode.substring(3, 5));   		// 타임코드의 : 다음 2자리(분)를 숫자로 변환
		ss = Integer.parseInt(timecode.substring(6, 8));      	// 타임코드의 : 다음 2자리(초)를 숫자로 변환
		ff = Integer.parseInt(timecode.substring(9)); ;     	// 타임코드의 : 다음 2자리(프레임)를 숫자로 변환

		m10 = mm / 10; // mm div 10;
		m1 = mm % 10;
		frame = hh * 107892 + m10 * 17982;

		if (0 == m1)
			frame = frame + ss * 30 + ff;
		else {
			frame = frame + (m1 - 1) * 1798 + 1800;

			if ( 0 == ss)
				frame = frame + ff - 2;
			else
				frame = frame + (ss - 1) * 30 + 28 + ff;
		}

		return frame;
	}

	/**
	 * duration = 타임코드로
	 * @param param frame
	 * @return  String
	 * @throws DataAccessException
	 */
	public static String changeDuration(long frame) {

		long hh, mm, m10, m1, ss, ff;  // 시, 분, 중간값, 중간값, 중간값, 중간값

		hh = frame / 107892; // (30000/1001)*3600
		frame = frame % 107892;
		m10 = frame / 17982; // (30000/1001)*600
		frame = frame % 17982;

		if ( frame < 1800 ) {
			m1 = 0;
			ss = frame / 30;
			ff = frame % 30;
		} else {      // except for each m10, plus 2 frame per m1

			frame = frame - 1800;  // in case of m10
			m1 = frame / 1798 + 1;
			frame = frame % 1798;

			if ( frame < 28 ) { // in case of m1
				ss = 0;
				ff = frame + 2;
			} else {    // in case of none of m10 and m1
				frame = frame - 28;
				ss = frame / 30 + 1;
				ff = frame % 30;
			}
		}
		mm = m10 * 10 + m1;

		return padLeft(String.valueOf(hh), "0", 2)+":"+padLeft(String.valueOf(mm), "0", 2)+":"+padLeft(String.valueOf(ss), "0", 2)+":"+padLeft(String.valueOf(ff), "0", 2);
	}

	public static String[] uploadFile(MultipartFile sourcefile, String path) {
		try {
			if ((sourcefile==null)||(sourcefile.isEmpty())) return null;

			String[] tmp = new String[3];

			SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");

			String date = sf.format(new Date());
			File f = new File(path+File.separator+date);

			if(!f.exists()) {
				f.mkdirs();
				if(!SystemUtils.IS_OS_WINDOWS){
					Runtime.getRuntime().exec("chown -R joytouch.joytouch "+f.getAbsolutePath());
				}
			}



			String orgNm = sourcefile.getOriginalFilename();
			String ext = orgNm.substring(orgNm.lastIndexOf("."));
			String uuid = UUID.randomUUID().toString();
			String targetFilePath = path+File.separator+date+File.separator+uuid+ext;

			sourcefile.transferTo(new File(targetFilePath));

			if(!SystemUtils.IS_OS_WINDOWS){
				Runtime.getRuntime().exec("chown -R joytouch.joytouch "+targetFilePath);
			}

			tmp[0] = date;
			tmp[1] = uuid+ext;
			tmp[2] = orgNm;

			return tmp;
		} catch (Exception e) {
			logger.error("uploadFile error", e);
		}

		return null;
	}




	public static String encodeMD5(String msg) {
		String tmp = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(msg.getBytes());

			byte byteData[] = md.digest();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			tmp = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			logger.error("encodeMD5",e);
		}
		return tmp;
	}



	/**
	 * duration = 초단위 시간으로 계상
	 * @param param frame
	 * @return  String
	 * @throws DataAccessException
	 */
	public static long changeDurationToSecond(long frame) {

		long hh, mm, m10, m1, ss, ff;  // 시, 분, 중간값, 중간값, 중간값, 중간값

		hh = frame / 107892; // (30000/1001)*3600
		frame = frame % 107892;
		m10 = frame / 17982; // (30000/1001)*600
		frame = frame % 17982;

		if ( frame < 1800 ) {
			m1 = 0;
			ss = frame / 30;
			ff = frame % 30;
		} else {      // except for each m10, plus 2 frame per m1

			frame = frame - 1800;  // in case of m10
			m1 = frame / 1798 + 1;
			frame = frame % 1798;

			if ( frame < 28 ) { // in case of m1
				ss = 0;
				ff = frame + 2;
			} else {    // in case of none of m10 and m1
				frame = frame - 28;
				ss = frame / 30 + 1;
				ff = frame % 30;
			}
		}
		mm = m10 * 10 + m1;
		long hourSecond = hh*60*60;
		long minuteSecond = mm*60;
		long second = ss;
		long frameSecond = 0L;

		long sumSecond = hourSecond + minuteSecond + second + frameSecond;
		return sumSecond;
	}

	/*
	 * 현재일을 Timestamp값으로 구한다.
	 * 
	 * @param format
	 * 
	 * @return
	 */
	public static java.sql.Timestamp getTimestamp() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.KOREA);
		Calendar cal = Calendar.getInstance();

		return Timestamp.valueOf(sdf.format(cal.getTime()));
	}

	public static java.sql.Timestamp getTimestamp(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.KOREA);
		return Timestamp.valueOf(sdf.format(timestamp));
	}
	
	// 현재일을 기준으로 파라미터 일수를 더한다.
		public static Date getWantDay(int _day, DStatus status) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(java.util.Calendar.DAY_OF_MONTH, _day);
			
			switch(status) {
			case FROM:
				calendar.set(Calendar.HOUR_OF_DAY, 00);
				calendar.set(Calendar.MINUTE, 00);
				calendar.set(Calendar.SECOND, 01);
				break;
			case TO:
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				break;
			default :
				break;
			}
			
			return calendar.getTime();
		}
		
		public static Date getWantMonth(int _month, DStatus status) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(java.util.Calendar.MONTH, _month);
			
			switch(status) {
			case FROM:
				calendar.set(Calendar.HOUR_OF_DAY, 00);
				calendar.set(Calendar.MINUTE, 00);
				calendar.set(Calendar.SECOND, 01);
				break;
			case TO:
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				break;
			default :
				break;
			}
			
			return calendar.getTime();
		}
		
		// 현재일을 기준으로 파라미터 일수를 더한다.
		public static String getWantDay(int _day, String _format, DStatus status) {
			SimpleDateFormat sdate = new SimpleDateFormat(_format);
			return sdate.format(getWantDay(_day, status));
		}

		
		public static String getOs() {
			return SystemUtils.IS_OS_LINUX ? "linux" : SystemUtils.IS_OS_MAC ? "mac" : "win";
		}

}
