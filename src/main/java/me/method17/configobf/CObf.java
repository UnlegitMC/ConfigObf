package me.method17.configobf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static me.method17.configobf.Utils.*;

public class CObf {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        System.out.println("CONFIGOBF BY METHOD17");

        System.out.print("Input amount of random files:");
        int amount = new Integer(input.nextLine());

        System.out.print("Input length of random string:");
        int length = new Integer(input.nextLine());

        System.out.print("Input do folder obf(true/false):");
        boolean obfFolder = input.nextLine().equals("true");

        System.out.print("Input do file obf(true/false):");
        boolean obfFile = input.nextLine().equals("true");

        System.out.print("Input do generate null script(true/false):");
        boolean genNullScript = input.nextLine().equals("true");

        System.out.print("Input do obf script(true/false):");
        boolean obfScript = input.nextLine().equals("true");

        System.out.print("Input do minify json(true/false):");
        boolean minifyJson = input.nextLine().equals("true");

        System.out.print("Input config path:");
        File path = new File(input.nextLine());
        if(!path.isDirectory()){
            throw new IllegalStateException("CONFIG PATH VALUE NOT A FOLDER");
        }

        System.out.print("Input message in random file:");
        String msg = input.nextLine();

        System.out.println("----- STARTING OBF -----");
        doObfFolder(amount,length,obfFolder,obfFile,minifyJson,path,msg);
        if(obfScript){
            System.out.println("Obf script");
            obfScript(length,msg,path);
        }
        if(genNullScript){
            System.out.println("Gen null script");
            genNullScript(amount,length,msg,path);
        }
        System.out.println("----- OBF COMPLETE -----");
    }

    private static void obfScript(int length,String msg, File path){
        File scriptPath=new File(path.getPath()+"/scripts/");
        for(File file:scriptPath.listFiles()){
            if(file.isFile()&&file.getName().endsWith(".js")){
                writeFile(scriptPath.getPath()+"/"+getRandomString(length)+".js",jsSov4(compileJs(readFile(file)),msg));
                file.delete();
            }
        }
    }

    private static String jsSov4(String js,String msg){
        StringBuilder result= new StringBuilder("['" + msg + "']['filter']['constructor']('ConfigOBF'['constructor']['fromCharCode']['apply'](null,[");

        char[] chars=new char[js.length()];
        js.getChars(0,js.length(),chars,0);
        for(char chr:chars){
            result.append((int) chr);
            result.append(",");
        }

        result = new StringBuilder(result.substring(0, result.length() - 1));
        result.append("]))();");

        return result.toString();
    }

    private static void genNullScript(int amount,int length,String msg,File path){
        String scriptPath=path.getPath()+"/scripts/";
        for(int i=0;i<amount;i++){
            if(!new File(scriptPath+getRandomString(length)+".js").exists()) {
                StringBuilder obfMsg= new StringBuilder("//" + msg);
                String randStr=getRandomString(amount);
                for(int j=0;j<randInt(amount*3,amount*10);j++){
                    obfMsg.append(" ").append(randStr);
                }
                writeFile(scriptPath + getRandomString(length) + ".js", jsSov4(obfMsg.toString(),msg));
            }
        }
    }

    private static void doObfFolder(int amount,int length,boolean obfFolder,boolean obfFile,boolean minifyJson,File path,String msg){
        System.out.println("Obf "+path.getPath()+"...");
        for(File file:path.listFiles()){
            if(file.isDirectory()){
                if(obfFolder) {
                    for (int i = 0; i < amount; i++) {
                        File opath = new File(file.getPath() + "_" + getRandomString(length));
                        opath.mkdir();
                        for (int j = 0; j < file.listFiles().length; j++) {
                            writeFile(opath.getPath() + "/FILE_" + getRandomString(length), msg);
                        }
                    }
                }
                doObfFolder(amount, length, obfFolder, obfFile,minifyJson, file, msg);
            }else if(file.isFile()){
                if(obfFile) {
                    for (int i = 0; i < amount; i++) {
                        writeFile(file.getPath() + "_" + getRandomString(length), msg);
                    }
                }
                if(minifyJson){
                    if(file.getName().endsWith(".json")){
                        writeFile(file.getPath(), doMinifyJson(readFile(file)));
                    }
                }
            }
        }
    }
}
