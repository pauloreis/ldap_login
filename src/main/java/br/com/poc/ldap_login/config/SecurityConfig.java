package br.com.poc.ldap_login.config;

import br.com.poc.ldap_login.filter.AutenticacaoViaTokenFilter;
import br.com.poc.ldap_login.service.AutenticacaoService;
import br.com.poc.ldap_login.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private TokenService tokenService;

    //sobrescrevemos essa classe aqui para tornar possivel a injeção do authenticationManager no controller.
    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //configurações de autenticacao
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(autenticacaoService)
        .passwordEncoder(new BCryptPasswordEncoder());
    }

    //configurações de autorização
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        .antMatchers(HttpMethod.POST, "/auth").permitAll()
        .anyRequest().authenticated()
        .and().csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, autenticacaoService), UsernamePasswordAuthenticationFilter.class);
        //antes de seguir com o fritro do spring "UsernamePasswordAuthenticationFilter", chame o nosso filtro AutenticacaoViaTokenFilter

                // utilizado para criação de sessão
        // .and().formLogin();
        //como vamos trabalhar com statelss

        //para chamar pagina customizada>
//        .and()
//                .formLogin(f -> f
//                        .loginPage("/login")
//                        .permitAll());

    }

    //configuracoes de recursos estaticos
    @Override
    public void configure(WebSecurity web) throws Exception {
    }

//    public static void main(String[] args) {
//        System.out.println(new BCryptPasswordEncoder().encode("123456"));
//    }
}
