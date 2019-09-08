package utils;

public class ThreadUtils {

	@SuppressWarnings("static-access")
	public static void goIdle(long sleepTime) {
		try {
			Thread.currentThread().sleep(sleepTime);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
