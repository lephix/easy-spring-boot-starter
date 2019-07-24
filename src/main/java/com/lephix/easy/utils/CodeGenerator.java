package com.lephix.easy.utils;

import com.google.common.base.CaseFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.*;

import static com.google.common.base.CaseFormat.*;
import static com.google.common.base.Preconditions.checkState;
import static java.io.File.separator;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

@Slf4j
public class CodeGenerator {

    public static final String F_TYPE = "type";
    public static final String F_NAME = "name";

    public enum ORMapping {
        VARCHAR("String"),
        TEXT("String"), TINYTEXT("String"), LONGTEXT("String"),
        INT("Integer"), TINYINT("Integer"), SMALLINT("Integer"),
        BIGINT("Long"),
        DOUBLE("Double"), FLOAT("Double"), REAL("Double"),
        DATE("Timestamp"), DATETIME("Timestamp"), TIMESTAMP("Timestamp");

        public final String className;

        ORMapping(String className) {
            this.className = className;
        }
    }

    private Configuration cfg;
    private String projectBasePackage;
    /**
     * Key is entity name, value is the list consist of all fields.
     */
    private Map<String, List<Map<String, Object>>> entityFieldsMap = Collections.emptyMap();
    /**
     * The destination for generating codes.
     */
    private String localPath;

    public CodeGenerator(String projectBasePackage, @Nullable String driverName, String jdbcUrl,
                         String username, String password) throws Exception {
        Class.forName(defaultIfBlank(driverName, "com.mysql.cj.jdbc.Driver"));
        Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
        DatabaseMetaData dbmd = conn.getMetaData();
        ResultSet dbrs = dbmd.getTables(conn.getCatalog(), null, null, new String[]{"TABLE", "VIEW"});

        // Collect all tables.
        entityFieldsMap = new HashMap<>();
        List<String> tables = new ArrayList<>();
        while (dbrs.next()) {
            tables.add(dbrs.getString("TABLE_NAME"));
        }

        // Collect all fields for each table.
        for (String table : tables) {
            List<Map<String, Object>> entityFieldList = new ArrayList<>();
            ResultSet rs = dbmd.getColumns(conn.getCatalog(), null, table, null);
            while (rs.next()) {
                if (rs.getString("COLUMN_NAME").equalsIgnoreCase("id")) {
                    continue;
                }
                Map<String, Object> fieldMap = MapHelper.<String>getInstance()
                        .add(F_NAME, LOWER_UNDERSCORE.to(LOWER_CAMEL, rs.getString("COLUMN_NAME")))
                        .add(F_TYPE, ORMapping.valueOf(rs.getString("TYPE_NAME")).className)
                        .build();
                entityFieldList.add(fieldMap);
            }

            String entityName = LOWER_UNDERSCORE.to(UPPER_CAMEL, table);
            entityFieldsMap.put(entityName, entityFieldList);
        }

        this.projectBasePackage = projectBasePackage;
        init();
    }

    public CodeGenerator(String projectBasePackage) {
        this.projectBasePackage = projectBasePackage;
        init();
    }

    private void init() {
        localPath = new File("").getAbsolutePath() + separator + "src" + separator + "main" + separator + "java"
                + separator + projectBasePackage.replace(".", separator);

        cfg = new Configuration(Configuration.VERSION_2_3_28);
        cfg.setClassForTemplateLoading(this.getClass(), "/code-generator-template");
        cfg.setDefaultEncoding("UTF-8");
    }

    public void generateAll() throws Exception {
        for (String entityName : entityFieldsMap.keySet()) {
            checkState(StringUtils.equals(capitalize(entityName), entityName), "entityName must be capitalized");
            generateEntity(entityName);
            generateJpaRepository(entityName);
            generateService(entityName);
            generateAdminController(entityName);
        }
    }

    private void generateEntity(String entityName) throws Exception {
        Template temp = cfg.getTemplate("entity.tpl");
        StringWriter writer = new StringWriter();
        Map<String, Object> data = prepareData(entityName);
        temp.process(data, writer);
        File file = createFile(localPath + separator + "entity" + separator + entityName + ".java");

        try (FileWriter fw = new FileWriter(file)) {
            IOUtils.write(writer.getBuffer().toString(), fw);
            log.info("[SUCCESS] Entity {} generated successfully.", file);
        }
    }

    private void generateJpaRepository(String entityName) throws Exception {
        Template temp = cfg.getTemplate("jpa-repository.tpl");
        StringWriter writer = new StringWriter();
        Map<String, Object> data = prepareData(entityName);
        temp.process(data, writer);
        File file = createParent(localPath + separator + "repository" + separator + entityName + "Repository.java");
        if (file.exists()) {
            log.info("[SKIP] Repository {} is exist. Skip it.", file.getAbsolutePath());
            return;
        }

        try (FileWriter fw = new FileWriter(file)) {
            IOUtils.write(writer.getBuffer().toString(), fw);
            log.info("[SUCCESS] Repository {} generated successfully.", file);
        }
    }

    private void generateService(String entityName) throws Exception {
        Template temp = cfg.getTemplate("service.tpl");
        StringWriter writer = new StringWriter();
        Map<String, Object> data = prepareData(entityName);
        temp.process(data, writer);
        File file = createParent(localPath + separator + "service/" + separator + entityName + "Service.java");
        if (file.exists()) {
            log.info("[SKIP] Service {} is exist. Skip it.", file.getAbsolutePath());
            return;
        }

        try (FileWriter fw = new FileWriter(file)) {
            IOUtils.write(writer.getBuffer().toString(), fw);
            log.info("[SUCCESS] Service {} generated successfully.", file);
        }
    }

    private void generateAdminController(String entityName) throws Exception {
        Template temp = cfg.getTemplate("admin-controller.tpl");
        StringWriter writer = new StringWriter();
        Map<String, Object> data = prepareData(entityName);
        temp.process(data, writer);
        File file = createParent(localPath + separator + "controller" + separator +
                "api" + separator + "admin" + separator + entityName + "Controller.java");
        if (file.exists()) {
            log.info("[SKIP] AdminController {} is exist. Skip it.", file.getAbsolutePath());
            return;
        }

        try (FileWriter fw = new FileWriter(file)) {
            IOUtils.write(writer.getBuffer().toString(), fw);
            log.info("[SUCCESS] AdminController {} generated successfully.", file);
        }
    }

    private Map<String, Object> prepareData(String entityName) {
        Map<String, Object> data = new HashMap<>();
        data.put("projectBasePackage", projectBasePackage);
        data.put("entityName", entityName);
        data.put("entityFields", entityFieldsMap.get(entityName));

        return data;
    }

    private File createFile(String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                checkState(file.getParentFile().mkdir(), "Create parent directory failed.");
            }
            checkState(file.createNewFile(), "Create file failed.");
        }
        return file;
    }

    private File createParent(String path) {
        File file = new File(path);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                checkState(file.getParentFile().mkdir(), "Create parent directory failed.");
            }
        }
        return file;
    }

    public void setEntityFieldsMap(Map<String, List<Map<String, Object>>> entityFieldsMap) {
        if (entityFieldsMap == null) {
            throw new RuntimeException("entityFieldsMap cannot be null");
        }
        this.entityFieldsMap = entityFieldsMap;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

}
