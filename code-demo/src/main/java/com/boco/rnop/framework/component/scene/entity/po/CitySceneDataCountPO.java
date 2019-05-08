package com.boco.rnop.framework.component.scene.entity.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 地市场景数据量统计
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月10日 下午4:24:40
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@ApiModel(description = "地市场景数据量统计")
@Data
public class CitySceneDataCountPO {
	@ApiModelProperty(value = "地市编号")
	private Integer regionId;
	@ApiModelProperty(value = "地市名称")
	private String regionName;
	@ApiModelProperty(value = "地理场景数据量")
	private Integer count;

}
