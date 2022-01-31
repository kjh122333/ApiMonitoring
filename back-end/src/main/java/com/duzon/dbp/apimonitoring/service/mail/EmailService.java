package com.duzon.dbp.apimonitoring.service.mail;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.duzon.dbp.apimonitoring.advice.exception.EmailSendEmployeeError;
import com.duzon.dbp.apimonitoring.advice.exception.EmployeeIdSigninFailedError;
import com.duzon.dbp.apimonitoring.advice.exception.IdAndTokenNotMatchError;
import com.duzon.dbp.apimonitoring.config.security.JwtTokenProvider;
import com.duzon.dbp.apimonitoring.dto.Employee;
import com.duzon.dbp.apimonitoring.dto.request.ReqCertificationDto;
import com.duzon.dbp.apimonitoring.dto.request.ReqEmailDto;
import com.duzon.dbp.apimonitoring.repo.EmployeeRepo;
import com.duzon.dbp.apimonitoring.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * MailService
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    EmployeeRepo employeeRepo;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void sendEmail(ReqEmailDto dto) throws MessagingException, IOException {
        Employee employee = employeeRepo.findEmailAuth(dto.getId(), dto.getMail(), dto.getName()).orElseThrow(EmailSendEmployeeError::new);

        String Jwt = jwtTokenProvider.createToken(String.valueOf(employee.getEmployee_no()), employee.getRoles());

        MimeMessage msg = javaMailSender.createMimeMessage();
        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(employee.getMail());
        helper.setSubject("ApiMonitoring 이메일 인증");

        String Url = "<form name='authform' action='http://15.165.25.145:9600/pwdreset' method='GET'>";
        String JwtKey = "<input type='hidden' name='token' value='" + Jwt + "' />";
        String IdKey = "<input type='hidden' name='id' value='" + employee.getId() + "' />";
        String RolesKey = "<input type='hidden' name='name' value='" + employee.getName() + "' />";
        String Text = "ApiMonitoringApplication 이메일 인증";

        helper.setText(Url + JwtKey + IdKey + RolesKey + "<input type='submit' value='" + Text + "'></form>", true);
        // helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));
        
        javaMailSender.send(msg);
    }

	public void FirstLoginEmail(ReqCertificationDto dto, String token) throws MessagingException, IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if(!userDetails.getUsername().equals(dto.getId()))
            throw new IdAndTokenNotMatchError();

        // if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword()))
        //     throw new EmployeeIdSigninFailedError();

        Employee employee = employeeRepo.findById(dto.getId()).orElseThrow(EmployeeIdSigninFailedError::new);

        MimeMessage msg = javaMailSender.createMimeMessage();
        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(employee.getMail());
        helper.setSubject("ApiMonitoring 이메일 인증");

        String Url = "<form name='authform' action='http://15.165.25.145:9600/pwdreset' method='GET'>";
        String JwtKey = "<input type='hidden' name='token' value='" + token + "' />";
        String IdKey = "<input type='hidden' name='id' value='" + userDetails.getUsername() + "' />";
        String RolesKey = "<input type='hidden' name='roles' value='" + userDetails.getAuthorities().toString() + "' />";
        String Text = "ApiMonitoringApplication 이메일 인증";

        helper.setText(Url + JwtKey + IdKey + RolesKey + "<input type='submit' value='" + Text + "'></form>", true);
        // helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));
        
        javaMailSender.send(msg);
	}
}