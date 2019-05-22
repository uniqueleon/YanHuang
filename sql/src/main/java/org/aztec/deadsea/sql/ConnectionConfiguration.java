package org.aztec.deadsea.sql;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;

public class ConnectionConfiguration {
	
	private String text;

	public ConnectionConfiguration(String text) {
		// TODO Auto-generated constructor stub
		this.text = text;
	}
	
	public ConnectionConfiguration(InputStream source) throws IOException {
		text = new String(IOUtils.readFully(source, source.available()),"UTF-8");
	}
	
	public ConnectionConfiguration(InputStream source,Map<String,String> params) throws IOException {
		text = new String(IOUtils.readFully(source, source.available()),"UTF-8");
		for(Entry<String,String> paramEntry : params.entrySet()) {
			text = text.replace(paramEntry.getKey(), paramEntry.getValue());
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String propertiesText) {
		this.text = propertiesText;
	}
	
	

}
