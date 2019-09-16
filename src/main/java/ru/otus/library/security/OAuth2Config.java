package ru.otus.library.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@RequiredArgsConstructor
public class OAuth2Config
        extends AuthorizationServerConfigurerAdapter {

    private String clientid = "client";
    private String clientSecret = "secret";
    private String privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIEowIBAAKCAQEAx0nyi/9zq7SVcKBiJraKb2OQQDsVBoSwr/TL9Y2EWvq+kMrs\n" +
            "e8E/ygE+eKxKUx0Cpi6Lz5Up+z5YH5SXBHDM2Y4+gob7qhh5rtABoZPRzlZvxktN\n" +
            "xbB//xLLj2wDOhxh4CuWZ04pB8ASzQAhHeu+lfRL+11rL4deKkxdlAmEt7mwxjDX\n" +
            "lczEvVEzoL2sWXflmhegdnOCfSNHWg4nCqLZLxOdu/lh1uI+YlwU94GvoK9Th5eh\n" +
            "E3pUPzDdBReFtsYXY9tusFVQWIeTntDTGNrqFMfdu4MwXEr2IqiB5A1cgL9rbb1c\n" +
            "N80+YnwKjrvcibAIV95zC28ttH0aj5f5+uef1QIDAQABAoIBADIPAxvjaNzCmZ8W\n" +
            "ylDLHW7pZXyc2qUug2tPZEQj7hEkVuXr8wcK/cjyEw0gyfKioN25Z2kXINp5SI12\n" +
            "N5Dn/KzRLCaBXYnWaApfOWmqxdK8p4r5s2lq8lvSDsvR5+Wa54L1r+v3GBtYReC0\n" +
            "nZz0cIUePk+/oYLoz+B0GjVrRqkXS5iu3NQcE/U677+EREmQKkdU+GwFRmEo4fZ6\n" +
            "UjcGMKv3WvTlXc2FOtl7Mc78K9iaYSgj6Hp+WmNBTBjWWtvu1AozRFwLddqg+jIy\n" +
            "GPxXcl3eN4QoZIdj2dT72Kbe3OcjXH9qnZISPky7YLbuGsiuobB3DVy5rCyYJLJg\n" +
            "McYup8ECgYEA+osztI86viPMiXmg1bWoGCR0m7Gl4qRZbK97atSvncnZ37XPAsdF\n" +
            "TYjEGR6f+CA0DT+OkpMWaWCmDAPOBEeAJ1Y+/in0sl7l5MHNhl/fCC5hXUiJ954L\n" +
            "wm8FVBeAY6VCLtOQHvDAuI8hngABSaWP6qRjleLx/caLp1uJuJucQ7kCgYEAy6D+\n" +
            "+rvJiqtTYkzewxRU2+4DUGSr/zrNWeoMPVssPyAN77iXnx3/R4Uewm4j44e5i1nH\n" +
            "LFJwl+U768AnFJrCMLqsSvboMSXoZWHgMsbZQUTo++MJAsDqbiEXEgOL4xYE2Mi4\n" +
            "ECNQ0RuziilqxUuMaDxBNB9kItzNEl/9JZ+bQv0CgYAt2TVwEFnmm7R9X+Wf1itZ\n" +
            "p6ozJ7I8pGi86+wA48TzchHNz35/eqnjA2knU6RgirwbxIq5pfMO+6HHRxb3T4JT\n" +
            "MIQ8jL77uUQGtbR/p6Gv5JTqtjCRQLnULe7isZ5MHdzbptmT3+jUnVTAji2uJX8N\n" +
            "qGGU9z/jeebqjGTKhKd2+QKBgHqeL+x8OBiwoj2HXHzMOW1WHdjDRm8i0ZVbQpOP\n" +
            "200H2FyKI4NFZPuERcl+2U2h5TDslusxmWteNn737pCRwG3neuiJVbiCwzaJgaAR\n" +
            "7bFH9aP4SuzFjmVSt7m5OPs9vx1rgKr3GStSKwL/fbPgkmQSJJFmdb4oh7SQ6LdH\n" +
            "LQ9hAoGBAKAPB7aJ5XVru4VjRB/KO6XyIobROqQveblz1fCBabcqzPup5mVW57VY\n" +
            "IrDke7BfEqtBWwqeh6xrH1aytdU2ljVFAH2Jb07HxZfB1OJwR8EoK9wFVtS/7CBy\n" +
            "1t9aaTVxsC3Sn9DwKDmLHUt1VZUOUpkPQEWw3aSoe8cVAB9GFngo\n" +
            "-----END RSA PRIVATE KEY-----";
    private String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAx0nyi/9zq7SVcKBiJraK\n" +
            "b2OQQDsVBoSwr/TL9Y2EWvq+kMrse8E/ygE+eKxKUx0Cpi6Lz5Up+z5YH5SXBHDM\n" +
            "2Y4+gob7qhh5rtABoZPRzlZvxktNxbB//xLLj2wDOhxh4CuWZ04pB8ASzQAhHeu+\n" +
            "lfRL+11rL4deKkxdlAmEt7mwxjDXlczEvVEzoL2sWXflmhegdnOCfSNHWg4nCqLZ\n" +
            "LxOdu/lh1uI+YlwU94GvoK9Th5ehE3pUPzDdBReFtsYXY9tusFVQWIeTntDTGNrq\n" +
            "FMfdu4MwXEr2IqiB5A1cgL9rbb1cN80+YnwKjrvcibAIV95zC28ttH0aj5f5+uef\n" +
            "1QIDAQAB\n" +
            "-----END PUBLIC KEY-----\n";

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;



    @Bean
    public JwtAccessTokenConverter tokenEnhancer() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(privateKey);
        converter.setVerifierKey(publicKey);
        return converter;
    }
    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(tokenEnhancer());
    }
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore())
                .accessTokenConverter(tokenEnhancer());
    }
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient(clientid).secret(passwordEncoder.encode(clientSecret)).scopes("read", "write")
                .authorizedGrantTypes("password", "refresh_token").accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(36000);

    }
}
