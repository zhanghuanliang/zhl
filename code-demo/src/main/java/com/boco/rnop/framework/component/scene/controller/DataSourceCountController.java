package com.boco.rnop.framework.component.scene.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boco.rnop.framework.common.ResponseMessage2;
import com.boco.rnop.framework.common.util.IdUtil;
import com.boco.rnop.framework.component.scene.entity.CascaderInfo;
import com.boco.rnop.framework.component.scene.entity.po.BaiduAkCountInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.BaiduAkInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.CitySceneDataCountPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneTypeDataCountPO;
import com.boco.rnop.framework.component.scene.entity.qo.BaiduAkQO;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3GisGeometry;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3MapAkType;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3PoiTypeRelation;
import com.boco.rnop.framework.component.scene.service.IBaiduSourseService;
import com.boco.rnop.framework.component.scene.service.IDataSourceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 场景管理-数据资源统计
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2019年4月4日 上午9:58:28
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@RestController
@RequestMapping("/SceneDataSourceCount")
@Api(tags = "场景管理-数据资源统计API")
public class DataSourceCountController {

	@Autowired
	private IDataSourceService dataSourceService;
	@Autowired
	private IBaiduSourseService baiduSourseService;

	/**
	 * 统计各地市地理场景数据
	 * 
	 * @return
	 */
	@ApiOperation(value = "统计各地市地理场景数据", notes = "统计各地市地理场景数据")
	@GetMapping(value = "/countSceneDataByRegion")
	public ResponseMessage2<List<CitySceneDataCountPO>> countSceneDataByRegion() {
		return ResponseMessage2.Success2(dataSourceService.countSceneDataByRegion());

	}

	/**
	 * 
	 * Title: selectSceneDataCount Description: 全省各类型场景数据量查询
	 * 
	 * @param provinceId
	 * @return
	 */
	@ApiOperation(value = "统计各类型地理场景数据量", notes = "统计各类型地理场景数据量")
	@RequestMapping(value = "/countSceneDataByType", method = RequestMethod.GET)
	public ResponseMessage2<List<SceneTypeDataCountPO>> countSceneDataByType() {
		return ResponseMessage2.Success2(dataSourceService.countSceneDataByType());
	}

	/**
	 * 统计百度ak消耗信息
	 * 
	 * @return
	 */
	@ApiOperation(value = "百度Ak消耗信息", notes = "百度Ak消耗信息")
	@RequestMapping(value = "/countBaiduAk", method = RequestMethod.GET)
	public ResponseMessage2<BaiduAkCountInfoPO> countBaiduAk() {
		return ResponseMessage2.Success2(baiduSourseService.countBaiduAk());
	}

	/**
	 * 新增一条百度ak信息
	 * 
	 * @param baiduAkQO
	 * @return
	 */
	@ApiOperation(value = "新增一条百度ak信息", notes = "新增一条百度ak信息")
	@PostMapping(value = "/insertBaiduAk")
	public ResponseMessage2<Integer> insertBaiduAk(@RequestBody BaiduAkQO baiduAkQO) {
		Integer id = IdUtil.getIntCRC32();
		baiduAkQO.setIntId(id);
		baiduAkQO.setUpdateDate(new Date());
		return ResponseMessage2.Success2(baiduSourseService.insertBaiduAk(baiduAkQO));
	}

	@ApiOperation(value = "删除Ak信息", notes = "删除Ak信息")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "intId", value = "Ak编号", required = true, dataType = "Long", paramType = "query") })
	@GetMapping(value = "/deleteAkInfo")
	public ResponseMessage2<Integer> deleteAkInfo(Long intId) {
		return ResponseMessage2.Success2(baiduSourseService.deleteAkInfo(intId));
	}

	/**
	 * 查询所有Ak信息
	 * 
	 * @return
	 */
	@ApiOperation(value = "查询所有Ak信息", notes = "查询所有Ak信息")
	@GetMapping(value = "/selectAllAkInfo")
	public ResponseMessage2<List<BaiduAkInfoPO>> selectAllAkInfo() {
		return ResponseMessage2.Success2(dataSourceService.selectAllAkInfo());
	}

	/**
	 * 查询Ak类型信息
	 * 
	 * @return
	 */
	@ApiOperation(value = "查询Ak类型信息", notes = "查询Ak类型信息")
	@GetMapping(value = "/selectAkTypeInfo")
	public ResponseMessage2<List<Tv3MapAkType>> selectAkTypeInfo() {
		return ResponseMessage2.Success2(dataSourceService.selectAkTypeInfo());
	}

	@ApiOperation(value = "查询百度poi类型信息（级联选择器）", notes = "查询百度poi类型信息（级联选择器）")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "parentId", value = "父类型编号（一级类型-1）", required = true, dataType = "Long", paramType = "query") })
	@GetMapping(value = "/selectBaiduPoiTypeInfo")
	public ResponseMessage2<List<CascaderInfo>> selectBaiduPoiTypeInfo(Long parentId) {

		return ResponseMessage2.Success2(dataSourceService.selectAllBaiduPoiTypeInfo(parentId));
	}

	@ApiOperation(value = "新增网优场景字典信息", notes = "新增网优场景字典信息")
	@PostMapping(value = "/insertSceneTypeDictionaryInfo")
	public ResponseMessage2<Integer> insertSceneTypeDictionaryInfo(@RequestBody List<Tv3PoiTypeRelation> list) {

		return ResponseMessage2.Success2(dataSourceService.insertSceneTypeDictionaryInfo(list));
	}

	@ApiOperation(value = "删除网优场景字典信息", notes = "删除网优场景字典信息")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "intId", value = "字典编号", required = true, dataType = "Long", paramType = "query") })
	@GetMapping(value = "/deleteSceneTypeDictionaryInfo")
	public ResponseMessage2<Integer> deleteSceneTypeDictionaryInfo(Long intId) {

		return ResponseMessage2.Success2(dataSourceService.deleteSceneTypeDictionaryInfo(intId));
	}

	@ApiOperation(value = "修改网优场景字典信息", notes = "修改网优场景字典信息")
	@PostMapping(value = "/updateSceneTypeDictionaryInfo")
	public ResponseMessage2<Integer> updateSceneTypeDictionaryInfo(@RequestBody Tv3PoiTypeRelation tv3PoiTypeRelation) {

		return ResponseMessage2.Success2(dataSourceService.updateSceneTypeDictionaryInfo(tv3PoiTypeRelation));
	}

	@ApiOperation(value = "查询网优场景字典信息", notes = "查询网优场景字典信息")
	@GetMapping(value = "/selectSceneTypeInfo")
	public ResponseMessage2<List<Tv3PoiTypeRelation>> selectSceneTypeDictionaryInfo() {

		return ResponseMessage2.Success2(dataSourceService.selectSceneTypeDictionaryInfo());
	}

	@ApiOperation(value = "查询行政区轮廓", notes = "查询行政区轮廓")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "regionName", value = "地市名称", required = true, dataType = "String", paramType = "query") })
	@GetMapping(value = "/selectRegionOutline")
	public ResponseMessage2<Tv3GisGeometry> selectRegionOutline(String regionName) {

		return ResponseMessage2.Success2(dataSourceService.selectRegionOutline(regionName));
	}

	@ApiOperation(value = "新增行政区轮廓", notes = "新增行政区轮廓")
	@PostMapping(value = "/insertRegionOutline")
	public ResponseMessage2<Integer> insertRegionOutline(@RequestBody Tv3GisGeometry tv3GisGeometry) {

		return ResponseMessage2.Success2(dataSourceService.insertRegionOutline(tv3GisGeometry));
	}

}
