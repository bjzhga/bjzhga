package com.example.customerprofilesystem.service;

import com.example.customerprofilesystem.domain.User;
import com.example.customerprofilesystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 自定义 UserDetailsService，用于从数据库加载用户信息以供 Spring Security 使用。
 */
@Service // 声明这是一个 Spring 的服务组件，Spring Boot 会自动扫描并注册它。
public class JpaUserDetailsService implements UserDetailsService {

    // 使用 final 关键字，并通过构造函数注入，这是推荐的最佳实践。
    private final UserRepository userRepository;

    /**
     * 构造函数注入 UserRepository。
     * 当 Spring 创建 JpaUserDetailsService 实例时，会自动将 UserRepository 的实例传入。
     * @param userRepository 数据访问仓库
     */
    @Autowired // 在只有一个构造函数的情况下，此注解可以省略，但加上更明确。
    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Spring Security 在进行用户认证时会调用此方法。
     * @param username 用户在登录时输入的用户名
     * @return 一个包含用户信息（用户名、密码、权限）的 UserDetails 对象
     * @throws UsernameNotFoundException 如果在数据库中找不到该用户，则必须抛出此异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 添加调试日志，方便观察方法是否被调用
        System.out.println(">>>>>>>>>> [Authentication] JpaUserDetailsService is trying to load user: " + username);

        // 从数据库根据用户名查找用户。
        // userRepository.findByUsername 返回一个 Optional<User> 对象。
        // 我们使用 orElseThrow 方法来处理：
        // - 如果 Optional 包含 User 对象，则返回该 User 对象。
        // - 如果 Optional 为空（即用户不存在），则抛出指定的 UsernameNotFoundException 异常。
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println(">>>>>>>>>> [Authentication] FAILED: User '" + username + "' not found in database.");
                    return new UsernameNotFoundException("User not found with username: " + username);
                });

        System.out.println(">>>>>>>>>> [Authentication] SUCCESS: User '" + user.getUsername() + "' found in database.");

        // 将数据库中的角色（例如 "ADMIN"）转换为 Spring Security 的 GrantedAuthority。
        // 假设你的 User 实体类有一个 getRole() 方法返回一个字符串，比如 "ADMIN"。
        // 注意：Spring Security 默认需要角色以 "ROLE_" 开头，但可以通过配置修改。这里我们手动加上。
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        // 将我们自己的 User 实体对象，转换成 Spring Security 内部使用的 UserDetails 对象。
        // org.springframework.security.core.userdetails.User 构造函数需要：
        // 1. 用户名 (username)
        // 2. 密码 (password) - 这里传递的是数据库中存储的密码（明文或加密后的）
        // 3. 权限集合 (authorities)
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}