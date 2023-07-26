/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.rabbitmq.constant;

/**
 * <a href="https://spring.io/guides/gs/messaging-rabbitmq/">...</a>
 *
 * @author laokou
 */
public interface MqConstant {

	String LAOKOU_GPS_INFO_TOPIC_EXCHANGE = "laokou.gps.info.topic.exchange";

	String LAOKOU_GPS_INFO_QUEUE = "laokou.gps.info.queue";

	String LAOKOU_GPS_INFO_ROUTER_KEY = "gps.info.#";

}