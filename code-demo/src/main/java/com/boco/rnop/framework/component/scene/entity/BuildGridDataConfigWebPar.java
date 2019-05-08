package com.boco.rnop.framework.component.scene.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 生成栅格数据配置
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2019年1月22日 下午3:25:05
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@ApiModel(description = "生成栅格数据配置前端参数")
@Data
public class BuildGridDataConfigWebPar {
	@ApiModelProperty(value = "面场景栅格边长")
	private Integer polygonGridBorderLength;

	@ApiModelProperty(value = "线场景栅格边长")
	private Integer lineStringGridBorderLength;

	@ApiModelProperty(value = "线场景路段步长")
	private Integer lineStringGridStep;

	@ApiModelProperty(value = "线场景路段扩充宽度")
	private Integer lineStringGridExtendWidth;

}
