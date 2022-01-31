package com.duzon.dbp.apimonitoring.config;

import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import net.rakugakibox.util.YamlResourceBundle;

/**
 * MessageConfiguration : 다국어 지원( 메시지 )
 * 
 * NOTE ### WebMvcConfigurer(interface) ### - 해당 인터페이스를 상속받아서 커스텀해야됨
 */
@Configuration
public class MessageConfiguration implements WebMvcConfigurer {
    /**
     * NOTE ### LocaleResolver ### - 인터페이스 - 요청과 관련된 지역을 추출, 해당 지역 객체로 해당 지역 언어의 메시지
     * 선택 - [class]SessionLocaleResolver : 지역 정보 추출 - setDefaultLocale() : 기본 지역 선언
     */
    @Bean // 세션에 지역 설정시킴( 기본 KOREAN )
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleesolver = new SessionLocaleResolver();
        sessionLocaleesolver.setDefaultLocale(Locale.KOREAN);
        return sessionLocaleesolver;
    }

    /**
     * NOTE ### LocaleChangeInterceptor ### - 지역을 변경시키는게 가능하도록하는 클래스 - 파라미터("lang")를
     * 받아서 지역을 변경시켜줌
     */
    @Bean // lang 값으로 지역 변경 가능
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptorLocaleChange = new LocaleChangeInterceptor();
        interceptorLocaleChange.setParamName("lang");
        return interceptorLocaleChange;
    }

    @Override // localeChangeInterceptor를 통해 변경된 지역을 레지스트리에 등록할게~
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    /**
     * NOTE ### MessageSource ### - 국제화(i18n) 지원해주는 INTERFACE
     * ------------------------------------------------------ ### annotation
     * - @Value ### - import org.springframework.beans.factory.annotation.Value -
     * 프로퍼티 파일을 건들이려고 ------------------------------------------------------ ###번외
     * Lombok - @Value ### ㅋ번외가 더 많너ㅋ - Immutable Class을 생성해줌 - 모든 필드를 기본적으로 Private
     * 및 Final로 로 함 - Setter 함수를 생성안함. - Class를 Final로 지정 - 사용이유 : Immutable Class로
     * 만들어서 - 생성자, 접근메소드에 방어복사 불필요하고 - 쓰레드세이프가 불변이라서 객체 안전 보장 - 멀티쓰레드 환경에서 동기화 없이 객체
     * 공유 가능
     */
    @Bean // yml파일(프로터티에 등록해놓음)을 참조
    public MessageSource messageSource(@Value("${spring.messages.basename}") String basename,
            @Value("${spring.messages.encoding}") String encoding) {
        YamlMessageSource msg = new YamlMessageSource();
        msg.setBasename(basename);
        msg.setDefaultEncoding(encoding);
        msg.setAlwaysUseMessageFormat(true);
        msg.setUseCodeAsDefaultMessage(true);
        msg.setFallbackToSystemLocale(true);
        return msg;
    }

    /**
     * NOTE ### ResourceBundleMessageSource ### - MessageSource의 구현 클래스
     */
    // 지역 정보에 따라 다른 yml파일을 읽도록 동작
    private static class YamlMessageSource extends ResourceBundleMessageSource {
        @Override
        protected ResourceBundle doGetBundle(String basename, Locale locale) {
            return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control.INSTANCE);
        }
    }
}