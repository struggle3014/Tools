package com.xinyan.bigdata.sunfish.demo;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xiumei.com
 * @Date: 8:10 2020/8/9
 * @Version: 1.0
 * @Description: Markdown 生成导航栏
 **/
public class MarkdownToc {

    public static void main(String[] args) {

        String file = "D:\\a.md";
        String s = new MarkdownToc().generateToc(file);
        System.out.println(s);
    }

    public String generateToc(String file) {
        StringBuffer result = new StringBuffer("<nav>").append("\r\n");
        BufferedReader br = null;
        String line = null;
        // 1- 读取文件
        try {
            br = new BufferedReader(new FileReader(new File(file)));
            while((line = br.readLine()) != null) {
                TocItem tocItem = getTocItem(line);
                if(tocItem == null) {
                    continue;
                }
                String linkModule = "${blank}<a href='#${content}' style='text-decoration:none;${border-style}'>${content}</a><br/>";
                if(tocItem.getLevel() <= 1) {
                    linkModule = linkModule.replace("${blank}", "")
                            .replace("${border-style}", "font-weight:bolder");
                } else {
                    StringBuffer blankStr = new StringBuffer();
                    for(int i = 1; i < tocItem.getLevel(); i++) {
                        blankStr.append("&nbsp;&nbsp;&nbsp;&nbsp;");
                    }
                    linkModule = linkModule.replace("${blank}", blankStr.toString()).replace("${blank}", "");
                }
                linkModule = linkModule.replaceAll("\\$\\{content\\}", tocItem.getContent());
                result.append(linkModule).append("\r\n");
            }
            result.append("</nav>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * 获取导航栏条目
     * @param line
     * @return
     */
    private TocItem getTocItem(String line) {
        TocItem result = null;
        if(StringUtils.isBlank(line) || !line.trim().startsWith("#")) {// 不属于导航栏
            return result;
        }
        String newLine = line.trim();
        // 考虑导航栏中可能存在是超链接的情形，去除该影响
        Pattern p = Pattern.compile(".*\\[.*\\]\\(.*\\)");
        Matcher m = p.matcher(newLine);
        if(m.matches()) {
            newLine = newLine.replace("[", "").replace("]", "");
            newLine = newLine.substring(0, newLine.indexOf("("));
        }
        String content = newLine.substring(newLine.indexOf(" ") + 1);
        String[] splits = newLine.split(" ");
        result = new TocItem(content, splits[0].length());
        return result;
    }

    /**
     * TOC 条目对象
     */
    private class TocItem {
        // 内容
        String content;
        // 等级
        int level;

        public TocItem(String content, int level) {
            this.content = content;
            this.level = level;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }

}

