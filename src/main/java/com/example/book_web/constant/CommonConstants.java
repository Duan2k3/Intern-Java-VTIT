package com.example.book_web.constant;

public class CommonConstants {
    public static final Integer ACTIVE = 1;
    public static final Integer IN_ACTIVE = 0;

    public static final String FILE_XLSX = ".xlsx";
    public static final String FILE_ZIP = ".zip";
    public static final String EMPTY = "";
    public static final String ROLE = "ROLE";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final Integer MAX_ROW_PER_QUERY = 999;

    public static final Integer MAX_CHUNK_SIZE = 1000;

    public static final int MAX_LENGTH_CODE = 50;
    public static final int MAX_LENGTH_NAME = 250;
    public static final int MAX_LENGTH_DESCRIPTION = 450;

    public static class SQL_STATEMENT {
        public static final String ORDER_BY = " ORDER BY ";
        public static final String DESC = "DESC";
        public static final String ASC = "ASC";
        public static final String PAGING = " OFFSET %s ROWS FETCH NEXT %s ROWS ONLY ";

        private SQL_STATEMENT() {
        }
    }

    public static class SPECIAL_CHARACTER {
        public static final String PERCENT = "%";
        public static final String COMMA = ", ";
        public static final String SPACE = " ";
        public static final String NEW_LINE = " \n ";
        public static final String SUBTRACTION = "-";
        public static final String SLASH = "/";
        public static final String APOSTROPHE = "'";
        public static final String SQUARE_BRACKET_OPEND = "[";
        public static final String SQUARE_BRACKET_CLOSE = "]";
        public static final String UNDERLINED = "_";
        public static final String COLON = ":";
        public static final String DOLLAR = "$";
        public static final String VND = "đ";

        private SPECIAL_CHARACTER() {
        }
    }
}