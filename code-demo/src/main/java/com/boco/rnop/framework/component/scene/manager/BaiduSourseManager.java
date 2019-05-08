package com.boco.rnop.framework.component.scene.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.time.DateFormatUtils;

import com.boco.rnop.framework.common.util.SpringContextUtil;
import com.boco.rnop.framework.component.scene.entity.po.BaiduAkInfoPO;
import com.boco.rnop.framework.component.scene.entity.qo.BaiduAkQO;
import com.boco.rnop.framework.component.scene.service.IBaiduSourseService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 百度ak资源管理
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月12日 上午9:15:26
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Slf4j
public class BaiduSourseManager {

	private static IBaiduSourseService baiduSourseService = SpringContextUtil.getBean(IBaiduSourseService.class);
	/**
	 * 百度ak
	 */
	private static Vector<BaiduAkInfoPO> baiduAkInfoPOs;

	public static Vector<BaiduAkInfoPO> getBaiduAkInfoPOs() {
		return baiduAkInfoPOs;
	}

	public static void setBaiduAkInfoPOs(Vector<BaiduAkInfoPO> baiduAkInfoPOs) {
		BaiduSourseManager.baiduAkInfoPOs = baiduAkInfoPOs;
	}

	/**
	 * 获取地理编码可用百度ak信息
	 * 
	 * @return
	 */
	public static BaiduAkInfoPO getGeocodingBaiduAkInfo() {
		int index = (int) (Math.random() * (baiduAkInfoPOs.size() - 1));
		BaiduAkInfoPO baiduAkInfoPO = baiduAkInfoPOs.get(index);
		if (baiduAkInfoPO != null && baiduAkInfoPO.getAkGeocodingTimes() > 0) {
			return baiduAkInfoPO;
		} else {
			for (BaiduAkInfoPO akInfoPO : baiduAkInfoPOs) {
				if (akInfoPO.getAkGeocodingTimes() > 0) {
					return akInfoPO;
				}
			}
			return null;
		}
	}

	/**
	 * 获取地点检索可用百度ak信息
	 * 
	 * @return
	 */
	public static BaiduAkInfoPO getRetrievalBaiduAkInfo() {

		int index = (int) (Math.random() * (baiduAkInfoPOs.size() - 1));
		BaiduAkInfoPO baiduAkInfoPO = baiduAkInfoPOs.get(index);
		if (baiduAkInfoPO != null && baiduAkInfoPO.getAkRetrievalTimes() > 0) {
			return baiduAkInfoPO;
		} else {
			for (BaiduAkInfoPO akInfoPO : baiduAkInfoPOs) {
				if (akInfoPO.getAkRetrievalTimes() > 0) {
					return akInfoPO;
				}
			}
			log.warn("未获取地点检索可用百度ak信息!");
			return null;
		}

	}

	/**
	 * TODO 获取坐标转换可用百度ak信息
	 * 
	 * @return
	 */
	public static BaiduAkInfoPO getGeoconvBaiduAkInfo() {

		int index = (int) (Math.random() * (baiduAkInfoPOs.size() - 1));
		BaiduAkInfoPO baiduAkInfoPO = baiduAkInfoPOs.get(index);
		if (baiduAkInfoPO != null && baiduAkInfoPO.getAkGeoconvTimes() > 0) {
			return baiduAkInfoPO;
		} else {
			for (BaiduAkInfoPO akInfoPO : baiduAkInfoPOs) {
				if (akInfoPO.getAkGeoconvTimes() > 0) {
					return akInfoPO;
				}
			}
			return null;
		}

	}

	/**
	 * 检查ak信息是否最新
	 * 
	 * @return
	 */
	public static void checkAkLatest() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		try {
			date = simpleDateFormat.parse(DateFormatUtils.format(new Date(), "yyyy-MM-dd 00:00:00"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for (BaiduAkInfoPO baiduAkInfoPO : baiduAkInfoPOs) {
			if (baiduAkInfoPO.getUpdateDate().before(date)) {
				baiduAkInfoPO.setIntId(baiduAkInfoPO.getIntId());
				baiduAkInfoPO.setAkGeocodingTimes(baiduAkInfoPO.getGeocodingQuota());
				baiduAkInfoPO.setAkGeoconvTimes(baiduAkInfoPO.getGeoconvQuota());
				baiduAkInfoPO.setAkRetrievalTimes(baiduAkInfoPO.getRetrievalQuota());
				baiduAkInfoPO.setUpdateDate(new Date());
			}
		}

	}

	/**
	 * 更新ak信息
	 * 
	 * @return
	 */
	public static Integer updateAkInfo() {
		List<BaiduAkQO> list = new ArrayList<>();
		for (BaiduAkInfoPO baiduAkInfoPO : baiduAkInfoPOs) {
			BaiduAkQO baiduAkQO = new BaiduAkQO();
			baiduAkQO.setIntId(baiduAkInfoPO.getIntId());
			baiduAkQO.setAkGeocodingTimes(baiduAkInfoPO.getAkGeocodingTimes());
			baiduAkQO.setAkGeoconvTimes(baiduAkInfoPO.getAkGeoconvTimes());
			baiduAkQO.setAkRetrievalTimes(baiduAkInfoPO.getAkRetrievalTimes());
			baiduAkQO.setUpdateDate(baiduAkInfoPO.getUpdateDate());
			list.add(baiduAkQO);
		}
		int count = 0;
		for (BaiduAkQO baiduAkQO : list) {
			count += baiduSourseService.updateAkTimes(baiduAkQO);
		}
		return count;
	}

}
