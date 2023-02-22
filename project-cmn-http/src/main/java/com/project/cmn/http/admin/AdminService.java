package com.project.cmn.http.admin;

import com.project.cmn.http.persistence.conditioncoupon.mapper.AdminMapper;
import com.project.cmn.http.persistence.conditioncoupon.model.AdminModel;
import com.project.cmn.http.persistence.lmmdev11.mapper.Lmmdev11AdminMapper;
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
