package com.kmzyc.search.facade.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import com.caucho.hessian.client.HessianProxyFactory;

public class IHessianProxyFactory extends HessianProxyFactory
{

	@Override
	protected URLConnection openConnection(URL url) throws IOException
	{
		URLConnection conn = super.openConnection(url);
		if (connectTimeOut > 0L) conn.setConnectTimeout((int) connectTimeOut);
		return conn;
	}

	public long getConnectTimeOut()
	{
		return connectTimeOut;
	}

	public void setConnectTimeOut(long connectTimeOut)
	{
		this.connectTimeOut = connectTimeOut;
	}

	private long	connectTimeOut;

}
