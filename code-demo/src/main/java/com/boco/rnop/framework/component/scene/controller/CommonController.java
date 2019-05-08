package com.boco.rnop.framework.component.scene.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boco.rnop.framework.common.ResponseMessage2;
import com.boco.rnop.framework.component.scene.entity.po.RegionCityPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneTypePO;
import com.boco.rnop.framework.component.scene.service.ICommonService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 场景管理-通用
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月10日 下午3:11:25
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@RestController
@RequestMapping("/SceneCommon")
@Api(tags = "场景管理-通用API")
public class CommonController {
	@Autowired
	private ICommonService commonService;

	/**
	 * 查询地市
	 * 
	 * @param provinceId
	 * @return
	 */
	@ApiOperation(value = "查询地市（不带全省）", notes = "查询地市（不带全省）")
	@GetMapping(value = "/selectRegion")
	public ResponseMessage2<List<RegionCityPO>> selectRegion() {
		return ResponseMessage2.Success2(commonService.selectRegion());
	}

	@ApiOperation(value = "查询地市（带全省）", notes = "查询地市（带全省）")
	@GetMapping(value = "/selectRegion")
	public ResponseMessage2<List<RegionCityPO>> selectRegionAndAll() {
		return ResponseMessage2.Success2(commonService.selectRegionAndAll());
	}

	/**
	 * 查询场景类型
	 * 
	 * @return
	 */
	@ApiOperation(value = "查询类型字典表网优场景类型", notes = "查询类型字典表网优场景类型")
	@GetMapping(value = "/selectSceneType")
	public ResponseMessage2<List<SceneTypePO>> selectSceneType() {
		return ResponseMessage2.Success2(commonService.selectSceneType());
	}

}
