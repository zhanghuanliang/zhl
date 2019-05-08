package com.boco.rnop.framework.component.scene.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 根据场景Geojson计算场景关联小区前端参数信息
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月17日 下午5:26:53
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@ApiModel(description = "根据场景Geojson计算场景关联小区前端参数信息")
@Data
public class ExecuteCellRelateDataByGeojsonWebParam {
	@ApiModelProperty(value = "场景制式（GSM、LTE、NB-IOT）")
	private String sceneTechnology;
	@ApiModelProperty(value = "场景Geojson")
	private String sceneGeojson;

}
