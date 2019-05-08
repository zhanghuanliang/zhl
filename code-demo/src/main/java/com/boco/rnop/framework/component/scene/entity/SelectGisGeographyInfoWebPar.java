package com.boco.rnop.framework.component.scene.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 查询Gis地理信息前端参数
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月12日 下午2:38:46
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@ApiModel(description = "查询Gis地理信息前端参数")
@Data
public class SelectGisGeographyInfoWebPar {
	@ApiModelProperty(value = "地市ID数组")
	private Long[] regionIds;
	@ApiModelProperty(value = "网优场景类型数组")
	private Long[] sceneTypeIds;
	@ApiModelProperty(value = "区域分类编号")
	private Integer geometryType;
	@ApiModelProperty(value = "地理信息类型（1信息点,2区域）")
	private Integer infoType;
	@ApiModelProperty(value = "地理信息名称(模糊查询)")
	private String name;
	@ApiModelProperty(value = "分页页数")
	private Integer pageNum;
	@ApiModelProperty(value = "分页每页数量")
	private Integer pageSize;

}
