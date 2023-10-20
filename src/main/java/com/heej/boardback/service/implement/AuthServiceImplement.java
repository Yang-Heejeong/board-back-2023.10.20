package com.heej.boardback.service.implement;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.heej.boardback.dto.request.auth.SignInRequestDto;
import com.heej.boardback.dto.request.auth.SignUpRequestDto;
import com.heej.boardback.dto.response.ResponseDto;
import com.heej.boardback.dto.response.auth.SignInResponseDto;
import com.heej.boardback.dto.response.auth.SignUpResponseDto;
import com.heej.boardback.entity.UserEntity;
import com.heej.boardback.provider.JwtProvider;
import com.heej.boardback.repository.UserRepository;
import com.heej.boardback.service.AuthService;

import lombok.RequiredArgsConstructor;

// service인터페이스 구현

// AuthServiceImpl의 Impl은 implementation로 구현이라는 뜻이다!

@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {

        try {

            String email = dto.getEmail();
            String nickname = dto.getNickname();
            String telNumber = dto.getTelNumber();

            boolean hasEmail = userRepository.existsByEmail(email);
            if (hasEmail) return SignUpResponseDto.duplicateEmail();

            boolean hasNickname = userRepository.existsByNickname(nickname);
            if (hasNickname) return SignUpResponseDto.duplicateNickname();

            boolean hasTelNumber = userRepository.existsByTelNumber(telNumber);
            if (hasTelNumber) return SignUpResponseDto.duplicateTelNumber();

            String password = dto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);

            dto.setPassword(encodedPassword);

            UserEntity userEntity = new UserEntity(dto);
            userRepository.save(userEntity);

        } catch(Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return SignUpResponseDto.success();

    }

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {
        
        String token = null;

        try {

            String email = dto.getEmail();

            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null) return SignInResponseDto.signInFailed();

            String password = dto.getPassword();
            String encodedPassword = userEntity.getPassword();

            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            if (!isMatched) return SignInResponseDto.signInFailed();

            token = jwtProvider.create(email);

        } catch(Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return SignInResponseDto.success(token);

    }
    
}
