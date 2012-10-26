package au.edu.qut.inn570.resxgen.poc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TestTimeZone {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'hhmmssz");
			//dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+10:00"));//Brisbane TimeZone
			String datetime = dateFormat.format(new Date());
			System.out.println(datetime);
		} catch (Exception e) {
			System.out.println("error: " + e);
		}

	}

}
