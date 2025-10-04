package com.mirexelle.bankappTesda.controller;

import com.mirexelle.bankappTesda.model.Account;
import com.mirexelle.bankappTesda.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class BankController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/dashboardtest")
    public String dashboard (Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        model.addAttribute("account", account);
        return "dashboardtest";
    }
    @GetMapping("/registertest")
    public String showRegistrationForm(){
        return "registertest";
    }

    @PostMapping("/registertest")
    public String registerAccount(@RequestParam String username,@RequestParam String password, Model model) {
        try {
            accountService.registerAccount(username, password);
            return "redirect:/logintest";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "registertest";
        }
    }

    @GetMapping("/logintest")
    public String login(Model model,
                        @RequestParam(value = "error", required = false) String error){
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "logintest";
    }



    @PostMapping("/deposit")
    public String deposit(@RequestParam BigDecimal amount, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);

        try {
            accountService.deposit(account, amount);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("account", account);
            return "dashboardtest";
        }

        return "redirect:/dashboardtest";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam BigDecimal amount, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);

        try {
            accountService.withdraw(account, amount);
        } catch (RuntimeException e){
            model.addAttribute("error", e.getMessage());
            model.addAttribute("account", account);
            return "dashboardtest";
        }

        return "redirect:/dashboardtest";
    }

    @GetMapping("/transactionstest")
    public String transactionHistory(Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        model.addAttribute("transactions", accountService.getTransactionHistory(account));
        return "transactionstest";
    }

    @PostMapping("/transfer")
    public String transferAmount (@RequestParam String toUsername, @RequestParam BigDecimal amount, Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account fromAccount = accountService.findAccountByUsername(username);

        try {
            accountService.transferAmount(fromAccount, toUsername, amount);
        } catch (RuntimeException e){
            model.addAttribute("error", e.getMessage());
            model.addAttribute("account", fromAccount);
            return "dashboardtest";
        }
        return "redirect:/dashboardtest";
    }
}


