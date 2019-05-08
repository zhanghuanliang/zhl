package com.boco.rnop.framework.component.scene.entity.tk;

import java.io.Serializable;

import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 小区-场景关联表
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月10日 下午2:13:41
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@ApiModel(description = "小区-场景关联表")
@Table(name = "TV3_SCENE_CELL")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tv3SceneCell implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** CGI */
	@ApiModelProperty(value = "CGI")
	private String cgi;
	/** 小区名称 */
	@ApiModelProperty(value = "小区名称")
	private String cellZhName;
	/** 制式 */
	@ApiModelProperty(value = "制式 (GSM  LTE  NB-IOT)")
	private String technology;
	/** 地市名称 */
	@ApiModelProperty(value = "地市名称")
	private String regionName;
	/** 小区ID */
	@ApiModelProperty(value = "小区ID")
	private Long cellId;
	/** 场景ID */
	@ApiModelProperty(value = "场景ID")
	private Long sceneId;
	/** 地市ID */
	@ApiModelProperty(value = "地市ID")
	private Long regionId;
	/** 区县名称 */
	@ApiModelProperty(value = "区县名称")
	private String cityName;
	/** 区县ID */
	@ApiModelProperty(value = "区县ID")
	private Long cityId;
}
