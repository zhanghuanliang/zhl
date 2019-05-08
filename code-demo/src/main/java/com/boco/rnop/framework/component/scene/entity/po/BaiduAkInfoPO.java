package com.boco.rnop.framework.component.scene.entity.po;

import java.util.Date;

/**
 * 
 * 百度ak
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月11日 下午7:33:11
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public class BaiduAkInfoPO {

	private Integer intId;

	private String ak;

	private Integer akTypeId;
	/**
	 * 百度ak正/逆地理编码可用次数
	 */
	private Integer akGeocodingTimes;
	/**
	 * 百度ak地点检索可用次数
	 */
	private Integer akRetrievalTimes;
	/**
	 * 百度ak坐标转换可用次数
	 */
	private Integer akGeoconvTimes;

	private Date updateDate;

	private String typeName;

	private Integer geocodingQuota;

	private Integer geocodingConcurrency;

	private Integer retrievalQuota;

	private Integer retrievalConcurrency;

	private Integer geoconvQuota;

	private Integer geoconvConcurrency;

	synchronized public Integer getAkGeoconvTimes() {
		try {
			Thread.sleep(80);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return akGeoconvTimes;
	}

	synchronized public void setAkGeoconvTimes(Integer akGeoconvTimes) {
		this.akGeoconvTimes = akGeoconvTimes;
	}

	public Integer getIntId() {
		return intId;
	}

	public void setIntId(Integer intId) {
		this.intId = intId;
	}

	public String getAk() {
		return ak;
	}

	public void setAk(String ak) {
		this.ak = ak;
	}

	public Integer getAkTypeId() {
		return akTypeId;
	}

	public void setAkTypeId(Integer akTypeId) {
		this.akTypeId = akTypeId;
	}

	synchronized public Integer getAkGeocodingTimes() {
		try {
			Thread.sleep(80);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return akGeocodingTimes;
	}

	synchronized public void setAkGeocodingTimes(Integer akGeocodingTimes) {
		this.akGeocodingTimes = akGeocodingTimes;
	}

	synchronized public Integer getAkRetrievalTimes() {
		try {
			Thread.sleep(80);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return akRetrievalTimes;
	}

	synchronized public void setAkRetrievalTimes(Integer akRetrievalTimes) {
		this.akRetrievalTimes = akRetrievalTimes;

	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getGeocodingQuota() {
		return geocodingQuota;
	}

	public void setGeocodingQuota(Integer geocodingQuota) {
		this.geocodingQuota = geocodingQuota;
	}

	public Integer getGeocodingConcurrency() {
		return geocodingConcurrency;
	}

	public void setGeocodingConcurrency(Integer geocodingConcurrency) {
		this.geocodingConcurrency = geocodingConcurrency;
	}

	public Integer getRetrievalQuota() {
		return retrievalQuota;
	}

	public void setRetrievalQuota(Integer retrievalQuota) {
		this.retrievalQuota = retrievalQuota;
	}

	public Integer getRetrievalConcurrency() {
		return retrievalConcurrency;
	}

	public void setRetrievalConcurrency(Integer retrievalConcurrency) {
		this.retrievalConcurrency = retrievalConcurrency;
	}

	public Integer getGeoconvQuota() {
		return geoconvQuota;
	}

	public void setGeoconvQuota(Integer geoconvQuota) {
		this.geoconvQuota = geoconvQuota;
	}

	public Integer getGeoconvConcurrency() {
		return geoconvConcurrency;
	}

	public void setGeoconvConcurrency(Integer geoconvConcurrency) {
		this.geoconvConcurrency = geoconvConcurrency;
	}

}
