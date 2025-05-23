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

package org.laokou.common.modbus4j.config;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;

/**
 * @author laokou
 */
public final class ModbusTcpMaster extends AbstractModbus {

	public ModbusTcpMaster(ModbusFactory modbusFactory, SpringModbusProperties properties) {
		super(properties);
		SpringModbusProperties.ModbusTcp tcp = properties.getTcp();
		super.modbusMaster = modbusFactory.createTcpMaster(getIpParameters(tcp), tcp.isKeepAlive());
	}

	@Override
	public ModbusResponse sendRequest(int slaveId, int startOffset, int numberOfRegisters)
			throws ModbusTransportException {
		return modbusMaster.send(new ReadHoldingRegistersRequest(slaveId, startOffset, numberOfRegisters));
	}

	private IpParameters getIpParameters(SpringModbusProperties.ModbusTcp tcp) {
		IpParameters ipParameters = new IpParameters();
		ipParameters.setHost(tcp.getHost());
		ipParameters.setPort(tcp.getPort());
		return ipParameters;
	}

}
