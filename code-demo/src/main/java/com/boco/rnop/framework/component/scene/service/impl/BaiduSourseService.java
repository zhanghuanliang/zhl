package com.boco.rnop.framework.component.scene.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.rnop.framework.component.scene.entity.po.BaiduAkCountInfoPO;
import com.boco.rnop.framework.component.scene.entity.po.BaiduAkInfoPO;
import com.boco.rnop.framework.component.scene.entity.qo.BaiduAkQO;
import com.boco.rnop.framework.component.scene.mapper.def.IBaiduSourseMapper;
import com.boco.rnop.framework.component.scene.mapper.def.ITv3MapAkMapper;
import com.boco.rnop.framework.component.scene.service.IBaiduSourseService;

/**
 * 
 * 百度ak
 *
 * @author zhanghuanliang
 * @email zhanghuanliang@boco.com.cn
 * @version v1.0
 * @time 2018年9月12日 下午2:39:50
 * @modify <BR/>
 *         修改内容：<BR/>
 *         修改人员：<BR/>
 *         修改时间：<BR/>
 */
@Service
public class BaiduSourseService implements IBaiduSourseService {

	@Autowired
	private IBaiduSourseMapper baiduSourseMapper;
	@Autowired
	private ITv3MapAkMapper tv3MapAkMapper;

	@Override
	public List<BaiduAkInfoPO> selectAllBaiduAk() {
		return baiduSourseMapper.selectAllBaiduAk();
	}

	@Override
	public BaiduAkCountInfoPO countBaiduAk() {
		return baiduSourseMapper.countBaiduAk();
	}

	@Override
	public Integer updateAkTimes(BaiduAkQO baiduAkQO) {
		return baiduSourseMapper.updateAkTimes(baiduAkQO);
	}

	@Override
	public Integer insertBaiduAk(BaiduAkQO baiduAkQO) {

		return baiduSourseMapper.insertBaiduAk(baiduAkQO);
	}

	@Override
	public Integer deleteAkInfo(Long intId) {

		return tv3MapAkMapper.deleteByPrimaryKey(intId);
	}

}
