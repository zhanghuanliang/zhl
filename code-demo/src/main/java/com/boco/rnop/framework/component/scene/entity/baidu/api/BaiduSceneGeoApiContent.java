package com.boco.rnop.framework.component.scene.entity.baidu.api;

public class BaiduSceneGeoApiContent {

	private String geo;

	private String uid;

	public String getGeo() {
		return geo;
	}

	public void setGeo(String geo) {
		this.geo = geo;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Override
	public String toString() {
		return "BaiduSceneGeoApiContent [geo=" + geo + ", uid=" + uid + "]";
	}

}
