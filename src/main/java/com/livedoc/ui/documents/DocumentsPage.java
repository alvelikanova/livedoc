package com.livedoc.ui.documents;

import com.livedoc.bl.domain.entities.Project;
import com.livedoc.ui.pages.MasterPage;

public class DocumentsPage extends MasterPage {

	private static final long serialVersionUID = 1573553502836695372L;

	// models
	private Project project;

	public DocumentsPage(Project project) {
		super();
		this.project = project;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
	}
}
