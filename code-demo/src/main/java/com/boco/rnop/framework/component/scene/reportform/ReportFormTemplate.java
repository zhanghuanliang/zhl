package com.boco.rnop.framework.component.scene.reportform;

import java.util.LinkedHashMap;
import java.util.List;

public class ReportFormTemplate {
	private String id;
	private String name;
	private List<ReportFormTemplateColumn> columns;

	private LinkedHashMap<String, String> linkedHashMap;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ReportFormTemplateColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<ReportFormTemplateColumn> columns) {
		this.columns = columns;
	}

	public LinkedHashMap<String, String> getLinkedHashMap() {
		return linkedHashMap;
	}

	public void setLinkedHashMap(LinkedHashMap<String, String> linkedHashMap) {
		this.linkedHashMap = linkedHashMap;
	}

	@Override
	public String toString() {
		return "ReportFormTemplate [id=" + id + ", name=" + name + ", columns=" + columns + ", linkedHashMap="
				+ linkedHashMap + "]";
	}

}
