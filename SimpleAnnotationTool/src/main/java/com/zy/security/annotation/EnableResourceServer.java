package com.zy.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * @author: zy;
 * @DateTime: 2020年3月24日 下午4:12:21;
 * @Description: 资源服务器注解
 */
public @interface EnableResourceServer {

}
