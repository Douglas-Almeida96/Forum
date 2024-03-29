package br.com.alura.forum.service;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${forum.jwt.expiration}")
    private  String expiration;
    @Value("${forum.jwt.secret}")
    private  String secret;

    public String gerarToken(Authentication authentication) {

        Usuario logado = (Usuario) authentication.getPrincipal();
        Date hoje = new Date();
        Date hojeExpiration = new Date(hoje.getTime()+Long.parseLong(expiration));

        return Jwts.builder()
                .setIssuer("API douglas")
                .setSubject(logado.getId().toString())
                .setIssuedAt(hoje)
                .setExpiration(hojeExpiration)
                .signWith(SignatureAlgorithm.HS256 , secret)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    public long findByIdUser(String token) {
        Claims claims =  Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return  Long.parseLong(claims.getSubject());
    }
}
