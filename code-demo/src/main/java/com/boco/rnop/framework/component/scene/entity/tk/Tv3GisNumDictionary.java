package com.boco.rnop.framework.component.scene.entity.tk;

import java.io.Serializable;

import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * Gis编号字典表
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2019年1月22日 上午9:42:26
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Data
@ApiModel(description = "Gis编号字典表")
@Table(name = "TV3_GIS_NUM_DICTIONARY")
public class Tv3GisNumDictionary implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "主键编号")
	private Long intId;
	@ApiModelProperty(value = "编号")
	private Long num;
	@ApiModelProperty(value = "编号名称")
	private String name;
	@ApiModelProperty(value = "编号类型")
	private String type;
	@ApiModelProperty(value = "描述")
	private String describe;

}
