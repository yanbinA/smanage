package com.temple.manage.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.temple.manage.entity.LocalAuth;
import com.temple.manage.service.LocalAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
@Slf4j
public class UserModelDetailsService implements UserDetailsService {
   private final List<String> defaultAuthority = new ArrayList<>();
   {
      defaultAuthority.add("ROLE_AUDIT");
   }

   private final LocalAuthService localAuthService;

   public UserModelDetailsService(LocalAuthService localAuthService) {
      this.localAuthService = localAuthService;
   }

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String login) {
      log.debug("Authenticating user '{}'", login);
      LocalAuth localAuth = localAuthService.getByLogin(login);
      return Optional.ofNullable(localAuth)
              .map(user -> new User(user.getUsername(), user.getPassword(), user.getRoles()))
              .orElseThrow(() -> new UsernameNotFoundException("User " + login + " was not found in the database"));

   }

   private User createSpringSecurityUser(LocalAuth user) {


      return new User(user.getUsername(), user.getPassword(), user.getRoles());
   }
}
