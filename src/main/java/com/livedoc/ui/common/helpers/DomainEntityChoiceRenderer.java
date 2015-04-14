package com.livedoc.ui.common.helpers;

import java.util.List;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

import com.livedoc.bl.domain.entities.BaseDomainEntity;

public abstract class DomainEntityChoiceRenderer<T extends BaseDomainEntity> implements IChoiceRenderer<T> {

	private static final long serialVersionUID = 8494998188101337948L;

	public String getIdValue(T object, int index) {
		return object.getId();
	}

	public T getObject(String id, IModel<? extends List<? extends T>> choices) {
		List<? extends T> list = choices.getObject();
		for (T object : list)
		{
			if (id.equals(object.getId()))
			{
				return object;
			}
		}
		return null;
	}

}
