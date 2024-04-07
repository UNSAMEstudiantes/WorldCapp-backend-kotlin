package ar.edu.unsam.algo3.bootstrap

import ar.edu.unsam.algo3.FiguritasApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

class ServletInitializer : SpringBootServletInitializer() {
    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(FiguritasApplication::class.java)
    }
}