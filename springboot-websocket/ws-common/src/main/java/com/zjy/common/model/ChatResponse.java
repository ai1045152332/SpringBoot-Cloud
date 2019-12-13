package com.zjy.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Chat响应
 *
 * @author from
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponse {

	/**
	 * 唯一标识 uniqueId
	 */
	private String uniqueId;

	/**
	 * 错误码，非0代表有系统错误
	 */
	private Integer errorCode;

	/**
	 * 有错误时返回出错信息
	 */
	private String errorMessage;

	/**
	 * 企业Id
	 */
	private Integer userId;

	/**
	 * 响应文本
	 */
	private String playText;
	/**
	 * 响应意图
	 */
	private String responseIntent;
}
