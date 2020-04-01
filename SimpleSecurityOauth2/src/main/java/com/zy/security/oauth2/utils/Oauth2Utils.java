package com.zy.security.oauth2.utils;

/**
 * @author: zy;
 * @DateTime: 2020年3月18日 下午4:04:23;
 * @Description: 默认属性类，提供请求默认参数和内容标识
 */
public final class Oauth2Utils {
	//-----------------------------------身份主体标识------------------------------------
	public static String bearer_type = "Bearer";
	public static String oauth2_type = "OAuth2";
	
	//-------------------------------------grant_type------------------------------------
	public static String implicit = "implicit";
	public static String authorization_code = "authorization_code";
	public static String password = "password";
	public static String client_credentials = "client_credentials";
	public static String refresh_token = "refresh_token";
	
	//---------------------------------oauth2 请求参数----------------------------------
	public static String client_id = "client_id";
	public static String client_secret = "client_secret";
	public static String state = "state";
	public static String scope = "scope";
	public static String token = "token";
	public static String code = "code";
	public static String tokenType = "token_type";
	public static String refresh_token_parame = "refresh_token";
	public static String expires = "expires_in";
	public static String redirect_uri = "redirect_uri";
	public static String response_type = "response_type";
	/**  访问令牌存储于请求参数中的参数名，默认“access_token” */
	public static String access_token = "access_token";
	/** 访问令牌存储于请求头中的参数名，默认“AuthorizationToken” - 令牌提取的时候使用 */
	public static String request_Header_token = "AuthorizationToken";
	// 批准自动授权
	public static String user_oauth_approval  = "user_oauth_approval";
	public static String grant_type = "grant_type";
	
	//---------------------------------checkToken相关参数------------------------------------------
	public static String user_name = "user_name";
	public static String resources_id = "resources_id";
	public static String active = "active" ;
	public static String authorities = "authorities";
	
	//---------------------------------------oauth2 uri-------------------------------------
	public static String oauth_authorize = "/oauth/authorize";
	public static String oauth_token = "/oauth/token";
	public static String oauth_check_token = "/oauth/check_token";
	// 用户授权界面
	public static String oauth_confirm_access = "/oauth/confirm_access";
	//---------------------------------------Security参数-------------------------------------
	public static String usernameParameter = "username";
	public static String passwordParameter = "password";
}
