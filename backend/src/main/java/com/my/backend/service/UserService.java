package com.my.backend.service;

import com.my.backend.dto.UserDto;
import com.my.backend.entity.User;
import com.my.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 모든 유저 조회
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 단일 유저 조회
    public UserDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));
        return UserDto.fromEntity(user);
    }

    // 회원가입
    public UserDto signup(UserDto dto) {
        // 비밀번호 암호화
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        // role이 null이면 기본값 USER로 세팅
        if (dto.getRole() == null) {
            dto.setRole(User.Role.USER);
        }

        User saved = userRepository.save(dto.toEntity());
        return UserDto.fromEntity(saved);
    }

    // 로그인
    public UserDto login(UserDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("등록되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return UserDto.fromEntity(user);
    }

    // 정보 수정
    public UserDto updateUser(UserDto dto) {
        if (dto.getUserId() == null) throw new RuntimeException("수정할 사용자 ID가 필요합니다.");
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        // null + 빈 문자열("") 체크
        if (dto.getUserName() != null && !dto.getUserName().isBlank()) {
            user.setUserName(dto.getUserName());
        }

        if (dto.getNickName() != null && !dto.getNickName().isBlank()) {
            user.setNickName(dto.getNickName());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
            // 전화번호 형식 체크
            if (!dto.getPhone().matches("\\d{10,11}")) {
                throw new RuntimeException("전화번호는 숫자만 10~11자리여야 합니다.");
            }
            user.setPhone(dto.getPhone());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            user.setEmail(dto.getEmail());
        }

        return UserDto.fromEntity(userRepository.save(user));
    }

    // 유저 삭제
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
