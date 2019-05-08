package com.boco.rnop.framework.component.scene.entity.po;

import lombok.Data;

/**
 * 
 * 缺省场景树信息
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2019年1月11日 下午3:52:48
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Data
public class DefaultSceneTreeInfoPO {
	/**
	 * 场景编号
	 */
	private Long intId;
	/**
	 * 场景名称
	 */
	private String name;
	/**
	 * 是否共享
	 */
	private Integer isSharing;
	/**
	 * 父场景编号
	 */
	private Long parentId;
	/**
	 * 场景关联小区数量
	 */
	private Integer cellRelateCount;
	/**
	 * 是否存在geojson(1\0)
	 */
	private Integer isExistGeojson;
}
