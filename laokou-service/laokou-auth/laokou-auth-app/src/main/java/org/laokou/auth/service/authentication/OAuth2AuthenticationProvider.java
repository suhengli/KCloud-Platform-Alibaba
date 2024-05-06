/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.auth.service.authentication;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.convertor.UserConvertor;
import org.laokou.auth.domain.ability.AuthDomainService;
import org.laokou.auth.extensionpoint.AuthValidatorExtPt;
import org.laokou.auth.domain.model.auth.AuthA;
import org.laokou.auth.domain.model.auth.DeptE;
import org.laokou.auth.domain.model.auth.MenuE;
import org.laokou.common.domain.context.DomainEventContextHolder;
import org.laokou.common.domain.publish.DomainEventPublisher;
import org.laokou.common.domain.service.DomainEventService;
import org.laokou.common.extension.BizScenario;
import org.laokou.common.extension.ExtensionExecutor;
import org.laokou.common.i18n.common.exception.AuthException;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.security.utils.UserDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import static org.laokou.common.security.handler.OAuth2ExceptionHandler.ERROR_URL;
import static org.laokou.common.security.handler.OAuth2ExceptionHandler.getException;

/**
 * @author laokou
 */
@RequiredArgsConstructor
@Component("authProvider")
public class OAuth2AuthenticationProvider {

	private final AuthDomainService authDomainService;

	private final DomainEventService domainEventService;

	private final DomainEventPublisher domainEventPublisher;

	private final UserConvertor userConvertor;

	private final ExtensionExecutor extensionExecutor;

	public UsernamePasswordAuthenticationToken authentication(AuthA auth) {
		try {
			// 校验
			extensionExecutor.executeVoid(AuthValidatorExtPt.class, BizScenario.valueOf(auth.getGrantType()),
					extension -> extension.validate(auth));
			// 认证
			authDomainService.auth(auth);
			UserDetail userDetail = convert(auth);
			return new UsernamePasswordAuthenticationToken(userDetail, userDetail.getUsername(),
					userDetail.getAuthorities());
		}
		catch (AuthException | SystemException e) {
			throw getException(e.getCode(), e.getMsg(), ERROR_URL);
		}
		finally {
			// 保存领域事件（事件溯源）
			// domainEventService.create(auth.getEvents());
			// 清除数据源上下文
			DynamicDataSourceContextHolder.clear();
			// 发布当前线程的领域事件(同步发布)
			// domainEventPublisher.publish(SYNC);
			// 清除领域事件上下文
			DomainEventContextHolder.clear();
			// 清空领域事件
			auth.clearEvents();
		}
	}

	private UserDetail convert(AuthA auth) {
		MenuE menu = auth.getMenu();
		DeptE dept = auth.getDept();
		UserDetail userDetail = userConvertor.convertClientObject(auth.getUser());
		userDetail.modify(menu.getPermissions(), dept.getDeptPaths(), auth.getSourceName());
		return userDetail;
	}

}