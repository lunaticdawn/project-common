package com.project.cmn.mybatis.mariadb.service;

import com.project.cmn.mybatis.dto.FileInfoDto;
import com.project.cmn.mybatis.mariadb.dto.ColumnsDto;
import com.project.cmn.mybatis.mariadb.mapper.ColumnsMapper;
import com.project.cmn.mybatis.util.MariaDbDataType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MakeFilesForMariaDbService {
    private final ColumnsMapper columnsMapper;

    public void makeFiles(String tableSchema, String tableName, FileInfoDto fileInfoDto) {
        StringBuilder buff = new StringBuilder();

        // Dto 생성
        buff.append("package ").append(fileInfoDto.getDtoPackage()).append("\n");
        buff.append("\n");
        buff.append("import com.fasterxml.jackson.annotation.JsonProperty;").append("\n");
        buff.append("import lombok.Getter;").append("\n");
        buff.append("import lombok.Setter;").append("\n");
        buff.append("import lombok.ToString;").append("\n");
        buff.append("\n");
        buff.append("/**");

        if (StringUtils.isNotBlank(tableSchema)) {
            buff.append(" * ").append(tableSchema).append(".").append(tableName).append("\n");
        } else {
            buff.append(" * ").append(tableName).append("\n");
        }

        buff.append(" */").append("\n");
        buff.append("@Getter").append("\n");
        buff.append("@Setter").append("\n");
        buff.append("@ToString").append("\n");
        buff.append("public class ").append(fileInfoDto.getDtoFilename()).append(" {").append("\n");

        List<ColumnsDto> columnsList = columnsMapper.selectColumnList(tableSchema, tableName);

        for (ColumnsDto columnsDto : columnsList) {
            buff.append("    /**").append("\n");
            buff.append("     * ").append(columnsDto.getColumnComment()).append("\n");
            buff.append("     */").append("\n");
            buff.append("    @JsonProperty(\"").append(columnsDto.getColumnName()).append("\")").append("\n");

            buff.append("    private ").append(this.getJavaType(columnsDto.getDataType(), false)).append(" ").append(CaseUtils.toCamelCase(columnsDto.getColumnName(), false, '_')).append(";").append("\n");

            buff.append("\n");
        }

        buff.append("}");
    }

    private String getJavaType(String dataType, boolean unsigned) {
        String javaType = MariaDbDataType.getJavaType(dataType);

        if (unsigned && StringUtils.equalsIgnoreCase(dataType, "INT")) {
            javaType = "Long";
        }

        return javaType;
    }
}
