package com.nowcoder.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
// 这个注解用于表示是否需要在登录状态下才能访问某些资源 比如：http://localhost:8080/community/user/setting 这个URL修改头像的界面必须要在登录状态下才能访问
@Target(ElementType.METHOD) // 表明注解要声明在方法之上
@Retention(RetentionPolicy.RUNTIME) // 声明注解的有效时间：在运行期有效
public @interface LoginRequired {
    // 内容不需要
    // 在本项目中，这个自定义的注解用于 /setting /upload 这两个方法
}
