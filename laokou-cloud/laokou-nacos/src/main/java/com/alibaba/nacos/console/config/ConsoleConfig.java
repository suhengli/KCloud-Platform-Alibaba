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

/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.nacos.console.config;

import com.alibaba.nacos.console.filter.XssFilter;
import com.alibaba.nacos.core.code.ControllerMethodsCache;
import com.alibaba.nacos.sys.filter.NacosTypeExcludeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.PostConstruct;
import java.time.ZoneId;

/**
 * Console config.
 *
 * @author yshen
 * @author nkorange
 * @since 1.2.0
 */
@Component
@EnableScheduling
@PropertySource("/application.yml")
@ComponentScan(basePackages = "com.alibaba.nacos",
		excludeFilters = { @ComponentScan.Filter(type = FilterType.CUSTOM, classes = { NacosTypeExcludeFilter.class }),
				@ComponentScan.Filter(type = FilterType.CUSTOM, classes = { TypeExcludeFilter.class }),
				@ComponentScan.Filter(type = FilterType.CUSTOM, classes = { AutoConfigurationExcludeFilter.class }) })
public class ConsoleConfig {

	@Autowired
	private ControllerMethodsCache methodsCache;

	@Value("${nacos.console.ui.enabled:true}")
	private boolean consoleUiEnabled;

	/**
	 * Init.
	 */
	@PostConstruct
	public void init() {
		methodsCache.initClassMethod("com.alibaba.nacos.core.controller");
		methodsCache.initClassMethod("com.alibaba.nacos.naming.controllers");
		methodsCache.initClassMethod("com.alibaba.nacos.config.server.controller");
		methodsCache.initClassMethod("com.alibaba.nacos.console.controller");
	}

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedHeader("*");
		config.setMaxAge(18000L);
		config.addAllowedMethod("*");
		config.addAllowedOriginPattern("*");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Bean
	public XssFilter xssFilter() {
		return new XssFilter();
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
		return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(ZoneId.systemDefault().toString());
	}

	public boolean isConsoleUiEnabled() {
		return consoleUiEnabled;
	}

}
