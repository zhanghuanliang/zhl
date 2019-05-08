package com.boco.rnop.framework.component.scene.entity.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 场景栅格统计数据
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月24日 上午9:36:44
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Data
public class SceneGridCountInfoPO {
	/** 场景名称 */
	@ApiModelProperty(value = "场景名称")
	private String sceneName;
	/** 场景ID */
	@ApiModelProperty(value = "场景ID")
	private Long sceneId;
	/** 路段步长 */
	@ApiModelProperty(value = "路段步长")
	private Integer step;
	/** 路段扩充宽度 */
	@ApiModelProperty(value = "路段扩充宽度")
	private Integer extendWidth;
	/** 栅格边长 */
	@ApiModelProperty(value = "栅格边长")
	private Integer gridBorderLength;
	/** 栅格数量 */
	@ApiModelProperty(value = "栅格数量")
	private Integer gridCount;

}
