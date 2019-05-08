package com.boco.rnop.framework.component.scene.entity.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 各场景数据量统计
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月10日 下午3:59:22
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@ApiModel(description = "各场景类型数据量统计")
@Data
public class SceneTypeDataCountPO {
	@ApiModelProperty(value = "场景类型")
	private String sceneType;
	@ApiModelProperty(value = "数据量")
	private Integer count;

}
