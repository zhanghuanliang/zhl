package com.boco.rnop.framework.component.scene.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 场景信息上传excel
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月26日 下午4:17:11
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Data
public class SceneInfoUploadExcel {

	/** 场景所属制式(GSM,LTE,NB-IOT) */
	@ApiModelProperty(value = "场景所属制式(GSM,LTE,NB-IOT)")
	private String sceneTechnology;
	/** 所属地市名称 */
	@ApiModelProperty(value = "所属地市名称")
	private String regionName;
	/** 所属区县名称 */
	@ApiModelProperty(value = "所属区县名称")
	private String cityName;
	/** 根级场景名称 */
	@ApiModelProperty(value = "根级场景名称")
	private String rootSceneName;
	/** 一级场景名称 */
	@ApiModelProperty(value = "一级场景名称")
	private String oneSceneName;
	/** 二级场景名称 */
	@ApiModelProperty(value = "二级场景名称")
	private String twoSceneName;
	/** 三级场景名称 */
	@ApiModelProperty(value = "三级场景名称")
	private String threeSceneName;
	/** 四级场景名称 */
	@ApiModelProperty(value = "四级场景名称")
	private String fourSceneName;
	/** 小区名 */
	@ApiModelProperty(value = "小区名")
	private String cellName;
	/** CGI */
	@ApiModelProperty(value = "CGI")
	private String cgi;
	/** 标签 */
	@ApiModelProperty(value = "标签")
	private String tag;
	/** 是否需要汇总场景粒度 */
	@ApiModelProperty(value = "是否需要汇总场景粒度")
	private String isNeedSceneSize;
	/** 是否需要汇总栅格粒度 */
	@ApiModelProperty(value = "是否需要汇总栅格粒度")
	private String isNeedGirdSize;
	/** 是否需要汇总小区粒度 */
	@ApiModelProperty(value = "是否需要汇总小区粒度")
	private String isNeedCellSize;
	/** 是否需要大数据汇总 */
	@ApiModelProperty(value = "是否需要大数据汇总")
	private String isNeedBigdataSum;
	/** 是否需要统计性能 */
	@ApiModelProperty(value = "是否需要统计性能")
	private String isNeedPerformanceSum;
	/** 是否共享场景 */
	@ApiModelProperty(value = "是否共享场景")
	private String isSharing;

}
