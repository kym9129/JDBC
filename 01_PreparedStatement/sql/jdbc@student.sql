--=======================
--관리자 계정
--======================
--student계정 생성 및 권한 부여
create user student
identified by student
default tablespace users;

grant connect, resource to student;

--=======================
--student 계정
--======================
create table member(
    member_id varchar2(20),
    password varchar2(20) not null,
    member_name varchar2(100) not null,
    gender char(1),
    age number,
    email varchar2(200),
    phone char(11) not null,
    address varchar2(1000),
    hobboy varchar2(100),  --농구,음악감상,영화 (콤마를 구분자로)
    enroll_date date default sysdate,
    constraint pk_member_id primary key(member_id),
    constraint ck_member_gender check(gender in ('M', 'F'))
);

insert into member
values(
    'honggd', '1234', '홍길동', 'M', 33, 'honggd@naver.com', '01012341234',
    '서울 강남구 테헤란로', '등산,그림,요리', default
);

insert into member
values(
    'sinsa', '5678', '신사임당', 'F', 48, 'sinsa@naver.com', '01056785678',
    '서울 강남구 신사동', '이사,음악감상,떡썰기', default
);

insert into member
values(
    'sejong', '1357', '세종', 'M', 76, 'sejong@naver.com', '01035353535',
    '서울 강서구', '독서,육식', default
);

insert into member
values(
    'ygsgs', '1234', '유관순', 'F', null, null, '01012345698',
    null, null, default
);

select * from member;

desc member;

alter table member
rename column hobboy to hobby;

commit;