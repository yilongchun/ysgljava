package com.vieking.resource;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class test {

	public static void main(String[] args) {
		File localFile = new File("F:\\icon.jpg");
		try {
			HttpURLConnection urlConnection = (HttpURLConnection) (new URL(
					"http://192.168.2.18:8080/bxl/resource/rest/file/uploadFile/402882925bfa38c8015bfa5762380004/TX/icon.jpg"))
					.openConnection();
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("PUT");
			OutputStream urlOutputStream = urlConnection.getOutputStream();
			FileInputStream fileInputStream = new FileInputStream(localFile);
			IOUtils.copy(fileInputStream, urlOutputStream);
			fileInputStream.close();
			urlOutputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

//		String str = "http://localhost:8080/bxl/resource/rest/file/upload?userId=402882925bfa38c8015bfa5762380004&type=TX&fileName=icon.jpg";
//		File localFile = new File("F:\\icon.jpg");
//		String filePath = "F:\\icon.jpg";
//		String fileName = "icon.jpg";
//		try {
//			URL url = new URL(str);
//			HttpURLConnection connection = (HttpURLConnection) url
//					.openConnection();
//			connection.setDoInput(true);
//			connection.setDoOutput(true);
//			connection.setRequestMethod("POST");
////			connection.addRequestProperty("FileName", fileName);
//			connection.setRequestProperty("content-type", "multipart/form-data");
//			BufferedOutputStream out = new BufferedOutputStream(
//					connection.getOutputStream());
//
//			// 读取文件上传到服务器
//			File file = new File(filePath);
//			FileInputStream fileInputStream = new FileInputStream(file);
//			byte[] bytes = new byte[1024];
//			int numReadByte = 0;
//			while ((numReadByte = fileInputStream.read(bytes, 0, 1024)) > 0) {
//				out.write(bytes, 0, numReadByte);
//			}
//
//			out.flush();
//			fileInputStream.close();
//			// 读取URLConnection的响应
//			DataInputStream in = new DataInputStream(
//					connection.getInputStream());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}
}
