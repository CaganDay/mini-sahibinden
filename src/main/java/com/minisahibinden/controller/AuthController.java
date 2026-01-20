package com.minisahibinden.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.minisahibinden.entity.User;
import com.minisahibinden.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session) {
        // If already logged in, redirect to home
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        // Debug: Check what's in the database
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isPresent()) {
            User u = userByEmail.get();
            System.out.println("DEBUG: Found user by email: " + u.getEmail());
            System.out.println("DEBUG: Password in DB: [" + u.getPassword() + "]");
            System.out.println("DEBUG: Password provided: [" + password + "]");
            System.out.println("DEBUG: Passwords match: " + password.equals(u.getPassword()));
        } else {
            System.out.println("DEBUG: No user found with email: " + email);
        }
        
        Optional<User> userOpt = userRepository.findByEmailAndPassword(email, password);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            session.setAttribute("loggedInUser", user);
            session.setAttribute("loggedInUserId", user.getUserId());
            session.setAttribute("loggedInUserName", user.getFullName());
            redirectAttributes.addFlashAttribute("success", "Welcome back, " + user.getFullName() + "!");
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid email or password.");
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model, HttpSession session) {
        // If already logged in, redirect to home
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/";
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String fullName,
                          @RequestParam String email,
                          @RequestParam String phone,
                          @RequestParam String password,
                          @RequestParam String confirmPassword,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        // Validate passwords match
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/register";
        }

        // Check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "An account with this email already exists.");
            return "redirect:/register";
        }

        // Create new user
        User newUser = new User(fullName, email, phone, password);
        userRepository.save(newUser);

        // Auto-login after registration
        session.setAttribute("loggedInUser", newUser);
        session.setAttribute("loggedInUserId", newUser.getUserId());
        session.setAttribute("loggedInUserName", newUser.getFullName());

        redirectAttributes.addFlashAttribute("success", "Registration successful! Welcome, " + fullName + "!");
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "You have been logged out successfully.");
        return "redirect:/";
    }
}
