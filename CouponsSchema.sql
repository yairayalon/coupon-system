select * from coupon
insert into coupon (company_id, coupon_name, start_date, end_date, amount, coupon_type, description, price, image) values (9, 'Tastyyyyyyyy', '1990/01/01' , '2018/12/05', 10, 'RESTAURANTS', 'The food is tasty', 300, 'image')
delete from coupon where coupon_id=48
delete from customer_coupon where customer_id=12
update company set company_id=?, company_name=?, password=?, email=? where company_id=?
SELECT EXISTS (
  SELECT * FROM company WHERE email = 'cocacola@gmail.com' AND password = 1234
)

SELECT *
FROM company
WHERE EXISTS (SELECT email, password FROM company WHERE email = 'dxg@dfhgfd.com' AND password = 41245);

insert into company (company_id, company_name, password, email) values (8, 'Facebook', 1234, 'facebook@gmail.com')

select * from company
where company_name='facebook' and password=1234

select * from customer_coupon
insert into customer_coupon (customer_id, coupon_id) values (12, 112)

insert into coupon (coupon_id, coupon_name, start_date, end_date, amount, coupon_type, message, price, image)
values (?, ?, ?, ?, ?, ?, ?, ?, ?)

select * from customer

create table customer_coupon (
coupon.customer_id numeric,
coupon_id numeric,
primary key (customer_id, coupon_id)
);

select customer_coupon.Customer_Id, coupon.Coupon_Id from customer LEFT JOIN coupon ON customer_coupon.Coupon_Id = coupon.Coupon_Id
select * from coupon JOIN customer_coupon ON coupon.coupon_id = customer_coupon.coupon_id where customer_coupon.customer_id = 7 AND coupon.coupon_type = 'RESTAURANTS'

SELECT coupon_id FROM coupon WHERE END_DATE > CURRENT_DATE()