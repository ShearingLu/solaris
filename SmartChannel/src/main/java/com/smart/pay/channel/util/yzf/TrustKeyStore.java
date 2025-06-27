package com.smart.pay.channel.util.yzf;

import javax.net.ssl.TrustManagerFactory;

/**
 * <b>功能说明:
 * </b>
 */
public class TrustKeyStore {
	private TrustManagerFactory trustManagerFactory;
	
	public TrustKeyStore(TrustManagerFactory trustManagerFactory){
		this.trustManagerFactory = trustManagerFactory;
	}
	
	public TrustManagerFactory getTrustManagerFactory(){
		return trustManagerFactory;
	}
}
