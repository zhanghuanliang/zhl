package com.boco.rnop.framework.component.scene.entity.tk;

import java.io.Serializable;

import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * Gis AK 类型
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月10日 上午10:43:49
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Table(name = "tv3_map_ak_type")
@ApiModel(description = "Gis AK 类型")
@Data
public class Tv3MapAkType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 平台编号 */
	@ApiModelProperty(value = "平台编号")
	private Integer platformId;
	/** 平台名称 */
	@ApiModelProperty(value = "平台名称")
	private String platformName;
	/** 正/逆地理编码配额(次/日) */
	@ApiModelProperty(value = "正/逆地理编码配额(次/日)")
	private Integer geocodingQuota;
	/** 正/逆地理编码并发(次/秒) */
	@ApiModelProperty(value = "正/逆地理编码并发(次/秒)")
	private Integer geocodingConcurrency;
	/** 唯一标识 */
	@ApiModelProperty(value = "唯一标识")
	private Long intId;
	/** 类型名称 */
	@ApiModelProperty(value = "类型名称")
	private String typeName;
	/** 地点检索 配额(次/日) */
	@ApiModelProperty(value = "地点检索 配额(次/日)")
	private Integer retrievalQuota;
	/** 地点检索 并发(次/秒) */
	@ApiModelProperty(value = "地点检索 并发(次/秒)")
	private Integer retrievalConcurrency;
	/** 坐标转换配额(次/日) */
	@ApiModelProperty(value = "坐标转换配额(次/日)")
	private Integer geoconvQuota;
	/** 坐标转换并发(次/秒) */
	@ApiModelProperty(value = "坐标转换并发(次/秒)")
	private Integer geoconvConcurrency;

}
