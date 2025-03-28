/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.menu.command.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.menu.convertor.MenuConvertor;
import org.laokou.admin.menu.dto.MenuTreeListQry;
import org.laokou.admin.menu.dto.clientobject.MenuTreeCO;
import org.laokou.admin.menu.gatewayimpl.database.MenuMapper;
import org.laokou.admin.menu.gatewayimpl.database.dataobject.MenuDO;
import org.laokou.admin.menu.model.MenuStatusEnum;
import org.laokou.admin.menu.model.MenuTypeEnum;
import org.laokou.admin.menu.model.TreeMenuTypeEnum;
import org.laokou.common.core.util.TreeUtils;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询菜单请求执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MenuTreeListQryExe {

	private final MenuMapper menuMapper;

	public Result<List<MenuTreeCO>> execute(MenuTreeListQry qry) {
		MenuTreeCO co = TreeUtils.buildTreeNode(MenuConvertor.toClientObjs(list(qry)), MenuTreeCO.class);
		return Result.ok(co.getChildren());
	}

	private List<MenuDO> list(MenuTreeListQry qry) {
		switch (TreeMenuTypeEnum.getByCode(qry.getCode())) {
			case USER -> {
				qry.setStatus(MenuStatusEnum.ENABLE.getCode());
				qry.setType(MenuTypeEnum.MENU.getCode());
				return menuMapper.selectObjectList(qry);
			}
			case SYSTEM -> {
				return menuMapper.selectObjectList(qry);
			}
			case null -> {
				return new ArrayList<>(0);
			}
		}
	}

}
