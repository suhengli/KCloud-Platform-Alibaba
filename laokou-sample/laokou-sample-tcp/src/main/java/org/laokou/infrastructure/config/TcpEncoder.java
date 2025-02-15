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

package org.laokou.infrastructure.config;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.fury.Fury;
import org.laokou.client.dto.clientobject.SensorCO;

/**
 * @author laokou
 */
public class TcpEncoder extends MessageToByteEncoder<SensorCO> {

	private static final Fury FURY = Fury.builder().build();

	static {
		FURY.register(SensorCO.class);
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, SensorCO co, ByteBuf byteBuf) {
		byteBuf.writeBytes(FURY.serialize(co));
	}

}
