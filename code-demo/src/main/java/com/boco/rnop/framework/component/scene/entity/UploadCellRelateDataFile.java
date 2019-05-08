package com.boco.rnop.framework.component.scene.entity;

import lombok.Data;

/**
 * 
 * 上传关联小区文件
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月18日 上午8:59:05
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Data
public class UploadCellRelateDataFile {
	/**
	 * 小区制式(2G、4G、NB)
	 */
	private String technology;
	/**
	 * 小区cgi
	 */
	private String cgi;

}
