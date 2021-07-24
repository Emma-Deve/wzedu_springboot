package com.wuze.security.config;


import com.wuze.security.filter.TokenAuthenticationFilter;
import com.wuze.security.filter.TokenLoginFilter;
import com.wuze.security.security.DefaultPasswordEncoder;
import com.wuze.security.security.TokenLogoutHandler;
import com.wuze.security.security.TokenManager;
import com.wuze.security.security.UnauthorizedEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <p>
 * Security配置类
 * </p>
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TokenWebSecurityConfig extends WebSecurityConfigurerAdapter {

    // UserDetailsService 接口（是 spring security 系统接口）
    //我们需要自己写实现类 去查询 （UserDetailsServiceImpl）
    private UserDetailsService userDetailsService;  //自己写的查询数据库的类（1、查询匹配用户名、密码）

    private TokenManager tokenManager;  //token生成控制类
    private DefaultPasswordEncoder defaultPasswordEncoder;  //密码处理
    private RedisTemplate redisTemplate;    //操作redis（2、把 ”用户名“、”对应权限列表“ 放到redis缓存）

    @Autowired
    public TokenWebSecurityConfig(UserDetailsService userDetailsService, DefaultPasswordEncoder defaultPasswordEncoder,
                                  TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.userDetailsService = userDetailsService;
        this.defaultPasswordEncoder = defaultPasswordEncoder;
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 配置设置
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .authenticationEntryPoint(new UnauthorizedEntryPoint())
                .and().csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().logout().logoutUrl("/admin/acl/index/logout")    //设置退出地址（一般只需改这个地方）
                .addLogoutHandler(new TokenLogoutHandler(tokenManager,redisTemplate)).and()
                .addFilter(new TokenLoginFilter(authenticationManager(), tokenManager, redisTemplate))
                .addFilter(new TokenAuthenticationFilter(authenticationManager(), tokenManager, redisTemplate)).httpBasic();
    }



    /**
     * 密码处理
     * @param auth
     * @throws Exception
     */
    //userDetailsService 接口的实现类 userDetailsServiceImpl 作用：
    // 根据用户名获取用户信息和对应权限列表，封装并返回（查数据库获取）
    //返回 ”封装了用户信息和权限列表“ 的对象，类型是 UserDetails（security 系统接口interface）
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(defaultPasswordEncoder);
    }

    /**
     * 配置哪些请求不拦截
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/**",
                "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**"
               );
//        web.ignoring().antMatchers("/*/**"
//        );
    }
}