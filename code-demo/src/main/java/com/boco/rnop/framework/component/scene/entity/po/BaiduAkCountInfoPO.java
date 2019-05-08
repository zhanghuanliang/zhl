package com.boco.rnop.framework.component.scene.entity.po;

/**
 * 
 * 百度ak统计信息
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月11日 下午8:28:17
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public class BaiduAkCountInfoPO {
	/**
	 * 百度ak正/逆地理编码可用次数
	 */
	private Integer akGeocodingTimes;

	private Integer akGeocodingUsedTimes;
	/**
	 * 百度ak地点检索可用次数
	 */
	private Integer akRetrievalTimes;

	private Integer akRetrievalUsedTimes;
	/**
	 * 百度ak坐标转换可用次数
	 */
	private Integer akGeoconvTimes;

	private Integer akGeoconvUsedTimes;

	public Integer getAkGeocodingTimes() {
		return akGeocodingTimes;
	}

	public void setAkGeocodingTimes(Integer akGeocodingTimes) {
		this.akGeocodingTimes = akGeocodingTimes;
	}

	public Integer getAkGeocodingUsedTimes() {
		return akGeocodingUsedTimes;
	}

	public void setAkGeocodingUsedTimes(Integer akGeocodingUsedTimes) {
		this.akGeocodingUsedTimes = akGeocodingUsedTimes;
	}

	public Integer getAkRetrievalTimes() {
		return akRetrievalTimes;
	}

	public void setAkRetrievalTimes(Integer akRetrievalTimes) {
		this.akRetrievalTimes = akRetrievalTimes;
	}

	public Integer getAkRetrievalUsedTimes() {
		return akRetrievalUsedTimes;
	}

	public void setAkRetrievalUsedTimes(Integer akRetrievalUsedTimes) {
		this.akRetrievalUsedTimes = akRetrievalUsedTimes;
	}

	public Integer getAkGeoconvTimes() {
		return akGeoconvTimes;
	}

	public void setAkGeoconvTimes(Integer akGeoconvTimes) {
		this.akGeoconvTimes = akGeoconvTimes;
	}

	public Integer getAkGeoconvUsedTimes() {
		return akGeoconvUsedTimes;
	}

	public void setAkGeoconvUsedTimes(Integer akGeoconvUsedTimes) {
		this.akGeoconvUsedTimes = akGeoconvUsedTimes;
	}

}
