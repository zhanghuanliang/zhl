package com.boco.rnop.framework.component.scene.entity.qo;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "百度ak")
public class BaiduAkQO {
	@ApiModelProperty(hidden = true)
	private Integer intId;
	@ApiModelProperty(value = "ak值")
	private String ak;
	@ApiModelProperty(value = "ak类型")
	private Integer akTypeId;
	@ApiModelProperty(value = "ak正/逆地理编码可用次数")
	private Integer akGeocodingTimes;
	@ApiModelProperty(value = "ak地点检索可用次数")
	private Integer akRetrievalTimes;
	@ApiModelProperty(hidden = true)
	private Date updateDate;
	@ApiModelProperty(value = "ak坐标转换可用次数")
	private Integer akGeoconvTimes;

}
