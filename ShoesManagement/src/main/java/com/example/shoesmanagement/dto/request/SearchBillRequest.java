package com.example.shoesmanagement.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class SearchBillRequest {
    private String fromDate;
    private String toDate;
    private boolean isLatest;
    private int page;
}
