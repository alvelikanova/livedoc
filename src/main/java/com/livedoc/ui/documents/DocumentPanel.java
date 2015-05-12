package com.livedoc.ui.documents;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.ChainingModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.dom4j.Document;

import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.services.DocumentService;
import com.livedoc.bl.services.DocumentTransformationsService;
import com.livedoc.ui.common.components.MarkupProviderPanel;

public class DocumentPanel extends GenericPanel<DocumentData> {

	private static final long serialVersionUID = 6585895264284178455L;

	// services
	@SpringBean
	private DocumentService documentService;
	@SpringBean
	private DocumentTransformationsService documentTransformationsService;

	// components
	private WebMarkupContainer documentContentContainer;
	private MarkupProviderPanel markupPanel;

	public DocumentPanel(String id, IModel<DocumentData> model) {
		super(id, model);
		Injector.get().inject(this);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		Label documentTitle = new Label("documentTitle",
				new PropertyModel<String>(getModel(), "title"));
		Label documentDescription = new Label("documentDescription",
				new PropertyModel<String>(getModel(), "description"));
		Label createdDate = new Label("createdDate", new DateToStringModel(
				new PropertyModel<Date>(getModel(), "createDate")));
		Label lastModDate = new Label("lastModDate", new DateToStringModel(
				new PropertyModel<Date>(getModel(), "lastModDate")));
		Label author = new Label("author", new PropertyModel<String>(
				getModel(), "createUser.name"));
		Label lastModUser = new Label("lastModUser", new PropertyModel<String>(
				getModel(), "lastModUser.name"));
		add(documentTitle, documentDescription, createdDate, lastModDate,
				author, lastModUser);

		documentContentContainer = new WebMarkupContainer("documentContent");
		add(documentContentContainer);

		markupPanel = new MarkupProviderPanel("markupPanel", new DocumentModel(
				getModel()));
		documentContentContainer.add(markupPanel);
	}

	class DateToStringModel extends ChainingModel<String> {

		private static final long serialVersionUID = -2597237819302801581L;

		private final SimpleDateFormat dateFormat = new SimpleDateFormat(
				"dd.MM.yyyy HH:mm");

		public DateToStringModel(IModel<Date> dateModel) {
			super(dateModel);
		}

		@Override
		public String getObject() {
			Date date = (Date) getChainedModel().getObject();
			return dateFormat.format(date);
		}
	}

	class DocumentModel extends ChainingModel<String> {

		private static final long serialVersionUID = -6596111106515977616L;

		public DocumentModel(IModel<DocumentData> modelObject) {
			super(modelObject);
		}

		public String getObject() {
			DocumentData docData = (DocumentData) this.getChainedModel()
					.getObject();
			if (docData != null) {
				Document document = documentService
						.buildDocument(getModelObject());
				String html = documentTransformationsService
						.transformXMLToString(document);
				return html;
			}
			return null;
		}

	}
}
