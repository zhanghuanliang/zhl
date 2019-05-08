package com.boco.rnop.framework.component.scene.entity.tk;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 类型字典表
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月10日 上午10:54:00
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Table(name = "tv3_poi_type_relation")
@ApiModel(description = "类型字典表")
@Data
public class Tv3PoiTypeRelation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** POI类型编号ID */
	@ApiModelProperty(value = "POI类型编号ID")
	private Long poiTypeId;
	/** 网优场景类型 */
	@ApiModelProperty(value = "网优场景类型")
	private String sceneTypeName;
	/** 网优场景编号ID */
	@ApiModelProperty(value = "网优场景编号ID")
	private Long sceneTypeId;
	/** 操作人 */
	@ApiModelProperty(value = "操作人")
	private String optr;
	/** INT_ID */
	@Id
	@ApiModelProperty(value = "INT_ID")
	private Long intId;
	/** POI类型名称 */
	@ApiModelProperty(value = "POI类型名称")
	private String poiTypeName;

}
