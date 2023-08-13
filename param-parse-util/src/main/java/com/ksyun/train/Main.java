package com.ksyun.train;

import com.ksyun.train.util.ParamParseUtil;
import com.sun.deploy.security.ruleset.RuleSetParser;

public class Main {
    public static void main(String[] args) {
        try {
//          String queryString="";

//          String queryString="Container=null";
//            String queryString="Container.3.Environment.1.Key=PORT&Container.2.Environment.1.Value=3306&Container.0.Environment.2.Key=ROOT_PASSWORD";
//            String queryString="fsafsfasfggsdfasdas";
            String queryString="Aaaaa=betav2\n" +
                    "&Bbbbb=false\n" +
                    "&Container.1.Command.1=/bin/bash\n" +
                    "&Container.1.Command.2=-c\n" +
                    "&Container.2.Command.3=sleep 20\n" +
                    "&Container.3.Name=nginx\n" +
                    "&Container.2.Port=8080\n" +
                    "&Container.4.Environment.1.Key=PORT\n" +
                    "&Container.5.Environment.1.Value=3306\n" +
                    "&Container.5.Environment.1.Key=ROOT_PASSWORD\n" +
                    "&Container.3.Environment.2.Value=123456\n" +
                    "&Container.4.ImagePullPolicy=IfNotPresent\n" +
                    "&Container.5.Name=mysql\n" +
                    "&Container.5.Port=8306\n" +
                    "&Memory=4.0\n" +
                    "&Metadata.Generation=1\n" +
                    "&Metadata.Name=nginx-deployment-1344556.2345\n" +
                    "&Cpu=2";

//            String queryString="Container.4.Environment.1.Key=PORT\n" +
//                    "&Container.5.Environment.1.Value=3306\n" +
//                    "&Container.5.Environment.1.Key=ROOT_PASSWORD\n" +
//                    "&Container.3.Environment.2.Value=123456\n" +
//                    "&Container.4.ImagePullPolicy=IfNotPresent\n";
            Pod parse = ParamParseUtil.parse(Pod.class, queryString);
            System.out.println(parse.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}