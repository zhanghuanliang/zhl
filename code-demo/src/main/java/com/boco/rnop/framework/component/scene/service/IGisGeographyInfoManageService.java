package com.boco.rnop.framework.component.scene.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.boco.rnop.framework.common.ResponseMessage2;
import com.boco.rnop.framework.component.scene.entity.SelectGisGeographyInfoWebPar;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3GisGeometry;
import com.boco.rnop.framework.component.scene.entity.tk.Tv3GisNumDictionary;
import com.github.pagehelper.PageInfo;

/**
 * 
 * Gis地理信息管理
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2019年1月14日 下午4:29:04
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public interface IGisGeographyInfoManageService {
	/**
	 * 查询Gis地理信息
	 * 
	 * @param regionIds
	 * @param geometryTypes
	 * @param infoType
	 * @param infoType2
	 * @param name
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public ResponseMessage2<PageInfo<Tv3GisGeometry>> selectGisGeographyInfo(SelectGisGeographyInfoWebPar webPar);

	/**
	 * 更新地理图元信息
	 * 
	 * @param tv3GisGeometries
	 * @return
	 */
	public ResponseMessage2<Integer> updateGeometryInfos(List<Tv3GisGeometry> tv3GisGeometries);

	/**
	 * 根据图层文件导出区域数据信息csv
	 * 
	 * @param fieldDescriptionInfos
	 * @param regionId
	 * @param regionName
	 * @param subtype
	 * @param files
	 * @param response
	 */
	public void exportGeometryDataByLayerFile(String fieldDescriptionInfos, Long regionId, String regionName,
			Integer subtype, MultipartFile[] files, HttpServletResponse response);

	/**
	 * 根据图层文件导出栅格数据信息csv
	 * 
	 * @param fieldDescriptionInfos
	 * @param regionId
	 * @param regionName
	 * @param subtype
	 * @param step
	 * @param extendWidth
	 * @param gridLength
	 * @param files
	 * @param response
	 */
	public void exportGridDataByLayerFile(String fieldDescriptionInfos, Long regionId, String regionName,
			Integer subtype, Integer step, Integer extendWidth, Integer gridLength, MultipartFile[] files,
			HttpServletResponse response);

	/**
	 * 查询Geometry类型
	 * 
	 * @return
	 */
	public ResponseMessage2<List<Tv3GisNumDictionary>> selectGeometryType();

	/**
	 * 删除地理图元信息
	 * 
	 * @param intIds
	 * @return
	 */
	public ResponseMessage2<Integer> deleteGeometryInfos(List<Long> intIds);

}
