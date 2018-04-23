package com.kmzyc.search.app.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

public class BaseController
{

	protected HttpServletRequest getRequest()
	{
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		return req;
	}

	protected HttpServletResponse getResponse()
	{
		HttpServletResponse resp = ((ServletWebRequest) RequestContextHolder
				.getRequestAttributes()).getResponse();
		return resp;
	}
}
