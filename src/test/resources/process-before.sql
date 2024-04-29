delete from agreement;

delete from tpp_product_register;

delete from tpp_product;

update account set bussy = false;

alter sequence tpp_product_id_seq restart with 1;

alter sequence tpp_product_register_id_seq restart with 5;

alter sequence agreement_id_seq restart with 10;

commit;
