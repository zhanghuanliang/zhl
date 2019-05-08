/**
 * 
 */
package com.boco.rnop.framework.component.scene.entity.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 场景树编号名称信息
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2019年3月13日 下午2:45:09
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@ApiModel(description = "场景树编号名称信息")
@Data
public class SceneTreeIdsNamesPO {
	@ApiModelProperty(value = "场景树编号")
	private String treeIds;
	@ApiModelProperty(value = "场景树名称")
	private String treeNames;
}
