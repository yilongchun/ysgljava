package com.vieking.resource;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

public class test {

	public static void executeMultiPartRequest(String urlString, File file,
			String fileName, String fileDescription) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(urlString);
		try {
			// Set various attributes
			MultipartEntity multiPartEntity = new MultipartEntity();
			multiPartEntity.addPart("fileDescription", new StringBody(
					fileDescription != null ? fileDescription : ""));
			multiPartEntity.addPart("fileName", new StringBody(
					fileName != null ? fileName : file.getName()));

			FileBody fileBody = new FileBody(file, "application/octect-stream");
			// Prepare payload
			multiPartEntity.addPart("attachment", fileBody);

			// Set to request body
			postRequest.setEntity(multiPartEntity);

			// Send request
			HttpResponse response = client.execute(postRequest);

			// Verify response if any
			if (response != null) {
				System.out.println(response.getStatusLine().getStatusCode());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {

		// File file = new File("F:\\1.png") ;
		// Upload the file
		// try {
		// executeMultiPartRequest("http://localhost:8080/bxl/resource/rest/file/uploadmultiple",
		// file, file.getName(), "File Uploaded :: Tulips.jpg") ;
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// upload();

		// System.out.println("upload start");
		// File localFile = new File("F:\\1.png");
		// try {
		// HttpURLConnection urlConnection = (HttpURLConnection) (new URL(
		// "http://localhost:8080/bxl/resource/rest/file/putBaoLiao/402880875ce8c7fa015cee90ea3300b5/123123/TP/1505059578358.jpg/XWLX20001"))
		// .openConnection();
		// urlConnection.setDoInput(true);
		// urlConnection.setDoOutput(true);
		// urlConnection.setRequestMethod("PUT");
		// OutputStream urlOutputStream = urlConnection.getOutputStream();
		// FileInputStream fileInputStream = new FileInputStream(localFile);
		// IOUtils.copy(fileInputStream, urlOutputStream);
		// fileInputStream.close();
		// urlOutputStream.close();
		// System.out.println("upload end");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		String str = "http://localhost:8080/bxl/resource/rest/file/setBaoLiao?userId=402880875ce8c7fa015cee90ea3300b5&type=TP&fileName=test.jpg&bllx=XWLX20002&info=123123123";
		File localFile = new File("F:\\1.png");
		String filePath = "F:\\1.png";
		String fileName = "1.png";
		try {
			URL url = new URL(str);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			// connection.addRequestProperty("FileName", fileName);
			connection
					.setRequestProperty("content-type", "multipart/form-data");
			BufferedOutputStream out = new BufferedOutputStream(
					connection.getOutputStream());

			// 读取文件上传到服务器
			File file = new File(filePath);
			FileInputStream fileInputStream = new FileInputStream(file);
			byte[] bytes = new byte[1024];
			int numReadByte = 0;
			while ((numReadByte = fileInputStream.read(bytes, 0, 1024)) > 0) {
				out.write(bytes, 0, numReadByte);
			}

			out.flush();
			fileInputStream.close();
			// 读取URLConnection的响应
			DataInputStream in = new DataInputStream(
					connection.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void upload() {
		String filepath = "F:\\1.png";
		String urlStr = "http://139.170.150.181:8090/bxl/resource/rest/file/setBaoLiao?userId=402880875ce8c7fa015cee90ea3300b5&type=TP&fileName=test.jpg&bllx=XWLX20002&info=123123123";
		Map<String, String> textMap = new HashMap<String, String>();
		textMap.put("name", "testname");
		Map<String, String> fileMap = new HashMap<String, String>();
		fileMap.put("file", filepath);
		String ret = formUpload(urlStr, textMap, fileMap);
		System.out.println(ret);
	}

	/**
	 * 上传图片
	 * 
	 * @param urlStr
	 * @param textMap
	 * @param fileMap
	 * @return
	 */
	public static String formUpload(String urlStr, Map<String, String> textMap,
			Map<String, String> fileMap) {
		String res = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator iter = textMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}

			// file
			if (fileMap != null) {
				Iterator iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
					String contentType = new MimetypesFileTypeMap()
							.getContentType(file);
					if (filename.endsWith(".png")) {
						contentType = "image/png";
					}
					if (contentType == null || contentType.equals("")) {
						contentType = "application/octet-stream";
					}

					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

					out.write(strBuf.toString().getBytes());

					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			System.out.println("发送POST请求出错。" + urlStr);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}
}
