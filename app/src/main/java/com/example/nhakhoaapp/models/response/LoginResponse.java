package com.example.nhakhoaapp.models.response;

    public class LoginResponse {
        private boolean success;
        private String message;
        private String role; // <--- THÊM BIẾN NÀY
        private BenhNhan data;

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }

        // <--- THÊM GETTER NÀY
        public String getRole() { return role; }

        public BenhNhan getData() { return data; }

        public static class BenhNhan {
            private String _id;
            private String ho_ten;
            private String email;
            // getter...
            public String get_id() { return _id; }
            public String getHo_ten() { return ho_ten; }
            public String getEmail() { return email; }
        }
    }
