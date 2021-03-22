package me.method17.configobf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.Compiler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Random;

public class Utils {
    public static String readFile(File file) {
        try {
            return new String(Files.readAllBytes(file.toPath()),StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeFile(String path, String text) {
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8));
            writer.write(text);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(str.length()-1);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static int randInt(double min, double max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static String doMinifyJson(String json){
        String result=json;
        try {
            Object object=JSON.parse(json);
            if(object instanceof JSONArray){
                result=((JSONArray)object).toJSONString();
            }else if(object instanceof JSONObject){
                result=((JSONObject)object).toJSONString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String compileJs(String code){
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

        // The compiler is responsible for generating the compiled code; it is not
        // accessible via the Result.
        if(compiler.getErrorCount() > 0){
            StringBuilder erroInfo = new StringBuilder();
            for(JSError jsError: compiler.getErrors()) {
                erroInfo.append(jsError.toString());
            }
        }
        return compiler.toSource();
    }
}
