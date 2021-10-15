package br.com.poc.ldap_login.filter;

import br.com.poc.ldap_login.service.AutenticacaoService;
import br.com.poc.ldap_login.service.TokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

    private TokenService tokenService;
    private AutenticacaoService autenticacaoService;

    public AutenticacaoViaTokenFilter(TokenService tokenService, AutenticacaoService autenticacaoService) {
        this.tokenService = tokenService;
        this.autenticacaoService = autenticacaoService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //recuperar token
        String token = recuperarToken(request);

        //validar token
        boolean valido = tokenService.isTokenValido(token);

        if(valido){
            //se for válido autentica
            autenticarCliente(token);
        }

        //segue o fluxo da requisição
        filterChain.doFilter(request, response);
    }

    private void autenticarCliente(String token) {
        //recuperar o id do usuário que esta no body do token
        Long idUsuario = tokenService.getIdUsuario(token);
        //lembrando aque aqui o usuário esta mockado, onde deveriamos utilizar um repository
        UserDetails usuario = autenticacaoService.loadUserByUsername(String.valueOf(idUsuario));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String recuperarToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token==null || token.isEmpty() || !token.startsWith("Bearer ")){
            return null;
        }
        return token.substring(7, token.length());
    }
}
