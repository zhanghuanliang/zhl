package com.boco.rnop.framework.component.scene.entity;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 场景树子数据
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2019年1月11日 下午2:06:55
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@ApiModel(description = "场景树子数据")
@Data
public class SceneTreeChildrenData {

	@ApiModelProperty(value = "标签")
	private String label;

	@ApiModelProperty(value = "子数据")
	private List<SceneTreeChildrenData> children;

	@ApiModelProperty(value = "场景Id")
	private Long sceneId;

	@ApiModelProperty(value = "场景名称")
	private String sceneName;

	@ApiModelProperty(value = "是否共享")
	private Integer isSharing;

	@ApiModelProperty(value = "场景关联小区数量")
	private Integer cellRelateCount;

	@ApiModelProperty(value = "是否存在geojson数据")
	private Integer isExistGeojson;

	public SceneTreeChildrenData() {
	}

	public SceneTreeChildrenData(String label, List<SceneTreeChildrenData> children, Long sceneId, String sceneName,
			Integer isSharing, Integer cellRelateCount, Integer isExistGeojson) {
		super();
		this.label = label;
		this.children = children;
		this.sceneId = sceneId;
		this.sceneName = sceneName;
		this.isSharing = isSharing;
		this.cellRelateCount = cellRelateCount;
		this.isExistGeojson = isExistGeojson;
	}

}
