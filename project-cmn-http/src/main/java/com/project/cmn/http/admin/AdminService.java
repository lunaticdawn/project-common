package com.project.cmn.http.admin;

import com.project.cmn.http.persistence.mapper.conditioncoupon.AdminMapper;
import com.project.cmn.http.persistence.mapper.lmmdev11.Lmmdev11AdminMapper;
import com.project.cmn.http.persistence.model.conditioncoupoon.AdminModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminMapper adminMapper;
    private final Lmmdev11AdminMapper lmmdev11AdminMapper;

    public AdminModel retrieveAdmin(String adminId) {
        lmmdev11AdminMapper.selectAdmin("test");
        return adminMapper.selectAdmin(adminId);
    }
}
