package com.zjy.common.model;

import lombok.Data;

/**
 * CC调用TiBot的请求体封装
 * 
 * @author Jiangsl
 *
 */
@Data
public class ChatRequest {

	/**
	 * 会话唯一标识
	 */
	private String uniqueId;
	/**
	 */
	private String type;
	/**
	 * 用户说的话
	 */
	private String query;
	/**
	 * 播放状态
	 */
	private Boolean playStatus;
	/**
	 * 意图
	 */
	private String intent;

}
