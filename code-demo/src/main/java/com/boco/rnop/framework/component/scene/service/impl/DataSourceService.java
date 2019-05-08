package com.boco.rnop.framework.component.scene.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boco.rnop.framework.common.util.IdUtil;
import com.boco.rnop.framework.component.scene.entity.CascaderInfo;
import com.boco.rnop.framework.component.scene.entity.po.BaiduAkInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.CitySceneDataCountPO;
import com.boco.rnop.framework.component.scene.entity.po.SceneTypeDataCountPO;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3GisGeometry;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3MapAkType;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3MapBdPoiType;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3PoiTypeRelation;
import com.boco.rnop.framework.component.scene.mapper.def.IBaiduSourseMapper;
import com.boco.rnop.framework.component.scene.mapper.def.IDataSourceMapper;
import com.boco.rnop.framework.component.scene.mapper.def.ITv3GisGeometryMapper;
import com.boco.rnop.framework.component.scene.mapper.def.ITv3MapAkMapper;
import com.boco.rnop.framework.component.scene.mapper.def.ITv3MapAkTypeMapper;
import com.boco.rnop.framework.component.scene.mapper.def.ITv3MapBdPoiTypeMapper;
import com.boco.rnop.framework.component.scene.mapper.def.ITv3PoiTypeRelationMapper;
import com.boco.rnop.framework.component.scene.service.IDataSourceService;

/**
 * 
 * 场景数据资源统计
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年12月10日 下午4:33:02
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Service
public class DataSourceService implements IDataSourceService {

	@Autowired
	private IDataSourceMapper dataSourceMapper;
	@Autowired
	private IBaiduSourseMapper baiduSourseMapper;
	@Autowired
	private ITv3MapAkMapper tv3GisAkMapper;
	@Autowired
	private ITv3MapAkTypeMapper tv3GisAkTypeMapper;
	@Autowired
	private ITv3MapBdPoiTypeMapper tv3MapBdPoiTypeMapper;
	@Autowired
	private ITv3PoiTypeRelationMapper tv3GisDictionaryMapper;
	@Autowired
	private ITv3GisGeometryMapper tv3GisGeometryMapper;

	@Override
	public List<SceneTypeDataCountPO> countSceneDataByType() {

		return dataSourceMapper.countSceneDataByType();
	}

	@Override
	public List<CitySceneDataCountPO> countSceneDataByRegion() {

		return dataSourceMapper.countSceneDataByRegion();
	}

	@Override
	public List<BaiduAkInfoPO> selectAllAkInfo() {
		return baiduSourseMapper.selectAllBaiduAk();
	}

	@Override
	public List<CascaderInfo> selectAllBaiduPoiTypeInfo(Long parentId) {
		List<CascaderInfo> result = new ArrayList<>();
		Tv3MapBdPoiType record = new Tv3MapBdPoiType();
		record.setParentId(parentId);
		List<Tv3MapBdPoiType> tv3MapBdPoiTypes = tv3MapBdPoiTypeMapper.select(record);
		for (Tv3MapBdPoiType tv3MapBdPoiType : tv3MapBdPoiTypes) {
			CascaderInfo cascaderInfo = new CascaderInfo();
			cascaderInfo.setLabel(tv3MapBdPoiType.getPoiTypeName());
			cascaderInfo.setValue(tv3MapBdPoiType.getIntId());
			result.add(cascaderInfo);
		}
		return result;
	}

	@Transactional
	@Override
	public Integer insertSceneTypeDictionaryInfo(List<Tv3PoiTypeRelation> list) {
		int resultCount = 0;
		for (Tv3PoiTypeRelation tv3GisDictionary : list) {
			tv3GisDictionary.setIntId(new Long(IdUtil.getIntCRC32()));
			// TODO optr
			// tv3GisDictionaryPO.setOptr(optr);
		}

		for (Tv3PoiTypeRelation tv3GisDictionaryPO : list) {
			resultCount += tv3GisDictionaryMapper.insert(tv3GisDictionaryPO);
		}

		return resultCount;
	}

	@Override
	public List<Tv3PoiTypeRelation> selectSceneTypeDictionaryInfo() {

		return tv3GisDictionaryMapper.selectAll();
	}

	@Override
	public Tv3GisGeometry selectRegionOutline(String regionName) {
		Tv3GisGeometry tv3GisGeometry = new Tv3GisGeometry();
		tv3GisGeometry.setRegionName(regionName);

		return tv3GisGeometryMapper.selectOne(tv3GisGeometry);
	}

	/**
	 * 新增行政区轮廓
	 */
	@Override
	public Integer insertRegionOutline(Tv3GisGeometry tv3GisGeometry) {
		tv3GisGeometry.setGeometryType(3);
		tv3GisGeometry.setGeometryTypeName("行政区边界");
		tv3GisGeometry.setIntId(IdUtil.getLongUUID());
		return tv3GisGeometryMapper.insert(tv3GisGeometry);
	}

	@Override
	public List<Tv3MapAkType> selectAkTypeInfo() {

		return tv3GisAkTypeMapper.selectAll();
	}

	@Override
	public Integer deleteSceneTypeDictionaryInfo(Long intId) {
		return tv3GisDictionaryMapper.deleteByPrimaryKey(intId);
	}

	@Override
	public Integer updateSceneTypeDictionaryInfo(Tv3PoiTypeRelation tv3PoiTypeRelation) {

		return tv3GisDictionaryMapper.updateByPrimaryKeySelective(tv3PoiTypeRelation);
	}

}
