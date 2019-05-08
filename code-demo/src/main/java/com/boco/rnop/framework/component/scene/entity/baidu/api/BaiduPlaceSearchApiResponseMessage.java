package com.boco.rnop.framework.component.scene.entity.baidu.api;

import java.util.List;

/**
 * 
 * 地点检索服务返回结果
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月12日 下午5:56:17
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public class BaiduPlaceSearchApiResponseMessage {
	private int status;

	private String message;

	private List<BaiduPlaceSearchApiResult> results;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<BaiduPlaceSearchApiResult> getResults() {
		return results;
	}

	public void setResults(List<BaiduPlaceSearchApiResult> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "BaiduPlaceSearchApiResponseMessage [status=" + status + ", message=" + message + ", results=" + results
				+ "]";
	}

}
