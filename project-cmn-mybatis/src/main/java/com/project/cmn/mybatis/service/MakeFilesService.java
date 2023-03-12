package com.project.cmn.mybatis.service;

import com.project.cmn.mybatis.dto.FileInfoDto;
import com.project.cmn.mybatis.dto.ProjectInfoDto;
import com.project.cmn.mybatis.mariadb.service.MakeFilesForMariaDbService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.springframework.stereotype.Service;

import java.io.File;

@RequiredArgsConstructor
@Service
public class MakeFilesService {
    private final MakeFilesForMariaDbService makeFilesForMariaDbService;

    public void makeFiles(ProjectInfoDto param) {
        String filename = param.getTableName();

        if (StringUtils.isNotBlank(param.getPrefixReplaceByBlank())) {
            filename = RegExUtils.replaceFirst(param.getTableName(), param.getPrefixReplaceByBlank(), "");
        }

        filename = CaseUtils.toCamelCase(filename, true, '_');

        String separator;

        if (File.separator.equals("\\")) {
            separator = "\\\\";
        } else {
            separator = "/";
        }

        String dir = param.getProjectPath()
                + File.separator + RegExUtils.replaceAll(param.getBasePackage(), "\\.", separator);

        FileInfoDto fileInfoDto = new FileInfoDto();

        if (StringUtils.isNotBlank(param.getDtoPostfix())) {
            fileInfoDto.setDtoFilename(filename + param.getDtoPostfix());
        } else {
            fileInfoDto.setDtoFilename(filename);
        }

        fileInfoDto.setDtoPath(dir + File.separator + RegExUtils.replaceAll(param.getDtoPackage(), "\\.", separator) + File.separator + fileInfoDto.getDtoFilename() + ".java");
        fileInfoDto.setDtoPackage(param.getBasePackage() + "." + param.getDtoPackage());

        if (StringUtils.equalsIgnoreCase(param.getDbmsName(), "mariadb")
                || StringUtils.equalsIgnoreCase(param.getDbmsName(), "mysql")) {
            makeFilesForMariaDbService.makeFiles(param.getTableSchema(), param.getTableName(), fileInfoDto);
        }
    }
}
