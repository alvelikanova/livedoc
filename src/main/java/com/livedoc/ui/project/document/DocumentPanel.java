package com.livedoc.ui.project.document;

import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.ChainingModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.dom4j.Document;

import com.livedoc.bl.domain.entities.Comment;
import com.livedoc.bl.domain.entities.DocumentData;
import com.livedoc.bl.services.CommentService;
import com.livedoc.bl.services.DocumentService;
import com.livedoc.bl.services.DocumentTransformationsService;
import com.livedoc.ui.common.components.MarkupProviderPanel;
import com.livedoc.ui.common.components.table.TablePagingNavigator;
import com.livedoc.ui.project.DocumentsCatalog;
import com.livedoc.ui.project.document.comments.CommentsPanel;

public class DocumentPanel extends GenericPanel<DocumentsCatalog> {

	private static final long serialVersionUID = 6585895264284178455L;

	// services
	@SpringBean
	private DocumentService documentService;
	@SpringBean
	private CommentService commentService;
	@SpringBean
	private DocumentTransformationsService documentTransformationsService;

	// components
	private DocumentInformationPanel documentInformationPanel;
	private PageableListView<Document> chaptersList;
	private TablePagingNavigator pager;

	public DocumentPanel(String id, IModel<DocumentsCatalog> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		// displays general document information (title, create author, etc.)
		documentInformationPanel = new DocumentInformationPanel("information",
				new PropertyModel<DocumentData>(getModel(), "selectedDocument"));
		add(documentInformationPanel);

		// document content
		chaptersList = new PageableListView<Document>("chaptersList",
				new PropertyModel<List<Document>>(getModel(), "chapters"), 1) {

			private static final long serialVersionUID = 8311836914149274100L;

			@Override
			protected void populateItem(final ListItem<Document> item) {
				HTMLModel htmlModel = new HTMLModel(item.getModel());
				MarkupProviderPanel markupPanel = new MarkupProviderPanel(
						"markupPanel", htmlModel);
				item.add(markupPanel);
				IModel<List<Comment>> commentsModel = new AbstractReadOnlyModel<List<Comment>>() {

					private static final long serialVersionUID = 4383734787420774625L;

					@Override
					public List<Comment> getObject() {
						DocumentsCatalog catalog = DocumentPanel.this
								.getModelObject();
						return commentService
								.getCommentsByDocumentAndChapterOrder(
										catalog.getSelectedDocument(),
										item.getIndex());
					}
				};
				CommentsPanel comments = new CommentsPanel("comments",
						commentsModel) {

					private static final long serialVersionUID = 7133016144471743378L;

					@Override
					protected String getCurrentChapterId() {
						DocumentsCatalog catalog = DocumentPanel.this
								.getModelObject();
						return documentService.getChapterId(catalog.getSelectedDocument(), item.getIndex());
					}

				};
				item.add(comments);
			}
		};
		pager = new TablePagingNavigator("navigation", chaptersList);
		add(pager);
		add(chaptersList);
	}

	public void resetPagination() {
		chaptersList.setCurrentPage(0);
	}

	class HTMLModel extends ChainingModel<String> {

		private static final long serialVersionUID = -6596111106515977616L;

		public HTMLModel(IModel<Document> modelObject) {
			super(modelObject);
		}

		public String getObject() {
			Document document = (Document) this.getChainedModel().getObject();
			if (document != null) {
				String html = documentTransformationsService
						.transformXMLToString(document);
				return html;
			}
			return null;
		}
	}
}
