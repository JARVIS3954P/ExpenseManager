package com.zidio.ExpenseManager.dto;

import com.zidio.ExpenseManager.enums.ExpenseStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseUpdateStatusDTO {
    private ExpenseStatus status;
    private String comment;
}
