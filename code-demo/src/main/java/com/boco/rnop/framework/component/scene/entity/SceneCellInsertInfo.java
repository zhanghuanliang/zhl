package com.boco.rnop.framework.component.scene.entity;

import java.util.List;

import com.boco.rnop.framework.component.scene.entity.tk.Tv3SceneCell;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3SceneInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 场景小区新增信息
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2019年1月8日 上午8:50:38
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@ApiModel(description = "场景小区新增信息")
@Data
public class SceneCellInsertInfo {
	/**
	 * 场景信息
	 */
	@ApiModelProperty(value = "场景信息")
	private Tv3SceneInfo tv3SceneInfo;
	/**
	 * 场景关联小区信息
	 */
	@ApiModelProperty(value = "场景关联小区信息")
	private List<Tv3SceneCell> tv3GisCellrelateGeos;

}
