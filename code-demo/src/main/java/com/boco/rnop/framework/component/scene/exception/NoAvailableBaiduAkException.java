package com.boco.rnop.framework.component.scene.exception;

/**
 * 
 * 无可用百度ak异常
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月15日 下午2:58:42
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public class NoAvailableBaiduAkException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoAvailableBaiduAkException() {
		super();
	}

	public NoAvailableBaiduAkException(String message) {
		super(message);
	}

	public NoAvailableBaiduAkException(Throwable cause) {
		super(cause);
	}

	public NoAvailableBaiduAkException(String message, Throwable cause) {
		super(message, cause);
	}
}
