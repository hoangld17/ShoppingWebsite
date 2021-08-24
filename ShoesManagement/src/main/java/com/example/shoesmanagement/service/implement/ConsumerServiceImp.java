package com.example.shoesmanagement.service.implement;

import com.example.shoesmanagement.controller.helper.MappingHelper;
import com.example.shoesmanagement.dto.request.UpdateConsumerRequest;
import com.example.shoesmanagement.dto.response.ConsumerResponse;
import com.example.shoesmanagement.dto.response.LoginResponse;
import com.example.shoesmanagement.exception.ApplicationException;
import com.example.shoesmanagement.model.Consumer;
import com.example.shoesmanagement.model.util.Validator;
import com.example.shoesmanagement.repository.ConsumerRepository;
import com.example.shoesmanagement.security.JwtTokenProvider;
import com.example.shoesmanagement.service.BillService;
import com.example.shoesmanagement.service.ConsumerService;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.shoesmanagement.model.util.ModelConstant.USER_NOT_FOUND;
import static com.example.shoesmanagement.model.util.SecurityConstant.TOKEN_PREFIX;

@Service
@Transactional
public class ConsumerServiceImp implements ConsumerService {
    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private BillService billService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${file.upload-dir-avatar}")
    private String UPLOADED_FOLDER_AVATAR;

    @Override
    public LoginResponse signin(String username, String password) {
        try {
            Consumer consumer = getConsumerByUsername(username);
            Validator.checkNotFound(consumer, String.format(USER_NOT_FOUND, username));
            final String passwordSalt = consumer.getPasswordSalt();
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(username,
                                    password + passwordSalt));

            final UserDetails principal = (UserDetails) authenticate.getPrincipal();
            String token = jwtTokenProvider.createToken(principal);
            LoginResponse consumerResponse = modelMapper.map(consumer, LoginResponse.class);
            consumerResponse.setToken(TOKEN_PREFIX + token);
            try {
                String nameImage = username;
                String lastFile = Files.exists(Paths.get(UPLOADED_FOLDER_AVATAR, nameImage + ".jpg")) ? ".jpg" : ".png";
                String fileName = UPLOADED_FOLDER_AVATAR + nameImage +lastFile;
                if (!Files.exists(Paths.get(fileName)))
                    fileName = UPLOADED_FOLDER_AVATAR + "default.jpg";
                File file = new File(fileName);
                byte[] fileContent = FileUtils.readFileToByteArray(file);
                consumerResponse.setImage(Base64.encodeBase64String(fileContent));
            } catch (Exception e){
            }
            return consumerResponse;
        } catch (AuthenticationException e) {
            throw new ApplicationException("Invalid username or password!", HttpStatus.BAD_REQUEST);
        }
    }
    @Override
    public void updateInfoUser(UpdateConsumerRequest updateConsumerRequest){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Consumer consumer = getConsumerByUsername(username);
        consumer = MappingHelper.mapConsumer(consumer, updateConsumerRequest);
        consumerRepository.save(consumer);
    }
    @Override
    public void updateAvatar(String image) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            byte[] imageByte=Base64.decodeBase64(image);

            String directory=UPLOADED_FOLDER_AVATAR + username+".jpg";

            new FileOutputStream(directory).write(imageByte);
        } catch (Exception e){
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Wrong format of image");
        }
    }


    @Override
    public void saveConsumer(Consumer consumer) {
        consumerRepository.save(consumer);
    }
    @Override
    public void signup(Consumer consumer) {

        consumer = consumerRepository.save(consumer);
        billService.createBillEmptyUser(consumer);
    }
    @Override
    public Consumer getConsumerByUsername(String username) {
        return consumerRepository.findByUsername(username);
    }


    public List<ConsumerResponse> getAllConsumers() {
        List<Consumer> list = (List<Consumer>) consumerRepository.findAll();
        return list.stream().map(x -> modelMapper.map(x, ConsumerResponse.class)).collect(Collectors.toList());
    }

//    public Consumer whoami(HttpServletRequest req) {
//        return consumerRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
//    }

//    public Consumer get(Long id) {
//        return consumerRepository.findById(id).orElseThrow(() -> new ApplicationException("Account does not exist!"));
//    }
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Consumer consumer = getConsumerByUsername(username);
            authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(username,
                                    oldPassword + consumer.getPasswordSalt()));
            String newPasswordHash = passwordEncoder.encode(newPassword.concat(consumer.getPasswordSalt()));
            consumer.setPasswordHash(newPasswordHash);
            consumerRepository.save(consumer);
        } catch (Exception e){
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Old password is not correct!");
        }
    }
}
