package com.boco.rnop.framework.component.scene.mapper.def;

import java.util.List;

import com.boco.rnop.framework.component.scene.entity.po.BaiduAkCountInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.BaiduAkInfoPO;
import com.boco.rnop.framework.component.scene.entity.qo.BaiduAkQO;

/**
 * 
 * 百度ak
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月12日 下午2:39:16
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
public interface IBaiduSourseMapper {
	/**
	 * 查询全部百度ak信息
	 * 
	 * @return
	 */
	public List<BaiduAkInfoPO> selectAllBaiduAk();

	/**
	 * 统计百度ak消耗信息
	 * 
	 * @return
	 */
	public BaiduAkCountInfoPO countBaiduAk();

	/**
	 * 更新ak次数
	 * 
	 * @param baiduAkQOs
	 * @return
	 */
	public Integer updateAkTimes(BaiduAkQO baiduAkQO);

	/**
	 * 新增一条百度ak信息
	 * 
	 * @param baiduAkQO
	 * @return
	 */
	public Integer insertBaiduAk(BaiduAkQO baiduAkQO);
}
