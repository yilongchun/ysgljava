package com.vieking.sys.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.vieking.resource.bean.ReturnMessage;
import com.vieking.sys.exception.Const;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.util.EarPath;
import com.vieking.sys.util.FileUtils;

public class FileServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3219711510618958506L;

	private static final String confpath = EarPath.ear_home()
			+ "/safety.properties";

	private Configuration config;

	// 定义文件的上传路径
	private String uploadPath = "D:\\upload\\";
	// 限制文件的上传大小
	private int maxPostSize = 100 * 1024 * 1024;

	public FileServlet() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	@SuppressWarnings("rawtypes")
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 保存文件到服务器中
		response.setContentType("text/html; charset=UTF-8");
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(4096);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(maxPostSize);
		request.setCharacterEncoding("ISO8859-1");
		try {
			config = new PropertiesConfiguration(confpath);
			uploadPath = config.getString(Const.KEY_UPLOADFILE_ROOT);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		try {
			String doc = request.getParameter("doc").toString();
			System.out.println(doc);
			uploadPath = uploadPath + "/" + doc;
			System.out.println(uploadPath);
			List fileItems = upload.parseRequest(request);
			Iterator iter = fileItems.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (!item.isFormField()) {
					String name = new String(item.getName().getBytes(
							"iso8859-1"), "GBK");
					request.getSession().setAttribute("uploadFile", uploadPath);
					try {
						item.write(new File(uploadPath + "/" + name));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	}
	
	private static byte[] subBytes(byte[] b, int from, int end) {
		byte[] result = new byte[end - from];
		System.arraycopy(b, from, result, 0, end - from);
		return result;
	}
	
	public int locateEnd(byte[] bytes, int start, int end, String endStr) {
		byte[] endByte = endStr.getBytes();
		for (int i = start + 1; i < end; i++) {
			if (bytes[i] == endByte[0]) {
				int k = 1;
				while (k < endByte.length) {
					if (bytes[i + k] != endByte[k]) {
						break;
					}
					k++;
				}
				System.out.println(i);
				if (i == 3440488) {
					System.out.println("start");
				}
				// 返回结束符的开始位置
				if (k == endByte.length) {
					return i;
				}
			}
		}

		return 0;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		doGet(request, response);
		
		// 1.判断当前request消息实体的总长度
		int totalBytes = request.getContentLength(); 
		// 2.在消息头类型中找出分解符,例如:boundary=----WebKitFormBoundaryeEYAk4vG4tRKAlB6
		String contentType = request.getContentType();
		int position = contentType.indexOf("boundary=");

		String startBoundary = "--"
				+ contentType.substring(position + "boundary=".length());
		String endBoundary = startBoundary + "--";
		// 将request的输入流读入到bytes中
		InputStream inputStream = request.getInputStream();
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		byte[] bytes = new byte[totalBytes];
		dataInputStream.readFully(bytes);
		dataInputStream.close();
		// 将字节读入到字符流中
		BufferedReader reader = new BufferedReader(new StringReader(
				new String(bytes)));

		// 开始读取reader(分割form表单内的表单域类型:文本或者文件)

		// 记录当前的读取行对应的bytes;
		int temPosition = 0;
		boolean flag = false;
		int end = 0;
		while (true) {
			// 当读取一次文件信息后
			if (flag) {
				bytes = subBytes(bytes, end, totalBytes);
				temPosition = 0;
				reader = new BufferedReader(new StringReader(new String(
						bytes)));
			}
			// 读取一行的信息:------WebKitFormBoundary5R7esAd459uwQsd5,即:lastBoundary
			String str = reader.readLine();
			// 换行算两个字符
			temPosition += str.getBytes().length + 2;
			// endBoundary:结束
			if (str == null || str.equals(endBoundary)) {
				break;
			}
			// 表示头信息的开始(一个标签,input,select等)
			if (str.startsWith(startBoundary)) {
				// 判断当前头对应的表单域类型

				str = reader.readLine(); // 读取当前头信息的下一行:Content-Disposition行
				temPosition += str.getBytes().length + 2;

				int position1 = str.indexOf("filename="); // 判断是否是文件上传
				// such as:Content-Disposition: form-data; name="fileName";
				// filename="P50611-162907.jpg"
				if (position1 == -1) {// 表示是普通文本域上传

				} else {// position1!=-1,表示是文件上传
						// 解析当前上传的文件对应的name(input标签的name),以及fieldname:文件名
						// int position2 = str.indexOf("name=");
					// 去掉name与filename之间的"和;以及空格
					// String name = str
					// .substring(position2 + "name=".length() + 1,
					// position1 - 3);
					// // 去掉两个"
					// String filename = str.substring(
					// position1 + "filename=".length() + +1,
					// str.length() - 1);
					
					// 读取行,such as:Content-Type: image/jpeg,记录字节数,此处两次换行
					temPosition += (reader.readLine().getBytes().length + 4);
					end = this.locateEnd(bytes, temPosition, totalBytes,
							endBoundary);
					
					String path = this.getServletContext().getRealPath("");
					path = path.substring(0, path.indexOf("tmp"));
					path = path + "deploy\\bxl-ear.ear\\bxl.war\\uediter\\lang\\";
					
					
					
					long currentTime = System.nanoTime();
					String fileName = path + currentTime + ".jpg";
					
					 DataOutputStream dOutputStream = new
					 DataOutputStream(
					 new FileOutputStream(new File(fileName)));
					 dOutputStream.write(bytes, temPosition, end
					 - temPosition - 2);
					//					url = fileDao.saveContactImg(userId, type, fileName,
					//							temPosition, end - temPosition - 2, bytes);
					// dOutputStream.close();
					 
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/json; charset=utf-8");
					String jsonStr = "{\"state\":\"SUCCESS\",\"url\":\""+currentTime + ".jpg" + "\",\"title\":\"demo.jpg\",\"original\":\"demo.jpg\"}";
					PrintWriter out = null;
					try {
					    out = response.getWriter();
					    out.write(jsonStr);
					} catch (IOException e) {
					    e.printStackTrace();
					} finally {
					    if (out != null) {
					        out.close();
					    }
					    if (dOutputStream != null) {
					    	dOutputStream.close();
						}
					}
					
					flag = true;
				}
			}
		}
		
	}

	

	public void init() throws ServletException {
		// Put your code here
	}

}
