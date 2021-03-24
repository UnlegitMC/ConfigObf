package me.method17.configobf.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;

public class ScriptUtil {
    public static String compileJs(String code) {
        Compiler compiler = new Compiler();

        CompilerOptions options = new CompilerOptions();
        // Simple mode is used here, but additional options could be set, too.
        CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);

        // To get the complete set of externs, the logic in
        // CompilerRunner.getDefaultExterns() should be used here.
        SourceFile extern = SourceFile.fromCode("externs.js",
                "function alert(x) {}");

        // The dummy input name "input.js" is used here so that any warnings or
        // errors will cite line numbers in terms of input.js.
        SourceFile input = SourceFile.fromCode("input.js", code);

        // compile() returns a Result, but it is not needed here.
        compiler.compile(extern, input, options);

        return compiler.toSource();
    }

    public static String obfJson(String json, int amount, int length, String msg) {
        String result = json;
        try {
            Object object = JSON.parse(json);
            if (object instanceof JSONArray) {
                result = ((JSONArray) object).toJSONString();
            } else if (object instanceof JSONObject) {
                JSONObject jsonObject = ((JSONObject) object);
                for (int i = 0; i < amount; i++) {
                    jsonObject.put(OtherUtil.randomString(length), msg);
                }
                result = jsonObject.toJSONString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
