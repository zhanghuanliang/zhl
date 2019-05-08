package com.boco.rnop.framework.component.scene.entity.tk;

import java.io.Serializable;

import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 百度poi类型
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2019年1月22日 上午9:11:36
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Data
@Table(name = "TV3_MAP_BD_POI_TYPE")
@ApiModel(description = "百度poi类型")
public class Tv3MapBdPoiType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "主键编号")
	private Long intId;
	@ApiModelProperty(value = "poi类型名称")
	private String poiTypeName;
	@ApiModelProperty(value = "父类型编号（一级类型-1）")
	private Long parentId;

}
