package com.boco.rnop.framework.component.scene.util;

import com.boco.rnop.framework.common.util.JsonUtil;
import com.boco.rnop.framework.component.scene.entity.SceneCrawlingDataGeoObject;
import com.boco.rnop.framework.component.scene.entity.SceneCrawlingDataMultipleGeoObject;
import com.boco.rnop.framework.component.scene.entity.SceneCrawlingDataSingleGeoObject;
import com.boco.rnop.framework.component.scene.entity.SceneCrawlingDataVarietyGeoObject;

/**
 * 
 * geojson解析工具类
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月28日 上午11:16:11
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public class GeojsonUtil {

	/**
	 * 解析geojson
	 * 
	 * @return
	 */
	public static SceneCrawlingDataGeoObject getSceneCrawlingDataGeoObject(String geometryJson) {
		SceneCrawlingDataGeoObject sceneCrawlingDataGeoObject = null;
		try {
			sceneCrawlingDataGeoObject = JsonUtil.fromJson(SceneCrawlingDataSingleGeoObject.class, geometryJson);
			if (sceneCrawlingDataGeoObject != null) {
				return sceneCrawlingDataGeoObject;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			sceneCrawlingDataGeoObject = JsonUtil.fromJson(SceneCrawlingDataMultipleGeoObject.class, geometryJson);
			if (sceneCrawlingDataGeoObject != null) {
				return sceneCrawlingDataGeoObject;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			sceneCrawlingDataGeoObject = JsonUtil.fromJson(SceneCrawlingDataVarietyGeoObject.class, geometryJson);
			if (sceneCrawlingDataGeoObject != null) {
				return sceneCrawlingDataGeoObject;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sceneCrawlingDataGeoObject;
	}

}
