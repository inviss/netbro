package sample;

import org.junit.Test;

public class QuartzMonitorTest {

	
	
	@Test
	public void formatConverter() {
		String[] formats = {"0 0 2 * * ?", "0 0 3 * * ?", "0 0/2 * * * ?", "0 0 0/1 * * ?", "0 0 1 * * ?"};
		for(String format : formats) {
			System.out.println(format);
			String[] fields = format.split(" ");
			System.out.println("fields[2] "+fields[2]);
			System.out.println("fields[1] "+fields[1]);
			System.out.println("fields[0] "+fields[0]);
			System.out.println(timeCheck(fields[2], 'H')+timeCheck(fields[1], 'M')+timeCheck(fields[0], 'S'));
		}
	}
	
	private String timeCheck(String time, char gb) {
		String msg = "";
		switch(gb) {
		case 'S':
			if(time.indexOf("/") > -1) {
				msg = "매 "+time.split("\\/")[1]+"초마다";
			}
			break;
		case 'M':
			if(time.indexOf("/") > -1) {
				msg = "매 "+time.split("\\/")[1]+"분마다";
			}
			break;
		case 'H':
			if(time.startsWith("0")) {
				if(time.indexOf("/") > -1) {
					msg = "매 "+time.split("\\/")[1]+"시간마다";
				}
			} else if(!time.equals("*")) {
				msg = "매일 "+time+"시에";
			}
			break;
		}
		return msg;
	}
}
