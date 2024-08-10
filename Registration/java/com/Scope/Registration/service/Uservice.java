package com.Scope.Registration.service;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.Scope.Registration.model.User;
import com.Scope.Registration.repository.Urepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import net.bytebuddy.utility.RandomString;
@Service
public class Uservice {
	@Autowired
	 private Urepository repo;
	
	 public void insert(User user,String siteUrl) throws UnsupportedEncodingException, MessagingException {
		 String randomCode=RandomString.make(64);
		 user.setVerificationcode(randomCode);
		 user.setEnabled(false);
		 sendVerificationEmail(user,siteUrl);
		 repo.save(user);
	 }
	 @Autowired
	 private JavaMailSender sender;
	 public void sendVerificationEmail(User user,String siteUrl)throws UnsupportedEncodingException,MessagingException {
		 String toaddr=user.getEmail();
		 String fromaddr="kbakash2002@gmail.com";
		 String senderName="Akash";
		 String subject="verify Registration";
		 String message="Dear [[name]] please click below link to verfiy<h3><a href=\"[[URL]]\" target=\"_blank\">VERIFY</a></h3>";
		 MimeMessage msg=sender.createMimeMessage();
		 MimeMessageHelper messageHelper=new MimeMessageHelper(msg);
		 
		 messageHelper.setFrom(fromaddr,senderName);
		 messageHelper.setTo(toaddr);
		 messageHelper.setSubject(subject);
		 message=message.replace("[[name]]", user.getFirstname());
		 String url=siteUrl+"/verify?code="+user.getVerificationcode();
		 message=message.replace("[[URL]]",url);
		 messageHelper.setText(message,true);
		 sender.send(msg);	 
	 }
	 public boolean verify(String verificationcode) {
		 User user=repo.findByVerificationcode(verificationcode);
		 if(user==null ||user.isEnabled() ) {
			 return false;
		 }else {
			 user.setVerificationcode(null);
			 user.setEnabled(true);
			 repo.save(user);
			 return true;
			 
		 }
		 
	 }
	 
	 public void sendEmail(String toAddress,String Otp) {
		 SimpleMailMessage message=new SimpleMailMessage();
		 message.setTo(toAddress);
		 message.setSubject("Otp for verification");
		 message.setText("Your Otp verification is : "+Otp);
		 sender.send(message);
	 }
	 public User getuserbyemail(String email) {
			return repo.findByEmail(email);
		}

		public void updatepassword(User user) {
			repo.save(user);
		}
		
		public void updateUserdetails( User updateuser) {
			User existinguser=repo.findByEmail(updateuser.getEmail());
			if(existinguser!=null) {
				existinguser.setFirstname(updateuser.getFirstname());
				existinguser.setLastname(updateuser.getLastname());
				existinguser.setPhoneno(updateuser.getPhoneno());
				existinguser.setGender(updateuser.getGender());
				existinguser.setAvatar(updateuser.getAvatar());
				existinguser.setDob(updateuser.getDob());
				existinguser.setCountry(updateuser.getCountry());
				existinguser.setState(updateuser.getState());
				existinguser.setCity(updateuser.getCity());
				existinguser.setHobbies(updateuser.getHobbies());
			}
			// TODO Auto-generated method stub
			repo.save(existinguser);
			
		}	
		public User getCurrentUser(String email) {
			return repo.findByEmail(email);
		}
		public void saveUser(User user) {
			repo.save(user);
		}
		public void sendingmail(String to,String from,String subject,String text) {
			 SimpleMailMessage message=new SimpleMailMessage();
			 message.setTo(to);
			 message.setFrom(from);
			 message.setSubject(subject);
			 message.setText(text);
			 sender.send(message);
		 }
}
