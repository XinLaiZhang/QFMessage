package top.fluffcotton.generator;


import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @Title: mybatisGenerator
 * @Description :逆向工程java代码实现
 * @author : 张逸辰
 * @date : 2020年4月30日 上午11:33:39
 */
public class MybatisGenerator {
    public static void main(String[] args) throws Exception {
        List<String> warnings = new ArrayList<>();
        boolean overwrite = true;
        File configFile = new File(Objects.requireNonNull(MybatisGenerator.class.getClassLoader().getResource("")).getPath() + "/config/mybatis/generatorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }
}
