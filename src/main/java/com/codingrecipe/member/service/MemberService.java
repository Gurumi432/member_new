package com.codingrecipe.member.service;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.entity.MemberEntity;
import com.codingrecipe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void save(MemberDTO memberDTO) { //회원가입처리 (DTO→Entity변환 및 DB에 저장)
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    public MemberDTO login(MemberDTO memberDTO) { //로그인처리
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail()); //이메일조회
        if (byMemberEmail.isPresent()) { //있으면
            MemberEntity memberEntity = byMemberEmail.get();//저장
            if (memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) { //비번일치
                return MemberDTO.toMemberDTO(memberEntity); //Entity→DTO변환반환
            } else {
                return null; //비밀번호불일치
            }
        } else {
            return null; //회원없음
        }
    }

    public List<MemberDTO> findAll() { //전체회원조회
        List<MemberEntity> memberEntityList = memberRepository.findAll(); //전체Entity조회
        List<MemberDTO> memberDTOList = new ArrayList<>(); //새DTO리스트생성

        for (MemberEntity memberEntity: memberEntityList) { //Entity→DTO변환
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity)); //새DTO리스트 내부 채우기
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) { //특정회원조회
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id); //ID조회

        if (optionalMemberEntity.isPresent()) { //회원존재
            return MemberDTO.toMemberDTO(optionalMemberEntity.get()); //Entity→DTO변환반환
        } else {
            return null; //회원없음
        }
    }

    public MemberDTO updateForm(String myEmail) { //수정용정보조회
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(myEmail); //db이메일조회
        if (optionalMemberEntity.isPresent()) { //있으면
            return MemberDTO.toMemberDTO(optionalMemberEntity.get()); //Entity→DTO변환반환
        } else {return null; //회원없음
        }
    }

    public void update(MemberDTO memberDTO) { //회원정보수정
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO)); //수정Entity저장
    }

    public void deleteById(Long id) { //회원삭제
        memberRepository.deleteById(id); //ID삭제
    }

    public String emailCheck(String memberEmail) { //이메일중복검사
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberEmail); //이메일조회

        if (byMemberEmail.isPresent()) { //이메일사용중
            return null;
        } else {
            return "ok"; //사용가능
        }
    }
}