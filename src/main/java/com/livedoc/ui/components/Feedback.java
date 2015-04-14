package com.livedoc.ui.components;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class Feedback extends FeedbackPanel {

	private static final long serialVersionUID = 6779868877044256597L;
	private static final String plainCSS = "alert ";
	private static final String infoCSS = "alert-info";
	private static final String warnCSS = "alert-warning";
	private static final String successCSS = "alert-success";
	private static final String errorCSS = "alert-danger";

	public Feedback(String id) {
		super(id);
	}

	public Feedback(String id, IFeedbackMessageFilter filter) {
		super(id, filter);
	}

	@Override
	protected String getCSSClass(FeedbackMessage message) {
		String css;
		switch (message.getLevel()) {
		case FeedbackMessage.SUCCESS:
			css = plainCSS + successCSS;
			break;
		case FeedbackMessage.INFO:
			css = plainCSS + infoCSS;
			break;
		case FeedbackMessage.WARNING:
			css = plainCSS + warnCSS;
			break;
		case FeedbackMessage.ERROR:
			css = plainCSS + errorCSS;
			break;
		default:
			css = plainCSS;
		}

		return css;
	}
}