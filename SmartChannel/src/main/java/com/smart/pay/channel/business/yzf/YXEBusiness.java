package com.smart.pay.channel.business.yzf;

import com.smart.pay.channel.pojo.yzf.YXEAddress;
import com.smart.pay.channel.pojo.yzf.YXEBankBin;

import java.util.List;

public interface YXEBusiness {


    YXEAddress getYXEAddressByCityName(String cityName);

    YXEBankBin getYXEBankBinByBankName(String creditCardBankName);

    List<YXEAddress> findByCity(String provinceId);

}
