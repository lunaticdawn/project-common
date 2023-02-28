package com.project.cmn.http.persistence.mapper.lmmdev11;

import com.project.cmn.http.persistence.model.lmmdev11.Lmmdev11AdminModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface Lmmdev11AdminMapper {
    Lmmdev11AdminModel selectAdmin(@Param("adminId") String adminId);
}
