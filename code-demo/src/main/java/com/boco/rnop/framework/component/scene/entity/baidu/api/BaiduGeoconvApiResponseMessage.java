package com.boco.rnop.framework.component.scene.entity.baidu.api;

import java.util.List;

/**
 * 
 * 坐标转换服务返回结果
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月14日 下午3:04:22
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public class BaiduGeoconvApiResponseMessage {

	private Integer status;

	private List<BaiduGeoconvApiCoordinate> result;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<BaiduGeoconvApiCoordinate> getResult() {
		return result;
	}

	public void setResult(List<BaiduGeoconvApiCoordinate> result) {
		this.result = result;
	}

}
