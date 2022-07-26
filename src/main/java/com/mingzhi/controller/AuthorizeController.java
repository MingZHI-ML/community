package com.mingzhi.controller;

import com.mingzhi.dto.AccessTokenDto;
import com.mingzhi.dto.GitHubUser;
import com.mingzhi.provider.GitHubProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

/**
 *
 */
@Controller
public class AuthorizeController {

    @Autowired
    private GitHubProvider gitHubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;
    /**
     *
     * @param code
     * @param state
     * @return
     */
    @GetMapping("/callback")
    public String callback(String code,String state){
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_uri(redirectUri);
        accessTokenDto.setClient_id(clientId);
        System.out.println(clientId);
        System.out.println(redirectUri);
        accessTokenDto.setClient_secret(clientSecret);
        System.out.println(clientSecret);
        accessTokenDto.setState(state);
        // ctrl+alt+v快速定义选中的对象变量
        String accessToken = gitHubProvider.getAccessToken(accessTokenDto);
        GitHubUser gitHubUser = gitHubProvider.gitHubUser(accessToken);
        System.out.println(gitHubUser.toString());
        return "index";
    }

}
