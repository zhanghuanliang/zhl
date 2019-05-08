package com.boco.rnop.framework.component.scene.exception;

/**
 * 
 * 百度APi Http请求失败异常
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月17日 下午7:26:23
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public class BaiduApiHttpFalseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BaiduApiHttpFalseException() {
		super();
	}

	public BaiduApiHttpFalseException(String message) {
		super(message);
	}

	public BaiduApiHttpFalseException(Throwable cause) {
		super(cause);
	}

	public BaiduApiHttpFalseException(String message, Throwable cause) {
		super(message, cause);
	}

}
