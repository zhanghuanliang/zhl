package com.boco.rnop.framework.component.scene.entity.tk;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 栅格字典表
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月10日 下午2:19:55
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@ApiModel(description = "栅格字典表")
@Table(name = "TV3_SCENE_GRID")
@Data
public class Tv3SceneGrid implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 主键ID */
	@Id
	@ApiModelProperty(value = "主键ID")
	private Long intId;
	/** 右下经度 */
	@ApiModelProperty(value = "右下经度")
	private Double gridRightbottomLongitude;
	/** 右上纬度 */
	@ApiModelProperty(value = "右上纬度")
	private Double gridLefttopLatitude;
	/** 右下纬度 */
	@ApiModelProperty(value = "右下纬度")
	private Double gridRightbottomLatitude;
	/** 左上经度 */
	@ApiModelProperty(value = "左上经度 ")
	private Double gridRighttopLongitude;
	/** 左下经度 */
	@ApiModelProperty(value = "左下经度")
	private Double gridLeftbottomLongitude;
	/** 左下纬度 */
	@ApiModelProperty(value = "左下纬度")
	private Double gridLeftbottomLatitude;
	/** 右上经度 */
	@ApiModelProperty(value = "右上经度")
	private Double gridLefttopLongitude;
	/** 左上纬度 */
	@ApiModelProperty(value = "左上纬度")
	private Double gridRighttopLatitude;
	/** 地市ID */
	@ApiModelProperty(value = "地市ID")
	private Long regionId;
	/** 地市名称 */
	@ApiModelProperty(value = "地市名称")
	private String regionName;
	/** 区县ID */
	@ApiModelProperty(value = "区县ID")
	private Long cityId;
	/** 区县名称 */
	@ApiModelProperty(value = "区县名称")
	private String cityName;
	/** 场景ID */
	@ApiModelProperty(value = "场景ID")
	private Long sceneId;
	/** 场景名称 */
	@ApiModelProperty(value = "场景名称")
	private String sceneName;
	/** 栅格边长 */
	@ApiModelProperty(value = "栅格边长")
	private Integer gridBorderLength;
	/** 插入时间 */
	@ApiModelProperty(value = "插入时间")
	private Date insertTime;
	/** 路段唯一标识ID */
	@ApiModelProperty(value = "路段唯一标识ID")
	private Long dtgridId;
	/** 军事栅格ID */
	@ApiModelProperty(value = "军事栅格ID")
	private String gridId;
}
