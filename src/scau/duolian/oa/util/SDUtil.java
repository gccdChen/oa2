package scau.duolian.oa.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class SDUtil {

	private static String TAG = SDUtil.class.getSimpleName();

	private static double MB = 1024;
	private static double FREE_SD_SPACE_NEEDED_TO_CACHE = 10;
	private static double IMAGE_EXPIRE_TIME = 10;

	public static boolean canUse() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static Bitmap getImage(String dir,String fileName) {
		// check image file exists
		String realFileName = dir + "/" + fileName;
		File file = new File(realFileName);
		if (!file.exists()) {
			return null;
		}
		// get original image
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(realFileName, options);
	}

	public static Bitmap getSample(String dir,String fileName) {
		// check image file exists
		String realFileName = dir + "/" + fileName;
		File file = new File(realFileName);
		if (!file.exists()) {
			return null;
		}
		// get original size
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(realFileName, options);
		int zoom = (int) (options.outHeight / (float) 50);
		if (zoom < 0)
			zoom = 1;
		// get resized image
		options.inSampleSize = zoom;
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(realFileName, options);
		return bitmap;
	}

	public static String saveImage(Bitmap bitmap, String fileName, String sdir) {

		if (bitmap == null) {
			Log.w(TAG, " trying to save null bitmap");
			return null;
		}
		// 判断sdcard上的空间
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > getFreeSpace()) {
			Log.w(TAG, "Low free space onsd, do not cache");
			return null;
		}
		// 不存在则创建目录
		File dir = new File(sdir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// 保存图片
		try {
			String realFileName = sdir + "/" + fileName;
			File file = new File(realFileName);
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
			Log.i(TAG, "Image saved tosd");
			return realFileName;
		} catch (FileNotFoundException e) {
			Log.w(TAG, "FileNotFoundException");
		} catch (IOException e) {
			Log.w(TAG, "IOException");
		}
		return null;
	}

	protected static void updateTime(String fileName) {
		File file = new File(fileName);
		long newModifiedTime = System.currentTimeMillis();
		file.setLastModified(newModifiedTime);
	}

	/**
	 * 计算sdcard上的剩余空间
	 * 
	 * @return
	 */
	public static int getFreeSpace() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}

	public static void removeExpiredCache(String dirPath, String filename) {
		File file = new File(dirPath, filename);
		if (System.currentTimeMillis() - file.lastModified() > IMAGE_EXPIRE_TIME) {
			Log.i(TAG, "Clear some expiredcache files ");
			file.delete();
		}
	}

	public static void removeCache(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > getFreeSpace()) {
			int removeFactor = (int) ((0.4 * files.length) + 1);
			Arrays.sort(files, new FileLastModifSort());
			Log.i(TAG, "Clear some expiredcache files ");
			for (int i = 0; i < removeFactor; i++) {
				files[i].delete();
			}

		}

	}

	private static class FileLastModifSort implements Comparator<File> {
		@Override
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	public static void clearDir(String dir) {
		File fdir = new File(dir);
		if (fdir.isDirectory()) {
			for (File file : fdir.listFiles()) {
				file.delete();
			}
		}
	}

	public static void sortByLastModify(File[] files) {
		if (files == null || files.length == 0)
			return;
		quick(files);
	}

	private static void quick(File[] list) {
		if (list.length > 0) { // 查看数组是否为空
			quickSort(list, 0, list.length - 1);
		}
	}

	private static void quickSort(File[] list, int low, int high) {
		if (low < high) {
			int middle = getMiddle(list, low, high);
			quickSort(list, low, middle - 1);
			quickSort(list, middle + 1, high);
		}
	}

	private static int getMiddle(File[] list, int low, int high) {
		File tmp = list[low];
		while (low < high) {
			while (low < high
					&& list[high].lastModified() <= tmp.lastModified()) {
				high--;
			}
			list[low] = list[high];
			while (low < high && list[low].lastModified() >= tmp.lastModified()) {
				low++;
			}
			list[high] = list[low];
		}
		list[low] = tmp;
		return low;
	}

	/**
	 * file1比file2新则返回1
	 * 
	 * @param file1
	 * @param file2
	 * @return file1.lastmodify()>file2?1:-1
	 */
	private static int compare(File file1, File file2) {
		return file1.lastModified() > file2.lastModified() ? 1 : -1;
	}

	public static String readText(File file) {
		if (file == null || !file.exists())
			return null;
		FileReader reader = null;
		BufferedReader bufferedReader = null;
		String temp = null;
		StringBuffer buffer = new StringBuffer();
		try {
			reader = new FileReader(file);
			bufferedReader = new BufferedReader(reader);
			while ((temp = bufferedReader.readLine()) != null) {
				buffer.append(temp);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return buffer.toString();
	}

	public static void writeText(String content, String filePath) {
		File file = new File(filePath);
		FileWriter writer = null;
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
				writer = new FileWriter(file);
				writer.write(content);
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static long getFileSize(File f) throws Exception {// 取得文件大小
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
		} else {
			f.createNewFile();
			System.out.println("文件不存在");
		}
		return s;
	}

	// 递归
	public static long getFilesSize(File dir) throws Exception// 取得文件夹大�?
	{
		long size = 0;
		File flist[] = dir.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	public static String FormetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public static long getlist(File dir) {// 递归求取目录文件个数
		long size = 0;
		File flist[] = dir.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;

	}

	/**
	 * 
	 * @param bm
	 * @return
	 */
	public static InputStream Bitmap2IS(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
		return sbs;
	}
	
	private final static int BUFFER_SIZE = 1024 * 64;
	/**
	 * 写入
	 * @param inputStream
	 * @param outputStream
	 */
	public static void write(InputStream inputStream, OutputStream outputStream) {
		BufferedInputStream bis = new BufferedInputStream(inputStream);
		BufferedOutputStream bos = new BufferedOutputStream(outputStream);
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			int len = 0;
			while ((len = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			bos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}