package com.boco.rnop.framework.component.scene.entity.baidu.api;

import java.util.List;

/**
 * 
 * 获取百度场景信息，调用百度api返回结果
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月18日 上午11:18:14
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public class BaiduSceneCrawlingApiMessage {

	/**
	 * 检索地市名称
	 */
	private String searchRegionName;

	/**
	 * 检索Poi名称
	 */
	private String searchPoiName;

	/**
	 * 百度地点检索api返回结果
	 */
	private BaiduPlaceSearchApiResponseMessage baiduPlaceSearchApiResponseMessage;

	/**
	 * 百度场景geoApi返回结果
	 */
	private BaiduSceneGeoApiResponseMessage baiduSceneGeoApiResponseMessage;

	/**
	 * 百度坐标转换api返回结果
	 */
	private List<BaiduGeoconvApiResponseMessage> baiduGeoconvApiResponseMessage;

	/**
	 * @return the searchRegionName
	 */
	public String getSearchRegionName() {
		return searchRegionName;
	}

	/**
	 * @param searchRegionName
	 *            the searchRegionName to set
	 */
	public void setSearchRegionName(String searchRegionName) {
		this.searchRegionName = searchRegionName;
	}

	/**
	 * @return the searchPoiName
	 */
	public String getSearchPoiName() {
		return searchPoiName;
	}

	/**
	 * @param searchPoiName
	 *            the searchPoiName to set
	 */
	public void setSearchPoiName(String searchPoiName) {
		this.searchPoiName = searchPoiName;
	}

	/**
	 * @return the baiduPlaceSearchApiResponseMessage
	 */
	public BaiduPlaceSearchApiResponseMessage getBaiduPlaceSearchApiResponseMessage() {
		return baiduPlaceSearchApiResponseMessage;
	}

	/**
	 * @param baiduPlaceSearchApiResponseMessage
	 *            the baiduPlaceSearchApiResponseMessage to set
	 */
	public void setBaiduPlaceSearchApiResponseMessage(
			BaiduPlaceSearchApiResponseMessage baiduPlaceSearchApiResponseMessage) {
		this.baiduPlaceSearchApiResponseMessage = baiduPlaceSearchApiResponseMessage;
	}

	/**
	 * @return the baiduSceneGeoApiResponseMessage
	 */
	public BaiduSceneGeoApiResponseMessage getBaiduSceneGeoApiResponseMessage() {
		return baiduSceneGeoApiResponseMessage;
	}

	/**
	 * @param baiduSceneGeoApiResponseMessage
	 *            the baiduSceneGeoApiResponseMessage to set
	 */
	public void setBaiduSceneGeoApiResponseMessage(BaiduSceneGeoApiResponseMessage baiduSceneGeoApiResponseMessage) {
		this.baiduSceneGeoApiResponseMessage = baiduSceneGeoApiResponseMessage;
	}

	/**
	 * @return the baiduGeoconvApiResponseMessage
	 */
	public List<BaiduGeoconvApiResponseMessage> getBaiduGeoconvApiResponseMessage() {
		return baiduGeoconvApiResponseMessage;
	}

	/**
	 * @param baiduGeoconvApiResponseMessage
	 *            the baiduGeoconvApiResponseMessage to set
	 */
	public void setBaiduGeoconvApiResponseMessage(List<BaiduGeoconvApiResponseMessage> baiduGeoconvApiResponseMessage) {
		this.baiduGeoconvApiResponseMessage = baiduGeoconvApiResponseMessage;
	}

}
