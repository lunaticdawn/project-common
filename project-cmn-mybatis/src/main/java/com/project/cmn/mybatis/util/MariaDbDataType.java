package com.project.cmn.mybatis.util;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * MariaDB의 Data Type과 맵핑되는 Java Type에 대한 열거형
 */
public enum MariaDbDataType {
    TINYINT("TINYINT", "Integer"),
    SMALLINT("SMALLINT", "Integer"),
    MEDIUMINT("MEDIUMINT", "Integer"),
    INT("INT", "Integer"),
    BIGINT("BIGINT", "Long"),
    DECIMAL("DECIMAL", "Double"),
    FLOAT("FLOAT", "Double"),
    DATE("DATE", "LocalDate"),
    TIME("TIME", "LocalTime"),
    DATETIME("DATETIME", "LocalDateTime"),
    TIMESTAMP("TIMESTAMP", "LocalDateTime"),
    YEAR("YEAR", "String"),
    BINARY("BINARY", "String"),
    BLOB("BLOB", "String"),
    TEXT("TEXT", "String"),
    CHAR("CHAR", "String"),
    ENUM("ENUM", "String"),
    INET4("INET4", "String"),
    INET6("INET6", "String"),
    JSON("JSON", "String"),
    MEDIUMBLOB("MEDIUMBLOB", "String"),
    MEDIUMTEXT("MEDIUMTEXT", "String"),
    LONGBLOB("LONGBLOB", "String"),
    LONGTEXT("LONGTEXT", "String"),
    TINYBLOB("TINYBLOB", "String"),
    TINYTEXT("TINYTEXT", "String"),
    VARCHR("VARCHAR", "String"),
    UUID("UUID", "String");

    @Getter
    private final String dataType;

    @Getter
    private final String javaType;

    MariaDbDataType(String dataType, String javaType) {
        this.dataType = dataType;
        this.javaType = javaType;
    }

    public static String getJavaType(String dataType) {
        String javaType = null;
        MariaDbDataType[] enumList = MariaDbDataType.values();

        for (MariaDbDataType mariaDbDataType : enumList) {
            if (StringUtils.equalsIgnoreCase(mariaDbDataType.dataType, dataType)) {
                javaType = mariaDbDataType.javaType;
                break;
            }
        }

        return javaType;
    }
}
