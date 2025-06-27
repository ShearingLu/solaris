package com.smart.pay.channel.business.yzf;

import com.smart.pay.channel.pojo.yzf.YXEAddress;
import com.smart.pay.channel.pojo.yzf.YXEBankBin;
import com.smart.pay.channel.repository.yzf.YXEAddressRepository;
import com.smart.pay.channel.repository.yzf.YXEBankBinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;


@Service
public class YXEBusinesslmpl implements YXEBusiness {

    @Autowired
    EntityManager em;

    @Autowired
    YXEBankBinRepository yxeBankBinRepository;

    @Autowired
    YXEAddressRepository yxeAddressRepository;

    // 获取银行标识码
    @Override
    public YXEBankBin getYXEBankBinByBankName(String creditCardBankName) {
        List<YXEBankBin> bankBinList = yxeBankBinRepository.findAll(new Specification<YXEBankBin>() {
            @Override
            public Predicate toPredicate(Root<YXEBankBin> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("bankName"), creditCardBankName);
            }
        });
        if(bankBinList!=null && !bankBinList.isEmpty()){
            return bankBinList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<YXEAddress> findByCity(String provinceId) {
        return yxeAddressRepository.findAll(new Specification<YXEAddress>() {
            @Override
            public Predicate toPredicate(Root<YXEAddress> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("provinceId"),provinceId);
            }
        });
    }

    // 获取地区标识码
    @Override
    public YXEAddress getYXEAddressByCityName(String cityName) {
        return yxeAddressRepository.findOne(new Specification<YXEAddress>() {
            @Override
            public Predicate toPredicate(Root<YXEAddress> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("cityName"),cityName);
            }
        });
    }


}
