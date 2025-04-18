package com.zidio.ExpenseManager.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachmentDTO {
    private Long id;
    private String fileName;
    private String fileUrl;
}
