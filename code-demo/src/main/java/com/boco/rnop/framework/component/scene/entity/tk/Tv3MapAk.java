package com.boco.rnop.framework.component.scene.entity.tk;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * Gis AK 信息表
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月10日 上午10:39:26
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@ApiModel(description = "Gis AK 信息表")
@Table(name = "tv3_map_ak")
@Data
public class Tv3MapAk implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 百度ak类型 */
	@ApiModelProperty(value = " ak类型")
	private Long akTypeId;
	/** 百度ak正/逆地理编码可用次数 */
	@ApiModelProperty(value = "ak正/逆地理编码可用次数")
	private Integer akGeocodingTimes;
	/** 百度ak地点检索可用次数 */
	@ApiModelProperty(value = "ak地点检索可用次数")
	private Integer akRetrievalTimes;
	/** 信息更新时间 */
	@ApiModelProperty(value = "信息更新时间")
	private Date updateDate;
	/** 唯一标识 */
	@Id
	@ApiModelProperty(value = "唯一标识")
	private Long intId;
	/** 百度ak值 */
	@ApiModelProperty(value = "ak值")
	private String ak;
	/** 百度ak坐标转换可用次数 */
	@ApiModelProperty(value = "ak坐标转换可用次数")
	private Integer akGeoconvTimes;

}
