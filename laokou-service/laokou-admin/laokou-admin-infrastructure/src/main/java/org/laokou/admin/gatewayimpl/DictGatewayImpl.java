/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.laokou.admin.gatewayimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.DictConvertor;
import org.laokou.admin.domain.common.DataPage;
import org.laokou.admin.domain.dict.Dict;
import org.laokou.admin.domain.gateway.DictGateway;
import org.laokou.admin.gatewayimpl.database.DictMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DictDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import static org.laokou.admin.common.Constant.TENANT;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DictGatewayImpl implements DictGateway {

	private final DictMapper dictMapper;

	private final TransactionalUtil transactionalUtil;

	@Override
	@DS(TENANT)
	public Boolean insert(Dict dict) {
		DictDO dictDO = DictConvertor.toDataObject(dict);
		return insertDict(dictDO);
	}

	@Override
	@DS(TENANT)
	public Boolean update(Dict dict) {
		DictDO dictDO = DictConvertor.toDataObject(dict);
		return updateDict(dictDO);
	}

	@Override
	public Dict getById(Long id) {
		DictDO dictDO = dictMapper.selectById(id);
		return ConvertUtil.sourceToTarget(dictDO, Dict.class);
	}

	@Override
	public Boolean deleteById(Long id) {
		return transactionalUtil.execute(r -> {
			try {
				return dictMapper.deleteById(id) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

	@Override
	public Datas<Dict> list(Dict dict, DataPage dataPage) {
		IPage<DictDO> page = new Page<>(dataPage.getPageNum(), dataPage.getPageSize());
		DictDO dictDO = DictConvertor.toDataObject(dict);
		IPage<DictDO> newPage = dictMapper.getDictList(page, dictDO);
		Datas<Dict> datas = new Datas<>();
		datas.setRecords(ConvertUtil.sourceToTarget(newPage.getRecords(), Dict.class));
		datas.setTotal(newPage.getTotal());
		return datas;
	}

	private Boolean insertDict(DictDO dictDO) {
		return transactionalUtil.execute(r -> {
			try {
				return dictMapper.insert(dictDO) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

	private Boolean updateDict(DictDO dictDO) {
		return transactionalUtil.execute(r -> {
			try {
				return dictMapper.updateById(dictDO) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

}