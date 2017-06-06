import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.paths.SwaggerPathProvider;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

@Configuration
@EnableWebMvc
@EnableSwagger
@ComponentScan(basePackages={"com.xxx.controller"})
public class CustomJavaPluginConfig extends WebMvcConfigurerAdapter {
  
  @Autowired
  private SpringSwaggerConfig springSwaggerConfig;
  
  @Bean
  public SwaggerSpringMvcPlugin customImplementation() {
    return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
        .apiInfo(apiInfo()).includePatterns(".*")
        .useDefaultResponseMessages(false)
      //  .pathProvider(new GtPaths())
        .apiVersion("1.0").swaggerGroup("user");
  }
  
  private ApiInfo apiInfo() {
    ApiInfo apiInfo = new ApiInfo("服务API接口平台",
        "服务-xx店api接口", "/",
        "xx@xx.org", "服务-xxx店", "/");
    return apiInfo;
  }
  
  //(这里的配置可以省去 springmvc配置文件中的 mvc:default-servlet-handler 和 mvc:resources)
  @Override
  public void configureDefaultServletHandling(
      DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
  }

  class GtPaths extends SwaggerPathProvider {

    @Override
    protected String applicationPath() {
      return "/restapi";
    }

    @Override
    protected String getDocumentationPath() {
      return "/restapi";
    }
  }

}
