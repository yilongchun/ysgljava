package com.vieking.sys.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.vieking.file.dao.DocInfoDao;
import com.vieking.file.model.DocInfo;
import com.vieking.sys.exception.DaoException;
import com.vieking.sys.exception.ImageConstants;
import com.vieking.sys.service.FileManager;

@Name("imageUtils")
@AutoCreate
@Scope(ScopeType.EVENT)
public class ImageSeamUtil {

	@In(value = "sys.dao.docInfo", create = true)
	private DocInfoDao docInfoDao;

	@In(value = "fileManager")
	private FileManager fileManager;

	@Logger
	protected Log log;

	public static final int J2D_ROTATE_90 = 90;
	public static final int J2D_ROTATE_180 = 180;
	public static final int J2D_ROTATE_270 = 270;

	public BufferedImage getImage(DocInfo di) throws DaoException {
		BufferedImage bsrc = null;
		try {
			// 获取图片
			bsrc = FileUtils.bitmapToImage(di.getFileDir(), di.getFileExt());
			return bsrc;
		} catch (IOException e1) {
			throw new DaoException("图片获取错误，IO读写异常！", "文件管理", "---");
		}
	}

	public DocInfo getDocInfo(String docId) throws DaoException {
		if (StringUtils.isEmpty(docId)) {
			return null;
		}
		DocInfo di = docInfoDao.eo(docId);
		if (di == null) {
			return null;
		}
		String fn = "/" + di.getUrlName() + "/" + di.getOriginalName();
		String absPath = fileManager.getFileAbsolutePath(fn);
		di.setFileDir(absPath);
		return di;
	}

	public void rotateImage(String docId, int degree) throws DaoException {
		if (degree != J2D_ROTATE_90 && degree != J2D_ROTATE_180
				&& degree != J2D_ROTATE_270) {
			return;
		}
		DocInfo di = getDocInfo(docId);
		if (di == null)
			return;
		BufferedImage bi = getImage(di);
		// 旋转图片
		BufferedImage dest = ImageUtil.rotateImage(bi, degree);
		try {
			// 保存文件
			FileUtils.imageToBitmap(dest, di.getFileDir(), di.getFileExt());
			if (ImageConstants.JPG.equals(di.getFileExt())) {
				di.setProcessed(false);
				docInfoDao.save(di);
			}
			dest = null;
		} catch (IOException ex) {
			throw new DaoException("图片文件生成失败，IO读写异常！", "文件管理", "---");
		}

	}
}
