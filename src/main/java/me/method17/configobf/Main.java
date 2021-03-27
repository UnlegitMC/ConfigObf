package me.method17.configobf;

import com.alibaba.fastjson.JSONObject;
import me.method17.configobf.utils.OtherUtil;
import me.method17.configobf.utils.ScriptUtil;

import java.io.File;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("CONFIGOBF BY METHOD17");

        System.out.print("Input amount of random files:");
        int amount = Integer.parseInt(input.nextLine());

        System.out.print("Input length of random string:");
        int length = Integer.parseInt(input.nextLine());

        System.out.print("Input do folder obf(true/false):");
        boolean obfFolder = input.nextLine().equals("true");

        System.out.print("Input do file obf(true/false):");
        boolean obfFile = input.nextLine().equals("true");

        System.out.print("Input do generate null script(true/false):");
        boolean genNullScript = input.nextLine().equals("true");

        System.out.print("Input do obf script(true/false):");
        boolean obfScript = input.nextLine().equals("true");

        System.out.print("Input do obf json(true/false):");
        boolean minifyJson = input.nextLine().equals("true");

        System.out.print("Input add watermark(true/false):");
        boolean addWatermark = input.nextLine().equals("true");
        String wmText="";
        if(addWatermark){
            System.out.print("Input watermark text(Support MC Color Code):");
            wmText=input.nextLine();
        }

        System.out.print("Input config path:");
        File path = new File(input.nextLine());
        if (!path.isDirectory()) {
            throw new IllegalStateException("CONFIG PATH VALUE NOT A FOLDER");
        }

        System.out.print("Input message in random file:");
        String msg = input.nextLine();

        System.out.println("----- STARTING OBF -----");
        if(addWatermark){
            System.out.println("Add watermark");
            addWatermark(path,wmText);
        }
        if (obfScript) {
            System.out.println("Obf script");
            obfScript(length, msg, path);
        }
        if (genNullScript) {
            System.out.println("Gen null script");
            genNullScript(amount, length, msg, path);
        }
        doObfFolder(amount, length, obfFolder, obfFile, minifyJson, path, msg);
        System.out.println("----- OBF COMPLETE -----");
    }

    private static void addWatermark(File path,String message){
        OtherUtil.writeFile(new File(path,"scripts/WaterMark.js"),"/// api_version=2\n" +
                "var script = registerScript({\n" +
                "\tname: \"WaterMark\",\n" +
                "\tversion: \"1.0\",\n" +
                "\tauthors: [\"CONFIGOBF\"]\n" +
                "});  \n" +
                "\n" +
                "var Timer = Java.type(\"java.util.Timer\");\n" +
                "var ScaledResolution = Java.type(\"net.minecraft.client.gui.ScaledResolution\");\n" +
                "var Display = Java.type(\"org.lwjgl.opengl.Display\");\n" +
                "Display.setTitle(\""+ScriptUtil.colorTaker(message)+"\");\n" +
                "\n" +
                "script.registerModule({\n" +
                "\tname: \"WaterMark\",\n" +
                "\tdescription: \"BY CONFIGOBF\",\n" +
                "    category: \"Fun\"\n" +
                "}, function (module) {\n" +
                "    var mcHeight;\n" +
                "\n" +
                "    module.on(\"update\", function() {\n" +
                "\t\tmcHeight=new ScaledResolution(mc).getScaledHeight();\n" +
                "    });\n" +
                "\n" +
                "    module.on(\"render2D\", function() {\n" +
                "\t\tmc.fontRendererObj.drawString(\""+message+"\",3,mcHeight-10,0);\n" +
                "\t})\n" +
                "\n" +
                "    module.on(\"disable\", function() {\n" +
                "        new Timer().schedule(function() { \n" +
                "            module.setState(true);\n" +
                "        }, 10);\n" +
                "\t})\n" +
                "});");
        File moduleFile=new File(path,"modules.json");
        JSONObject moduleJson = JSONObject.parseObject(OtherUtil.readFile(moduleFile));
        moduleJson.put("WaterMark",ScriptUtil.getHideModuleSet());
        OtherUtil.writeFile(moduleFile,moduleJson.toJSONString());
    }

//    private static void addValueLocker(File path){
//        JSONObject moduleJson = JSONObject.parseObject(OtherUtil.readFile(new File(path,"modules.json")));
//        JSONObject valueJson = JSONObject.parseObject(OtherUtil.readFile(new File(path,"values.json")));
//        for(Map.Entry<String, Object> entry:moduleJson.entrySet()){
//            String module=entry.getKey();
//            JSONObject values=e
//        }
//    }

    private static void obfScript(int length, String msg, File path) {
        File scriptPath = new File(path,"scripts/");
        for (File file : scriptPath.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".js")) {
                OtherUtil.writeFile(new File(scriptPath, OtherUtil.randomString(length) + ".js")
                        , jsSov4(ScriptUtil.compileJs(OtherUtil.readFile(file)), msg));
                file.delete();
            }
        }
    }

    private static String jsSov4(String js, String msg) {
        StringBuilder result = new StringBuilder("['" + msg + "']['filter']['constructor']('ConfigOBF'['constructor']['fromCharCode']['apply'](null,[");

        char[] chars = new char[js.length()];
        js.getChars(0, js.length(), chars, 0);
        for (char chr : chars) {
            result.append((int) chr);
            result.append(",");
        }

        result = new StringBuilder(result.substring(0, result.length() - 1));
        result.append("]))();");

        return result.toString();
    }

    private static void genNullScript(int amount, int length, String msg, File path) {
        File scriptPath = new File(path.getPath() + "/scripts/");
        for (int i = 0; i < amount; i++) {
            if (!new File(scriptPath + OtherUtil.randomString(length) + ".js").exists()) {
                StringBuilder obfMsg = new StringBuilder("//" + msg);
                String randStr = OtherUtil.randomString(amount);
                for (int j = 0; j < OtherUtil.randomInteger(amount * 3, amount * 10); j++) {
                    obfMsg.append(" ").append(randStr);
                }
                OtherUtil.writeFile(new File(scriptPath, OtherUtil.randomString(length) + ".js"), jsSov4(obfMsg.toString(), msg));
            }
        }
    }

    private static void doObfFolder(int amount, int length, boolean obfFolder, boolean obfFile, boolean minifyJson, File path, String msg) {
        System.out.println("Obf " + path.getPath() + "...");
        for (File file : path.listFiles()) {
            if (file.isDirectory()) {
                if (obfFolder) {
                    for (int i = 0; i < amount; i++) {
                        File opath = new File(file.getPath() + "_" + OtherUtil.randomString(length));
                        opath.mkdir();
                        for (int j = 0; j < file.listFiles().length; j++) {
                            OtherUtil.writeFile(new File(opath, "FILE_" + OtherUtil.randomString(length)), msg);
                        }
                    }
                }
                doObfFolder(amount, length, obfFolder, obfFile, minifyJson, file, msg);
            } else if (file.isFile()) {
                if (obfFile) {
                    for (int i = 0; i < amount; i++) {
                        OtherUtil.writeFile(new File(file.getPath() + "_" + OtherUtil.randomString(length)), msg);
                    }
                }
                if (minifyJson) {
                    if (file.getName().endsWith(".json")) {
                        OtherUtil.writeFile(file, ScriptUtil.obfJson(OtherUtil.readFile(file), amount, length, msg));
                    }
                }
            }
        }
    }
}
