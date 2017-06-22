package com.vieking.sys.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.zip.CRC32;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.lang.StringUtils;

import com.vieking.sys.exception.DaoException;
import com.vieking.sys.exception.ImageConstants;
import com.vieking.sys.exception.ImageDimension;

/**
 * Utility class for operations with file-system
 * 
 */

public class FileUtils {

	private static final String JPEG = "jpeg";
	private static final String JPG = "jpg";
	private static final int BUFFER_SIZE = 4 * 1024;
	private static final boolean CLOCK = true;
	private static final boolean VERIFY = true;

	public static Long writeFile(byte[] data, String absPath, Long position,
			Long fileSize) throws DaoException, IOException {
		FileOutputStream out = null;
		Long result;
		try {
			File file = new File(absPath);

			if ((file.exists()) & (position == 0)) {
				file.delete();
			} else {
				// 目标文件对应的目录不存在，创建新的目录
				int index = absPath.lastIndexOf("/");
				String dpath = absPath.substring(0, index);
				new File(dpath).mkdirs();
			}
			out = new FileOutputStream(absPath, true);
			out.write(data);
			result = out.getChannel().position();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new DaoException("文件没有找到！", "文件管理", "---");
		} catch (IOException e) {
			out.close();
			e.printStackTrace();
			throw new DaoException("文件操作异常！", "文件管理", "---");
		} catch (Exception e) {
			out.close();
			e.printStackTrace();
			throw new DaoException("文件操作中发生未知异常！", "文件管理", "---");
		}
		return result;
	}

	public static byte[] readFile(String absPath, Long position, int dataSize)
			throws DaoException, IOException {
		ByteArrayOutputStream out = FileUtils.getFileOutputStream(absPath,
				position, dataSize);
		return out.toByteArray();

	}

	/***
	 * 
	 * @param file
	 *            文件
	 * @return 二进制流
	 * @throws DaoException
	 * @throws IOException
	 */
	public static ByteArrayOutputStream getFileOutputStream(String fn,
			long position, int dataSize) throws DaoException, IOException {
		RandomAccessFile raf = new RandomAccessFile(fn, "r");
		ByteArrayOutputStream out = null;
		// if (fs < position) {
		// throw new DaoException("文件读取失败，请求定位超出文件大小！", "文件管理", "---");
		// }
		// if (fs < (position + dataSize)) {
		// dataSize = new Long(fs - position).intValue();
		// }
		byte[] buffer = new byte[dataSize];
		try {
			raf.seek(position);
			out = new ByteArrayOutputStream();
			int num = 0;
			num = raf.read(buffer);
			if (num != -1)
				out.write(buffer, 0, num);
			return out;
		} catch (FileNotFoundException ex) {
			throw new DaoException("文件读取失败，文件没有找到！", "文件管理", "---");
		} catch (IOException e) {
			throw new DaoException("文件读取失败，IO读写异常！", "文件管理", "---");
		} finally {
			try {
				if (raf != null) {
					raf.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
				throw new DaoException("关闭文件失败，IO异常！", "文件管理", "---");
			}

		}
	}

	/**
	 * 转换路径为文件名替换后的路径 例如 取宽度为 80的图片 原始路径为 /user/1/2/image.jpg, 替换字符串为 80 转换后返回
	 * /user/1/2/image_80.jpg
	 * 
	 * @param target
	 *            - 需转换路径
	 * @param substitute
	 *            - new 'addon' to the path
	 */
	public static String transformPath(String target, String substitute) {
		if (target.length() < 2 || target.lastIndexOf(ImageConstants.DOT) == -1) {
			return "";
		}
		final String begin = target.substring(0,
				target.lastIndexOf(ImageConstants.DOT));
		final String end = target.substring(target
				.lastIndexOf(ImageConstants.DOT));
		return begin + substitute + end;
	}

	/**
	 * 
	 * @param fn
	 *            相对路径文件名
	 * @param includeUploadRoot
	 *            是否包含上传文件根路径
	 * @return 返回目标文件名
	 */
	public static String getDestFileName(String fn, String rootPath,
			boolean mkdirs) {
		String dest = StringUtils.isNotEmpty(rootPath) ? rootPath + "/" + fn
				: fn;
		if (mkdirs) {
			File destFile = new File(dest);
			if (!destFile.exists()) {// 目标文件对应的目录不存在，创建新的目录
				int index = dest.lastIndexOf("/");
				String path = dest.substring(0, index);
				new File(path).mkdirs();
			}
		}
		return dest;
	}

	/***
	 * 生成缩略图
	 * 
	 * @param originalFileName
	 *            原始文件名 （相对路径）
	 * @throws DaoException
	 */
	public static void genScaleJpgImages(String originalFileName,
			String rootPath) throws DaoException {
		BufferedImage bsrc = null;
		// 绝对路径文件名
		String absOriginalFileName = getDestFileName(originalFileName,
				rootPath, true);
		try {
			// 获取图片
			bsrc = FileUtils.bitmapToImage(absOriginalFileName,
					ImageConstants.JPG);
		} catch (IOException e1) {
			throw new DaoException("图片获取错误，IO读写异常！", "文件管理", "---");
		}

		for (ImageDimension d : ImageDimension.values()) {
			// 不对原始图处理
			if (ImageDimension.ORIGINAL.equals(d))
				continue;
			scaleJpgImage(bsrc, originalFileName, d.getFilePostfix(), d.getX(),
					rootPath);
		}
		bsrc = null;
	}

	public static boolean scaleJpgImage(BufferedImage bsrc,
			String originalFileName, String pattern, int size, String rootPath)
			throws DaoException {

		int resizedParam = bsrc.getWidth() > bsrc.getHeight() ? bsrc.getWidth()
				: bsrc.getHeight();
		double scale = (double) size / resizedParam;
		Double widthInDouble = ((Double) scale * bsrc.getWidth());
		int width = widthInDouble.intValue();
		Double heightInDouble = ((Double) scale * bsrc.getHeight());
		int height = heightInDouble.intValue();
		// Too small picture or original size
		if (width > bsrc.getWidth() || height > bsrc.getHeight() || size == 0) {
			width = bsrc.getWidth();
			height = bsrc.getHeight();
		}
		// scale image if need
		BufferedImage bdest = FileUtils.getScaledInstance(bsrc, width, height,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);
		// 根据图像比例确定目标文件名称
		String dest = getDestFileName(transformPath(originalFileName, pattern),
				rootPath, true);
		try {
			// 保存文件
			FileUtils.imageToBitmap(bdest, dest, ImageConstants.JPG);
			bdest = null;
		} catch (IOException ex) {
			throw new DaoException("图片文件生成失败，IO读写异常！", "文件管理", "---");
		}
		return true;
	}

	/**
	 * Utility method for copying file
	 * 
	 * @param srcFile
	 *            - source file
	 * @param destFile
	 *            - destination file
	 */
	public static void copyFile(File srcFile, File destFile) throws IOException {
		if (!srcFile.getPath().toLowerCase().endsWith(JPG)
				&& !srcFile.getPath().toLowerCase().endsWith(JPEG)) {
			return;
		}
		final InputStream in = new FileInputStream(srcFile);
		final OutputStream out = new FileOutputStream(destFile);
		try {
			long millis = System.currentTimeMillis();
			CRC32 checksum;
			if (VERIFY) {
				checksum = new CRC32();
				checksum.reset();
			}
			final byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = in.read(buffer);
			while (bytesRead >= 0) {
				if (VERIFY) {
					checksum.update(buffer, 0, bytesRead);
				}
				out.write(buffer, 0, bytesRead);
				bytesRead = in.read(buffer);
			}
			if (CLOCK) {
				millis = System.currentTimeMillis() - millis;
				System.out.println("Copy file '" + srcFile.getPath() + "' on "
						+ millis / 1000L + " second(s)");
			}
		} catch (IOException e) {
			throw e;
		} finally {
			out.close();
			in.close();
		}
	}

	public static void writeFile(byte[] data, int begin, int end, String destFn)
			throws DaoException {
		FileOutputStream out = null;
		try {
			File dest = new File(destFn);
			if (!dest.exists()) {// 目标文件对应的目录不存在，创建新的目录
				int index = destFn.lastIndexOf("/");
				String path = destFn.substring(0, index);
				new File(path).mkdirs();
			}
			out = new FileOutputStream(destFn);

			out.write(data, begin, end);
		} catch (FileNotFoundException ex) {
			throw new DaoException("写入文件失败，文件没有找到！", "文件管理", "---");
		} catch (IOException e) {
			throw new DaoException("写入文件失败，IO读写异常！", "文件管理", "---");
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException ex) {
				throw new DaoException("关闭文件失败，IO异常！", "文件管理", "---");
			}

		}
	}

	public static void writeFile(byte[] data, String destFn)
			throws DaoException {
		FileOutputStream out = null;
		try {
			File dest = new File(destFn);
			if (!dest.exists()) {// 目标文件对应的目录不存在，创建新的目录
				int index = destFn.lastIndexOf("/");
				String path = destFn.substring(0, index);
				new File(path).mkdirs();
			}
			out = new FileOutputStream(destFn);
			int num = data.length;
			out.write(data, 0, num);
		} catch (FileNotFoundException ex) {
			throw new DaoException("写入文件失败，文件没有找到！", "文件管理", "---");
		} catch (IOException e) {
			throw new DaoException("写入文件失败，IO读写异常！", "文件管理", "---");
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException ex) {
				throw new DaoException("关闭文件失败，IO异常！", "文件管理", "---");
			}

		}
	}

	/**
	 * 
	 * @param fileName
	 *            绝对路径文件名
	 * @return 二进制流
	 * @throws DaoException
	 */
	public static ByteArrayOutputStream getFileOutputStream(String fileName)
			throws DaoException {
		File file = new File(fileName);
		return getFileOutputStream(file);
	}

	/***
	 * 
	 * @param file
	 *            文件
	 * @return 二进制流
	 * @throws DaoException
	 */
	public static ByteArrayOutputStream getFileOutputStream(File file)
			throws DaoException {
		FileInputStream in = null;
		ByteArrayOutputStream out = null;
		byte[] buffer = new byte[102400];
		try {
			in = new FileInputStream(file);
			out = new ByteArrayOutputStream();
			int num = 0;
			while ((num = in.read(buffer)) != -1) {
				out.write(buffer, 0, num);
			}
			return out;
		} catch (FileNotFoundException ex) {
			throw new DaoException("文件读取失败，文件没有找到！", "文件管理", "---");
		} catch (IOException e) {
			throw new DaoException("文件读取失败，IO读写异常！", "文件管理", "---");
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
				throw new DaoException("关闭文件失败，IO异常！", "文件管理", "---");
			}

		}
	}

	public byte[] getFileToByte(File file) {
		final int BUFFER_SIZE = 0x3000000;// 缓冲区大小为30M

		/**
		 * 
		 * map(FileChannel.MapMode mode,long position, long size)
		 * 
		 * mode - 根据是按只读、读取/写入或专用（写入时拷贝）来映射文件，分别为 FileChannel.MapMode 类中所定义的
		 * READ_ONLY、READ_WRITE 或 PRIVATE 之一
		 * 
		 * position - 文件中的位置，映射区域从此位置开始；必须为非负数
		 * 
		 * size - 要映射的区域大小；必须为非负数且不大于 Integer.MAX_VALUE
		 * 
		 * 所以若想读取文件后半部分内容，如例子所写；若想读取文本后1/8内容，需要这样写map(FileChannel.MapMode.
		 * READ_ONLY, f.length()*7/8,f.length()/8)
		 * 
		 * 想读取文件所有内容，需要这样写map(FileChannel.MapMode.READ_ONLY, 0,f.length())
		 * 
		 */

		MappedByteBuffer inputBuffer;
		try {
			inputBuffer = new RandomAccessFile(file, "r").getChannel().map(
					FileChannel.MapMode.READ_ONLY, file.length() / 2,
					file.length() / 2);

			byte[] dst = new byte[BUFFER_SIZE];// 每次读出3M的内容
			long start = System.currentTimeMillis();
			for (int offset = 0; offset < inputBuffer.capacity(); offset += BUFFER_SIZE) {

				if (inputBuffer.capacity() - offset >= BUFFER_SIZE) {

					for (int i = 0; i < BUFFER_SIZE; i++)

						dst[i] = inputBuffer.get(offset + i);

				} else {

					for (int i = 0; i < inputBuffer.capacity() - offset; i++)

						dst[i] = inputBuffer.get(offset + i);

				}

				int length = (inputBuffer.capacity() % BUFFER_SIZE == 0) ? BUFFER_SIZE
						: inputBuffer.capacity() % BUFFER_SIZE;

				System.out.println(new String(dst, 0, length));// new
				// String(dst,0,length)这样可以取出缓存保存的字符串，可以对其进行操作

			}

			long end = System.currentTimeMillis();

			System.out.println("读取文件文件一半内容花费：" + (end - start) + "毫秒");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * the traditional io way
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(File file) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(
				(int) file.length());
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size))) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bos.close();
		}
	}

	/**
	 * NIO way
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray2(File file) throws IOException {
		FileChannel channel = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(file);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {

			}
			return byteBuffer.array();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Mapped File way MappedByteBuffer
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static byte[] getFiletoByteBuffer(File file) throws IOException {

		FileChannel fc = null;
		try {
			fc = new RandomAccessFile(file, "r").getChannel();
			MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0,
					fc.size()).load();
			System.out.println(byteBuffer.isLoaded());
			byte[] result = new byte[(int) fc.size()];
			if (byteBuffer.remaining() > 0) {
				// System.out.println("remain");
				byteBuffer.get(result, 0, byteBuffer.remaining());
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				fc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static int copyFile(String destFn, String srcFn) throws DaoException {
		int size = 0;
		FileInputStream in = null;
		FileOutputStream out = null;
		byte[] buffer = new byte[102400];
		try {
			in = new FileInputStream(srcFn);
			size = in.available();
			File dest = new File(destFn);
			if (!dest.exists()) {// 目标文件对应的目录不存在，创建新的目录
				int index = destFn.lastIndexOf("/");
				String path = destFn.substring(0, index);
				new File(path).mkdirs();
			}
			out = new FileOutputStream(destFn);
			int num = 0;
			while ((num = in.read(buffer)) != -1) {
				out.write(buffer, 0, num);
			}
		} catch (FileNotFoundException ex) {
			throw new DaoException("拷贝文件失败，文件没有找到！", "文件管理", "---");
		} catch (IOException e) {
			throw new DaoException("拷贝文件失败，IO读写异常！", "文件管理", "---");
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
				throw new DaoException("关闭文件失败，IO异常！", "文件管理", "---");
			}

		}
		return size;
	}

	/**
	 * Utility method for copying directory
	 * 
	 * @param srcDir
	 *            - source directory
	 * @param dstDir
	 *            - destination directory
	 */
	public static void copyDirectory(File srcDir, File dstDir)
			throws IOException {

		if (".svn".equals(srcDir.getName())) {
			return;
		}

		if (srcDir.isDirectory()) {
			if (!dstDir.exists()) {
				dstDir.mkdir();
			}

			for (String aChildren : srcDir.list()) {
				copyDirectory(new File(srcDir, aChildren), new File(dstDir,
						aChildren));
			}
		} else {
			copyFile(srcDir, dstDir);
		}
	}

	/**
	 * Utility method for delete directory
	 * 
	 * @param dir
	 *            - directory to delete
	 * @param isInitialDelete
	 *            - determine if the deleting process running at startup or on
	 *            destroy of application
	 * @return true if directory succesfully deleted
	 */
	public static boolean deleteDirectory(File dir, boolean isInitialDelete) {
		if (dir.isDirectory()) {
			if (dir.exists()) {
				for (File child : dir.listFiles()) {
					try {
						deleteDirectory(child, isInitialDelete);
					} catch (Exception e) {
						if (isInitialDelete) {
							continue;
						} else
							return false;
					}
				}
			}

		} else {
			if (dir.exists()) {
				final boolean isFileDeleted = dir.delete();
				System.out.println((isFileDeleted ? "OK     " : "ERROR ")
						+ "Delete file '" + dir.getPath() + '\'');
			}
		}
		dir.delete();
		return true;
	}

	/**
	 * Utility method for concatenation names of collection of files
	 * 
	 * @param files
	 *            - array of strings to concatenate
	 * @return concatenated string
	 */
	public static String joinFiles(String... files) {
		final StringBuilder res = new StringBuilder();
		for (String file : files) {
			res.append(file).append(File.separatorChar);
		}

		return res.substring(0, res.length() - 1);
	}

	/**
	 * Utility method for delete file
	 * 
	 * @param file
	 *            - file to delete
	 */
	public static void deleteFile(File file) {
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * Utility method to read image from disk and transform image to
	 * BufferedImage object
	 * 
	 * @param data
	 *            - relative path to the image
	 * @param format
	 *            - file prefix of the image
	 * @return BufferedImage representation of the image
	 * 
	 */
	public static BufferedImage bitmapToImage(String data, String format)
			throws IOException {
		final InputStream inb = new FileInputStream(data);
		final ImageReader rdr = ImageIO.getImageReadersByFormatName(format)
				.next();
		final ImageInputStream imageInput = ImageIO.createImageInputStream(inb);
		rdr.setInput(imageInput);
		final BufferedImage image = rdr.read(0);
		inb.close();
		return image;
	}

	/**
	 * Utility method to write BufferedImage object to disk
	 * 
	 * @param image
	 *            - BufferedImage object to save.
	 * @param data
	 *            - relative path to the image
	 * @param format
	 *            - file prefix of the image
	 * @return BufferedImage representation of the image
	 * 
	 */
	public static void imageToBitmap(BufferedImage image, String data,
			String format) throws IOException {
		final OutputStream inb = new FileOutputStream(data);
		final ImageWriter wrt = ImageIO.getImageWritersByFormatName(format)
				.next();
		final ImageInputStream imageInput = ImageIO
				.createImageOutputStream(inb);
		wrt.setOutput(imageInput);
		wrt.write(image);
		inb.close();
	}

	/**
	 * Convenience method that returns a scaled instance of the provided
	 * {@code BufferedImage}.
	 * 
	 * @param img
	 *            the original image to be scaled
	 * @param targetWidth
	 *            the desired width of the scaled instance, in pixels
	 * @param targetHeight
	 *            the desired height of the scaled instance, in pixels
	 * @param hint
	 *            one of the rendering hints that corresponds to
	 *            {@code RenderingHints.KEY_INTERPOLATION} (e.g.
	 *            {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
	 *            {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
	 *            {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
	 * @param higherQuality
	 *            if true, this method will use a multi-step scaling technique
	 *            that provides higher quality than the usual one-step technique
	 *            (only useful in downscaling cases, where {@code targetWidth}
	 *            or {@code targetHeight} is smaller than the original
	 *            dimensions, and generally only when the {@code BILINEAR} hint
	 *            is specified)
	 * @return a scaled version of the original {@code BufferedImage}
	 */
	public static BufferedImage getScaledInstance(BufferedImage img,
			int targetWidth, int targetHeight, Object hint,
			boolean higherQuality) {
		final int type = img.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) img;
		int w;
		int h;
		if (higherQuality) {
			// Use multi-step technique: start with original size, then
			// scale down in multiple passes with drawImage()
			// until the target size is reached
			w = img.getWidth();
			h = img.getHeight();
		} else {
			// Use one-step technique: scale directly from original
			// size to target size with a single drawImage() call
			w = targetWidth;
			h = targetHeight;
		}

		do {
			if (higherQuality && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}

			if (higherQuality && h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}

			final BufferedImage tmp = new BufferedImage(w, h, type);
			final Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();

			ret = tmp;
		} while (w != targetWidth || h != targetHeight);

		return ret;
	}

	/**
	 * Utility method for creation of directory
	 * 
	 * @param directory
	 *            - directory to create
	 * 
	 */
	public static void addDirectory(File directory) {
		if (directory.exists()) {
			deleteDirectory(directory, false);
		}
		directory.mkdirs();
	}

	/*
	 * 生成随机文件名
	 */
	public static String generateRandomFilename() {
		String RandomFilename = "";
		Random rand = new Random();// 生成随机数
		int random = rand.nextInt();
		SimpleDateFormat df = new SimpleDateFormat("yymmdd");
		String now = df.format(Calendar.getInstance().getTime());
		RandomFilename = now
				+ String.valueOf(random > 0 ? random : (-1) * random);
		return RandomFilename;
	}

	/**
	 * 修改文件的最后访问时间。 * 如果文件不存在则创建该文件。 <b>
	 * 目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * 
	 * @param file
	 *            需要修改最后访问时间的文件。
	 * 
	 */
	public static void touch(File file) {
		long currentTime = System.currentTimeMillis();
		if (!file.exists()) {
			System.err.println("file not found:" + file.getName());
			System.err.println("Create a new file:" + file.getName());
			try {
				if (file.createNewFile()) { // System.out.println("Succeeded!");
				} else { // System.err.println("Create file failed!");
				}
			} catch (IOException e) { // System.err.println("Create file
				// failed!");
				e.printStackTrace();
			}
		}
		boolean result = file.setLastModified(currentTime);
		if (!result) { // System.err.println("touch failed: " +
			// file.getName());
		}
	}

	/**
	 * 修改文件的最后访问时间。 <br>
	 * 如果文件不存在则创建该文件。 <br>
	 * 目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * 
	 * @param fileName
	 *            需要修改最后访问时间的文件的文件名。
	 */
	public static void touch(String fileName) {
		File file = new File(fileName);
		touch(file);
	}

	/**
	 * 修改文件的最后访问时间。 如果文件不存在则创建该文件。 <br>
	 * 目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
	 * 
	 * @param files
	 *            需要修改最后访问时间的文件数组。
	 */
	public static void touch(File[] files) {
		for (int i = 0; i < files.length; i++) {
			touch(files);
		}
	}

	/**
	 * * 修改文件的最后访问时间。 * 如果文件不存在则创建该文件。 *
	 * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b> *
	 * 
	 * @param fileNames
	 *            需要修改最后访问时间的文件名数组。 *
	 * 
	 */
	public static void touch(String[] fileNames) {
		File[] files = new File[fileNames.length];
		for (int i = 0; i < fileNames.length; i++) {
			files[i] = new File(fileNames[i]);
		}
		touch(files);
	}

	/**
	 * * 判断指定的文件是否存在。 *
	 * 
	 * @param fileName
	 *            要判断的文件的文件名 *
	 * @return 存在时返回true，否则返回false。 *
	 * 
	 */
	public static boolean isFileExist(String fileName) {
		return new File(fileName).isFile();
	}

	/**
	 * * 创建指定的目录。 * 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。 *
	 * <b>注意：可能会在返回false的时候创建部分父目录。</b> *
	 * 
	 * @param file
	 *            要创建的目录 *
	 * @return 完全创建成功时返回true，否则返回false。 *
	 * 
	 */
	public static boolean makeDirectory(File file) {
		File parent = file.getParentFile();
		if (parent != null) {
			return parent.mkdirs();
		}
		return false;
	}

	/**
	 * * 创建指定的目录。 * 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。 *
	 * <b>注意：可能会在返回false的时候创建部分父目录。</b> *
	 * 
	 * @param fileName
	 *            要创建的目录的目录名 *
	 * @return 完全创建成功时返回true，否则返回false。 *
	 * 
	 */
	public static boolean makeDirectory(String fileName) {
		File file = new File(fileName);
		return makeDirectory(file);
	}

	/**
	 * * 清空指定目录中的文件。 * 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。 *
	 * 另外这个方法不会迭代删除，即不会删除子目录及其内容。 *
	 * 
	 * @param directory
	 *            要清空的目录 *
	 * @return 目录下的所有文件都被成功删除时返回true，否则返回false. *
	 * 
	 */
	public static boolean emptyDirectory(File directory) {
		boolean result = false;
		File[] entries = directory.listFiles();
		for (int i = 0; i < entries.length; i++) {
			if (!entries[i].delete()) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * * 清空指定目录中的文件。 * 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。 *
	 * 另外这个方法不会迭代删除，即不会删除子目录及其内容。 *
	 * 
	 * @param directoryName
	 *            要清空的目录的目录名 *
	 * @return 目录下的所有文件都被成功删除时返回true，否则返回false。 *
	 * 
	 */
	public static boolean emptyDirectory(String directoryName) {
		File dir = new File(directoryName);
		return emptyDirectory(dir);
	}

	/**
	 * * 删除指定目录及其中的所有内容。 *
	 * 
	 * @param dirName
	 *            要删除的目录的目录名 *
	 * @return 删除成功时返回true，否则返回false。 *
	 * 
	 */
	public static boolean deleteDirectory(String dirName) {
		return deleteDirectory(new File(dirName));
	}

	/**
	 * * 删除指定目录及其中的所有内容。
	 * 
	 * @param dir
	 *            要删除的目录
	 * @return 删除成功时返回true，否则返回false。
	 * 
	 */
	public static boolean deleteDirectory(File dir) {
		if ((dir == null) || !dir.isDirectory()) {
			throw new IllegalArgumentException("Argument " + dir
					+ " is not a directory. ");
		}
		File[] entries = dir.listFiles();
		int sz = entries.length;
		for (int i = 0; i < sz; i++) {
			if (entries[i].isDirectory()) {
				if (!deleteDirectory(entries[i])) {
					return false;
				}
			} else {
				if (!entries[i].delete()) {
					return false;
				}
			}
		}
		if (!dir.delete()) {
			return false;
		}
		return true;
	}

	/**
	 * * 返回文件的URL地址。
	 * 
	 * @param file
	 *            文件
	 * @return 文件对应的的URL地址
	 * @throws MalformedURLException
	 * @deprecated 在实现的时候没有注意到File类本身带一个toURL方法将文件路径转换为URL。 * 请使用File.toURL方法。
	 */
	public static URL getURL(File file) throws MalformedURLException {
		String fileURL = "file:/" + file.getAbsolutePath();
		URL url = new URL(fileURL);
		return url;
	}

	/**
	 * 从文件路径得到文件名。
	 * 
	 * @param filePath
	 *            文件的路径，可以是相对路径也可以是绝对路径
	 * @return 对应的文件名
	 */
	public static String getFileName(String filePath) {
		File file = new File(filePath);
		return file.getName();
	}

	/**
	 * 从文件名得到文件绝对路径。
	 * 
	 * @param fileName
	 *            文件名
	 * @return 对应的文件路径
	 */
	public static String getFilePath(String fileName) {
		File file = new File(fileName);
		return file.getAbsolutePath();
	}

	/**
	 * 将DOS/Windows格式的路径转换为UNIX/Linux格式的路径。
	 * 其实就是将路径中的"\"全部换为"/"，因为在某些情况下我们转换为这种方式比较方便，
	 * 某中程度上说"/"比"\"更适合作为路径分隔符，而且DOS/Windows也将它当作路径分隔符。
	 * 
	 * @param filePath
	 *            转换前的路径
	 * @return 转换后的路径
	 */
	public static String toUNIXpath(String filePath) {
		return filePath.replace('\\', '/');
	}

	/**
	 * 从文件名得到UNIX风格的文件绝对路径。
	 * 
	 * @param fileName
	 *            文件名
	 * @return 对应的UNIX风格的文件路径
	 * @see #toUNIXpath(String filePath) toUNIXpath
	 */
	public static String getUNIXfilePath(String fileName) {
		File file = new File(fileName);
		return toUNIXpath(file.getAbsolutePath());
	}

	/**
	 * 得到文件的类型。 实际上就是得到文件名中最后一个“.”后面的部分。
	 * 
	 * @param fileName
	 *            文件名
	 * @return 文件名中的类型部分
	 */
	public static String getTypePart(String fileName) {
		int point = fileName.lastIndexOf('.');
		int length = fileName.length();
		if (point == -1 || point == length - 1) {
			return "";
		} else {
			return fileName.substring(point + 1, length);
		}
	}

	/**
	 * * 得到文件的类型。 * 实际上就是得到文件名中最后一个“.”后面的部分。 *
	 * 
	 * @param file
	 *            文件 *
	 * @return 文件名中的类型部分 *
	 * 
	 */
	public static String getFileType(File file) {
		return getTypePart(file.getName());
	}

	/**
	 * * 得到文件的名字部分。 实际上就是路径中的最后一个路径分隔符后的部分。
	 * 
	 * @param fileName
	 *            文件名
	 * @return 文件名中的名字部分
	 */
	public static String getNamePart(String fileName) {
		int point = getPathLsatIndex(fileName);
		int length = fileName.length();
		if (point == -1) {
			return fileName;
		} else if (point == length - 1) {
			int secondPoint = getPathLsatIndex(fileName, point - 1);
			if (secondPoint == -1) {
				if (length == 1) {
					return fileName;
				} else {
					return fileName.substring(0, point);
				}
			} else {
				return fileName.substring(secondPoint + 1, point);
			}
		} else {
			return fileName.substring(point + 1);
		}
	}

	/**
	 * 得到文件名中的父路径部分。 对两种路径分隔符都有效。 不存在时返回""。
	 * 如果文件名是以路径分隔符结尾的则不考虑该分隔符，例如"/path/"返回""。
	 * 
	 * @param fileName
	 *            文件名
	 * @return 父路径，不存在或者已经是父目录时返回""
	 */
	public static String getPathPart(String fileName) {
		int point = getPathLsatIndex(fileName);
		int length = fileName.length();
		if (point == -1) {
			return "";
		} else if (point == length - 1) {
			int secondPoint = getPathLsatIndex(fileName, point - 1);
			if (secondPoint == -1) {
				return "";
			} else {
				return fileName.substring(0, secondPoint);
			}
		} else {
			return fileName.substring(0, point);
		}
	}

	/**
	 * 得到路径分隔符在文件路径中首次出现的位置。 对于DOS或者UNIX风格的分隔符都可以。
	 * 
	 * @param fileName
	 *            文件路径
	 * @return 路径分隔符在路径中首次出现的位置，没有出现时返回-1。
	 */
	public static int getPathIndex(String fileName) {
		int point = fileName.indexOf('/');
		if (point == -1) {
			point = fileName.indexOf('\\');
		}
		return point;
	}

	/**
	 * 得到路径分隔符在文件路径中指定位置后首次出现的位置。 对于DOS或者UNIX风格的分隔符都可以。
	 * 
	 * @param fileName
	 *            文件路径
	 * @param fromIndex
	 *            开始查找的位置
	 * @return 路径分隔符在路径中指定位置后首次出现的位置，没有出现时返回-1。
	 */
	public static int getPathIndex(String fileName, int fromIndex) {
		int point = fileName.indexOf('/', fromIndex);
		if (point == -1) {
			point = fileName.indexOf('\\', fromIndex);
		}
		return point;
	}

	/**
	 * 得到路径分隔符在文件路径中最后出现的位置。 * 对于DOS或者UNIX风格的分隔符都可以。
	 * 
	 * @param fileName
	 *            文件路径
	 * @return 路径分隔符在路径中最后出现的位置，没有出现时返回-1。
	 * 
	 */
	public static int getPathLsatIndex(String fileName) {
		int point = fileName.lastIndexOf('/');
		if (point == -1) {
			point = fileName.lastIndexOf('\\');
		}
		return point;
	}

	/**
	 * 得到路径分隔符在文件路径中指定位置前最后出现的位置。 * 对于DOS或者UNIX风格的分隔符都可以。
	 * 
	 * @param fileName
	 *            文件路径
	 * @param fromIndex
	 *            开始查找的位置
	 * @return 路径分隔符在路径中指定位置前最后出现的位置，没有出现时返回-1。
	 * 
	 */
	public static int getPathLsatIndex(String fileName, int fromIndex) {
		int point = fileName.lastIndexOf('/', fromIndex);
		if (point == -1) {
			point = fileName.lastIndexOf('\\', fromIndex);
		}
		return point;
	}

	/**
	 * 将文件名中的类型部分去掉。
	 * 
	 * @param filename
	 *            文件名
	 * @return 去掉类型部分的结果
	 */
	public static String trimType(String filename) {
		int index = filename.lastIndexOf(".");
		if (index != -1) {
			return filename.substring(0, index);
		} else {
			return filename;
		}
	}

	/**
	 * * 得到相对路径。 * 文件名不是目录名的子节点时返回文件名。 *
	 * 
	 * @param pathName
	 *            目录名 *
	 * @param fileName
	 *            文件名 *
	 * @return 得到文件名相对于目录名的相对路径，目录下不存在该文件时返回文件名 *
	 * 
	 */
	public static String getSubpath(String pathName, String fileName) {
		int index = fileName.indexOf(pathName);
		if (index != -1) {
			return fileName.substring(index + pathName.length() + 1);
		} else {
			return fileName;
		}
	}

	/**
	 * 输出目录中的所有文件及目录名字
	 * 
	 */

	/**
	 * 返回目录中的所有文件及目录名字
	 * 
	 * @param filePath
	 */
	public static String[] readFolderByPath(String filePath) {
		File file = new File(filePath);
		File[] tempFile = file.listFiles();
		String[] result = null;
		if (tempFile != null) {
			result = new String[tempFile.length];
			for (int i = 0; i < tempFile.length; i++) {
				if (tempFile[i].isFile()) {
					result[i] = tempFile[i].getName();
				}
				if (tempFile[i].isDirectory()) {

				}
			}
		}
		return result;
	}

}
