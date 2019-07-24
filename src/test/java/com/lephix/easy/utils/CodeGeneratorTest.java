package com.lephix.easy.utils;

import org.junit.Ignore;
import org.junit.Test;

public class CodeGeneratorTest {

    @Test
    @Ignore
    public void testDB() throws Exception {
        new CodeGenerator("com.lephix.easy", null, "jdbc:mysql://localhost:3306/easy_jdbc", "root", "");

    }

}