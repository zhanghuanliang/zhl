package com.boco.rnop.framework.component.scene.entity.tk;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "场景路段信息")
@Table(name = "TV3_SCENE_DTGRID")
@Data
public class Tv3SceneDtgrid implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "路段唯一标识ID")
	private Long intId;
	/** 路段名称(如合芜高速_1) */
	@ApiModelProperty(value = "路段名称(如合芜高速_1)")
	private String dtgridName;
	/** 路段左下经度 */
	@ApiModelProperty(value = "路段左下经度")
	private Double dtgridLeftbottomLongitude;
	/** 路段左下纬度 */
	@ApiModelProperty(value = "路段左下纬度")
	private Double dtgridLeftbottomLatitude;
	/** 路段左上经度 */
	@ApiModelProperty(value = "路段左上经度")
	private Double dtgridLefttopLongitude;
	/** 路段左上纬度 */
	@ApiModelProperty(value = "路段左上纬度")
	private Double dtgridLefttopLatitude;
	/** 路段右上经度 */
	@ApiModelProperty(value = "路段右上经度")
	private Double dtgridRighttopLongitude;
	/** 路段右上纬度 */
	@ApiModelProperty(value = "路段右上纬度")
	private Double dtgridRighttopLatitude;
	/** 路段右下经度 */
	@ApiModelProperty(value = "路段右下经度 ")
	private Double dtgridRightbottomLongitude;
	/** 路段右下纬度 */
	@ApiModelProperty(value = "路段右下纬度")
	private Double dtgridRightbottomLatitude;
	/** 路段步长 */
	@ApiModelProperty(value = "路段步长")
	private Integer step;
	/** 路段扩充宽度 */
	@ApiModelProperty(value = "路段扩充宽度")
	private Integer extendWidth;
	/** 路段序号 */
	@ApiModelProperty(value = "路段序号")
	private Integer orderNum;
	/** 场景ID */
	@ApiModelProperty(value = "场景ID")
	private Long sceneId;
	/** 场景名称 */
	@ApiModelProperty(value = "场景名称")
	private String sceneName;
	/** 插入时间 */
	@ApiModelProperty(value = "插入时间")
	private Date insertTime;

}
