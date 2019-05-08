package com.boco.rnop.framework.component.scene.entity.tk;

import java.io.Serializable;

import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Table(name = "tv3_poi_crawl_task_region")
@ApiModel(description = "任务地市关联表")
@Data
public class Tv3PoiCrawlTaskRegion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 地市名称 */
	@ApiModelProperty(value = "地市名称")
	private String regionName;
	/** 地市编号 */
	@ApiModelProperty(value = "地市编号")
	private Long regionId;
	/** 地市序号 */
	@ApiModelProperty(value = "地市序号")
	private Integer orderNum;
	/** 编号 */
	@ApiModelProperty(value = "编号")
	private Long intId;
	/** 任务编号 */
	@ApiModelProperty(value = "任务编号")
	private Long taskId;
}
