package com.devops.devops_gateway.client;

import com.devops.devops_gateway.dto.UserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "user", url = "http://devops-user:8081")
public interface UserClient {
    @RequestMapping(method = RequestMethod.POST, value = "/api/user/save")
    Boolean createUserInUserService(@RequestBody UserRequest userRequest);
}
