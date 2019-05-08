package com.boco.rnop.framework.component.scene.entity.baidu.api;

public class BaiduPlaceSearchApiLocation {
	private Double lat;
	private Double lng;

	public double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	@Override
	public String toString() {
		return "BaiduPlaceSearchApiLocation [lat=" + lat + ", lng=" + lng + "]";
	}

}
