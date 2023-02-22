package com.project.cmn.http.persistence.lmmdev11.mapper;

import com.project.cmn.http.persistence.lmmdev11.model.Lmmdev11AdminModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface Lmmdev11AdminMapper {
    Lmmdev11AdminModel selectAdmin(@Param("adminId") String adminId);
}
