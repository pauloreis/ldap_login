package br.com.poc.ldap_login.service;

import br.com.poc.ldap_login.model.Perfil;
import br.com.poc.ldap_login.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // aqui devemos pegar o usuário de um repositorio.
        // para simplificar criei o usuário manualmente.
        User user = User.builder()
                .id(1L)
                .nome("Paulo")
                .email("paulo@teste.com")
                .senha("$2a$10$mb5tI320VkDA6kLeuhTFuejyKCKKuz2QDMpMgRPPo52511yeQ4gTS")
                .perfis(List.of(Perfil.builder().id(1L).nome("ADM").build()))
                .build();
        return user;
    }
}
