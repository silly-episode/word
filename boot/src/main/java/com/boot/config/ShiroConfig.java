package com.boot.config;


import com.boot.common.Jwt.JwtFilter;
import com.boot.common.Realm.AdminRealm;
import com.boot.common.Realm.CommonUserRealm;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/1 14:31
 * @FileName: ShiroConfig
 * @Description: shiro配置类
 */
@Configuration
public class ShiroConfig {

    @Bean
    public CommonUserRealm commonUserRealm() {
        CommonUserRealm commonUserRealm = new CommonUserRealm();
        //设置 凭证匹配器
//        commonUserRealm.setCredentialsMatcher();
        return commonUserRealm;
    }


    @Bean
    public AdminRealm adminRealm() {
        AdminRealm adminRealm = new AdminRealm();
        //设置 凭证匹配器
//        commonUserRealm.setCredentialsMatcher();
        return adminRealm;
    }

    @Bean
    public Authenticator authenticator() {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();//留意这一行哟
        // 设置两个Realm，一个用于用户登录验证；一个用于jwt token的认证和访问权限获取
        authenticator.setRealms(Arrays.asList(adminRealm(), commonUserRealm()));
        // 设置多个realm认证策略，一个成功即跳过其它的
        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return authenticator;
    }

    /**
     * 初始化authorizer 认证器 权限认证
     *
     * @return
     */
    @Bean
    public Authorizer authorizer() {
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();//这里的
        authorizer.setRealms(Arrays.asList(adminRealm(), commonUserRealm()));
        return authorizer;
    }


    /**
     * 2.创建 DefaultWebSecurityManager
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager getManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        // 使用自己的realm
//        List<Realm> realms =new ArrayList<Realm>();
//        realms.add(commonUserRealm);
//        realms.add(new AdminRealm());
//        manager.setRealms(realms);


        manager.setAuthenticator(authenticator());
        manager.setAuthorizer(authorizer());


        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);

        return manager;
    }

    /**
     * 3.创建ShiroFilterFactoryBean
     */
    @Bean(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean factory(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JwtFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);
        factoryBean.setUnauthorizedUrl("/401");

        /*
         * 自定义url规则
         * http://shiro.apache.org/web.html#urls-
         */
        Map<String, String> filterRuleMap = new HashMap<>();
        // 所有请求通过我们自己的JWT Filter
//        登录接口和获取短信的接口不需要认证
        filterRuleMap.put("/user/loginPassword", "anon");
        filterRuleMap.put("/user/loginSms", "anon");
        filterRuleMap.put("/user/sms", "anon");
        filterRuleMap.put("/user/user", "anon");
//
        filterRuleMap.put("/guest/**", "anon");
        // 访问401和404页面不通过我们的Filter
        filterRuleMap.put("/401", "anon");
        filterRuleMap.put("/404", "anon");
        filterRuleMap.put("/**", "jwt");

//        测试拦截
        filterRuleMap.put("/test/**", "jwt");


//        添入拦截器
        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }

    /**
     * 下面的代码是添加注解支持
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启Shiro注解通知器
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
