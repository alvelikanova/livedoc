package com.livedoc.ui.project.document.comments;

import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.context.SecurityContextHolder;

import com.livedoc.bl.domain.entities.Comment;
import com.livedoc.bl.services.CommentService;
import com.livedoc.bl.services.UserService;
import com.livedoc.security.SecurityUserDetails;
import com.livedoc.ui.common.components.DateToStringModel;

public abstract class CommentsPanel extends GenericPanel<List<Comment>> {

	private static final long serialVersionUID = 7222759364119455586L;

	// services
	@SpringBean
	private CommentService commentsService;
	@SpringBean
	private UserService userService;

	// components
	private ListView<Comment> comments;
	private WebMarkupContainer commentsListContainer;
	private Form<Void> form;
	private TextArea<String> commentText;

	// models
	private Comment commentToSubmit;

	public CommentsPanel(String id, IModel<List<Comment>> model) {
		super(id, model);
		commentToSubmit = new Comment();
		Injector.get().inject(this);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		commentsListContainer = new WebMarkupContainer("commentsListContainer");
		commentsListContainer.setOutputMarkupId(true);
		// previously saved comments
		comments = new ListView<Comment>("comments", getModel()) {

			private static final long serialVersionUID = -6396116022344554877L;

			@Override
			protected void populateItem(ListItem<Comment> item) {
				Label author = new Label("author", new PropertyModel<String>(
						item.getModel(), "author.name"));
				Label submitDate = new Label("submitDate",
						new DateToStringModel(new PropertyModel<Date>(
								item.getModel(), "submitDate")));
				Label comment = new Label("comment", new PropertyModel<String>(
						item.getModel(), "comment"));
				item.add(author, submitDate, comment);
			}
		};
		commentsListContainer.add(comments);
		add(commentsListContainer);

		form = new Form<Void>("form");
		commentText = new TextArea<String>("commentText",
				new PropertyModel<String>(this, "commentToSubmit.comment"));
		AjaxButton submitComment = new AjaxButton("submitComment", form) {
			private static final long serialVersionUID = 5750571221685652119L;

			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextHolder
						.getContext().getAuthentication().getPrincipal();
				commentToSubmit.setAuthor(userDetails.getUser());
				commentToSubmit.setSubmitDate(new Date());
				commentsService.saveComment(commentToSubmit,
						getCurrentChapterId());
				target.add(commentsListContainer);
				commentText.clearInput();
				commentToSubmit = new Comment();
			}
		};
		form.add(commentText, submitComment);
		add(form);
	}

	public Comment getCommentToSubmit() {
		return commentToSubmit;
	}

	public void setCommentToSubmit(Comment commentToSubmit) {
		this.commentToSubmit = commentToSubmit;
	}

	protected abstract String getCurrentChapterId();
}
