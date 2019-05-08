/**
 * 
 */
package com.boco.rnop.framework.component.scene.entity.tk;

import lombok.Data;

/**
 * 关联小区基本信息
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月14日 下午2:59:15
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Data
public class BaseCellRelateInfo {
	/** id */
	private Long intId;
	/** cgi */
	private String cgi;

	/** 中文名称 */
	private String zhName;

	/** 经度 */
	private Double longitude;

	/** 纬度 */
	private Double latitude;
	/** 地市编号 */
	private Long regionId;

	/** 地市名称 */
	private String regionName;

	/** 区县编号 */
	private Long cityId;

	/** 区县名称 */
	private String cityName;
}
