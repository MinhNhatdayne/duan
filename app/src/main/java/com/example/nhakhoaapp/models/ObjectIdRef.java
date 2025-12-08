package com.example.nhakhoaapp.models;

/**
 * Class đại diện cho kiểu dữ liệu ObjectId của MongoDB.
 * Trong JSON, ObjectId thường được truyền dưới dạng một chuỗi 24 ký tự hex (String).
 * LƯU Ý QUAN TRỌNG: Khi sử dụng GSON hoặc các thư viện JSON khác, bạn có thể cần 
 * cấu hình (TypeAdapter) để thư viện tự động ánh xạ chuỗi JSON thành đối tượng ObjectIdRef này.
 */
public class ObjectIdRef {
    // Trường này sẽ giữ giá trị chuỗi ObjectId (ví dụ: "60b8d295...")
    private String id; 

    public ObjectIdRef() {
    }

    public ObjectIdRef(String id) {
        this.id = id;
    }

    // Getters
    public String getId() {
        return id;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}