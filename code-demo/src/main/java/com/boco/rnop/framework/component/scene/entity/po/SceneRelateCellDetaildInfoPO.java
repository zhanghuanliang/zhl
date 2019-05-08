package com.boco.rnop.framework.component.scene.entity.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 场景关联小区详情信息
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2019年1月16日 下午5:59:46
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@ApiModel(description = "场景关联小区详情信息")
@Data
public class SceneRelateCellDetaildInfoPO {

	@ApiModelProperty(value = "id")
	private Long intId;
	@ApiModelProperty(value = "地市名称")
	private String regionName;
	@ApiModelProperty(value = "区县名称")
	private String cityName;
	@ApiModelProperty(value = "名称")
	private String name;
	@ApiModelProperty(value = "中文名称")
	private String zhName;
	@ApiModelProperty(value = "cgi")
	private String cgi;
	@ApiModelProperty(value = "GSM:LAC,LTE:TAC,NB-IOT:TAC")
	private Integer tac;
	@ApiModelProperty(value = "GSM:CI,LTE:PCI,NB-IOT:PCI")
	private Integer pci;
	@ApiModelProperty(value = "覆盖类型名称")
	private String coverTypeName;
	@ApiModelProperty(value = "GSM:BCCH_FEQ,LTE:WORK_FRQBAND,NB-IOT:WORK_FRQBAND")
	private String workFrqband;
	@ApiModelProperty(value = "厂家名称")
	private String vendorName;

}
