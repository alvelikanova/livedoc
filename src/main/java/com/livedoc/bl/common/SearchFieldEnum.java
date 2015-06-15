package com.livedoc.bl.common;

public enum SearchFieldEnum {

    ID("id"), PARENT_ID("parentid"), CONTENT("content"), AUTHOR("author"), TITLE(
	    "title");

    private String name;

    private SearchFieldEnum(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }
}
