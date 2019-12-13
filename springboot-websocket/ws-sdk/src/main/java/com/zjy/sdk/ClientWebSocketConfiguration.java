package com.zjy.sdk;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 客户端权限配置
 * 
 * @author zhaojy
 * @date 2019/12
 */
@Data
public class ClientWebSocketConfiguration {

	private String host;
	private String accessKeyId;
	private String accessKeySecret;

	public ClientWebSocketConfiguration(@NonNull String host, @NonNull String accessKeyId, @NonNull String accessKeySecret) {

		Assert.notNull(host, "[error] websocket host can't be null");

		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
		this.host = host;
	}

}
