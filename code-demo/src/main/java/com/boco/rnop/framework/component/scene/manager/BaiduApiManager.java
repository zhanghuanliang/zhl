package com.boco.rnop.framework.component.scene.manager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.boco.com.framework.gis.engine.EngineTransCoord;
import com.boco.com.framework.gis.engine.geometrys.EnginePoint;
import com.boco.com.framework.gis.engine.geometrys.MilitaryGrid;
import com.boco.rnop.framework.common.util.JsonUtil;
import com.boco.rnop.framework.component.scene.entity.baidu.api.BaiduGeoconvApiResponseMessage;
import com.boco.rnop.framework.component.scene.entity.baidu.api.BaiduPlaceSearchApiResponseMessage;
import com.boco.rnop.framework.component.scene.entity.baidu.api.BaiduPlaceSearchApiResult;
import com.boco.rnop.framework.component.scene.entity.baidu.api.BaiduSceneCrawlingApiMessage;
import com.boco.rnop.framework.component.scene.entity.baidu.api.BaiduSceneGeoApiResponseMessage;
import com.boco.rnop.framework.component.scene.entity.po.BaiduAkInfoPO;
import com.boco.rnop.framework.component.scene.exception.BaiduApiHttpFalseException;
import com.boco.rnop.framework.component.scene.exception.NoAvailableBaiduAkException;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 百度API管理器
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月18日 上午10:43:10
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */

@Component
@Slf4j
public class BaiduApiManager {
	/**
	 * 地点检索服务
	 */
	@Value(value = "${gis.api.baiduPlaceSearch:http://api.map.baidu.com/place/v2/search}")
	private String baiduPlaceSearchApi;
	/**
	 * 地点详情检索服务
	 */
	@Value("${gis.api.baiduPlaceDetail:http://api.map.baidu.com/place/v2/detail}")
	private String baiduPlaceDetailApi;
	/**
	 * 地理编码
	 */
	@Value("${gis.api.baiduGeocoder:http://api.map.baidu.com/geocoder/v2/}")
	private String baiduGeocoderApi;
	/**
	 * poi场景服务
	 */
	@Value("${gis.api.baiduScene:http://map.baidu.com/}")
	private String baiduSceneApi;
	/**
	 * 坐标转换服务
	 */
	@Value("${gis.api.baiduGeoconv:http://api.map.baidu.com/geoconv/v1/}")
	private String baiduGeoconv;

	/**
	 * getHttp请求
	 * 
	 * @param apiUrl
	 * @param getHttpData
	 * @return
	 */
	private String sendGetRequest(String apiUrl, Map<String, String> httpData) {
		HttpURLConnection httpURLConnection = null;
		InputStream is = null;
		// StringBuilder:线程非安全，可有多线程采用，速度比StingBuffer快,用法同StringBuffer
		// StringBuffer:线程安全，只能单线程采用
		StringBuilder sb = new StringBuilder();
		try {
			StringBuffer urlPath = new StringBuffer(apiUrl + "?");
			for (Entry<String, String> entry : httpData.entrySet()) {
				urlPath.append(entry.getKey() + "=" + entry.getValue() + "&");
			}
			log.info(urlPath.toString());
			// 准备请求的网络地址
			URL url = new URL(urlPath.toString());
			// 调用openConnection得到网络连接，网络连接处于就绪状态
			httpURLConnection = (HttpURLConnection) url.openConnection();
			// 设置网络连接超时时间5S
			httpURLConnection.setConnectTimeout(5 * 1000);
			// 设置读取超时时间
			httpURLConnection.setReadTimeout(5 * 1000);
			httpURLConnection.connect();
			// if连接请求码成功
			if (httpURLConnection.getResponseCode() == 200) {
				is = httpURLConnection.getInputStream();
				byte[] bytes = new byte[1024];
				int i = 0;
				while ((i = is.read(bytes)) != -1) {
					sb.append(new String(bytes, 0, i, "utf-8"));
				}
				is.close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
		log.info(sb.toString());
		return sb.toString();
	}

	/**
	 * 地点检索-行政区划区域检索
	 * 
	 * @param query
	 *            检索关键字。圆形区域检索和矩形区域内检索支持多个关键字并集检索，不同关键字间以$符号分隔，最多支持10个关键字检索。如:”银行$酒店”
	 *            如果需要按POI分类进行检索，请将分类通过query参数进行设置，如query=美食
	 * @param region
	 *            检索行政区划区域（增加区域内数据召回权重，如需严格限制召回数据在区域内，请搭配使用city_limit参数），可输入行政区划名或对应
	 * @param ak
	 *            开发者的访问密钥，必填项。v2之前该属性为key。
	 * @param pageNum
	 *            分页页码，默认为0,0代表第一页，1代表第二页，以此类推。 常与page_size搭配使用。
	 * @param page_size
	 *            单次召回POI数量，默认为10条记录，最大返回20条。多关键字检索时，返回的记录数为关键字个数*page_size。
	 * @return
	 * @throws BaiduApiHttpFalseException
	 */
	public BaiduPlaceSearchApiResponseMessage getBaiduPlaceSearchApiResponseMessage(String query, String region,
			String ak, String pageNum, String pageSize) throws BaiduApiHttpFalseException {
		BaiduPlaceSearchApiResponseMessage baiduPlaceSearchApiResponseMessage = null;
		Map<String, String> httpData = new HashMap<>();
		httpData.put("query", query);
		httpData.put("region", region);
		httpData.put("output", "json");
		httpData.put("scope", "2");
		httpData.put("page_num", pageNum);
		httpData.put("page_size", pageSize);
		httpData.put("ak", ak);
		String json = sendGetRequest(baiduPlaceSearchApi, httpData);
		try {
			baiduPlaceSearchApiResponseMessage = JsonUtil.fromJson(BaiduPlaceSearchApiResponseMessage.class, json);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaiduApiHttpFalseException("地点检索-行政区划区域检索Http请求失败！");
		}
		return baiduPlaceSearchApiResponseMessage;
	}

	/**
	 * 地点检索-行政区划区域检索(无秘钥)
	 * 
	 * @param query
	 *            检索关键字。圆形区域检索和矩形区域内检索支持多个关键字并集检索，不同关键字间以$符号分隔，最多支持10个关键字检索。如:”银行$酒店”
	 *            如果需要按POI分类进行检索，请将分类通过query参数进行设置，如query=美食
	 * @param region
	 *            检索行政区划区域（增加区域内数据召回权重，如需严格限制召回数据在区域内，请搭配使用city_limit参数），可输入行政区划名或对应
	 * @param pageNum
	 *            分页页码，默认为0,0代表第一页，1代表第二页，以此类推。 常与page_size搭配使用。
	 * @param page_size
	 *            单次召回POI数量，默认为10条记录，最大返回20条。多关键字检索时，返回的记录数为关键字个数*page_size。
	 * @return
	 * @throws BaiduApiHttpFalseException
	 * @throws NoAvailableBaiduAkException
	 */
	public BaiduPlaceSearchApiResponseMessage getBaiduPlaceSearchApiResponseMessage(String query, String region,
			String pageNum, String pageSize) throws BaiduApiHttpFalseException, NoAvailableBaiduAkException {
		BaiduAkInfoPO akInfoPO = BaiduSourseManager.getRetrievalBaiduAkInfo();
		if (akInfoPO == null) {
			throw new NoAvailableBaiduAkException("无可用地点检索AK!");
		}
		BaiduPlaceSearchApiResponseMessage baiduPlaceSearchApiResponseMessage;
		try {
			baiduPlaceSearchApiResponseMessage = getBaiduPlaceSearchApiResponseMessage(query, region, akInfoPO.getAk(),
					pageNum, pageSize);
		} finally {
			akInfoPO.setAkRetrievalTimes(akInfoPO.getAkRetrievalTimes() - 1);
			akInfoPO.setUpdateDate(new Date());
		}
		return baiduPlaceSearchApiResponseMessage;
	}

	/**
	 * 地点检索-矩形区域检索
	 * 
	 * @param query
	 *            检索关键字。圆形区域检索和矩形区域内检索支持多个关键字并集检索，不同关键字间以$符号分隔，最多支持10个关键字检索。如:”银行$酒店”
	 *            如果需要按POI分类进行检索，请将分类通过query参数进行设置，如query=美食
	 * @param bounds
	 *            检索矩形区域，多组坐标间以","分隔，如：38.76623,116.43213,39.54321,116.46773
	 *            lat,lng(左下角坐标),lat,lng(右上角坐标)
	 * @param ak
	 *            开发者的访问密钥，必填项。v2之前该属性为key。
	 * @param pageNum
	 *            分页页码，默认为0,0代表第一页，1代表第二页，以此类推。 常与page_size搭配使用。
	 * @param pageSize
	 *            单次召回POI数量，默认为10条记录，最大返回20条。多关键字检索时，返回的记录数为关键字个数*page_size。
	 * @return
	 * @throws BaiduApiHttpFalseException
	 */
	public BaiduPlaceSearchApiResponseMessage getBaiduRectanglePlaceSearchApiResponseMessage(String query,
			String bounds, String ak, String pageNum, String pageSize) throws BaiduApiHttpFalseException {
		BaiduPlaceSearchApiResponseMessage baiduPlaceSearchApiResponseMessage = null;
		Map<String, String> httpData = new HashMap<>();
		httpData.put("query", query);
		httpData.put("bounds", bounds);
		httpData.put("output", "json");
		httpData.put("scope", "2");
		httpData.put("page_num", pageNum);
		httpData.put("page_size", pageSize);
		httpData.put("ak", ak);
		String json = sendGetRequest(baiduPlaceSearchApi, httpData);
		try {
			baiduPlaceSearchApiResponseMessage = JsonUtil.fromJson(BaiduPlaceSearchApiResponseMessage.class, json);
		} catch (Exception e) {
			e.printStackTrace();
			BaiduApiHttpFalseException baiduApiHttpFalseException = new BaiduApiHttpFalseException(
					"地点检索-行政区划区域检索Http请求失败！");
			throw baiduApiHttpFalseException;
		}
		return baiduPlaceSearchApiResponseMessage;
	}

	/**
	 * 地点检索-矩形区域检索(无ak)
	 * 
	 * @param query
	 *            检索关键字。圆形区域检索和矩形区域内检索支持多个关键字并集检索，不同关键字间以$符号分隔，最多支持10个关键字检索。如:”银行$酒店”
	 *            如果需要按POI分类进行检索，请将分类通过query参数进行设置，如query=美食
	 * @param bounds
	 *            检索矩形区域，多组坐标间以","分隔，如：38.76623,116.43213,39.54321,116.46773
	 *            lat,lng(左下角坐标),lat,lng(右上角坐标)
	 * @param pageNum
	 *            分页页码，默认为0,0代表第一页，1代表第二页，以此类推。 常与page_size搭配使用。
	 * @param pageSize
	 *            单次召回POI数量，默认为10条记录，最大返回20条。多关键字检索时，返回的记录数为关键字个数*page_size。
	 * @return
	 * @throws BaiduApiHttpFalseException
	 * @throws NoAvailableBaiduAkException
	 */
	public BaiduPlaceSearchApiResponseMessage getBaiduRectanglePlaceSearchApiResponseMessage(String query,
			String bounds, String pageNum, String pageSize)
			throws BaiduApiHttpFalseException, NoAvailableBaiduAkException {
		BaiduAkInfoPO akInfoPO = BaiduSourseManager.getRetrievalBaiduAkInfo();
		if (akInfoPO == null) {
			throw new NoAvailableBaiduAkException("无可用地点检索AK!");
		}
		BaiduPlaceSearchApiResponseMessage baiduPlaceSearchApiResponseMessage;
		try {
			baiduPlaceSearchApiResponseMessage = getBaiduRectanglePlaceSearchApiResponseMessage(query, bounds,
					akInfoPO.getAk(), pageNum, pageSize);
		} finally {
			akInfoPO.setAkRetrievalTimes(akInfoPO.getAkRetrievalTimes() - 1);
			akInfoPO.setUpdateDate(new Date());
		}
		return baiduPlaceSearchApiResponseMessage;
	}

	/**
	 * 地点检索-矩形区域检索(无ak,不传分页信息，连续查询，直到无法查询到信息)
	 * 
	 * @param query
	 *            检索关键字。圆形区域检索和矩形区域内检索支持多个关键字并集检索，不同关键字间以$符号分隔，最多支持10个关键字检索。如:”银行$酒店”
	 *            如果需要按POI分类进行检索，请将分类通过query参数进行设置，如query=美食
	 * @param bounds
	 *            检索矩形区域，多组坐标间以","分隔，如：38.76623,116.43213,39.54321,116.46773
	 *            lat,lng(左下角坐标),lat,lng(右上角坐标)
	 * @return
	 * @throws BaiduApiHttpFalseException
	 * @throws NoAvailableBaiduAkException
	 */
	public List<BaiduPlaceSearchApiResponseMessage> getBaiduRectanglePlaceSearchApiResponseMessage(String query,
			String bounds) throws BaiduApiHttpFalseException, NoAvailableBaiduAkException {
		BaiduAkInfoPO akInfoPO = BaiduSourseManager.getRetrievalBaiduAkInfo();
		if (akInfoPO == null) {
			throw new NoAvailableBaiduAkException("无可用地点检索AK!");
		}
		// 检索结果集
		List<BaiduPlaceSearchApiResponseMessage> list = new ArrayList<>();

		BaiduPlaceSearchApiResponseMessage baiduPlaceSearchApiResponseMessage;

		try {

			baiduPlaceSearchApiResponseMessage = getBaiduRectanglePlaceSearchApiResponseMessage(query, bounds,
					akInfoPO.getAk(), "0", "20");
			list.add(baiduPlaceSearchApiResponseMessage);

			int pageNum = 1;
			while (baiduPlaceSearchApiResponseMessage != null && baiduPlaceSearchApiResponseMessage.getResults() != null
					&& baiduPlaceSearchApiResponseMessage.getResults().size() == 20) {
				log.info("请求第" + pageNum + "页数据！");
				baiduPlaceSearchApiResponseMessage = getBaiduRectanglePlaceSearchApiResponseMessage(query, bounds,
						akInfoPO.getAk(), pageNum + "", "20");
				list.add(baiduPlaceSearchApiResponseMessage);
				pageNum++;
			}

		} finally {
			akInfoPO.setAkRetrievalTimes(akInfoPO.getAkRetrievalTimes() - 1);
			akInfoPO.setUpdateDate(new Date());
		}
		return list;
	}

	/**
	 * 地点检索-军事栅格矩形区域检索(无ak)
	 * 
	 * @param query
	 *            检索关键字。圆形区域检索和矩形区域内检索支持多个关键字并集检索，不同关键字间以$符号分隔，最多支持10个关键字检索。如:”银行$酒店”
	 *            如果需要按POI分类进行检索，请将分类通过query参数进行设置，如query=美食
	 * @param militaryGrid
	 *            军事栅格
	 * @param pageNum
	 *            分页页码，默认为0,0代表第一页，1代表第二页，以此类推。 常与page_size搭配使用。
	 * @param pageSize
	 *            单次召回POI数量，默认为10条记录，最大返回20条。多关键字检索时，返回的记录数为关键字个数*page_size。
	 * @return
	 * @throws BaiduApiHttpFalseException
	 * @throws NoAvailableBaiduAkException
	 */
	public BaiduPlaceSearchApiResponseMessage getBaiduRectanglePlaceSearchApiResponseMessage(String query,
			MilitaryGrid militaryGrid, String pageNum, String pageSize)
			throws BaiduApiHttpFalseException, NoAvailableBaiduAkException {
		EnginePoint lbPoint = EngineTransCoord.WGS84ToBD(militaryGrid.getPoint_lb());
		EnginePoint ruPoint = EngineTransCoord.WGS84ToBD(militaryGrid.getPoint_ru());
		String bounds = lbPoint.getY() + "," + lbPoint.getX() + "," + ruPoint.getY() + "," + ruPoint.getX();
		return getBaiduRectanglePlaceSearchApiResponseMessage(query, bounds, pageNum, pageSize);
	}

	/**
	 * 地点检索-军事栅格矩形区域检索(无ak,,不传分页信息，连续查询，直到无法查询到信息)
	 * 
	 * @param query
	 *            检索关键字。圆形区域检索和矩形区域内检索支持多个关键字并集检索，不同关键字间以$符号分隔，最多支持10个关键字检索。如:”银行$酒店”
	 *            如果需要按POI分类进行检索，请将分类通过query参数进行设置，如query=美食
	 * @param militaryGrid
	 *            军事栅格
	 * @return
	 * @throws BaiduApiHttpFalseException
	 * @throws NoAvailableBaiduAkException
	 */
	public List<BaiduPlaceSearchApiResponseMessage> getBaiduRectanglePlaceSearchApiResponseMessage(String query,
			MilitaryGrid militaryGrid) throws BaiduApiHttpFalseException, NoAvailableBaiduAkException {
		EnginePoint lbPoint = EngineTransCoord.WGS84ToBD(militaryGrid.getPoint_lb());
		EnginePoint ruPoint = EngineTransCoord.WGS84ToBD(militaryGrid.getPoint_ru());
		String bounds = lbPoint.getY() + "," + lbPoint.getX() + "," + ruPoint.getY() + "," + ruPoint.getX();
		return getBaiduRectanglePlaceSearchApiResponseMessage(query, bounds);
	}

	/**
	 * 获取场景geo轮廓点
	 * 
	 * @param uid
	 * @return
	 * @throws BaiduApiHttpFalseException
	 */
	public BaiduSceneGeoApiResponseMessage getBaiduSceneGeoApiResponseMessage(String uid)
			throws BaiduApiHttpFalseException {
		BaiduSceneGeoApiResponseMessage baiduSceneGeoApiResponseMessage = null;
		Map<String, String> httpData = new HashMap<>();
		httpData.put("reqflag", "pcmap");
		httpData.put("from", "webmap");
		httpData.put("qt", "ext");
		httpData.put("uid", uid);
		httpData.put("ext_ver", "new");
		httpData.put("l", "18");
		String json = sendGetRequest(baiduSceneApi, httpData);
		try {
			baiduSceneGeoApiResponseMessage = JsonUtil.fromJson(BaiduSceneGeoApiResponseMessage.class, json);
		} catch (Exception e) {
			e.printStackTrace();
			BaiduApiHttpFalseException baiduApiHttpFalseException = new BaiduApiHttpFalseException(
					"获取场景geo轮廓点Http请求失败！");
			throw baiduApiHttpFalseException;
		}
		return baiduSceneGeoApiResponseMessage;
	}

	/**
	 * 获取坐标转换结果
	 * 
	 * @param coords
	 *            需转换的源坐标，多组坐标以“；”分隔 （经度，纬度） 114.21892734521,29.575429778924
	 * @param from
	 *            源坐标类型： 1：GPS设备获取的角度坐标，WGS84坐标; 2：GPS获取的米制坐标、sogou地图所用坐标;
	 *            3：google地图、soso地图、aliyun地图、mapabc地图和amap地图所用坐标，国测局（GCJ02）坐标;
	 *            4：3中列表地图坐标对应的米制坐标; 5：百度地图采用的经纬度坐标; 6：百度地图采用的米制坐标; 7：mapbar地图坐标;
	 *            8：51地图坐标
	 * @param to
	 *            目标坐标类型： 3：国测局（GCJ02）坐标; 4：3中对应的米制坐标; 5：bd09ll(百度经纬度坐标);
	 *            6：bd09mc(百度米制经纬度坐标)
	 * @param ak
	 *            开发者密钥
	 * @return
	 * @throws BaiduApiHttpFalseException
	 */
	public BaiduGeoconvApiResponseMessage getBaiduGeoconvApiResponseMessage(String coords, String from, String to,
			String ak) throws BaiduApiHttpFalseException {
		BaiduGeoconvApiResponseMessage baiduGeoconvApiResponseMessage = null;
		Map<String, String> httpData = new HashMap<>();
		httpData.put("coords", coords);
		httpData.put("from", from);
		httpData.put("to", to);
		httpData.put("ak", ak);
		String json = sendGetRequest(baiduGeoconv, httpData);
		try {
			baiduGeoconvApiResponseMessage = JsonUtil.fromJson(BaiduGeoconvApiResponseMessage.class, json);
		} catch (Exception e) {
			e.printStackTrace();
			BaiduApiHttpFalseException baiduApiHttpFalseException = new BaiduApiHttpFalseException("获取坐标转换结果Http请求失败！");
			throw baiduApiHttpFalseException;
		}
		return baiduGeoconvApiResponseMessage;
	}

	/**
	 * 获取坐标转换结果(无ak)
	 * 
	 * @param coords
	 *            需转换的源坐标，多组坐标以“；”分隔 （经度，纬度） 114.21892734521,29.575429778924
	 * @param from
	 *            源坐标类型： 1：GPS设备获取的角度坐标，WGS84坐标; 2：GPS获取的米制坐标、sogou地图所用坐标;
	 *            3：google地图、soso地图、aliyun地图、mapabc地图和amap地图所用坐标，国测局（GCJ02）坐标;
	 *            4：3中列表地图坐标对应的米制坐标; 5：百度地图采用的经纬度坐标; 6：百度地图采用的米制坐标; 7：mapbar地图坐标;
	 *            8：51地图坐标
	 * @param to
	 *            目标坐标类型： 3：国测局（GCJ02）坐标; 4：3中对应的米制坐标; 5：bd09ll(百度经纬度坐标);
	 *            6：bd09mc(百度米制经纬度坐标)
	 * @return
	 * @throws BaiduApiHttpFalseException
	 * @throws NoAvailableBaiduAkException
	 */
	public BaiduGeoconvApiResponseMessage getBaiduGeoconvApiResponseMessage(String coords, String from, String to)
			throws BaiduApiHttpFalseException, NoAvailableBaiduAkException {
		BaiduAkInfoPO akInfoPO = BaiduSourseManager.getGeoconvBaiduAkInfo();
		if (akInfoPO == null) {
			throw new NoAvailableBaiduAkException("无可用地点检索AK!");
		}
		BaiduGeoconvApiResponseMessage baiduGeoconvApiResponseMessage;
		try {
			baiduGeoconvApiResponseMessage = getBaiduGeoconvApiResponseMessage(coords, from, to, akInfoPO.getAk());
		} finally {
			akInfoPO.setAkGeoconvTimes(akInfoPO.getAkGeoconvTimes() - 1);
			akInfoPO.setUpdateDate(new Date());
		}
		return baiduGeoconvApiResponseMessage;
	}

	/**
	 * 根据地市、poi名称爬取场景数据
	 * 
	 * @param regionName
	 * @param address
	 * @return
	 * @throws NoAvailableBaiduAkException
	 * @throws BaiduApiHttpFalseException
	 */
	public BaiduSceneCrawlingApiMessage crawlSceneDataByPoiName(String regionName, String address)
			throws BaiduApiHttpFalseException, NoAvailableBaiduAkException {
		BaiduSceneCrawlingApiMessage baiduSceneCrawlingApiMessage = new BaiduSceneCrawlingApiMessage();
		baiduSceneCrawlingApiMessage.setSearchRegionName(regionName);
		baiduSceneCrawlingApiMessage.setSearchPoiName(address);
		BaiduPlaceSearchApiResponseMessage baiduPlaceSearchApiResponseMessage = getBaiduPlaceSearchApiResponseMessage(
				address, regionName, "0", "1");
		baiduSceneCrawlingApiMessage.setBaiduPlaceSearchApiResponseMessage(baiduPlaceSearchApiResponseMessage);
		if (baiduPlaceSearchApiResponseMessage != null && baiduPlaceSearchApiResponseMessage.getResults().size() > 0) {
			BaiduPlaceSearchApiResult baiduPlaceSearchApiResult = baiduPlaceSearchApiResponseMessage.getResults()
					.get(0);
			BaiduSceneGeoApiResponseMessage baiduSceneGeoApiResponseMessage = getBaiduSceneGeoApiResponseMessage(
					baiduPlaceSearchApiResult.getUid());
			BaiduAkInfoPO baiduGeoconvAkInfoPO = BaiduSourseManager.getGeoconvBaiduAkInfo();
			if (baiduGeoconvAkInfoPO == null) {
				throw new NoAvailableBaiduAkException("无法获取[坐标转换]可用的百度ak!");
			}
			baiduSceneCrawlingApiMessage.setBaiduSceneGeoApiResponseMessage(baiduSceneGeoApiResponseMessage);
			List<BaiduGeoconvApiResponseMessage> baiduGeoconvApiResponseMessages = new ArrayList<>();
			try {
				List<String> strings = baiduSceneGeoApiResponseMessage.getGoeCoordinates();
				for (String string : strings) {
					// 百度ak坐标转换可用次数
					baiduGeoconvAkInfoPO.setAkGeoconvTimes(baiduGeoconvAkInfoPO.getAkGeoconvTimes() - 1);
					BaiduGeoconvApiResponseMessage baiduGeoconvApiResponseMessage = getBaiduGeoconvApiResponseMessage(
							string, "6", "5", baiduGeoconvAkInfoPO.getAk());
					baiduGeoconvApiResponseMessages.add(baiduGeoconvApiResponseMessage);
				}
				baiduSceneCrawlingApiMessage.setBaiduGeoconvApiResponseMessage(baiduGeoconvApiResponseMessages);
			} finally {
				baiduGeoconvAkInfoPO.setUpdateDate(new Date());
			}
		}
		return baiduSceneCrawlingApiMessage;
	}

	public String getBaiduPlaceSearchApi() {
		return baiduPlaceSearchApi;
	}

	public void setBaiduPlaceSearchApi(String baiduPlaceSearchApi) {
		this.baiduPlaceSearchApi = baiduPlaceSearchApi;
	}

	public String getBaiduPlaceDetailApi() {
		return baiduPlaceDetailApi;
	}

	public void setBaiduPlaceDetailApi(String baiduPlaceDetailApi) {
		this.baiduPlaceDetailApi = baiduPlaceDetailApi;
	}

	public String getBaiduGeocoderApi() {
		return baiduGeocoderApi;
	}

	public void setBaiduGeocoderApi(String baiduGeocoderApi) {
		this.baiduGeocoderApi = baiduGeocoderApi;
	}

	public String getBaiduSceneApi() {
		return baiduSceneApi;
	}

	public void setBaiduSceneApi(String baiduSceneApi) {
		this.baiduSceneApi = baiduSceneApi;
	}

	public String getBaiduGeoconv() {
		return baiduGeoconv;
	}

	public void setBaiduGeoconv(String baiduGeoconv) {
		this.baiduGeoconv = baiduGeoconv;
	}
}