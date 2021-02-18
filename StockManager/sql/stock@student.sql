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

insert into product_stock
values('nb_ss7', '삼성노트북', 1570000, '시리즈 7', 55);
insert into product_stock
values('nb_macbook_air', '맥북에어', 1200000, '애플 울트라북', 0);
insert into product_stock
values('nb_ibm', 'ibmPC', 750000, 'windows 8', 10);

/*
상품입출고 테이블 PRODUCT_IO
* IO_NO NUMBER PRIMARY KEY => sequence처리할 것.
* PRODUCT_ID VARCHAR2(30) => PRODUCT_STOCK테이블 PRODUCT_ID 참조
* IODATE DATE DEFAULT SYSDATE
* AMOUNT NUMBER
* STATUS CHAR(1) CHECK (STATUS IN ('I', 'O'))
*/

create table product_io( --insert trigger
    io_no number, --pk, seq
    product_id varchar2(30), --fk
    iodate date default sysdate,
    amount number,
    status char(1), --check(status in ('I', 'O')
    constraint pk_io_no primary key(io_no),
    constraint fk_io_product_id foreign key(product_id) references product_stock(product_id),
    constraint ck_status check(status in ('I', 'O'))
);

create sequence seq_p_io_no;

select * from product_io;

--drop trigger trg_product_stock;

create or replace trigger trg_product_stock
    before
    insert on product_io --execute before insert values on PRODUCT_IO
    for each row
begin
    --입고
    if :new.status = 'I' then
        update product_stock
        set stock = stock + :new.amount
        where product_id = :new.product_id;
    
    --출고
    else
        update product_stock
        set stock = stock - :new.amount
        where product_id = :new.product_id;
    
    end if;
end;
/

select * from product_stock;
select * from product_io;


--***** 상품재고관리프로그램 *****
--1. 전체상품조회 selectAll
select * from product_stock;
commit;

--2. 상품아이디검색 selectById
select * from product_stock where product_id = ?;

--3. 상품명검색 selectByName
select * from product_stock where p_name like ?;

--4. 상품추가 addProduct
insert into product_stock
values (?, ?, ?, ?, ?);

--5. 상품정보변경 메뉴
--6. 상품삭제 (입출고 데이터도 삭제) DeleteProduct(1개쿼리로)
delete from product_stock
where product_id = ?;

delete from product_io
where product_id = ?;

--(테이블명을 변수로 받아서 처리해도 되겠다.)
delete from #
where product_id = ?;

--7. 상품입/출고 메뉴
--9. 프로그램 종료

--***** 상품정보변경메뉴 ***** (UpdateStockInfo)
--1. 상품명 변경 
update product_stock
set p_name = ?
where product_id = ?;

--2. 가격변경
update product_stock
set price = ?
where product_id = ?;


--3. 설명변경
update product_stock
set description = ?
where product_id = ?;

--(컬럼명을 변수로 받아서 처리)
update product_stock
set # = ?
where product_id = ?;

--9.메인메뉴로 돌아가기

--***** 상품 입출고 메뉴 *****
--1. 전체 입출고 내역 조회 selectAllIO
select * from product_io order by io_no;

--2. 상품 입고 (status = I)
--insert into product_io
--values (seq_p_io_no.nextval, ?, sysdate, ?, ?)
insert into product_io
values (seq_p_io_no.nextval, 'nb_ss7', sysdate, 80, 'I');
insert into product_io
values (seq_p_io_no.nextval, 'pc_ibm', sysdate, 5, 'O');
--3. 상품 출고 (status = O)
insert into product_io
values (seq_p_io_no.nextval, ?,sysdate, ?, ?)

select * from product_stock;
commit;

--(입출고 같은 쿼리로 처리) IO
--insert into product_io
--values (seq_p_io_no.nextval, ?, sysdate, ?, #)

--4. 메인메뉴로 돌아가기

select constraint_name, 
        uc.table_name,
        ucc.column_name,
        uc.constraint_type,
        uc.search_condition
from user_constraints uc
    join user_cons_columns ucc
        using(constraint_name)
where uc.table_name = 'PRODUCT_STOCK';