package com.boco.rnop.framework.component.scene.entity;

import java.util.List;

import com.boco.rnop.framework.component.scene.entity.po.SceneDetailedInfoPO;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3SceneCell;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * excel场景信息返回前端信息
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2019年1月3日 上午9:58:03
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@ApiModel(description = "excel场景信息返回前端信息")
public class Tv3SceneExcelWebInfo extends SceneDetailedInfoPO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "是否已存在")
	private Boolean isExist;
	@ApiModelProperty(value = "信息是否详尽（父场景、根场景）")
	private Boolean isFull;
	@ApiModelProperty(value = "关联小区信息")
	private List<Tv3SceneCell> tv3GisCellrelateGeos;

	public Boolean getIsExist() {
		return isExist;
	}

	public void setIsExist(Boolean isExist) {
		this.isExist = isExist;
	}

	public Boolean getIsFull() {
		return isFull;
	}

	public void setIsFull(Boolean isFull) {
		this.isFull = isFull;
	}

	public List<Tv3SceneCell> getTv3GisCellrelateGeos() {
		return tv3GisCellrelateGeos;
	}

	public void setTv3GisCellrelateGeos(List<Tv3SceneCell> tv3GisCellrelateGeos) {
		this.tv3GisCellrelateGeos = tv3GisCellrelateGeos;
	}

}
