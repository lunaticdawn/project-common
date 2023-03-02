package com.project.cmn.datasource.jta;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import java.util.List;

@Slf4j
@Getter
@Setter
@ToString
public class JtaConfig {
    public static JtaConfig init(Environment env) {
        return Binder.get(env).bindOrCreate("project.jta", JtaConfig.class);
    }

    /**
     * JTA 사용여부
     */
    private boolean enabled;

    /**
     * XA 데이터소스 설정
     */
    private List<XADataSourceItem> itemList;
}
