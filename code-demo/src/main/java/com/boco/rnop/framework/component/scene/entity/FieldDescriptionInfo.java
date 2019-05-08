package com.boco.rnop.framework.component.scene.entity;

import io.swagger.annotations.ApiModel;

/**
 * 
 * 字段描述信息
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年11月27日 下午7:31:15
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@ApiModel(description = "字段描述信息")
public class FieldDescriptionInfo {
	/**
	 * 字段名称
	 */
	private String fieldName;
	/**
	 * 字段描述
	 */
	private String fieldDes;

	public FieldDescriptionInfo() {
		// TODO Auto-generated constructor stub
	}

	public FieldDescriptionInfo(String fieldName, String fieldDes) {
		super();
		this.fieldName = fieldName;
		this.fieldDes = fieldDes;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldDes() {
		return fieldDes;
	}

	public void setFieldDes(String fieldDes) {
		this.fieldDes = fieldDes;
	}

}
