package com.project.cmn.http.admin;

import com.project.cmn.http.persistence.mapper.conditioncoupon.AdminMapper;
import com.project.cmn.http.persistence.mapper.lmmdev11.Lmmdev11AdminMapper;
import com.project.cmn.http.persistence.model.conditioncoupoon.AdminModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminMapper adminMapper;
    private final Lmmdev11AdminMapper lmmdev11AdminMapper;

    @Transactional
    public AdminModel retrieveAdmin(String adminId) {
        AdminModel adminModel = new AdminModel();

        adminModel.setAdminNum("99");
        adminModel.setAdminId("AdminId");
        adminModel.setPasswd("Passwd");
        adminModel.setAdminNm("AdminNm");
        adminModel.setEmpNo("EmpNo");
        adminModel.setHpNum("HpNum");
        adminModel.setPwdInitYn("N");
        adminModel.setValidYn("Y");
        adminModel.setLoginFailCnt(0);

        adminMapper.insertAdmin(adminModel);

        return adminModel;
    }
}
