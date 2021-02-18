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

--create log table
create table member_del
as
select member.*, sysdate del_date 
from member
where 1=0;

alter table member_del
add constraint ck_member_del_gender check(gender in ('M', 'F'));

select constraint_name,
            uc.table_name,
            ucc.column_name,
            uc.constraint_type,
            uc.search_condition
from user_constraints uc
    join user_cons_columns ucc
        using(constraint_name)
where uc.table_name = 'MEMBER_DEL';

--drop table member_del;
desc member_del;
select * from member_del;

--회원탈퇴(delete)시 탈퇴회원테이블MEMBER_DEL에 회원정보를 insert하는 trigger 생성
create or replace trigger trig_member_del
    before
    delete on member --member_del테이블에서 delete문이 실행되기 전에 트리거
    for each row

begin

    --삭제 회원 정보를 member_del에 추가
    insert into member_del
    values(:old.member_id, :old.password, :old.member_name, :old.gender, :old.age, :old.email, :old.phone, :old.address, :old.hobby, :old.enroll_date, sysdate);

end;
/

--drop trigger trig_member_del;

commit;
rollback;

select * from user_triggers;

select * from member;
select * from member_del;

delete from member
where member_id = 'honggd';