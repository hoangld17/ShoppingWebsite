package com.example.shoesmanagement.controller;

import com.example.shoesmanagement.controller.helper.MappingHelper;
import com.example.shoesmanagement.dto.request.CreateConsumerRequest;
import com.example.shoesmanagement.dto.request.UpdateAvatar;
import com.example.shoesmanagement.dto.request.UpdateConsumerRequest;
import com.example.shoesmanagement.dto.response.ShowDataResponse;
import com.example.shoesmanagement.model.Consumer;
import com.example.shoesmanagement.model.util.Validator;
import com.example.shoesmanagement.service.ConsumerService;
import org.springframework.web.bind.annotation.*;

import static com.example.shoesmanagement.model.util.ModelConstant.USER_EXISTED;

@RestController
@RequestMapping("/consumer")
@CrossOrigin(origins = "http://localhost:4200")
public class ConsumerController {

    private final MappingHelper mappingHelper;
    private final ConsumerService consumerService;

    public ConsumerController(MappingHelper mappingHelper, ConsumerService consumerService) {
        this.mappingHelper = mappingHelper;
        this.consumerService = consumerService;
    }

    @GetMapping("/signin")
    public ShowDataResponse<?> signin(@RequestParam("username") String userName,
                                      @RequestParam("password") String password) {
        return new ShowDataResponse<>(consumerService.signin(userName, password));
    }

    @PostMapping("/signup")
    public ShowDataResponse<?> createConsumer(@RequestBody CreateConsumerRequest request) {
        final Consumer consumerByUsername = consumerService.getConsumerByUsername(request.getUsername());
        Validator.checkExistingObject(consumerByUsername, String.format(USER_EXISTED, request.getUsername()));
        Validator.validateEmail(request.getEmail());
        Validator.checkMatchObject(request.getPassword(), request.getConfirmed());
        Consumer consumer = mappingHelper.mapConsumer(request);
        consumerService.signup(consumer);
        return new ShowDataResponse<>("Register successful!");
    }
    @GetMapping("/changePassword")
    public ShowDataResponse<?> changePassword(@RequestParam("oldPassword") String oldPassword,
                                              @RequestParam("newPassword") String newPassword){
        consumerService.changePassword(oldPassword, newPassword);
        return new ShowDataResponse<>("Change password successful!");
    }
    @PostMapping("/updateAvatar")
    public ShowDataResponse<?> updateAvatar(@RequestBody UpdateAvatar image){
        consumerService.updateAvatar(image.getImage());
        return new ShowDataResponse<>("Update avatar successful!");
    }
    @PostMapping("/updateInfoUser")
    public ShowDataResponse<?> updateInfoUser(@RequestBody UpdateConsumerRequest updateConsumerRequest){
        consumerService.updateInfoUser(updateConsumerRequest);
        return new ShowDataResponse<>("Update information user successful!");
    }
//    @PutMapping("/{id}")
//    public ShowDataResponse<?> updateConsumer(
//            @PathVariable("id") Long id, @RequestBody UpdateConsumerRequest updateConsumerRequest
//    ){
//        Consumer consumer = MappingHelper.mapConsumer(consumerService.get);
//        List<Role> roles = new ArrayList<>();
//        roles.add(Role.ROLE_GUEST);
//        consumer.setRoles(roles);
//        return new ShowDataResponse<>(consumerService.signup(consumer));
//    }

}
