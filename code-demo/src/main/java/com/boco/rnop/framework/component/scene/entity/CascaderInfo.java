package com.boco.rnop.framework.component.scene.entity;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 场景级联选择器信息
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2019年1月21日 下午4:34:40
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Data
@ApiModel(description = "级联选择器信息")
public class CascaderInfo {
	@ApiModelProperty(value = "标签名称")
	private String label;
	@ApiModelProperty(value = "值")
	private Long value;
	@ApiModelProperty(value = "子标签")
	private List<CascaderInfo> children;

}
