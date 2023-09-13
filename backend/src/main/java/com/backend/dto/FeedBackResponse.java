package com.backend.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Getter
@Setter
public class FeedBackResponse {

    private List<FeedBack> feedBackList;
    private Integer total_count;
}
