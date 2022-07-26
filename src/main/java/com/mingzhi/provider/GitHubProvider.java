package com.mingzhi.provider;

import com.alibaba.fastjson2.JSON;
import com.mingzhi.dto.AccessTokenDto;
import com.mingzhi.dto.GitHubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 *
 */
@Component   // 把对象交给spring容器管理
public class GitHubProvider {

    /**
     * 获取accessToken
     * @param accessTokenDto
     * @return
     */
    public String getAccessToken(AccessTokenDto accessTokenDto){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accessTokenDto));
            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String string = response.body().string();
                String[] split = string.split("&");
                String token = split[0].split("=")[1];
                System.out.println(token);
                return token;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }

    /**
     * 通过accessToken可以获得用户信息
     * @param accessToken
     * @return
     */
    public GitHubUser gitHubUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        /*Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();*/
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization","token "+ accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GitHubUser gitHubUser = JSON.parseObject(string, GitHubUser.class);
            return gitHubUser;
        } catch (IOException e) {
        }
        return null;
    }
}
