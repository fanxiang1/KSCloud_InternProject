package com.ksyun.train.plugins;

import com.ksyun.train.utils.JarUtils;
import com.ksyun.train.utils.ManifestUtils;
import com.ksyun.train.utils.ZipUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.jar.Manifest;
import java.util.stream.Collectors;



// 目标名统一为bootJar
@Mojo(name = "bootJar")
public class BootJarMojo extends AbstractMojo {
    // 可以自由获得maven内置变量
    @Parameter(
            defaultValue = "${settings.localRepository}",
            required = true
    )
    private String localRepository;

    // 接受通过命令mvn -Dmain.class=com.ksyun.train.App传递的参数，勿修改参数名
    @Parameter(
            property = "main.class",
            required = true
    )
    private String mainClass;

    // maven项目信息，需要的数据基本可以从此对象中获取
    // 可以自行调试打印观察project信息，开发过程中可利用json工具打印该对象信息
    @Component
    protected MavenProject project;

    // 请实现插件的核心逻辑方法execute
    @Override
    public void execute() throws MojoFailureException {
        getLog().info("project localRepository is " + localRepository);
        File baseDir = project.getBasedir(); // 基础的目录
        getLog().info("project base dir is " + baseDir);
        String artifactId = project.getArtifactId();
        String version = project.getVersion();
        File targetDirectory = new File(baseDir, "target"); //zip包要保存的位置
        File classesDirectory = new File(targetDirectory, "classes"); // classes文件的位置
        getLog().info("project classes dir is " + classesDirectory.getAbsolutePath());

        // 得到项目依赖的jar包
        List<File> dependencyFiles = project.getDependencyArtifacts()
                .stream()
                .map(Artifact::getFile)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 准备manifest文件需要的mainClass和classPath
        String classPath="";
        if(Objects.nonNull(dependencyFiles) && dependencyFiles.size()>0){
            // classPath="lib/guava-18.0.jar lib/commons-collections-3.1.jar";
            classPath= dependencyFiles.stream().map(File::getName).map(a -> "lib/"+a).collect(Collectors.joining(" "));
        }

        // 生成manifest
        // 将Main-Class存入manifest文件
        // 将Class-Path存入manifest文件
        Manifest manifest = ManifestUtils.createManifest(mainClass, classPath);
        getLog().info("mainClass "+manifest.getMainAttributes().getValue("Main-Class"));
        getLog().info("manifest generated complete");

        // 生成jar包
        String artifactName=artifactId+"-"+version;
        File bootJar = new File(targetDirectory, artifactName+".jar");
        // 需要把manifest和对应的文件打入jar包内
        JarUtils.createJar(bootJar,manifest,classesDirectory);

        // 生成压缩包
        File zip = new File(targetDirectory, artifactName + ".zip");
        ZipUtils.createZip(zip,bootJar,dependencyFiles);
        getLog().info(zip.getAbsolutePath() + "create successful");

        clearFile(bootJar);
    }

    public void clearFile(File file){
        if(!file.exists()){
            return;
        }else{
            boolean deleted = file.delete();

            if (deleted) {
                getLog().info("Deleted file: " + file.getAbsolutePath());
            } else {
                getLog().warn("Failed to delete file: " + file.getAbsolutePath());
            }
        }
        // 若是文件夹，保证内部也删除
        File[] files = file.listFiles();
        if(Objects.nonNull(files) && files.length>0){
            for(File f:files){
                clearFile(f);
            }
        }
    }
}
