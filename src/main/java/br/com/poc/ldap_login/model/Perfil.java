package br.com.poc.ldap_login.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Perfil implements GrantedAuthority {
    private Long id;
    private String nome;

    @Override
    public String getAuthority() {
        return this.nome;
    }
}
