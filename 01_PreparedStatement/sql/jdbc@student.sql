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

--------------------------------------------
/*
2021.02.18 @최종실습문제
상품재고관리프로그램을 작성하세요.

다음과 같은 데이터를 담을수 있도록 처리하세요.
--------------------------------------------------------------
product_id	    p_name	    price	    description     stock
--------------------------------------------------------------
nb_ss7		    삼성노트북  1570000	    시리즈 7		   55
nb_macbook_air	맥북에어	1200000	    애플 울트라북	    0
pc_ibm		    ibmPC	    750000	    windows 8	      10
--------------------------------------------------------------

상품테이블 PRODUCT_STOCK
* PRODUCT_ID  VARCHAR2(30) PRIMARY KEY,
* PRODUCT_NAME  VARCHAR2(30)  NOT NULL,
* PRICE NUMBER(10)  NOT NULL,
* DESCRIPTION VARCHAR2(50),
* STOCK NUMBER DEFAULT 0 
*/


create table product_stock(
    product_id varchar2(30), --pk
    p_name varchar2(30) not null,
    price number(10) not null,
    description varchar2(50),
    stock number default 0,
    constraint pk_p_stock_id primary key(product_id)
);

/*
상품입출고 테이블 PRODUCT_IO
* IO_NO NUMBER PRIMARY KEY => sequence처리할 것.
* PRODUCT_ID VARCHAR2(30) => PRODUCT_STOCK테이블 PRODUCT_ID 참조
* IODATE DATE DEFAULT SYSDATE
* AMOUNT NUMBER
* STATUS CHAR(1) CHECK (STATUS IN ('I', 'O'))
*/

create table product_io(
    io_no number, --pk, seq
    product_id varchar2(30), --fk
    iodate date default sysdate,
    amount number,
    status char(1), --check(status in ('I', 'O')
    constraint pk_io_no primary key(io_no),
    constraint fk_io_product_id foreign key(product_id) references product_stock(product_id),
    constraint ck_status check(status in ('I', 'O'))
);