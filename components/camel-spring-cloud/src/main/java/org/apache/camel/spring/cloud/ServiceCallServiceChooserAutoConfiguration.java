/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.camel.spring.cloud;

import org.apache.camel.cloud.ServiceChooser;
import org.apache.camel.impl.cloud.RandomServiceChooser;
import org.apache.camel.impl.cloud.RoundRobinServiceChooser;
import org.apache.camel.spring.boot.util.GroupCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;

@Configuration
@ConditionalOnBean(CamelCloudAutoConfiguration.class)
@EnableConfigurationProperties(ServiceCallConfigurationProperties.class)
public class ServiceCallServiceChooserAutoConfiguration {

    @Lazy
    @Scope("prototype")
    @Order(1)
    @Bean(name = "round-robin-service-chooser")
    @Conditional(ServiceCallServiceChooserAutoConfiguration.ServiceChooserCondition.class)
    public ServiceChooser roundRobinLoadBalancer() {
        return new RoundRobinServiceChooser();
    }

    @Lazy
    @Scope("prototype")
    @Order(2)
    @Bean(name = "random-service-chooser")
    @Conditional(ServiceCallServiceChooserAutoConfiguration.ServiceChooserCondition.class)
    public ServiceChooser randomLoadBalancer() {
        return new RandomServiceChooser();
    }

    public static class ServiceChooserCondition extends GroupCondition {
        public ServiceChooserCondition() {
            super(
                "camel.cloud.servicecall",
                "camel.cloud.servicecall.service-chooser"
            );
        }
    }
}