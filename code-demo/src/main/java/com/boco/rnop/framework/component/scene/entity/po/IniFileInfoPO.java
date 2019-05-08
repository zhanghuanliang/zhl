package com.boco.rnop.framework.component.scene.entity.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "底层采集的ini文件信息")
@Data
public class IniFileInfoPO {

	@ApiModelProperty(value = "场景唯一标拾ID(特殊生成规则)")
	private Long intId;
	/** 场景中文名称 */
	@ApiModelProperty(value = "场景中文名称")
	private String name;
	/** 所属地市ID */
	@ApiModelProperty(value = "所属地市ID")
	private Long regionId;
	@ApiModelProperty(value = "当前场景级别")
	private Integer curLevel;
	@ApiModelProperty(value = "场景所属制式(GSM  LTE  NB-IOT)")
	private String technology;
	@ApiModelProperty(value = "是否共享场景")
	private Integer isSharing;
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
	@ApiModelProperty(value = "场景树场景编号")
	private String treeIds;
	@ApiModelProperty(value = "场景树场景名称")
	private String treeNames;
	@ApiModelProperty(value = "小区编号")
	private String cellIds;

}
