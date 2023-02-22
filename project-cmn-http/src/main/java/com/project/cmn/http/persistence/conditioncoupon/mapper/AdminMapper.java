package com.project.cmn.http.persistence.conditioncoupon.mapper;

import com.project.cmn.http.persistence.conditioncoupon.model.AdminModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMapper {
    AdminModel selectAdmin(@Param("adminId") String adminId);
}
