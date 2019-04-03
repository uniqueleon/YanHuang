package org.aztec.deadsea.sql;

public interface SQLTemplates {

	public static final String CREATE_DATABASE = "CREATE DATABASE IF NOT EXISTS %1$s DEFAULT CHARACTER SET = '%2$s' COLLATE = '%3$s'";
}
