package com.boco.rnop.framework.component.scene.entity.baidu.api;

public class BaiduPlaceSearchApiResult {
	private String name;
	private BaiduPlaceSearchApiLocation location;
	private String address;
	private String province;
	private String city;
	private String area;
	private String street_id;
	private String detail;
	private String uid;
	private BaiduPlaceSearchApiResultDetailInfo detail_info;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BaiduPlaceSearchApiLocation getLocation() {
		return location;
	}

	public void setLocation(BaiduPlaceSearchApiLocation location) {
		this.location = location;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getStreet_id() {
		return street_id;
	}

	public void setStreet_id(String street_id) {
		this.street_id = street_id;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public BaiduPlaceSearchApiResultDetailInfo getDetail_info() {
		return detail_info;
	}

	public void setDetail_info(BaiduPlaceSearchApiResultDetailInfo detail_info) {
		this.detail_info = detail_info;
	}

	@Override
	public String toString() {
		return "BaiduPlaceSearchApiResult [name=" + name + ", location=" + location + ", address=" + address
				+ ", province=" + province + ", city=" + city + ", area=" + area + ", street_id=" + street_id
				+ ", detail=" + detail + ", uid=" + uid + "]";
	}

}
