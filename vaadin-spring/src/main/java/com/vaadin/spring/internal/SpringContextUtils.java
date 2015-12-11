/*
 * Copyright 2015 The original authors
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
 */
package com.vaadin.spring.internal;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Aleksander Grzebyk
 */
public abstract class SpringContextUtils {

    public static String[] getBeanNamesForAnnotation(ApplicationContext applicationContext,
                                                     Class<? extends Annotation> annotationType) {
        if (!ConfigurableApplicationContext.class.isAssignableFrom(applicationContext.getClass())) {
            throw new IllegalArgumentException("ApplicationContext is not a ConfigurableListableBeanFactory (" + applicationContext.getClass() + ")");
        }
        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        List<String> results = new ArrayList<String>();
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            if (!beanDefinition.isAbstract() && beanFactory.findAnnotationOnBean(beanName, annotationType) != null) {
                results.add(beanName);
            }
        }
        for (String beanName : beanFactory.getSingletonNames()) {
            if (!results.contains(beanName) && beanFactory.findAnnotationOnBean(beanName, annotationType) != null) {
                results.add(beanName);
            }
        }
        return results.toArray(new String[results.size()]);
    }

}
