package com.project.cmn.mybatis.service;

import com.project.cmn.mybatis.dto.ProjectInfoDto;
import com.project.cmn.mybatis.mariadb.service.MakeFilesForMariaDbService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MakeFilesService {
    private final MakeFilesForMariaDbService makeFilesForMariaDbService;

    public void makeFiles(ProjectInfoDto param) {
        if (StringUtils.equalsIgnoreCase(param.getDbmsName(), "mariadb")
                || StringUtils.equalsIgnoreCase(param.getDbmsName(), "mysql")) {
            makeFilesForMariaDbService.makeFiles(param);
        }
    }
}
