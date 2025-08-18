package com.example.book_web.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class MessageKeys {
    public static final String LOGIN_SUCCESSFULLY = "user.login.login_successfully";
    public static final String PASSWORD_NOT_MATCH = "user.register.password_not_match";
    public static final String USER_IS_LOCKED = "user.login.user_is_locked";
    public static final String USER_NOT_EXIST = "user.account_not_exist";
    public static final String USER_EXISTING = "user.user_existing";

    public static final String USER_NAME_NOT_FOUND = "user.username.notfound";


    public static class USER {
        public static final String USER_NAME_NOT_BLANK = "user.user_name_not_blank";
        public static final String PASSWORD_NOT_BLANK = "user.password_not_blank";
        public static final String FULLNAME_NOT_BLANK = "user.full_name_not_blank";
        public static final String PHONE_NUMBER_NOT_BLANK = "user.phone_number_not_blank";
        public static final String IDENTITY_NUMBER_NOT_BLANK = "user.identity_number_not_blank";
        public static final String AGE_NOT_NULL = "user.age_not_blank";
        public static final String ADDRESS_NOT_BLANK = "user.address_not_blank";
        public static final String USER_EXISTING = "user.user_existing";
        public static final String USER_IS_NULL = "user.user_is_null";

        private USER() {
        }
    }

    public static class BOOK {
        public static final String BOOK_TITLE_NOT_BLANK = "book.book_title_not_blank";
        public static final String BOOK_AUTHOR_NOT_BLANK = "book.book_author_not_blank";
        public static final String BOOK_PUBLISHER_NOT_BLANK = "book.book_publisher_not_blank";
        public static final String BOOK_QUANTITY_NOT_NULL = "book.book_quantity_not_null";
        public static final String BOOK_DESCRIPTION_NOT_BLANK = "book.book_description_not_blank";
        public static final String BOOK_EXISTING = "book.book_existing";
        public static final String BOOK_NOT_EXIST = "book.book_not_exist";
        public static final String QUANTITY_NOT_VALID = "book.quantity_not_valid";

        private BOOK() {
        }
    }

    public static class BORROW {
        public static final String BORROW_USER_ID_NOT_NULL = "borrow.borrow_user_id_not_null";
        public static final String BORROW_DATE_NOT_NULL = "borrow.borrow_date_not_null";
        public static final String BORROW_RETURN_DATE_NOT_NULL = "borrow.borrow_return_date_not_null";

        private BORROW() {
        }
    }

    public static class BORROW_HISTORY {
        public static final String BORROW_DATE_NOT_NULL = "borrow_history.borrow_date_not_null";
        public static final String RETURNED_DATE_NOT_NULL = "borrow_history.returned_date_not_null";
        public static final String TITLE_NOT_NULL = "borrow_history.title_not_null";

        private BORROW_HISTORY() {
        }
    }

    public static class CATEGORY {
        public static final String CATEGORY_NAME_NOT_BLANK = "category.category_name_not_blank";
        public static final String CATEGORY_DESCRIPTION_NOT_BLANK = "category.category_description_not_blank";
        public static final String CATEGORY_NAME_NOT_EXIST = "category.category_name_not_exist";
        public static final String CATEGORY_NAME_EXISTING = "category.category_name_existing";
        public static final String CATEGORY_NOT_EXIST = "category.category_not_exist";

        private CATEGORY() {
        }
    }

    public static class COMMENT {
        public static final String CONTENT_NOT_BLANK = "comment.comment_content_not_blank";
        public static final String USER_ID_NOT_NULL = "comment.comment_user_id_not_null";
        public static final String USER_NAME_NOT_BLANK = "comment.comment_user_name_not_blank";
        public static final String POST_ID_NOT_NULL = "comment.comment_post_id_not_null";
        public static final String POST_ID_NOT_BLANK = "comment.comment_post_id_not_blank";
        public static final String USER_ID_NOT_BLANK = "comment.comment_user_id_not_blank";

        private COMMENT() {
        }
    }

    public static class LOGIN {
        public static final String USER_NAME_NOT_BLANK = "login.user_name_not_blank";
        public static final String PASSWORD_NOT_BLANK = "login.password_not_blank";

        private LOGIN() {
        }
    }

    public static class POST {
        public static final String POST_CONTENT_NOT_BLANK = "post.post_content_not_blank";

        private POST() {
        }
    }

    public static class TOKEN {
        public static final String TOKEN_NOT_EXIST = "token.token_not_exist";
        public static final String TOKEN_EXPIRED =  "token.token_expired";
        public static final String LOGOUT_SUCCESS = "token.logout_success";
        private TOKEN() {
        }
    }
}