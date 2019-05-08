package com.boco.rnop.framework.component.scene.reportform;

import java.util.List;

public class ReportFormTemplateColumn {

	private String name;
	private String propertyName;
	private String width = "150";
	private String isFix = "false";
	private List<ReportFormTemplateColumn> columns;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getIsFix() {
		return isFix;
	}

	public void setIsFix(String isFix) {
		this.isFix = isFix;
	}

	public List<ReportFormTemplateColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<ReportFormTemplateColumn> columns) {
		this.columns = columns;
		// 当有下行
		if (columns != null && columns.size() > 0) {
			this.width = null;
			this.isFix = null;
		}
	}

	@Override
	public String toString() {
		return "ReportFormTemplateColumn [name=" + name + ", propertyName=" + propertyName + ", width=" + width
				+ ", isFix=" + isFix + ", columns=" + columns + "]";
	}

}
