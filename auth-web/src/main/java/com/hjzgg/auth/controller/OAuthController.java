package com.hjzgg.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.hjzgg.auth.domain.dto.LightUserResult;
import com.hjzgg.auth.service.UserApiImpl;
import com.hjzgg.auth.util.OAuthValidate;
import com.hjzgg.auth.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by hujunzheng on 2017/5/23.
 */
@Controller
@RequestMapping(value = "oauth")
public class OAuthController {

    @Resource
    private UserApiImpl userApi;

    @Value(value = "#{config['expiresIn']}")
    private String expiresIn;

    /**
     * 获取授权码-服务端
     *
     * @param request
     * @return
     * @throws OAuthProblemException
     * @throws OAuthSystemException
     */
    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    @ResponseBody
    public Object authorize(HttpServletRequest request) throws URISyntaxException, OAuthProblemException, OAuthSystemException {
        try {
            // 构建OAuth授权请求
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);

            //得到到客户端重定向地址
            String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
            String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);

            // 1.获取OAuth客户端id
            String clientId = oauthRequest.getClientId();
            // 校验客户端id是否正确
            LightUserResult lightUserResult = userApi.queryUserByClientId(clientId);
            if (null == lightUserResult) {
                OAuthResponse response =
                        OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                                .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                                .setErrorDescription("无效的客户端ID")
                                .buildJSONMessage();
                return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
            }

            Subject subject = SecurityUtils.getSubject();
            //如果用户没有登录，跳转到登陆页面
            if (!subject.isAuthenticated()) {
                if (!login(subject, request)) {//登录失败时跳转到登陆页面
                    HttpHeaders headers = new HttpHeaders();
                    headers.setLocation(new URI(request.getContextPath() + "/login.jsp?"
                            + OAuth.OAUTH_REDIRECT_URI + "=" + redirectURI
                            + "&" + OAuth.OAUTH_RESPONSE_TYPE + "=" + responseType
                            + "&" + OAuth.OAUTH_CLIENT_ID + "=" + clientId)
                    );
                    return new ResponseEntity(headers, HttpStatus.TEMPORARY_REDIRECT);
                }
            }

            // 2.生成授权码
            String authCode = null;
            // ResponseType仅支持CODE和TOKEN
            if (responseType.equals(ResponseType.CODE.toString())) {
                OAuthIssuerImpl oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
                authCode = oAuthIssuer.authorizationCode();
                // 存入缓存中authCode-username
                RedisUtil.getRedis().set(authCode, lightUserResult.getUserName());
            }

            //进行OAuth响应构建
            OAuthASResponse.OAuthAuthorizationResponseBuilder builder =
                    OAuthASResponse.authorizationResponse(request,
                            HttpServletResponse.SC_FOUND);
            //设置授权码
            builder.setCode(authCode);

            //构建响应
            final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();
            //根据OAuthResponse返回ResponseEntity响应
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(response.getLocationUri()));
            return new ResponseEntity(headers, HttpStatus.valueOf(response.getResponseStatus()));
        } catch (OAuthProblemException e) {
            //出错处理
            String redirectUri = e.getRedirectUri();
            if (OAuthUtils.isEmpty(redirectUri)) {
                //告诉客户端没有传入redirectUri直接报错
                return new ResponseEntity(
                        "OAuth callback url needs to be provided by client!!!", HttpStatus.NOT_FOUND);
            }
            //返回错误消息（如?error=）
            final OAuthResponse response =
                    OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                            .error(e).location(redirectUri).buildQueryMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(response.getLocationUri()));
            return new ResponseEntity(headers, HttpStatus.valueOf(response.getResponseStatus()));
        } catch (Exception e) {
            return new ResponseEntity("内部错误", HttpStatus.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
        }
    }

    private boolean login(Subject subject, HttpServletRequest request) {
        if ("get".equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return false;
        }

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            return true;
        } catch (Exception e) {
            request.setAttribute("error", "登录失败:" + e.getClass().getName());
            return false;
        }
    }

    /**
     * 获取访问令牌
     *
     * @param request
     * @return
     * @throws OAuthProblemException
     * @throws OAuthSystemException
     */
    @RequestMapping(value = "accessToken", method = RequestMethod.POST)
    @ResponseBody
    public Object accessToken(HttpServletRequest request) throws OAuthProblemException, OAuthSystemException {
        try {
            // 构建OAuth请求
            OAuthTokenRequest tokenRequest = new OAuthTokenRequest(request);

            // 1.获取OAuth客户端id
            String clientId = tokenRequest.getClientId();
            // 校验客户端id是否正确
            LightUserResult lightUserResult = userApi.queryUserByClientId(clientId);
            if (null == lightUserResult) {
                OAuthResponse oAuthResponse = OAuthResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription("无效的客户端ID")
                        .buildJSONMessage();
                return new ResponseEntity(oAuthResponse.getBody(), HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
            }

            // 2.检查客户端安全key是否正确
            if (!lightUserResult.getClientSecret().equals(tokenRequest.getClientSecret())) {
                OAuthResponse oAuthResponse = OAuthResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                        .setErrorDescription("客户端安全key认证不通过")
                        .buildJSONMessage();
                return new ResponseEntity<>(oAuthResponse.getBody(), HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
            }

            // 3.检查授权码是否正确
            String authCode = tokenRequest.getParam(OAuth.OAUTH_CODE);
            // 检查验证类型，此处只检查AUTHORIZATION_CODE类型，其他的还有password或REFRESH_TOKEN
            if (!tokenRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
                if (null == RedisUtil.getRedis().get(authCode)) {
                    OAuthResponse response = OAuthASResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.TokenResponse.INVALID_GRANT)
                            .setErrorDescription("授权码错误")
                            .buildJSONMessage();
                    return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));

                }
            }

            // 4.生成访问令牌Access Token
            OAuthIssuer oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
            final String accessToken = oAuthIssuer.accessToken();
            // 将访问令牌加入缓存：accessToken-username
            RedisUtil.getRedis().set(accessToken, lightUserResult.getUserName());

            // 5.生成OAuth响应
            OAuthResponse response = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setExpiresIn(expiresIn)
                    .buildJSONMessage();

            return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("内部错误", HttpStatus.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
        }

    }

    @RequestMapping("validate")
    @ResponseBody
    public JSONObject oauthValidate(HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {
        ResponseEntity responseEntity = OAuthValidate.oauthValidate(request);
        JSONObject result = new JSONObject();
        result.put("msg", responseEntity.getBody());
        result.put("code", responseEntity.getStatusCode().value());
        return result;
    }

    @RequestMapping(value = "userInfo", method = RequestMethod.GET)
    @ResponseBody
    public Object userInfo(HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {
        try {
            //构建OAuth资源请求
            OAuthAccessResourceRequest oauthRequest =
                    new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);
            //获取Access Token
            String accessToken = oauthRequest.getAccessToken();

            //验证Access Token
            ResponseEntity responseEntity = OAuthValidate.oauthValidate(request);
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                // 如果不存在/过期了，返回未验证错误，需重新验证
                OAuthResponse oauthResponse = OAuthRSResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setRealm("auth-web")
                        .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                        .buildHeaderMessage();

                HttpHeaders headers = new HttpHeaders();
                headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,
                        oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
                return new ResponseEntity(headers, HttpStatus.UNAUTHORIZED);
            }
            //返回用户名
            String username = RedisUtil.getRedis().get(accessToken);
            return new ResponseEntity(username, HttpStatus.OK);
        } catch (OAuthProblemException e) {
            //检查是否设置了错误码
            String errorCode = e.getError();
            if (OAuthUtils.isEmpty(errorCode)) {
                OAuthResponse oauthResponse = OAuthRSResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setRealm("auth-web")
                        .buildHeaderMessage();

                HttpHeaders headers = new HttpHeaders();
                headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,
                        oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
                return new ResponseEntity(headers, HttpStatus.UNAUTHORIZED);
            }

            OAuthResponse oauthResponse = OAuthRSResponse
                    .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                    .setRealm("auth-web")
                    .setError(e.getError())
                    .setErrorDescription(e.getDescription())
                    .setErrorUri(e.getUri())
                    .buildHeaderMessage();

            HttpHeaders headers = new HttpHeaders();
            headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,
                    oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}