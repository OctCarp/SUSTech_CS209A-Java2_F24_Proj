package io.github.octcarp.sustech.cs209a.proj.apijava;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    scanBasePackages = {
        "io.github.octcarp.sustech.cs209a.proj.database",
        "io.github.octcarp.sustech.cs209a.proj.apijava"
    }
)
@MapperScan({"io.github.octcarp.sustech.cs209a.proj.database.mapper",
    "io.github.octcarp.sustech.cs209a.proj.apijava.mapper"})
public class ApiJavaApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiJavaApplication.class, args);
    }
}
