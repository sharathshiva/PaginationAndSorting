package com.backend.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document
@Getter
@Setter
public class FeedBack {

    @Id
    private String feedbackId;

    private String description;

    private Date createdDate;

    private boolean likeOrDislike;

    private String submitterName;

    private String submitterEmail;

    private String location;

    private double sentimentScore;

    public FeedBack(){
        this.createdDate = new Date();
    }

}
