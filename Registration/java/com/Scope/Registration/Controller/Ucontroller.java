package com.Scope.Registration.Controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.Scope.Registration.model.City;
import com.Scope.Registration.model.Contact;
import com.Scope.Registration.model.Country;
import com.Scope.Registration.model.Course;
import com.Scope.Registration.model.State;
import com.Scope.Registration.model.User;
import com.Scope.Registration.repository.CourseRepository;
import com.Scope.Registration.repository.OtpRepository;
import com.Scope.Registration.repository.Urepository;
import com.Scope.Registration.service.CityService;
import com.Scope.Registration.service.CountryService;
import com.Scope.Registration.service.StateService;
import com.Scope.Registration.service.Uservice;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
@Controller
public class Ucontroller {
	@Autowired
	private Uservice service;
	@Autowired
	private CountryService countryservice;
	@Autowired
	private StateService stateservice;
	@Autowired
	private CityService cityservice;
	@Autowired
	private OtpRepository repository;
	@Autowired
	private CourseRepository coro;
	
	@Autowired
	 private Urepository repo;
@RequestMapping("/")
public String form(Model model) {
	List<Country>countryList=countryservice.countrylist();
	model.addAttribute("countryList",countryList);
	model.addAttribute("user",new User());
	return "regfile";
}
@PostMapping("/register")
public String register(@Valid @ModelAttribute("user") User user,BindingResult bindingResult,@RequestParam("avatar")MultipartFile file,HttpServletRequest request)throws IOException,MessagingException {
	System.out.println("check1");
	if(!file.isEmpty()) {
		System.out.println("check3");
			user.setAvatar(file.getBytes());
			System.out.println("check4");
			service.insert(user,getSiteURL(request));
			System.out.println("check5");
		    return "success";
	}
		else {
			System.out.println("check8");
			return "redirect:/";
			
		}	
} 

private String getSiteURL(HttpServletRequest request) {
	String siteurl=request.getRequestURL().toString();
	return siteurl.replace(request.getServletPath(),"");
	
}
@RequestMapping("/verify")
public String verify(@Param("code")String code,Model model) {
	System.out.println(code);
	if(service.verify(code)) {
		model.addAttribute("student",new User());
		return "SendOtp";
	}else {
		return "error";
	}
}

@GetMapping("/states/{id}")
public @ResponseBody Iterable<State> getstateByCountry(@PathVariable Country id){
	return stateservice.getStateBy(id);
	
}
@GetMapping("/cities/{stateid}")
public @ResponseBody Iterable<City> getcityByState(@PathVariable State stateid){
	return cityservice.getCityBy(stateid);
	
}
@PostMapping("/send-otp")
public String sendOtp(Model model,@RequestParam("email")String email,User user) {
	User existingStudent=repository.findByEmail(email);
	if(existingStudent!=null) {
		String newOtp=generateRandomOtp();
		existingStudent.setOtp(newOtp);
		repository.save(existingStudent);
		service.sendEmail(email,newOtp);
		model.addAttribute("email",email);
		model.addAttribute("user",new User());
		return "verifyotp";
		

	}
	else {
		model.addAttribute("error","Email not found please register first");
		return "error";
		
	}
}

@PostMapping("/confirm-otp")
public String verifyOtp(@RequestParam("email") String email, @RequestParam("otp") String enteredOTP, Model model) {
    User user = repository.findByEmail(email);
    if (user != null && user.getOtp() != null && user.getOtp().equals(enteredOTP)) {
    	user.setVerified(true);        
    	repository.save(user);
        model.addAttribute("email", email);
        model.addAttribute("pass",new User());
        return "set-new-password";
    } else {
        model.addAttribute("error", "Invalid OTP. Please try again.");
        return "error";
    }
}
private String generateRandomOtp() {
	String otp=String.valueOf(new Random().nextInt(900000)+100000);
	return otp;
}

@PostMapping("/setpassword")
public String password(@RequestParam("email")String email,@RequestParam("password")String password,Model model) {
	User studentpass=repository.findByEmail(email);
	studentpass.setPassword(password);
	repository.save(studentpass);
	model.addAttribute("email",email);
	model.addAttribute("pass",new User());
	return "login";
	
}
@GetMapping("/login")
public String log(Model model) {
	model.addAttribute("pass",new User());
	return "login";	
}
@PostMapping("/login")
public String login(@RequestParam("email")String email,@RequestParam("password")String password,Model model,HttpSession session) {
	User user=repository.findByEmail(email);

	if(user!=null && user.getPassword().equals(password) && user.isVerified()) {
		session.setAttribute("loggedEmail",user.getEmail());
		String firstname=user.getFirstname();
		String lastname=user.getLastname();
		String Email=user.getEmail();
		String phonenumber=user.getPhoneno();
		String gender=user.getGender();
		byte[] image=user.getAvatar();
		String base64Avatar=Base64.getEncoder().encodeToString(image);
		
	     model.addAttribute("email",Email);
		 model.addAttribute("lastname",lastname);
		 model.addAttribute("firstname",firstname);
		 model.addAttribute("phonenumber",phonenumber);
		 model.addAttribute("gender",gender);
		 model.addAttribute("avatar",base64Avatar);
		// model.addAttribute("email",email);
		
		return "Dashboard";
	}
	else {
        model.addAttribute("error", "Invalid email or password.");
		return "error";
	}
}
@GetMapping("/new-password")
public String updatepassword(Model model) {
	model.addAttribute("user", new User())	;
	
	return "reset-password";	
}
@RequestMapping("/update-password")
public String resetpassword(@ModelAttribute("pass") User pass) {
	
User use =service.getuserbyemail(pass.getEmail());
use.setPassword(pass.getPassword());
	service.updatepassword(use);	
	return "login";
}
@RequestMapping(value = "/editdata", method = {RequestMethod.GET, RequestMethod.POST})
public String editDetails(Model model, HttpSession session) {
    // Retrieve user from session
  String loggedUser =(String) session.getAttribute("loggedEmail");
    if (loggedUser == null) {
        // If the user is not logged in, redirect to the login page
        return "error";
    }
    
    User user =service.getuserbyemail(loggedUser);
    model.addAttribute("user", user);
    byte[] image=user.getAvatar();
	String base64Uploadavatar=Base64.getEncoder().encodeToString(image);
	 model.addAttribute("uploadavatar",base64Uploadavatar);  

   return "edit";  
}

@RequestMapping(value ="/save", method = {RequestMethod.GET, RequestMethod.POST})
public String updateDetails( @Valid @ModelAttribute("user") User user, BindingResult result, @RequestParam("avatar") MultipartFile file,Model model, HttpSession session) throws IOException {
    
	 user.setAvatar(file.getBytes());
   service.updateUserdetails(user);
   model.addAttribute("user", user);
   model.addAttribute("message", "User details updated successfully");
   
   session.setAttribute("user",user);
	String firstname=user.getFirstname();
	String lastname=user.getLastname();
	String Email=user.getEmail();
	String phonenumber=user.getPhoneno();
	String gender=user.getGender();
	byte[] image=user.getAvatar();
	System.out.println(image);
	String base64Uploadavatar=Base64.getEncoder().encodeToString(image);
//	System.out.println(base64Uploadavatar);
//	
	 model.addAttribute("email",Email);
	 model.addAttribute("lastname",lastname);
	 model.addAttribute("firstname",firstname);
	 model.addAttribute("phonenumber",phonenumber);
	 model.addAttribute("gender",gender);
	 model.addAttribute("avatar",base64Uploadavatar);  

   
    
    return "DashBoard"; 
}

@RequestMapping("/dashboard")
public String dashboard(Model model,HttpSession session) {
	//User user=repository.findByEmail(email);
	String emails=(String) session.getAttribute("loggedEmail");
	User user=repository.findByEmail(emails);
	//User user=repo.findByEmail(email);
	if(user!=null) {
		
		String firstname=user.getFirstname();
		String lastname=user.getLastname();
		String Emails=user.getEmail();
		String phonenumber=user.getPhoneno();
		String gender=user.getGender();
		byte[] image=user.getAvatar();
		String base64Avatar=Base64.getEncoder().encodeToString(image);
		
	     model.addAttribute("email",Emails);
		 model.addAttribute("lastname",lastname);
		 model.addAttribute("firstname",firstname);
		 model.addAttribute("phonenumber",phonenumber);
		 model.addAttribute("gender",gender);
		 model.addAttribute("avatar",base64Avatar);		// model.addAttribute("email",email);
		
		return "Dashboard";
	}
	else {
        model.addAttribute("error", "Invalid email or password.");
		return "redirect:/login";
	}
}


 @GetMapping("/course")
 public String coursee(Model model) {
	 List<Course>course=coro.findAll();
	model.addAttribute("courses", course);
	return "courselist";
}
@GetMapping("/coursedetails/{courseid}")
public String courseDetailsPage(@PathVariable String courseid, Model model, HttpSession session) {
    try {
        int courseId = Integer.parseInt(courseid);
        Course course = coro.findById(courseId);
        if (course != null) {
            model.addAttribute("course", course);

            // Retrieve email from session
            String userEmail = (String) session.getAttribute("loggedEmail");

            // Get the currently logged-in user using the email
            User currentUser = service.getCurrentUser(userEmail);
            if (currentUser != null) {
                model.addAttribute("currentUser", currentUser);
            }
            return "coursedetails";
        } else {
        	model.addAttribute("message", "the current user is null");
            return "Empty"; 
        }
    } catch (NumberFormatException e) {
    	model.addAttribute("message", " if courseid is not a valid integer");
        return "Empty"; 
    }
}

 	@GetMapping("/saveid/{courseid}")
 		public String saveCourseId(@PathVariable(name = "courseid", required = false) String courseIdString,
                           @RequestParam("email") String email,
                           Model model, HttpSession session) {

 			// Initialize courseId to null
 			Integer courseId = null;
 				// Parse courseIdString to an integer if it's not null
 		if (courseIdString != null) {
 			try {
 				courseId = Integer.parseInt(courseIdString);
 			} catch (NumberFormatException e) {
            // Handle the case when courseIdString cannot be parsed to an integer
            // Log the error or perform appropriate error handling
            return "error";
        }
    }
    // Check if courseId is not null
    if (courseId != null) {
        // Get the user by email
        User user = service.getCurrentUser(email);

        if (user != null) {
            // Set the joined course for the user
            user.setJoinedcourse(courseId);
            
            // Save the updated user
            service.saveUser(user);
            
            if(user.getJoinedcourse()!= null) {
            	model.addAttribute("message", "Oops! You have been already successfully enrolled");
            	return "Empty";
            }else {
            
            // Add success message to the model
            model.addAttribute("message", "You have been successfully enrolled");
            
            // Redirect to a success page
            return "Empty";
            }
        } else {
            // If user is not found, handle the error appropriately
            model.addAttribute("message", "User with provided email does not exist");
            return "Empty"; // or redirect to an error page
        }
    }
    // If courseId is null or if any other error occurs, add an error message to the model
    model.addAttribute("message", "Some error occurred");    
    // Redirect to a page indicating the error
    return "Empty";
}
 	@GetMapping("/home")
 	public String home(Model model) {
 		model.addAttribute("user", new User())	;
 		
 		return "home";	
 	}
 	@RequestMapping("/sendmail")  
 	 public String sendMail(Contact contact, Model model, User user) {
 	     String toAddress = " akashkumarseelathi@gmail.com";
 	     String fromAddress = user.getEmail();
 	     String name = contact.getName();
 	     String number = contact.getNumber();
 	     String message = contact.getMessage();
    
     
 	     String emailContent = "Name: " + name + "\nNumber: " + number + "\nMessage: " + message;
 	     
 	     // Send email
 	     service.sendingmail(toAddress, fromAddress, "New Contact Form Submission", emailContent);

 	   
 	    	    
 	     model.addAttribute("usercontact", new Contact());
 	    // model.addAttribute("sucess", "Your message has been sent successfully!");
 	     return "contact";
}
}
