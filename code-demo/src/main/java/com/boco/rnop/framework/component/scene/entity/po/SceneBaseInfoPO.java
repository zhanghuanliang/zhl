package com.boco.rnop.framework.component.scene.entity.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 场景基本信息（场景编号、场景名称、父场景编号）
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月18日 上午10:14:22
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Data
@ApiModel(description = "场景基本信息")
public class SceneBaseInfoPO {
	/** 场景唯一标拾ID(特殊生成规则) */
	@ApiModelProperty(value = "场景唯一标拾ID(特殊生成规则)")
	private Long intId;
	/** 场景中文名称 */
	@ApiModelProperty(value = "场景中文名称")
	private String name;
	/** 父场景ID */
	@ApiModelProperty(value = "父场景ID")
	private Long parentId;
	@ApiModelProperty(value = "子场景数量")
	private Integer childCount;

}
