package com.boco.rnop.framework.component.scene.entity;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 栅格数据导出csv实体类
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月25日 下午6:48:54
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Data
public class GisGridExportCsvEntity {
	/** 主键ID */
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

}
