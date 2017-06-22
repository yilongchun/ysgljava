package com.vieking.sys.util;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class Thumbnail {
	private static final int canvas_width = 198;
	private static final int canvas_height = 183;
	private static final float p = canvas_width / canvas_height;

	public static byte[] get(InputStream ins) {
		int x = 0;
		int y = 0;
		int nw = 0;
		int nh = 0;
		Image src = null;
		BufferedImage tag = null;
		ByteArrayOutputStream out = null;
		JPEGImageEncoder encoder = null;
		byte[] result = null;
		try {
			src = javax.imageio.ImageIO.read(ins);
			int w = src.getWidth(null);
			int h = src.getHeight(null);
			if (w / h > p) {
				nw = canvas_width;
				nh = canvas_width * h / w;
				x = 0;
				y = (canvas_height - nh) / 2;
			} else {
				nw = canvas_height * w / h;
				nh = canvas_height;
				x = (canvas_width - nw) / 2;
				y = 0;
			}
			tag = new BufferedImage(canvas_width, canvas_height,
					BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().setColor(Color.WHITE);
			tag.getGraphics().fillRect(0, 0, canvas_width, canvas_height);
			tag.getGraphics().drawImage(
					src.getScaledInstance(nw, nh, Image.SCALE_SMOOTH), x, y,
					null); // 绘制缩小后的图
			out = new ByteArrayOutputStream(); // 输出到流
			encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(tag);
			result = out.toByteArray();
		} catch (ImageFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ins.close();
				ins = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				out.close();
				out = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			src = null;
			tag = null;
			encoder = null;
		}
		return result;
	}

	public static byte[] get(byte[] b) throws Exception {
		InputStream byin = new ByteArrayInputStream(b);
		return get(byin);
	}

	public static void main(String[] args) {
		File src = new File("/home/rmc/tmp/t.tif");
		File dst = new File("/home/rmc/tmp/ts.jpg");
		try {
			FileInputStream fis = new FileInputStream(src);
			byte[] result = Thumbnail.get(fis);
			FileOutputStream fos = new FileOutputStream(dst);
			fos.write(result);
			fis.close();
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
