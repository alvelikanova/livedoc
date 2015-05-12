package com.livedoc.bl.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

public class XsltSystemIdProvider {

	private static final Logger logger = Logger
			.getLogger(XsltSystemIdProvider.class);
	private static final String PATH_TO_XSLT = "/xsl/html/docbook.xsl";
	private String systemId;

	public XsltSystemIdProvider() {
		try {
			systemId = URLEncoder
					.encode(XsltSystemIdProvider.class.getClassLoader()
							.getResource(PATH_TO_XSLT).toString(), "UTF-8")
					.replace("%2F", "/").replace("%3A", ":");
		} catch (UnsupportedEncodingException ex) {
			logger.error("Could not build system id for xsl stylesheets: ", ex);
		}
	}

	public String getSystemId() {
		return systemId;
	}

}
