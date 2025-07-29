package com.example.customerprofilesystem.controller;

import com.example.customerprofilesystem.model.Customer;
import com.example.customerprofilesystem.service.CustomerService;
import com.example.customerprofilesystem.service.ProfileGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller // 注意这里是 @Controller, 因为我们要返回视图，而不是JSON
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // 显示客户列表
    @GetMapping("/customers")
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerService.findAll());
        return "customer-list"; // 返回 src/main/resources/templates/customer-list.html
    }

    // 显示添加客户的表单
    @GetMapping("/customers/new")
    public String createCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer-form";
    }

    // 处理添加客户的请求
    @PostMapping("/customers")
    public String saveCustomer(@ModelAttribute("customer") Customer customer) {
        customerService.save(customer);
        return "redirect:/customers";
    }

    // 显示编辑客户的表单
    @GetMapping("/customers/edit/{id}")
    public String editCustomerForm(@PathVariable Long id, Model model) {
        Customer customer = customerService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        model.addAttribute("customer", customer);
        return "customer-form";
    }

    // 处理编辑客户的请求
    @PostMapping("/customers/{id}")
    public String updateCustomer(@PathVariable Long id, @ModelAttribute("customer") Customer customer) {
        customer.setId(id); // 确保ID被设置
        customerService.save(customer);
        return "redirect:/customers";
    }

    // 删除客户
    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteById(id);
        return "redirect:/customers";
    }

    // 显示客户画像详情页
    @GetMapping("/customers/profile/{id}")
    public String viewProfile(@PathVariable Long id, Model model) {
        Customer customer = customerService.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        model.addAttribute("customer", customer);
        return "customer-profile"; // 返回 src/main/resources/templates/customer-profile.html
    }

    @Autowired
    private ProfileGenerationService profileGenerationService;

    @GetMapping("/admin/generate-profiles")
    public String generateProfiles() {
        profileGenerationService.generateAllProfiles();
        return "redirect:/customers";
    }
}