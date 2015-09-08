package kr.co.d2net.utils;

import org.apache.commons.lang.StringUtils;

public class StringUtil {
	/**
	 * strTarget이 null 이거나 화이트스페이스 일 경우 strDest을 반환한다.
	 * 
	 * @param strTarget
	 *            대상 문자열
	 * @param strDest
	 *            대체 문자열
	 * @return strTarget이 null 이거나 화이트스페이스 일 경우 strDest 문자열로 반환
	 */
	public static String nvl(final String strTarget, final String strDest) {
		if (StringUtils.isBlank(strTarget)) {
			return strDest;
		}
		return strTarget;
	}
	
	public static String nvl(final Object strTarget, final String strDest) {
		if (strTarget == null || StringUtils.isBlank((String)strTarget)) {
			return strDest;
		}
		return (String)strTarget;
	}

	/**
	 * strTarget이 null 이거나 화이트스페이스 일 경우 화이트스페이스로 반환한다.
	 * 
	 * @param strTarget
	 *            대상문자열
	 * @return strTarget이 null 이거나 화이트스페이스 일 경우 화이트스페이스로 반환
	 */
	public static String nvl(final String strTarget) {
		return nvl(strTarget, "");
	}

	/**
	 * 대상문자열이 null 인지 여부 확인하기
	 * 
	 * @paramstrTarget 대상 문자열
	 * @return null 여부
	 */
	public static boolean isNull(final String strTarget) {
		if (strTarget == null) {
			return true;
		}
		return false;
	}

	/**
	 * 대상 문자열이 지정한 길이보다 길 경우 지정한 길이만큼 잘라낸 문자열 반환하기
	 * 
	 * @param strTarget
	 *            대상 문자열
	 * @param nLimit
	 *            길이
	 * @param bDot
	 *            잘린 문자열이 존재할 경우 ... 표시 여부
	 * @return 대상 문자열이 지정한 길이보다 길 경우 지정한 길이만큼 잘라낸 문자열
	 */
	public static String cutText(final String strTarget, final int nLimit, final boolean bDot) {
		if (strTarget == null || "".equals(strTarget)) {
			return strTarget;
		}

		String retValue = null;
		int nLen = strTarget.length();
		int nTotal = 0;
		int nHex = 0;
		String strDot = "";

		if (bDot) {
			strDot = "...";
		}

		for (int i = 0; i < nLen; i++) {
			nHex = (int) strTarget.charAt(i);
			nTotal += Integer.toHexString(nHex).length() / 2;

			if (nTotal > nLimit) {
				retValue = strTarget.substring(0, i) + strDot;
				break;
			}
			else if (nTotal == nLimit) {
				if (i == (nLen - 1)) {
					retValue = strTarget.substring(0, i - 1) + strDot;
					break;
				}

				retValue = strTarget.substring(0, i + 1) + strDot;
				break;
			}
			else {
				retValue = strTarget;
			}
		}

		return retValue;
	}

	/**
	 * 대상문자열에 지정한 문자가 위치한 위치 값을 반환하기(대소문자 무시)
	 * 
	 * @param strTarget
	 *            대상 문자열
	 * @param strDest
	 *            찾고자 하는 문자열
	 * @param nPos
	 *            시작 위치
	 * @return 대상문자열에 지정한 문자가 위치한 위치 값을 반환
	 */
	public static int indexOfIgnore(final String strTarget, final String strDest, final int nPos) {
		if (strTarget == null || "".equals(strTarget)) {
			return -1;
		}
		return strTarget.toLowerCase().indexOf(strDest.toLowerCase(), nPos);
	}

	/**
	 * 대상문자열에 지정한 문자가 위치한 위치 값을 반환하기(대소문자 무시)
	 * 
	 * @param strTarget
	 *            대상 문자열
	 * @param strDest
	 *            찾고자 하는 문자열
	 * @return 대상문자열에 지정한 문자가 위치한 위치 값을 반환
	 */
	public static int indexOfIgnore(final String strTarget, final String strDest) {
		return indexOfIgnore(strTarget, strDest, 0);
	}

	/**
	 * 대상 문자열 치환하기
	 * 
	 * @param strTarget
	 *            대상문자열
	 * @param strOld
	 *            찾고자 하는 문자열
	 * @param strNew
	 *            치환할 문자열
	 * @param bIgnoreCase
	 *            대소문자 구분 여부
	 * @param bOnlyFirst
	 *            한 번만 치환할지 여부
	 * @return 치환한 문자열
	 */
	public static String replace(final String strTarget, final String strOld, final String strNew,
			final boolean bIgnoreCase, final boolean bOnlyFirst) {
		if (strTarget == null || "".equals(strTarget)) {
			return strTarget;
		}

		StringBuffer objDest = new StringBuffer("");
		int nLen = strOld.length();
		int strTargetLen = strTarget.length();
		int nPos = 0;
		int nPosOld = 0;

		if (bIgnoreCase) { // 대소문자 구분하지 않을 경우
			while ((nPos = indexOfIgnore(strTarget, strOld, nPosOld)) >= 0) {
				objDest.append(strTarget.substring(nPosOld, nPos));
				objDest.append(strNew);
				nPosOld = nPos + nLen;

				if (bOnlyFirst) { // 한번만 치환할시
					break;
				}
			}
		}
		else { // 대소문자 구분하는 경우
			while ((nPos = strTarget.indexOf(strOld, nPosOld)) >= 0) {
				objDest.append(strTarget.substring(nPosOld, nPos));
				objDest.append(strNew);
				nPosOld = nPos + nLen;

				if (bOnlyFirst) {
					break;
				}
			}
		}

		if (nPosOld < strTargetLen) {
			objDest.append(strTarget.substring(nPosOld, strTargetLen));
		}

		return objDest.toString();
	}

	/**
	 * 대상 문자열 치환하기
	 * 
	 * @param strTarget
	 *            대상문자열
	 * @param strOld
	 *            찾고자 하는 문자열
	 * @param strNew
	 *            치환할 문자열
	 * @return 치환된 문자열
	 */
	public static String replaceAll(final String strTarget, final String strOld, final String strNew) {
		return replace(strTarget, strOld, strNew, false, false);
	}

	/**
	 * 각종 구분자 제거하기
	 * 
	 * @param String
	 *            strTarget 대상문자열
	 * @return String 구분자가 제거된 문자열
	 */
	public static String removeFormat(final String strTarget) {
		if (strTarget == null || "".equals(strTarget)) {
			return strTarget;
		}
		return strTarget.replaceAll("[$|^|*|+|?|/|:|\\-|,|.|\\s]", "");
	}

	/**
	 * 콤마 제거하기
	 * 
	 * @param String
	 *            strTarget 대상 문자열
	 * @return String 콤마가 제거된 문자열
	 */
	public static String removeComma(final String strTarget) {
		if (strTarget == null || "".equals(strTarget)) {
			return strTarget;
		}
		return strTarget.replaceAll("[,|\\s]", "");
	}

	/**
	 * HTML 태그 제거하기
	 * 
	 * @param strTarget
	 *            대상문자열
	 * @return 태그가 제거된 문자열
	 */
	public static String removeHTML(final String strTarget) {
		if (strTarget == null || "".equals(strTarget)) {
			return strTarget;
		}
		return strTarget.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
	}

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
	 * 좌측에 공백 채우기
	 * 
	 * @param strTarget
	 *            대상 문자열
	 * @param nSize
	 *            총 문자열 길이
	 * @return 채워진 문자열 길이
	 */
	public static String padLeft(final String strTarget, final int nSize) {
		return padValue(strTarget, " ", nSize, true);
	}

	/**
	 * 우측으로 값 채우기
	 * 
	 * @param strTarget
	 *            대상문자열
	 * @param strDest
	 *            채워질 문자열
	 * @param nSize
	 *            총 문자열 길이
	 * @return 채워진 문자열 길이
	 */
	public static String padRight(final String strTarget, final String strDest, final int nSize) {
		return padValue(strTarget, strDest, nSize, false);
	}

	/**
	 * 우측으로 공백 채우기
	 * 
	 * @param strTarget
	 *            대상문자열
	 * @param nSize
	 *            총 문자열 길이
	 * @return 채워진 문자열
	 */
	public static String padRight(final String strTarget, final int nSize) {
		return padValue(strTarget, " ", nSize, false);
	}

	/**
	 * HTML을 캐리지 리턴 값으로 변환하기
	 * 
	 * @param strTarget
	 *            대상 문자열
	 * @return HTML을 캐리지 리턴값으로 반환한 문자열
	 */
	public static String encodingHTML(final String strTarget) {
		String strResult = strTarget;

		if (strResult == null || "".equals(strResult)) {
			return strResult;
		}

		strResult = strResult.replaceAll("<br>", "\r\n");
		strResult = strResult.replaceAll("<q>", "'");
		strResult = strResult.replaceAll("&quot;", "\"");
		strResult = strResult.replaceAll("<BR>", "\r\n");
		strResult = strResult.replaceAll("<Q>", "'");
		strResult = strResult.replaceAll("&QUOT;", "\"");

		return strResult;
	}

	/**
	 * 캐리지리턴값을 HTML 태그로 변환하기
	 * 
	 * @param strTarget
	 *            대상 문자열
	 * @return 캐리지 리턴값을 HTML 태그로 변환한 문자열
	 */
	public static String decodingHTML(final String strTarget) {
		String strResult = strTarget;

		if (strResult == null || "".equals(strResult)) {
			return strResult;
		}

		strResult = strResult.replaceAll("\r\n", "<br>");
		strResult = strResult.replaceAll("'", "<q>");
		strResult = strResult.replaceAll("\"", "&quot;");
		strResult = strResult.replaceAll("\r\n", "<BR>");
		strResult = strResult.replaceAll("'", "<Q>");
		strResult = strResult.replaceAll("\"", "&QUOT;");

		return strResult;
	}

	/**
	 * 대상 문자열을 금액형 문자열로 변환하기
	 * 
	 * @param strTarget
	 *            대상 문자열
	 * @return 금액형 문자열
	 */
	public static String formatMoney(final String strTarget) {
		String strResult = strTarget;

		if (strResult == null || "".equals(strResult.trim())) {
			return "0";
		}

		strResult = removeComma(strResult);
		String strSign = strResult.substring(0, 1);

		if ("+".equals(strSign) || "-".equals(strSign)) { // 부호가 존재할 경우
			strSign = strResult.substring(0, 1);
			strResult = strResult.substring(1);
		}
		else {
			strSign = "";
		}

		String strDot = "";

		if (strResult.indexOf(".") != -1) { // 소숫점이 존재할 경우
			int nPosDot = strResult.indexOf(".");
			strDot = strResult.substring(nPosDot, strResult.length());
			strResult = strResult.substring(0, nPosDot);
		}

		StringBuffer objSB = new StringBuffer(strResult);
		int nLen = strResult.length();

		for (int i = nLen; 0 < i; i -= 3) // Comma 단위
		{
			objSB.insert(i, ",");
		}

		return strSign + objSB.substring(0, objSB.length() - 1) + strDot;
	}

	/**
	 * 대상문자열의 소숫점 설정하기
	 * 
	 * @param strTarget
	 *            대상문자열
	 * @param nDotSize
	 *            소숫점 길이
	 * @return
	 */
	public static String round(final String strTarget, final int nDotSize) {
		String strResult = strTarget;

		if (strResult == null || "".equals(strResult.trim())) {
			return strResult;
		}

		String strDot = null;
		int nPosDot = strResult.indexOf(".");

		if (nPosDot == -1) { // 소숫점이 존재하지 않을 경우
			strDot = (nDotSize == 0) ? padValue("", "0", nDotSize, false) : "." + padValue("", "0", nDotSize, false);
		}
		else { // 소숫점이 존재할 경우
			String strDotValue = strResult.substring(nPosDot + 1); // 소숫점 이하 값
			strResult = strResult.substring(0, nPosDot); // 정수 값

			if (strDotValue.length() >= nDotSize) { // 실제 소숫점 길이가 지정한 길이보다 크다면
				// 지정한 소숫점 길이 만큼 잘라내기
				strDot = "." + strDotValue.substring(0, nDotSize);
			}
			else { // 실제 소숫점길이가 지정한 길이보다 작다면 지정한 길이만큼 채우기
				strDot = "." + padValue(strDotValue, "0", nDotSize, false);
			}
		}

		return strResult + strDot;
	}

	/**
	 * 대상 문자열을 날짜 포멧형 문자열로 변환하기
	 * 
	 * @param strTarget
	 *            대상 문자열
	 * @return 날짜 포멧 문자열로 변환하기
	 */
	public static String formatDate(String strTarget) {
		String strValue = removeFormat(strTarget);

		if (strValue.length() != 8) {
			return strTarget;
		}

		StringBuffer objSB = new StringBuffer(strValue);
		objSB.insert(4, "-");
		objSB.insert(7, "-");

		return objSB.toString();
	}

	/**
	 * 대상 문자열을 주민등록번호 포멧 문자열로 변환하기
	 * 
	 * @param strTarget
	 *            대상 문자열
	 * @return 주민등록번호 포멧 문자열
	 */
	public static String formatJuminID(String strTarget) {
		String strValue = removeFormat(strTarget);

		if (strValue.length() != 8) {
			return strTarget;
		}

		StringBuffer objSB = new StringBuffer(strValue);
		objSB.insert(4, "-");
		objSB.insert(7, "-");

		return objSB.toString();
	}

	/**
	 * 대상 문자열을 전화번호 포멧 문자열로 변환하기
	 * 
	 * @param strTarget
	 *            대상문자열
	 * @return 전화번호 포멧 문자열
	 */
	public static String formatPhone(String strTarget) {
		String strValue = removeFormat(strTarget);
		int nLength = strValue.length();

		if (nLength < 9 || nLength > 12) { // 9 ~ 12
			return strTarget;
		}

		StringBuffer objSB = new StringBuffer(strValue);

		if (strValue.startsWith("02")) { // 서울지역일 경우
			if (nLength == 9) {
				objSB.insert(2, "-");
				objSB.insert(6, "-");
			}
			else {
				objSB.insert(2, "-");
				objSB.insert(7, "-");
			}
		}
		else { // 서울외 지역 또는 휴대폰 일 경우
			if (nLength == 10) {
				objSB.insert(3, "-");
				objSB.insert(7, "-");
			}
			else { // 내선번호등과 같은 특수 번호일 경우
				objSB.insert(3, "-");
				objSB.insert(8, "-");
			}
		}

		return objSB.toString();
	}

	/**
	 * 대상문자열을 우편번호 포멧형식으로 변환하기
	 * 
	 * @param strTarget
	 *            대상문자열
	 * @return 우편번호 포멧형 문자열
	 */
	public static String formatZIP(String strTarget) {
		String strValue = removeFormat(strTarget);

		if (strValue.length() != 6) {
			return strTarget;
		}

		StringBuffer objSB = new StringBuffer(strValue);
		objSB.insert(3, "-");

		return objSB.toString();
	}

	/**
	 * RGB 10진수 값을 2진수 값으로 리턴
	 * 
	 * @param rgb
	 * @return rgb (ex: FFFFFF)
	 */
	public static String transDecToBinary(int rgb) {
		return Integer.toBinaryString(rgb).toUpperCase();
	}

	/**
	 * RGB 10진수 값을 8진수 값으로 리턴
	 * 
	 * @param rgb
	 * @return rgb (ex: FFFFFF)
	 */
	public static String transDecToOctal(int rgb) {
		return Integer.toOctalString(rgb).toUpperCase();
	}

	/**
	 * RGB 10진수 값을 16진수 값으로 리턴
	 * 
	 * @param rgb
	 * @return rgb (ex: FFFFFF)
	 */
	public static String transDecToHex(int rgb) {
		return Integer.toHexString(rgb).toUpperCase();
	}

	/**
	 * 안전하게 String을 int로 변환
	 * 
	 * @param str
	 * @return
	 */
	public static int parseInt(String str) {
		int num;
		try {
			num = Integer.parseInt(str);
		}
		catch (NumberFormatException e) {
			num = 0;
		}
		return num;
	}

	/**
	 * 안전하게 String을 long으로 변환
	 * 
	 * @param str
	 * @return
	 */
	public static long parseLong(String str) {
		long num;
		try {
			num = Long.parseLong(str);
		}
		catch (NumberFormatException e) {
			num = 0;
		}
		return num;
	}

	/**
	 * 안전하게 String을 float으로 변환
	 * 
	 * @param str
	 * @return
	 */
	public static float parseFloat(String str) {
		float num;
		try {
			num = Float.parseFloat(str);
		}
		catch (NumberFormatException e) {
			num = 0;
		}
		return num;
	}

	/**
	 * 안전하게 String을 double로 변환
	 * 
	 * @param str
	 * @return
	 */
	public static double parseDouble(String str) {
		double num;
		try {
			num = Double.parseDouble(str);
		}
		catch (NumberFormatException e) {
			num = 0;
		}
		return num;
	}

	/**
	 * String Escape 처리
	 * 
	 * @param src
	 * @return
	 */
	public static String escape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			}
			else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	/**
	 * String UnEscape 처리
	 * 
	 * @param src
	 * @return
	 */
	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				}
				else {
					ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			}
			else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				}
				else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}
}
