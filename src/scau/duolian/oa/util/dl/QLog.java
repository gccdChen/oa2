package scau.duolian.oa.util.dl;

import android.content.Context;

public class QLog {

	public static void i(String stag, String smsg) {
		QLogImpl.c(stag, 4, smsg, null);
	}

	public static void w(String stag, String smsg, Throwable e) {
		QLogImpl.c(stag, 4, smsg, e);
	}

	public static void close() {
		QLogImpl.close();
	}
}
