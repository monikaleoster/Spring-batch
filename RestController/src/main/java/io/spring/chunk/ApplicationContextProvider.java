package io.spring.chunk;

import org.springframework.context.ApplicationContext;

public  class ApplicationContextProvider {
    static ApplicationContext applicationContext;

    public static ApplicationContext getContext() {
        return applicationContext;
    }

    public static void setContext(ApplicationContext context) {
        applicationContext = context;
    }


}
