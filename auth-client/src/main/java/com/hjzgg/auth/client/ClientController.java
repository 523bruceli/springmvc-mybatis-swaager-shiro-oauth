package com.hjzgg.auth.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by apple on 2017/6/4.
 */

@Controller
@RequestMapping("client")
public class ClientController {

    @RequestMapping("authority")
    @ResponseBody
    public JSONObject authority() throws OAuthSystemException, OAuthProblemException {
        JSONObject result = new JSONObject();
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthClientRequest codeTokenRequest = OAuthClientRequest
                .authorizationLocation("http://127.0.0.1:8080/auth-web/oauth/authorize")
                .setResponseType(ResponseType.CODE.toString())
                .setClientId("c1ebe466-1cdc-4bd3-ab69-77c3561b9dee")
                .buildQueryMessage();
        //获取 code
        OAuthResourceResponse codeResponse = oAuthClient.resource(
                codeTokenRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
        if(codeResponse.getResponseCode() != HttpServletResponse.SC_OK) {
            result.put("code", codeResponse.getResponseCode());
            result.put("msg", codeResponse.getBody());
        } else {
            String authCode = codeResponse.getBody();
            OAuthClientRequest accessTokenRequest = OAuthClientRequest
                    .tokenLocation("http://127.0.0.1:8080/auth-web/oauth/accessToken")
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setClientId("c1ebe466-1cdc-4bd3-ab69-77c3561b9dee").setClientSecret("d8346ea2-6017-43ed-ad68-19c0f971738b")
                    .setCode(authCode).setRedirectURI("http://127.0.0.1:8080/auth-web/")
                    .buildQueryMessage();
            //获取access token
            OAuthAccessTokenResponse tokenResponse =
                    oAuthClient.accessToken(accessTokenRequest, OAuth.HttpMethod.POST);
            if(tokenResponse.getResponseCode() != HttpServletResponse.SC_OK) {
                result.put("code", tokenResponse.getResponseCode());
                result.put("msg", tokenResponse.getBody());
                return result;
            } else {
                //验证token
                OAuthClientRequest validateRequest =
                        new OAuthBearerClientRequest("http://127.0.0.1:8080/auth-web/oauth/validate")
                                .setAccessToken(tokenResponse.getAccessToken()).buildQueryMessage();
                OAuthResourceResponse validateResponse = oAuthClient.resource(
                        validateRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
                if(validateResponse.getResponseCode() != HttpServletResponse.SC_OK) {
                    result.put("code", validateResponse.getResponseCode());
                    result.put("msg", validateResponse.getBody());
                } else {
                    JSONObject body = JSON.parseObject(validateResponse.getBody());
                    result.put("code", body.getString("code"));
                    result.put("msg", body.getString("msg"));
                }
            }
        }
        return result;
    }

}
