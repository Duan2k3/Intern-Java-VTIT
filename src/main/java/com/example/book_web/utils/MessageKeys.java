package com.example.book_web.utils;

public class MessageKeys {
    public static final String LOGIN_SUCCESSFULLY =  "user.login.login_successfully";
    public static final String REGISTER_SUCCESSFULLY =  "user.login.register_successfully";
    public static final String LOGIN_FAILED =  "user.login.login_failed";
    public static final String PASSWORD_NOT_MATCH =  "user.register.password_not_match";
    public static final String USER_IS_LOCKED = "user.login.user_is_locked";
    public static final String USER_NOT_EXIST = "user.account_not_exist";
    public static final String INSERT_CATEGORY_SUCCESSFULLY = "category.create_category.create_successfully";
    public static final String DELETE_CATEGORY_SUCCESSFULLY = "category.delete_category.delete_successfully";
    public static final String UPDATE_CATEGORY_SUCCESSFULLY = "category.update_category.update_successfully";
    public static final String DELETE_ORDER_SUCCESSFULLY = "order.delete_order.delete_successfully";
    public static final String DELETE_ORDER_DETAIL_SUCCESSFULLY = "order.delete_order_detail.delete_successfully";
    public static final String INSERT_CATEGORY_FAILED = "category.create_category.create_failed";
    public static final String ROLE_DOES_NOT_EXISTS = "user.login.role_not_exist";

    public static final String DATA_EXISTING = "category.view_list";
    public static final String CREATE_BOOK= "book.create";
    public static final String DELETE_BOOK= "book.delete";

    public static final String CREATE_BORROW= "borrow.create";
    public static final String UPDATE_BORROW= "borrow.update";
    public static final String DELETE_BORROW= "borrow.delete";

    public static final String CREATE_COMMENT= "comment.create";
    public static final String DELETE_COMMENT= "comment.delete";
    public static final String UPDATE_COMMENT= "comment.update";

    public static final String CREATE_USER= "user.create";
    public static final String DELETE_USER= "user.delete";
    public static final String UPDATE_USER= "user.update";

    //Validate Request User
    public static final String USER_NAME_NOT_FOUND = "user.username.notfound";


    public static class USER{
        public static final String USER_NAME_NOT_BLANK = "user.user_name_not_blank";
        public static final String PASSWORD_NOT_BLANK = "user.password_not_blank";
        public static final String FULLNAME_NOT_BLANK = "user.full_name_not_blank";
        public static final String PHONE_NUMBER_NOT_BLANK = "user.phone_number_not_blank";
        public static final String IDENTITY_NUMBER_NOT_BLANK = "user.identity_number_not_blank";
        public static final String AGE_NOT_NULL = "user.age_not_blank";
        public static final String ADDRESS_NOT_BLANK = "user.address_not_blank";

        private USER(){
        }
    }
}
