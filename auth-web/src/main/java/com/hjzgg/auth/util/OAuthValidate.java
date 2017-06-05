package com.hjzgg.auth.util;

import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OAuthValidate {

    public static ResponseEntity oauthValidate(HttpServletRequest request) throws OAuthProblemException, OAuthSystemException {

        // 构建OAuth资源请求
        OAuthAccessResourceRequest resourceRequest = new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);
        // 获取访问令牌access Token
        String accessToken = resourceRequest.getAccessToken();
        // 验证访问令牌
        if (null == RedisUtil.getRedis().get(accessToken)) {
            // 如果不存在或过期了，返回未验证错误，需重新验证
            OAuthResponse response = OAuthRSResponse
                    .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                    .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                    .setErrorDescription("访问令牌不存在或已过期，请重新验证")
                    .buildJSONMessage();
            return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
        }
        return new ResponseEntity("验证成功", HttpStatus.valueOf(HttpServletResponse.SC_OK));
    }

}
