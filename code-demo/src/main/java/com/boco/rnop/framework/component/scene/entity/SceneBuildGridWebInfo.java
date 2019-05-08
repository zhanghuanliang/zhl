package com.boco.rnop.framework.component.scene.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 场景生成栅格前端信息
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月17日 上午10:03:46
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Data
@ApiModel(description = "场景生成栅格前端信息")
public class SceneBuildGridWebInfo {
	@ApiModelProperty(value = "删除栅格数量")
	private Integer delectGridCount;
	@ApiModelProperty(value = "生成栅格数量")
	private Integer buildGridCount;

}
