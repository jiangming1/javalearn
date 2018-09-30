package com.stylefeng.guns.modular.cms.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.common.persistence.dao.TaxonomyMapper;
import com.stylefeng.guns.common.persistence.model.Taxonomy;
import com.stylefeng.guns.modular.cms.service.ITaxonomyService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分类表 服务实现类
 * </p>
 *
 * @author zhoujin7
 * @since 2017-12-02
 */
@Service
public class TaxonomyServiceImpl extends ServiceImpl<TaxonomyMapper, Taxonomy> implements ITaxonomyService {

}
