package com.boco.rnop.framework.component.scene.entity.tk;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.type.JdbcType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.ColumnType;

/**
 * 
 * 地理图元信息表
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月10日 上午10:48:33
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Table(name = "TV3_GIS_GEOMETRY_TEMPORARY")
@ApiModel(description = "地理图元信息临时存储表")
@Data
public class Tv3GisGeometryTemporary implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 图元Id */
	@Id
	@ApiModelProperty(value = "图元Id")
	private Long intId;
	/** 特殊编号 */
	@ApiModelProperty(value = "特殊编号")
	private String customId;
	/** 图元名称 */
	@ApiModelProperty(value = "图元名称")
	private String name;
	/** 中心经度 */
	@ApiModelProperty(value = "中心经度")
	private Double centriodLon;
	/** 中心纬度 */
	@ApiModelProperty(value = "中心纬度")
	private Double centriodLat;
	/** 网优场景类型编号 */
	@ApiModelProperty(value = "网优场景类型编号")
	private Long sceneTypeId;
	/** 图元轮廓点 */
	@ColumnType(jdbcType = JdbcType.CLOB)
	@ApiModelProperty(value = "图元轮廓点 ")
	private String geometryJson;
	/** 所属地市Id */
	@ApiModelProperty(value = "所属地市Id")
	private Long regionId;
	/** 所属地市名称 */
	@ApiModelProperty(value = "所属地市名称")
	private String regionName;
	/** 所属区县Id */
	@ApiModelProperty(value = "所属区县Id")
	private Long cityId;
	/** 所属区县名称 */
	@ApiModelProperty(value = "所属区县名称")
	private String cityName;
	/** 图元周长 */
	@ApiModelProperty(value = "图元周长")
	private Double geometryLength;
	/** 图元面积 */
	@ApiModelProperty(value = "图元面积")
	private Double geometryArea;
	/** 插入时间 */
	@ApiModelProperty(value = "插入时间")
	private Date insertTime;
	/** 创建人ID */
	@ApiModelProperty(value = " 创建人ID")
	private String createUserid;
	/** 创建人名称 */
	@ApiModelProperty(value = " 创建人名称")
	private String createUsername;
	/** 任务编号 */
	@ApiModelProperty(value = "任务编号")
	private Long taskId;
	/** 地址 */
	@ApiModelProperty(value = "地址")
	private String address;
	/** Poi类型 名称 */
	@ApiModelProperty(value = "Poi类型名称")
	private String poiTypeName;
	/**
	 * geojson类型(Point、MultiPoint、LineString、MultiLineString、Polygon、MultiPlygon、GeometryCollection)
	 */
	@ApiModelProperty(value = "geojson类型(Point、MultiPoint、LineString、MultiLineString、Polygon、MultiPlygon、GeometryCollection)")
	private String geojsonType;
	/** 经度 */
	@ApiModelProperty(value = "经度")
	private Double longitude;
	/** 纬度 */
	@ApiModelProperty(value = "纬度")
	private Double latitude;
	/** 区域分类 */
	@ApiModelProperty(value = "区域分类")
	private Integer geometryType;
	/** 区域分类名称 */
	@ApiModelProperty(value = "区域分类名称")
	private String geometryTypeName;
	/** uuid */
	@ApiModelProperty(value = "uuid")
	private String uuid;
	/** 获取途径编号 */
	@ApiModelProperty(value = "获取途径编号")
	private Integer acquireWayId;
	/** 获取途径名称 */
	@ApiModelProperty(value = "获取途径名称")
	private String acquireWayName;
	/** 批次号（用于后续批量入数据库表） */
	@ApiModelProperty(value = " 批次号（用于后续批量入数据库表）")
	private Integer batchNum;

}
