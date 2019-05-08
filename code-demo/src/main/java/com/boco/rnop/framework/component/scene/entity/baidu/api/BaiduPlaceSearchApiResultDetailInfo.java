package com.boco.rnop.framework.component.scene.entity.baidu.api;

public class BaiduPlaceSearchApiResultDetailInfo {
	/**
	 * 标签
	 */
	private String tag;
	/**
	 * 所属分类，如’hotel’、’cater’。
	 */
	private String type;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
