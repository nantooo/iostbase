package jp.ac.kyoto_u.i.soc.ai.iostbase;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  // アカウント登録時のパスワードエンコードで利用するためDI管理する。
  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    // @formatter:off
    web
      .debug(false)
      .ignoring()
      .antMatchers("/images/**", "/js/**", "/css/**", "/jsServices/**")
    ;
    // @formatter:on
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
      .authorizeRequests()
        .mvcMatchers("/", "/signup", "/jsServices/**", "/h2-console/**").permitAll()
        .mvcMatchers("/members/user/**").hasRole("USER")
        .mvcMatchers("/members/admin/**").hasRole("ADMIN")
        .anyRequest().authenticated()
      .and()
      .formLogin()
        .defaultSuccessUrl("/")
      .and()
      .logout()
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .logoutSuccessUrl("/")
    ;
    // @formatter:on
    http.headers().frameOptions().disable();
    http.csrf().disable();
  }

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		UserDetails user =
			 User.withDefaultPasswordEncoder()
				.username("user")
				.password("password")
				.roles("USER")
				.build();

		return new InMemoryUserDetailsManager(user);
	}

	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // (7) Basic認証の実装を指定
        auth.authenticationProvider(new BasicAuthenticationProvider());
    }

    // (8) 認証処理の実装クラス
    public class BasicAuthenticationProvider implements AuthenticationProvider {

        // (9) 認証チェック
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {

            String name = authentication.getName();
            String password = authentication.getCredentials().toString();

            //  入力された name / password をチェックする
            if( name.equals("user") && password.equals("password") ){
               return new UsernamePasswordAuthenticationToken(name,password,authentication.getAuthorities());
            }

            throw new AuthenticationCredentialsNotFoundException("basic auth error");
        }

        // (10) 処理すべきAuthenticationクラスのチェック
        @Override
        public boolean supports(Class<?> authentication) {
            return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        }
    }
}