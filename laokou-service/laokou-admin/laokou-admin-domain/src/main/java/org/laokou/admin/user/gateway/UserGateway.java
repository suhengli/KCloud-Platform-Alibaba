/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.user.gateway;

import org.laokou.admin.user.model.UserE;

/**
 * 用户网关【防腐】.
 *
 * @author laokou
 */
public interface UserGateway {

	/**
	 * 新增用户.
	 */
	void create(UserE userE);

	/**
	 * 修改用户.
	 */
	void update(UserE userE);

	/**
	 * 删除用户.
	 */
	void delete(Long[] ids);

}