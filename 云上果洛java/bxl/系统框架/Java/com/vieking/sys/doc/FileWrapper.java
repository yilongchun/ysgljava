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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;

import com.vieking.file.model.DocInfo;
import com.vieking.sys.exception.Const;

@Name("sys.upload.fw")
@Scope(ScopeType.PAGE)
@AutoCreate
public class FileWrapper implements Serializable {

	private static final long serialVersionUID = -1767281809514660171L;

	private boolean complete = false;

	private String fwId;

	private List<DocInfo> docs = new ArrayList<DocInfo>();

	private List<ErrorImage> errorFiles = new ArrayList<ErrorImage>();

	class ErrorImage {
		private DocInfo doc;
		private String errorDescription;

		ErrorImage(DocInfo i, String description) {
			doc = i;
			errorDescription = description;
		}

		public DocInfo getImage() {
			return doc;
		}

		public void setImage(DocInfo image) {
			this.doc = image;
		}

		public String getErrorDescription() {
			return errorDescription;
		}

		public void setErrorDescription(String errorDescription) {
			this.errorDescription = errorDescription;
		}
	}

	public int getSize() {
		return getDocs().size();
	}

	public List<DocInfo> getDocs() {
		return docs;
	}

	public void setDocs(List<DocInfo> docs) {
		this.docs = docs;

	}

	@Observer(Const.IMAGE_DRAGGED_EVENT)
	public void removeImage(DocInfo doc, String pathOld) {
		docs.remove(doc);
	}

	@Observer(Const.CLEAR_FILE_UPLOAD_EVENT)
	public void clear() {
		docs.clear();
		errorFiles.clear();
		complete = false;
	}

	public void onFileUploadError(DocInfo image, String error) {
		ErrorImage e = new ErrorImage(image, error);
		errorFiles.add(e);
	}

	public DocInfo getErrorImage(ErrorImage e) {
		return e.getImage();
	}

	public String getErrorDescription(ErrorImage e) {
		return e.getErrorDescription();
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public List<ErrorImage> getErrorFiles() {
		return errorFiles;
	}

	public void setErrorFiles(List<ErrorImage> errorFiles) {
		this.errorFiles = errorFiles;
	}

	public String getFwId() {
		return fwId;
	}

	public void setFwId(String fwId) {
		this.fwId = fwId;
	}
}
