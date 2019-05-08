package com.boco.rnop.framework.component.scene.entity.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 网优场景类型
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月10日 下午3:28:47
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@ApiModel(description = "网优场景类型")
@Data
public class SceneTypePO {
	@ApiModelProperty(value = "网优场景名称")
	private String sceneTypeName;
	@ApiModelProperty(value = "网优场景编号")
	private Integer sceneTypeId;

}
