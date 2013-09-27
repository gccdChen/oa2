package scau.duolian.oa.util.dl;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class QLogImpl {
	private static boolean busefilelog = true;
	private static String slogpath = "";
	private static File flog;
	private static Calendar cal = Calendar.getInstance();

	static String getfilename() {
		Calendar t = Calendar.getInstance();
		return "zs" + t.get(Calendar.YEAR) + "_" + (t.get(Calendar.MONTH)+1) + "_" + t.get(Calendar.DAY_OF_MONTH) + ".txt";
	}

	public static void close() {
		if (flog != null)
		{
			flog = null;
		}
	}

	static void openlogfile() throws IOException {
		if (flog != null)
			return;
		slogpath = Environment.getExternalStorageDirectory().getPath() + "/duolian";
		File f = new File(slogpath);
		if (f.exists() == false)
			if(f.mkdirs() == false)
				return;
		String sfile = slogpath + "/" + getfilename();
        flog = new File(sfile);
        if(flog.exists() == false)
        {
        	if(flog.createNewFile() == false)
        		flog = null;
        }
	}

	public static void log2file(String stag, String slvl,
			String smsg, Throwable e1) {
		if (busefilelog) {
			try {
				openlogfile();
				if(flog == null)
					return;
				FileOutputStream fos = new FileOutputStream(flog, true);
				fos.write(("\n" + gettime() + "-" + stag + ":" + smsg).getBytes());
				fos.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
        		flog = null;
			}
		}
	}

	private static String gettime() {
		java.util.Date t = new java.util.Date();
		return t.getYear() + "-" + t.getMonth() + "-" + t.getDay() + " " + t.getHours() + ":" + t.getMinutes() + ":" + t.getSeconds();
	}

	public static void c(String stag, int nlvl,
			String smsg, Throwable e) {
		log2file(stag, "", smsg, e);
	}
}
