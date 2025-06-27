package com.smart.pay.channel.repository.hqx;

import com.smart.pay.channel.pojo.hq.HQERegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HQERegionRepository extends JpaRepository<HQERegion, String>, JpaSpecificationExecutor<HQERegion> {

	@Query("select hq from HQERegion hq where hq.parentId=:parentId")
	public List<HQERegion> getHQERegionByParentId(@Param("parentId") String parentId);

	@Query("select hq from HQERegion hq where hq.regionName LIKE CONCAT('%',:name,'%')")
	public List<HQERegion> getHQERegionByParentName(@Param("name") String name);

}
