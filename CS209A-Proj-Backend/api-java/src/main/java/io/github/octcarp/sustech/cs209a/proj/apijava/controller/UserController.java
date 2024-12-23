package io.github.octcarp.sustech.cs209a.proj.apijava.controller;

import io.github.octcarp.sustech.cs209a.proj.apijava.dto.basic.UserReputationDistribution;
import io.github.octcarp.sustech.cs209a.proj.apijava.service.ApiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private ApiUserService apiUserService;

    @GetMapping("/all/rep-dist")
    public List<UserReputationDistribution> getUserReputationDistribution() {
        return apiUserService.getUserReputationDistribution();
    }
}
