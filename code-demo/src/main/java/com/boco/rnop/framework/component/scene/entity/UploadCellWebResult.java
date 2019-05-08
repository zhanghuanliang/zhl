package com.boco.rnop.framework.component.scene.entity;

import com.boco.rnop.framework.component.scene.entity.tk.Tv3SceneCell;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 上传关联小区返回前端结果信息
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月18日 上午8:59:05
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Data
public class UploadCellWebResult extends Tv3SceneCell {

	@ApiModelProperty(value = "是否已存在")
	private Boolean isExisted;
}
