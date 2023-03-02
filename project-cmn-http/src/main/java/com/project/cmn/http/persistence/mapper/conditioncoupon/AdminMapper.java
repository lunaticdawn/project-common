package com.project.cmn.http.persistence.mapper.conditioncoupon;

import com.project.cmn.http.persistence.model.conditioncoupoon.AdminModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMapper {
    AdminModel selectAdmin(@Param("adminId") String adminId);

    int insertAdmin(AdminModel model);
}
