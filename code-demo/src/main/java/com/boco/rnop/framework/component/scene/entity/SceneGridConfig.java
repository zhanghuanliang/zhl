package com.boco.rnop.framework.component.scene.entity;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 场景栅格配置
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2019年1月22日 下午3:11:47
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@ApiModel(description = "场景栅格配置")
@Data
public class SceneGridConfig {

	/**
	 * 场景管理 栅格边长
	 */
	@ApiModelProperty(value = "场景管理 栅格边长")
	private List<Integer> gridBorderLength;
	/**
	 * 场景管理 线场景路段步长
	 */
	@ApiModelProperty(value = "场景管理 线场景路段步长")
	private List<Integer> gridStep;
	/**
	 * 场景管理 线场景路段扩充宽度
	 */
	@ApiModelProperty(value = "场景管理 线场景路段扩充宽度")
	private List<Integer> gridExtendWidth;

}
