package com.project.cmn.mybatis.mariadb.service;

import com.project.cmn.mybatis.dto.ProjectInfoDto;
import com.project.cmn.mybatis.mariadb.dto.ColumnsDto;
import com.project.cmn.mybatis.mariadb.mapper.ColumnsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MakeFilesForMariaDbService {
    private final ColumnsMapper columnsMapper;

    public void makeFiles(ProjectInfoDto param) {
        List<ColumnsDto> columnsList = columnsMapper.selectColumnList(param.getTableSchema(), param.getTableName());
    }
}
