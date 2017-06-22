/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package com.vieking.sys.service;

/**
 * Class encapsulated all functionality, related to working with the file system.
 *
 * @author Andrey Markhel
 */
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.document.ByteArrayDocumentData;
import org.jboss.seam.document.DocumentData;
import org.jboss.seam.document.DocumentStore;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.log.Log;

import com.vieking.sys.exception.Const;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.exception.ImageConstants;
import com.vieking.sys.exception.ImageDimension;
import com.vieking.sys.exception.KeException;
import com.vieking.sys.util.CalendarUtil;
import com.vieking.sys.util.Config;
import com.vieking.sys.util.FileUtils;

@Name("fileManager")
@Scope(ScopeType.APPLICATION)
@Startup(depends = "app.config")
public class FileManager {

	@Logger
	protected Log log;

	private File uploadRoot;

	/** 文档上传根路径 */
	private String uploadRootPath;

	/** 模板根路径 */
	private String templateRootPath;

	@SuppressWarnings("unused")
	private Map<String, String> pathMap = new HashMap<String, String>();

	@In(value = "app.config")
	private Config config;

	public static FileManager instance() {
		return (FileManager) Component.getInstance("fileManager");
	}

	@Create
	@Observer(create = false, value = Const.CONFIGCHANGED)
	public void create() throws KeException {
		uploadRootPath = config.get(Const.KEY_UPLOADFILE_ROOT);
		uploadRoot = new File(uploadRootPath);
		templateRootPath = config.get(Const.KEY_TEMPLATE_ROOT);
	}

	public byte[] loadFile(String fn) {
		File imageResource = getFileByPath(fn);
		if ((imageResource != null) && (imageResource.exists())) {

			try {
				ByteArrayOutputStream out = null;
				FileInputStream in = new FileInputStream(imageResource);
				in.close();
				out = FileUtils.getFileOutputStream(imageResource);
				return out.toByteArray();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}
		return null;
	}

	public void newFileFromTemplate(String 目标路径, String 目标文件名, String 模板名称)
			throws DaoException {
		writeFile(目标路径 + "/" + 目标文件名, templateRootPath + "/" + 模板名称, true, true);
	}

	public void addDoc(String path, String fileName, String tempFilePath,
			boolean isJpg) throws DaoException {

		writeFile(path + "/" + fileName, tempFilePath, true, false);
		// 处理jpg图片生成缩率图
		// if (isJpg)
		// {
		// addJpgImage(path, fileName, tempFilePath);
		// }
		// else
		// {
		// writeFile(path + "/" + fileName, tempFilePath, true, false);
		// }
	}

	public void addJpgImage(String path, String fileName, String tempFilePath)
			throws DaoException {
		for (ImageDimension d : ImageDimension.values()) {
			writeJpgImage(path + "/" + fileName, tempFilePath,
					d.getFilePostfix(), d.getX(), true);
		}
	}

	public void createPDF(String path, String fileName, String renderFile)
			throws DaoException {
		createPDF(renderFile);
		byte[] pdfByte = createPDF(renderFile);
		FileUtils.writeFile(pdfByte, path + "/" + fileName);

	}

	public byte[] createPDF(String renderFile) {
		// String DATA_STORE =
		// "org.jboss.seam.document.documentStore.dataStore";
		// EmptyFacesContext emptyFacesContext = new EmptyFacesContext();
		// Context conversation = Contexts.getConversationContext();
		byte[] bytes = null;
		try {
			Renderer.instance().render(renderFile);
			DocumentStore doc = DocumentStore.instance();

			if (doc != null) {

				DocumentData data = doc.getDocumentData(String.valueOf(Integer
						.valueOf(doc.newId()) - 1));
				ByteArrayDocumentData byteData = null;
				if (data instanceof ByteArrayDocumentData) {
					byteData = (ByteArrayDocumentData) data;
				} else {
					throw new Exception(
							"Couldnt get the bytes from the pdf document, unkown class "
									+ data.getClass().getName());
				}
				bytes = byteData.getData();
			}

		} catch (Exception ex) {
			log.error("产生pdf文档内容发生错误： #0", ex.getMessage());
			ex.printStackTrace();
		}
		return bytes;
	}

	public File getFileByPath(String path) {
		if (this.uploadRoot != null) {
			File result = new File(this.uploadRoot, path);
			log.debug(path);
			try {
				final String resultCanonicalPath = result.getCanonicalPath();
				// windows 系统文件不区分大小写
				if (!resultCanonicalPath.toLowerCase().startsWith(
						this.uploadRootPath.toLowerCase())) {
					// result = null;
				}
				return result;
			} catch (IOException e) {
				result = null;
			}
			return result;
		}
		return null;
	}

	/**
	 * This method used to get reference to the file with the absolute path
	 * 
	 * @param path
	 *            - absolute path of file
	 * @return File reference
	 */
	public File getFileByAbsolutePath(String path) {
		return new File(uploadRootPath + path);
	}

	/**
	 * This method used to get reference to the file with the absolute path
	 * 
	 * @param path
	 *            - absolute path of file
	 * @return File reference
	 */
	public String getFileAbsolutePath(String path) {
		return uploadRootPath + path;
	}

	/**
	 * This utility method used to determine if the directory with specified
	 * relative path exist
	 * 
	 * @param path
	 *            - absolute path of directory
	 * @return File reference
	 */
	public boolean isDirectoryPresent(String path) {
		final File file = getFileByPath(path);
		return file.exists() && file.isDirectory();
	}

	/**
	 * This utility method used to determine if the file with specified relative
	 * path exist
	 * 
	 * @param path
	 *            - absolute path of file
	 * @return File reference
	 */
	public boolean isFilePresent(String path) {
		final File file = getFileByPath(path);
		return file.exists();
	}

	private void writeFile(String newFileName, String fileName,
			boolean includeUploadRoot, boolean mkdirs) throws DaoException {

		// 确定目标文件名称
		String dest = getDestFileName(newFileName, includeUploadRoot, mkdirs);
		// 拷贝临时文件到目标文件
		FileUtils.copyFile(dest, fileName);

	}

	/**
	 * 
	 * @param fn
	 *            相对路径文件名
	 * @param includeUploadRoot
	 *            是否包含上传文件根路径
	 * @return 返回目标文件名
	 */
	public String getDestFileName(String fn, boolean includeUploadRoot,
			boolean mkdirs) {
		String dest = includeUploadRoot ? this.uploadRootPath + "/" + fn : fn;
		
		try {
			dest = config.get("uploadPath") + fn;
		} catch (KeException e) {
			e.printStackTrace();
		}
		
		
		log.debug("目标文件名{0}", dest);
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

	public String getDiskFileName(String fn) {
		return getDestFileName(fn, true, false);
	}

	private boolean writeJpgImage(String newFileName, String fileName,
			String pattern, int size, boolean includeUploadRoot)
			throws DaoException {

		BufferedImage bsrc = null;
		try {
			// 获取图片
			bsrc = FileUtils.bitmapToImage(fileName, ImageConstants.JPG);
		} catch (IOException e1) {
			throw new DaoException("图片获取错误，IO读写异常！", "文件管理", "---");
		}
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
		String dest = FileUtils.getDestFileName(
				FileUtils.transformPath(newFileName, pattern), uploadRootPath,
				true);
		try {
			// 保存文件
			FileUtils.imageToBitmap(bdest, dest, ImageConstants.JPG);
			bdest = null;
		} catch (IOException ex) {
			throw new DaoException("图片文件生成失败，IO读写异常！", "文件管理", "---");
		}
		bsrc = null;
		return true;
	}

	public String saveSignature(byte[] image) throws DaoException {

		String path = "sign/" + CalendarUtil.getTodayYymmdd() + "/"
				+ UUID.randomUUID().toString() + ".png";
		String absPath = uploadRootPath + "/" + path;
		FileUtils.writeFile(image, absPath);
		return path;
	}

	public Long getFileSize(String path) {
		String absPath = uploadRootPath + "/" + path;
		File file = new File(absPath);
		boolean exists = (file).exists();
		if (exists) {
			return file.length();
		} else {
			return 0l;
		}
	}

	public Long writeFile(byte[] data, String path, Long position, Long fileSize)
			throws DaoException, IOException {
		String absPath = uploadRootPath + "/" + path;
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
			if (result.equals(fileSize)) {
				// 上传时处理缩略图
				// scaleImage(path,absPath);
			}
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
			throw new DaoException("文件上传文件异常！", "文件管理", "---");
		}
		return result;
	}

	/**
	 * 
	 * @param path
	 *            目的文件相对路径 path
	 * @param srcPath
	 *            原图文件绝对路径
	 * @throws DaoException
	 */
	public void scaleImage(String path, String srcPath) throws DaoException {
		for (ImageDimension d : ImageDimension.values()) {
			if ("".equals(d.getFilePostfix()))
				continue;
			writeJpgImage(path, srcPath, d.getFilePostfix(), d.getX(), false);
		}
	}

	public String getUploadRootPath() {
		return uploadRootPath;
	}
}
