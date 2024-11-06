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

package org.laokou.iot.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Result;
import org.springframework.http.MediaType;
import org.laokou.iot.cp.dto.clientobject.CpCO;
import org.springframework.web.bind.annotation.*;
import org.laokou.iot.cp.api.CpsServiceI;
import org.laokou.iot.cp.dto.*;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.idempotent.annotation.Idempotent;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * 通讯协议管理控制器.
 *
 * @author laokou
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v3/cps")
@Tag(name = "通讯协议管理", description = "通讯协议管理")
public class CpsControllerV3 {

	private final CpsServiceI cpsServiceI;

	@Idempotent
	@PostMapping
	@PreAuthorize("hasAuthority('iot:cp:save')")
	@OperateLog(module = "通讯协议管理", operation = "保存通讯协议")
	@Operation(summary = "保存通讯协议", description = "保存通讯协议")
	public void saveV3(@RequestBody CpSaveCmd cmd) {
		cpsServiceI.save(cmd);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('iot:cp:modify')")
	@OperateLog(module = "通讯协议管理", operation = "修改通讯协议")
	@Operation(summary = "修改通讯协议", description = "修改通讯协议")
	public void modifyV3(@RequestBody CpModifyCmd cmd) {
		cpsServiceI.modify(cmd);
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('iot:cp:remove')")
	@OperateLog(module = "通讯协议管理", operation = "删除通讯协议")
	@Operation(summary = "删除通讯协议", description = "删除通讯协议")
	public void removeV3(@RequestBody Long[] ids) {
		cpsServiceI.remove(new CpRemoveCmd(ids));
	}

	@PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('iot:cp:import')")
	@OperateLog(module = "通讯协议管理", operation = "导入通讯协议")
	@Operation(summary = "导入通讯协议", description = "导入通讯协议")
	public void importV3(@RequestPart("file") MultipartFile[] files) {
		cpsServiceI.importI(new CpImportCmd(files));
	}

	@PostMapping("export")
	@PreAuthorize("hasAuthority('iot:cp:export')")
	@OperateLog(module = "通讯协议管理", operation = "导出通讯协议")
	@Operation(summary = "导出通讯协议", description = "导出通讯协议")
	public void exportV3(@RequestBody CpExportCmd cmd) {
		cpsServiceI.export(cmd);
	}

	@TraceLog
	@PostMapping("page")
	@PreAuthorize("hasAuthority('iot:cp:page')")
	@Operation(summary = "分页查询通讯协议列表", description = "分页查询通讯协议列表")
	public Result<Page<CpCO>> pageV3(@RequestBody CpPageQry qry) {
		return cpsServiceI.page(qry);
	}

	@TraceLog
	@GetMapping("{id}")
	@Operation(summary = "查看通讯协议详情", description = "查看通讯协议详情")
	public Result<CpCO> getByIdV3(@PathVariable("id") Long id) {
		return cpsServiceI.getById(new CpGetQry(id));
	}

}