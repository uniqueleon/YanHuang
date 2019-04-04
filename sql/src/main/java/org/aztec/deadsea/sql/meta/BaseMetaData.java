package org.aztec.deadsea.sql.meta;

public class BaseMetaData {
	
	protected String name;
	protected String alias;
	private Location location;

	public BaseMetaData() {
		// TODO Auto-generated constructor stub
	}

	public BaseMetaData(String name, String alias,Location location) {
		super();
		this.name = name;
		this.alias = alias;
		this.location = location;
	}

	public String name() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String alias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Location location() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	
}
