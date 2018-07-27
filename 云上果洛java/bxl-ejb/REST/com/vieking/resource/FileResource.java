package com.vieking.resource;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jboss.resteasy.annotations.GZIP;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.vieking.resource.bean.ReturnMessage;
import com.vieking.resource.dao.FileDao;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.exception.ReConst;
import com.vieking.sys.util.FileUtils;

@Path("file")
@Name("fileResource")
public class FileResource implements ReConst {

	@Logger
	private Log log;

	@In(value = "fileDao")
	private FileDao fileDao;

	@PUT
	@Path(value = "uploadFile/{fileName}")
	@GZIP
	@Produces("application/json;charset=UTF-8")
	public ReturnMessage uploadFile(@PathParam("fileName") String fileName,
			@Context HttpServletRequest request) {
		InputStream is = null;
		try {
			is = request.getInputStream();
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			byte[] buff = new byte[100];
			int rc = 0;
			while ((rc = is.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
			byte[] in2b = swapStream.toByteArray();
			try {
				FileUtils.writeFile(in2b, "d:/" + fileName);

			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ReturnMessage(OS_FAILED, "上传成功");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ReturnMessage(OS_FAILED, "上传成功");
		}
		return new ReturnMessage(OS_OK, "上传成功");
	}

	@POST
	@Path("/uploadmultiple")
	@GZIP
	@Produces("application/json;charset=UTF-8")
	@Consumes("multipart/form-data")
	public String uploadMultipleFile(@Context HttpServletRequest request)
			throws Exception {
		String upload_file_path = request.getSession().getServletContext()
				.getRealPath("/")
				+ "upload/";
		System.out.println(upload_file_path);
		// 设置工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置文件存储位置
		factory.setRepository(Paths.get(upload_file_path).toFile());
		// 设置大小，如果文件小于设置大小的话，放入内存中，如果大于的话则放入磁盘中,单位是byte
		factory.setSizeThreshold(1024 * 1024);

		ServletFileUpload upload = new ServletFileUpload(factory);
		// 这里就是中文文件名处理的代码，其实只有一行
		upload.setHeaderEncoding("utf-8");
		String fileName = null;
		List<FileItem> list = upload.parseRequest(request);
		for (FileItem item : list) {
			if (item.isFormField()) {
				String name = item.getFieldName();
				String value = item.getString("utf-8");
				System.out.println(name);
				System.out.println(value);
				request.setAttribute(name, value);
			} else {
				String name = item.getFieldName();
				String value = item.getName();
				System.out.println(name);
				System.out.println(value);

				fileName = Paths.get(value).getFileName().toString();
				request.setAttribute(name, fileName);
				if (!Paths.get(upload_file_path).toFile().exists()) {
					Paths.get(upload_file_path).toFile().mkdirs();
				}
				// 写文件到path目录，文件名问filename
				item.write(new File(upload_file_path, fileName));
			}
		}
		return "12321321";
	}

	// 上传附件
	@PUT
	@Path(value = "uploadFile/{userId}/{type}/{fileName}")
	@GZIP
	@Produces("application/json;charset=UTF-8")
	public ReturnMessage uploadFile(@PathParam("userId") String userId,
			@PathParam("type") String type,
			@PathParam("fileName") String fileName,
			@Context HttpServletRequest request) {
		InputStream is = null;
		String url = "";
		try {
			is = request.getInputStream();
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			byte[] buff = new byte[100];
			int rc = 0;
			while ((rc = is.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
			byte[] in2b = swapStream.toByteArray();
			url = fileDao.saveContactImg(userId, type, fileName, in2b);
			Thread.sleep(5000);
		} catch (IOException e) {
			e.printStackTrace();
			return new ReturnMessage(OS_FAILED, "上传失败");
		} catch (Exception e) {
			return new ReturnMessage(OS_FAILED, "上传失败");
		}

		log.debug("android upload image success url:" + url);

		return new ReturnMessage(OS_OK, url);
	}

	@POST
	@Path("/upload")
	@Consumes("multipart/form-data")
	@Produces("application/json;charset=UTF-8")
	@GZIP
	public ReturnMessage upload(@Context HttpServletRequest request) {
		String userId = request.getParameter("userId");
		String type = request.getParameter("type");
		String fileName = request.getParameter("fileName");
		String url = "";
		try {

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

						// DataOutputStream dOutputStream = new
						// DataOutputStream(
						// new FileOutputStream(new File("d:/test.jpg")));
						// dOutputStream.write(bytes, temPosition, end
						// - temPosition - 2);
						url = fileDao.saveContactImg(userId, type, fileName,
								temPosition, end - temPosition - 2, bytes);
						// dOutputStream.close();
						flag = true;
					}
				}
			}

			System.out.println(url);
			Thread.sleep(5000);
		} catch (EOFException e) {
			e.printStackTrace();
			return new ReturnMessage(OS_FAILED, "上传失败");
		} catch (IOException e) {
			e.printStackTrace();
			return new ReturnMessage(OS_FAILED, "上传失败");
		} catch (Exception e) {
			e.printStackTrace();
			return new ReturnMessage(OS_FAILED, "上传失败");
		}

		return new ReturnMessage(OS_OK, url);
	}

	// 上传附件
	@PUT
	@Path(value = "putBaoLiao/{userId}/{info}/{type}/{fileName}/{bllx}")
	@GZIP
	@Produces("application/json;charset=UTF-8")
	public ReturnMessage putBaoLiao(@PathParam("userId") String userId,
			@PathParam("info") String info, @PathParam("type") String type,
			@PathParam("fileName") String fileName,
			@PathParam("bllx") String bllx, @Context HttpServletRequest request) {

		// String userId = request.getParameter("userId");//用户ID
		// String info = request.getParameter("info");//报料信息
		// String type = request.getParameter("type");//附件类型
		// String fileName = request.getParameter("fileName");//附件名称
		// String bllx = request.getParameter("bllx");//栏目
		InputStream is = null;
		String url = "";
		try {
			is = request.getInputStream();
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			byte[] buff = new byte[100];
			int rc = 0;
			while ((rc = is.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
//			byte[] in2b = swapStream.toByteArray();
			// url = fileDao.saveContactImg(userId, type, fileName, in2b);
			// fileDao.saveBaoLiao(userId, type, bllx, info, fileName, 0,
			// in2b.length, in2b);
			// Thread.sleep(5000);
		} catch (IOException e) {
			e.printStackTrace();
			return new ReturnMessage(OS_FAILED, "上传失败");
		} catch (Exception e) {
			return new ReturnMessage(OS_FAILED, "上传失败");
		}

		log.debug("android upload image success url:" + url);

		return new ReturnMessage(OS_OK, url);
	}

	/** 报料上传 */

	@POST
	@Path("/setBaoLiao")
	@Consumes("multipart/form-data")
	@Produces("application/json;charset=UTF-8")
	@GZIP
	public ReturnMessage setBaoLiao(@Context HttpServletRequest request) {
		String userId = request.getParameter("userId");// 用户ID
		String info = request.getParameter("info");// 报料信息
		String type = request.getParameter("type");// 附件类型
		String fileName = request.getParameter("fileName");// 附件名称
		String bllx = request.getParameter("bllx");// 栏目
		String biaoti = request.getParameter("biaoti");// 报料id
		String blid = request.getParameter("blid");// 报料id
		String id = "";
		try {

			// byte[] byt = new byte[1024 * 1024];
			// InputStream is = request.getInputStream();
			//
			// int nRead = 1;
			// int nTotalRead = 0;
			// while (nRead > 0) {
			// nRead = is.read(byt, nTotalRead, byt.length - nTotalRead);
			// if (nRead > 0)
			// nTotalRead = nTotalRead + nRead;
			// }
			// String strs = new String(byt, 0, nTotalRead, "UTF-8");
			// System.out.println("Str:" + strs);
			// log.debug(strs);

			// 1.判断当前request消息实体的总长度
			int totalBytes = request.getContentLength();
			// 2.在消息头类型中找出分解符,例如:boundary=----WebKitFormBoundaryeEYAk4vG4tRKAlB6
			String contentType = request.getContentType();

			int position = contentType.indexOf("boundary=");

			String startBoundary = "--"
					+ contentType.substring(position + "boundary=".length());

			String endBoundary = startBoundary + "--";

			log.debug("s--", startBoundary);
			log.debug("--s", endBoundary);
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
					// such as:Content-Disposition: form-data;
					// name="fileName";
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

						// DataOutputStream dOutputStream = new
						// DataOutputStream(
						// new FileOutputStream(new File("d:/test.jpg")));
						// dOutputStream.write(bytes, temPosition, end
						// - temPosition - 2);
						id = fileDao.saveBaoLiao(userId, type, bllx, biaoti, info,
								fileName, blid, temPosition, end - temPosition
										- 2, bytes);
						// dOutputStream.close();
						flag = true;
					}
				}
			}
		} catch (EOFException e) {
			e.printStackTrace();
			return new ReturnMessage(OS_FAILED, "上传失败");
		} catch (IOException e) {
			e.printStackTrace();
			return new ReturnMessage(OS_FAILED, "上传失败");
		} catch (Exception e) {
			e.printStackTrace();
			return new ReturnMessage(OS_FAILED, "上传失败");
		}

		return new ReturnMessage(OS_OK, id);
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

	private static byte[] subBytes(byte[] b, int from, int end) {
		byte[] result = new byte[end - from];
		System.arraycopy(b, from, result, 0, end - from);
		return result;
	}

	public static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	@POST
	@Path("/uploads")
	@Consumes("application/json")
	@Produces("application/json")
	public ReturnMessage uploads(@Context HttpServletRequest request) {
//		String userId = request.getParameter("userId");
//		String type = request.getParameter("type");
//		String fileName = request.getParameter("fileName");
		return new ReturnMessage(OS_OK, "");
	}

	@GET
	@Path("loadFile/{docInfoId}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@GZIP
	public Response loadFile(@PathParam("docInfoId") String docInfoId) {
		ResponseBuilder response = Response.status(Status.BAD_REQUEST);
		return response.build();
		// }
		// Prepare a file object with file to return
		// File file = new File("c:/demoJpegFile.jpeg");
		//
		// ResponseBuilder response = Response.ok((Object) file);
		// response.header("Content-Disposition",
		// "attachment; filename='howtodoinjava.jpeg'");
		// return response.build();
	}

	@GET
	@Path("downLoad/{docInfoId}")
	@Produces("application/json")
	@GZIP
	public ReturnMessage downLoad(@PathParam("docInfoId") String docInfoId) {
		return new ReturnMessage(fileDao.getFileUrl(docInfoId));
	}

	@PUT
	@Path("/upLoadBean")
	@GZIP
	@Produces("application/json")
	public ReturnMessage upLoadBean(@Context HttpServletRequest request) {
//		String jtr = "";
//		try {
//			jtr = IOUtils.toString(request.getInputStream(), "UTF-8");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		JSONArray json = JSONArray.fromObject(jtr);
		// List<CarBean> persons = (List<CarBean>) JSONArray.toCollection(json,
		// CarBean.class);
		// System.out.println(persons);
		return new ReturnMessage(OS_OK, "上传成功");
	}

}
