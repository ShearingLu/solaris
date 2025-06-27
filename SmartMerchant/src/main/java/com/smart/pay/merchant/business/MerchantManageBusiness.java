package com.smart.pay.merchant.business;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.smart.pay.merchant.pojo.Merchant;
import com.smart.pay.merchant.pojo.MerchantChannel;

public interface MerchantManageBusiness {


	/**注册一个商户*/
	public Merchant  createMerchant(Merchant merchant);


	public List<Merchant> allAuthMerchant();


	public Merchant  queryMerchantById(String merchantId);

	public 	Page<Merchant>   queryMerchantByParentId(String parentId ,String merchantId,Pageable pageable);


	public Page<Merchant> queryAllMerchants(String merchant_id,String merAuthStatus,Pageable pageable);

	public Merchant  queryMerchantByLoginid(String loginid, String loginpass);

	public Merchant  queryMerchantByLoginid(String loginid);


	public Merchant updateMerchant(Merchant merchant);



	/**新增一个商户可以使用的通道**/
	public MerchantChannel createMerchantChannel(MerchantChannel merchantChannel);

	/**查询所有的通道*/
	public List<MerchantChannel>  queryAllMerchantChannels(String merchantId);

	public List<MerchantChannel>  queryAllMerchantChannels();


	/**查询商家可以使用的支付通道*/
	public List<MerchantChannel>  queryAllMerchantChannelsByStatus(String merchantId, String status);

	/**关闭一个商家通道**/
	public void closeOrOpenMerchantChannel(String merchantId, String channelCode, String status);
}
