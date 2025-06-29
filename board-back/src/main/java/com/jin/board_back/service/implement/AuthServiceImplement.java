package com.jin.board_back.service.implement;

import org.springframework.http.HttpHeaders;
import java.util.Optional;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jin.board_back.dto.request.auth.SignInRequestDto;
import com.jin.board_back.dto.request.auth.SignUpRequestDto;
import com.jin.board_back.dto.response.ResponseDto;
import com.jin.board_back.dto.response.auth.SignInResponseDto;
import com.jin.board_back.dto.response.auth.SignUpResponseDto;
import com.jin.board_back.dto.response.user.PatchNicknameResponseDto;
import com.jin.board_back.entity.UserEntity;
import com.jin.board_back.provider.JwtProvider;
import com.jin.board_back.repository.UserRepository;
import com.jin.board_back.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {
    
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;


    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {
        try {
            String email = dto.getEmail();
            boolean existsByEmail = userRepository.existsByEmail(email);
            if(existsByEmail) return SignUpResponseDto.duplicateEmail();

            String nickname = dto.getNickname();
            boolean existsByNickname = userRepository.existsByNickname(nickname);
            if (existsByNickname) return SignUpResponseDto.duplicateNickname();

            String telNumber = dto.getTelNumber();
            boolean existsByTelNumber = userRepository.existsByTelNumber(telNumber);
            if (existsByTelNumber) return SignUpResponseDto.duplicateTelNumber();

            String password = dto.getPassword();
            String encoderPassword = passwordEncoder.encode(password);
            dto.setPassword(encoderPassword);

            UserEntity userEntity = new UserEntity(dto);
            userRepository.save(userEntity);

        }catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return SignUpResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto, HttpServletResponse response) {
        
        String token = null;

        try {
            String email = dto.getEmail();
            // Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);
            // if (optionalUserEntity.isEmpty()) return SignInResponseDto.signInFail();
            
            // UserEntity userEntity = optionalUserEntity.get();
            
            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null) return SignInResponseDto.signInFail();

            String password = dto.getPassword();
            String encodePassword = userEntity.getPassword();
            boolean isMatched = passwordEncoder.matches(password, encodePassword);
            if(!isMatched) return SignInResponseDto.signInFail();

            token = jwtProvider.create(email);

            ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(true) // 배포 시 https면 true로 설정
                .sameSite("None") // 또는 "None" (None이면 secure true 필수)
                .path("/")
                .maxAge(60 * 60 * 24) // 1일
                .domain("jinhozinoboard.click")
                .build();

            return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(SignInResponseDto.success(token)); // 필요시 token 전달
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
    }
}
