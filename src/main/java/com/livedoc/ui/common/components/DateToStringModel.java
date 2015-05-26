package com.livedoc.ui.common.components;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.model.ChainingModel;
import org.apache.wicket.model.IModel;

public class DateToStringModel extends ChainingModel<String> {

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