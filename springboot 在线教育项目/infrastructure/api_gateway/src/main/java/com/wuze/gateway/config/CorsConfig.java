package com.wuze.gateway.config;


/**
 * <p>
 * 处理跨域
 * </p>
 *
 * @author qy
 * @since 2019-11-21
 */

/**
 * description:
 *
 * @author wangpeng
 * @date 2019/01/02
 */
/*
// 实现跨域（这个类 和 @CrossOrigin 注解 功能一样，两个选一个写即可，同时写会冲突！）
@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*");
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
*/

