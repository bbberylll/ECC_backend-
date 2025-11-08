package hello.hellospring.domain;

public class Member {

    private Long id; //임의의 값, 고객이 저장하는 것이 아니라 시스템이 저장하는 값
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}




package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member); //회원이 저장됨
    //첫 번째 Member는 메서드의 반환 타입을 나타내며, 이 경우 save 메서드가 호출되면 Member 객체를 반환합니다.
    // 두 번째 Member는 메서드의 매개변수 타입을 나타내며, member라는 이름의 Member 객체를 매개변수로 받아 메서드를 호출할 수 있음을 의미
    Optional<Member> findById(Long id); //findById, findByName으로 가져오는 값이 null일 수 있음.
    //요즘에는 null을 처리하는 방법에서 null을 그대로 반환하는 방법 대신 Optional 이라는 걸로 감싸서 반환하는 방법을 많이 선호
    Optional<Member> findByName(String name);
    List<Member> findAll(); //지금까지 저장된 모든 회원들의 리스트를 반환



package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository{

    private static Map<Long, Member> store = new HashMap<>();
    //Long 타입의 키와 Member 객체를 값으로 가지는 HashMap 객체로 초기화
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        //member 객체의 id를 키로 사용하고, member 객체 자체를 값으로 저장합니다.
        // 이렇게 하면 id를 사용하여 회원을 조회할 수 있게 됩니다.
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id)); //null이어도 감쌀 수 있음
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }
    //스트림을 통해 맵에 저장된 모든 Member 객체에 접근
    //스트림의 filter 메서드를 사용하여 조건을 만족하는 요소만 남깁니다. 여기서는 회원의 이름이 주어진 name과 동일한 경우만을 필터링
    //Optional로 'name'과 일치하는 회원을 못찾았을 때엔 빈 'Optional'반환
    //.findAny(): 필터링된 요소 중 하나를 찾음

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }
}



package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemoryMemberRepositoryTest { // 굳이 public 으로 할 필요없어서 지움

    // Test를 하기 위한 저장소
    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }

    @Test
    public void save(){
        Member member = new Member();
        member.setName("spring");

        repository.save(member);

        Member result = repository.findById(member.getId()).get();
        //Assertions.assertEquals(member, result); //기대하는 것(expected), actual 순
        //result랑 member랑 똑같은지 확인해 볼 수 있음
        assertThat(member).isEqualTo(result);
    }

    @Test
    public void findByName(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();

        assertThat(member1).isEqualTo(result);
    }

    @Test
    public void findAll(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();

        assertThat(result.size()).isEqualTo(2);
    }
}



@AfterEach
    public void afterEach(){
        repository.clearStore();
    }




public void clearStore(){
    store.clear();
}


package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {

    private final MemberRepository memberRepository = new MemoryMemberRepository();

    /**
     * 회원 가입
     */
    public Long join(Member member) {
        //같은 이름이 있는 중복 회원X
//        Optional<Member> result = memberRepository.findByName(member.getName());
//        result.ifPresent(m -> {
//            throw new IllegalStateException("이미 존재하는 회원입니다.");
//        });

        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
            .ifPresent(m -> {
                throw new IllegalStateException("이미 존재하는 회원입니다.");
            });
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }


}



package hello.hellospring.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    @Test
    void join() {
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}


package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    MemberService memberService = new MemberService();
    MemoryMemberRepository memberRepository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach(){
        memberRepository.clearStore();
    }

    @Test
    void 회원가입() {
        //given
        Member member = new Member();
        member.setName("spring");

        //when
        Long saveId = memberService.join(member);

        //then
        Member findMember = memberService.findOne(saveId).get();
        //memberRepository에서 가져와서 비교해야되니까 간단한 findOne으로 객체 가져옴
        // 회원가입한 member의 id가 저장소에 있으면, 해당 member 객체를 findMember로
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외() {
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);
//        try{
//            memberService.join(member2); //validate에서 걸려서 예외가 터짐
//            fail();
//        } catch (IllegalStateException e){
//            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//        }
        //try catch문은 번거로움

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        //then
        
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}


MemberService memberService = new MemberService();
MemoryMemberRepository memberRepository = new MemoryMemberRepository();

@AfterEach // 메서드 실행이 끝날 때마다 실행됨
public void afterEach() {
	memberRepository.clearStore(); // 저장소 내용 다 지움
}



private static Map<Long, Member> store = new HashMap<>();



private final MemberRepository memberRepository;
    
public MemberService(MemberRepository memberRepository) {
	this.memberRepository = memberRepository;
}




MemberService memberService;
MemoryMemberRepository memberRepository;

@BeforeEach // 메서드 시작되기 전
public void beforeEach() {
	memberRepository = new MemoryMemberRepository();
	memberService = new MemberService(memberRepository);
}

@AfterEach // 메서드 시작된 후
public void afterEach() {
	memberRepository.clearStore();
}


#=============================
## Section 4

package hello.hellospring.controller;

import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController {
	private final MemberService memberService;

	@Autowired
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}
}



@Service
public class MemberService { 
	private final MemberRepository memberRepository;

	@Autowired
 	public MemberService(MemberRepository memberRepository) {
 		this.memberRepository = memberRepository;
 	}
}



package hello.hellospring;

import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
}
