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
package com.vieking.sys.doc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.vieking.file.model.DocInfo;
import com.vieking.sys.exception.ImageConstants;
import com.vieking.sys.exception.ImageDimension;
import com.vieking.sys.service.FileManager;
import com.vieking.sys.util.FileUtils;

@Name("imageLoader")
@Scope(ScopeType.EVENT)
@AutoCreate
public class ImageLoader implements Serializable {

	private static final long serialVersionUID = -1572789608594870285L;

	@In("fileManager")
	private FileManager fileManager;
	

	@Logger
	protected Log log;

	public byte[] loadFile(String fn) {
		File imageResource = fileManager.getFileByPath(fn);
		if ((imageResource != null) && (imageResource.exists())) {

			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] toWrite = new byte[ImageConstants.DEFAULT_BUFFER_SIZE];
				FileInputStream in = new FileInputStream(imageResource);
				try {

					while (in.read(toWrite) != -1) {

						out.write(toWrite);
					}
				} finally {
					in.close();
				}

				return out.toByteArray();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}
		return null;
	}

	public List<DocInfo> loadDocInfo() {
		return null;
	}

	public Object loadImage(DocInfo di, int size) {
		if (di.isJpgImage()) {
			String fn = di.getUrlName() + "/" + di.getOriginalName();
			fn = FileUtils.transformPath(fn, ImageDimension.getInstance(size)
					.getFilePostfix());
			byte[] out = loadFile(fn);
			if (out != null) {
				return out;
			} else {
				return "/img/page.png";
			}
		}
		return "/img/page.png";
	}
	
	
	public Object loadSign(String fn) {
		byte[] out = loadFile(fn);
		if (out != null) {
			return out;
		} else {
			return "/img/page.png";
		}

	}

	public Object loadImage(DocInfo di) {
		return "/img/page.png";
	}

	@SuppressWarnings("unused")
	private String excludeFilePrefix(String path) {
		final int begin = path.lastIndexOf("_");
		final int end = path.lastIndexOf(ImageConstants.DOT);
		return path.substring(begin, end);
	}
}
