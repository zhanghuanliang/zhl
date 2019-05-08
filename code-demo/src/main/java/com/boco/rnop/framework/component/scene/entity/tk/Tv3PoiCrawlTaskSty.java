package com.boco.rnop.framework.component.scene.entity.tk;

import java.io.Serializable;

import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Table(name = "tv3_poi_crawl_task_sty")
@ApiModel(description = "任务网优类型关联表")
@Data
public class Tv3PoiCrawlTaskSty implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	/** POI类型名称 */
	@ApiModelProperty(value = "POI类型名称")
	private String poiTypeName;
	/** POI类型编号ID */
	@ApiModelProperty(value = "POI类型编号ID")
	private Long poiTypeId;
	/** 网优场景类型 */
	@ApiModelProperty(value = "网优场景类型")
	private String sceneTypeName;
	/** 网优场景编号ID */
	@ApiModelProperty(value = "网优场景编号ID")
	private Long sceneTypeId;
	/** 编号 */
	@ApiModelProperty(value = "编号")
	private Long intId;
	/** 任务编号 */
	@ApiModelProperty(value = "任务编号")
	private Long taskId;
	/** 场景序号 */
	@ApiModelProperty(value = "场景序号")
	private Integer orderNum;

}
