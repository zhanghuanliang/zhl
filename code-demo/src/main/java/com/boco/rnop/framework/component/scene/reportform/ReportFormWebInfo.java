package com.boco.rnop.framework.component.scene.reportform;

import com.github.pagehelper.PageInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "报表前端信息")
public class ReportFormWebInfo<T> {
	@ApiModelProperty(value = "分页信息")
	private PageInfo<T> pageInfo;
	@ApiModelProperty(value = "报表模板信息")
	private ReportFormTemplate reportFormTemplate;

	public PageInfo<T> getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo<T> pageInfo) {
		this.pageInfo = pageInfo;
	}

	public ReportFormTemplate getReportFormTemplate() {
		return reportFormTemplate;
	}

	public void setReportFormTemplate(ReportFormTemplate reportFormTemplate) {
		this.reportFormTemplate = reportFormTemplate;
	}

}
