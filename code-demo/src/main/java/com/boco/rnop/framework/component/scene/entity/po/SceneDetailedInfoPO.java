package com.boco.rnop.framework.component.scene.entity.po;

import java.util.Date;

import javax.persistence.Id;

import org.apache.ibatis.type.JdbcType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.ColumnType;

/**
 * 
 * 场景详细详细（含父场景名称、关联小区数量）
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月19日 下午3:56:08
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Data
@ApiModel(description = "场景详细详细（含父场景名称、关联小区数量）")
public class SceneDetailedInfoPO {

	@Id
	@ApiModelProperty(value = "场景唯一标拾ID(特殊生成规则)")
	private Long intId;
	/** 场景中文名称 */
	@ApiModelProperty(value = "场景中文名称")
	private String name;
	/** 所属地市ID */
	@ApiModelProperty(value = "所属地市ID")
	private Long regionId;
	/** 所属地市名称 */
	@ApiModelProperty(value = "所属地市名称")
	private String regionName;
	/** 所属区县ID */
	@ApiModelProperty(value = "所属区县ID")
	private Long cityId;
	/** 场景图片名称 */
	@ApiModelProperty(value = "场景图片名称")
	private String imageName;
	/** 所属区县名称 */
	@ApiModelProperty(value = "所属区县名称")
	private String cityName;
	/** 场景获取方式（导入或爬取或绘制） */
	@ApiModelProperty(value = "场景获取方式（导入或爬取或绘制）")
	private String acquireWay;
	/** 场景创建人ID */
	@ApiModelProperty(value = "场景创建人ID")
	private String createUserid;
	/** 场景创建人名称 */
	@ApiModelProperty(value = "场景创建人名称")
	private String createUsername;
	/** 地理图元ID(关联的tv3_gis_geometry的id) */
	@ApiModelProperty(value = "地理图元ID(关联的tv3_gis_geometry的id)")
	private Long geometryId;
	/** 场景中心经度 */
	@ApiModelProperty(value = "场景中心经度")
	private Double sceneCentriodLon;
	/** 场景中心纬度 */
	@ApiModelProperty(value = "场景中心纬度")
	private Double sceneCentriodLat;
	/** 场景JSON */
	@ApiModelProperty(value = "场景JSON")
	@ColumnType(jdbcType = JdbcType.CLOB)
	private String geojson;
	/**
	 * geojson类型（Point、MultiPoint、LineString、MultiLineString、Polygon、MultiPlygon、GeometryCollection）
	 */
	@ApiModelProperty(value = "geojson类型（Point、MultiPoint、LineString、MultiLineString、Polygon、MultiPlygon、GeometryCollection）")
	private String geojsonType;
	/** 场景周长 */
	@ApiModelProperty(value = "场景周长")
	private Double sceneLength;
	/** 场景面积 */
	@ApiModelProperty(value = "场景面积")
	private Double sceneArea;
	/** 场景相关描述 */
	@ApiModelProperty(value = "场景相关描述")
	private String description;
	/** 当前场景级别 */
	@ApiModelProperty(value = "当前场景级别")
	private Integer curLevel;
	/** 根场景ID */
	@ApiModelProperty(value = "根场景ID")
	private Long rootId;
	/** 父场景ID */
	@ApiModelProperty(value = "父场景ID")
	private Long parentId;
	/** 场景优先级 */
	@ApiModelProperty(value = "场景优先级")
	private Integer orderNo;
	/** 场景所属制式(GSM LTE NB-IOT) */
	@ApiModelProperty(value = "场景所属制式(GSM  LTE  NB-IOT)")
	private String technology;
	/** 是否需要汇总场景粒度 */
	@ApiModelProperty(value = "是否需要汇总场景粒度")
	private Integer isNeedSceneSize;
	/** 是否需要汇总栅格粒度 */
	@ApiModelProperty(value = "是否需要汇总栅格粒度")
	private Integer isNeedGirdSize;
	/** 是否需要汇总小区粒度 */
	@ApiModelProperty(value = "是否需要汇总小区粒度")
	private Integer isNeedCellSize;
	/** 是否需要大数据汇总 */
	@ApiModelProperty(value = "是否需要大数据汇总")
	private Integer isNeedBigdataSum;
	/** 是否需要统计性能 */
	@ApiModelProperty(value = "是否需要统计性能")
	private Integer isNeedPerformanceSum;
	/** 是否锁定（只能由管理员编辑） */
	@ApiModelProperty(value = "是否锁定（只能由管理员编辑）")
	private Integer isLocking;
	/** 是否共享场景 */
	@ApiModelProperty(value = "是否共享场景")
	private Integer isSharing;
	/** 场景入库时间 */
	@ApiModelProperty(value = "场景入库时间")
	private Date createTime;
	@ApiModelProperty(value = "父场景名称")
	private String parentSceneName;
	@ApiModelProperty(value = "根场景名称")
	private String systemSceneName;
	@ApiModelProperty(value = "关联小区数量")
	private Integer cellRelateCount;
	@ApiModelProperty(value = "地理图元名称")
	private String geometryName;
	@ApiModelProperty(value = "标签")
	private String tag;
	/** 场景树编号集合 */
	@ApiModelProperty(value = "场景树编号集合（123,234,134）")
	private String treeIds;
	/** 场景树名称集合 */
	@ApiModelProperty(value = "场景树名称集合（根级场景,一级场景,二级场景）")
	private String treeNames;

}
