package com.smart.pay.channel.repository.yzf;

import com.smart.pay.channel.pojo.yzf.YXEBankBin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface YXEBankBinRepository extends JpaRepository<YXEBankBin,Long>, JpaSpecificationExecutor<YXEBankBin> {

}
