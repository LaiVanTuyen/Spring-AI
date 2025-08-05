package com.vti.springai.dto;
// dùng để lưu thông tin chi tiêu
public record ExpenseInfo(
        String category,
        String itemName,
        Double amount
) {
}
