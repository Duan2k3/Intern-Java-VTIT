package com.example.book_web.Base;

import lombok.*;


import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto<T>  implements Serializable {
    private String code;
    private String message;
    private T data;
}
