package com.backend.controller;

import com.backend.analyzer.SentimentAnalyzer;
import com.backend.dto.FeedBack;
import com.backend.dto.FeedBackResponse;
import com.backend.service.SentimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/feedback")
@CrossOrigin(origins = "http://localhost:4200")
public class SentimentController {
    private final SentimentAnalyzer sentimentAnalyzer;
    private final SentimentService sentimentService;

    @Autowired
    public SentimentController(SentimentAnalyzer sentimentAnalyzer, SentimentService sentimentService) {
        this.sentimentAnalyzer = sentimentAnalyzer;
        this.sentimentService = sentimentService;
    }

    @PostMapping("/sentiment")
    public double getSentimentScore(@RequestBody String text) {
        return sentimentAnalyzer.getSentimentScore(text);
    }

    @PostMapping("/saveFeedback")
    public FeedBack saveFeedback(@RequestBody FeedBack feedBack){
        UUID uuid = UUID.randomUUID();
        feedBack.setFeedbackId(uuid.toString());
        return sentimentService.saveFeedBack(feedBack);
    }

//    @GetMapping("/feedbackList")
//    public List<FeedBack> feedBackList(){
//        return sentimentService.getFeedBackList();
//    }

    //filter between
    @GetMapping("/feedbackList")
    public FeedBackResponse filteredList(@RequestParam(value = "startDate", required = false) String startDate,
                                         @RequestParam(value = "endDate", required = false) String endDate,
                                         @RequestParam(value = "location", required = false) String location,
                                         @RequestParam(value = "words", required = false) String words,
                                         @RequestParam(value = "page", defaultValue = "0") int pageNo,
                                         @RequestParam(value = "size", defaultValue = "10") int limit,
                                         @RequestParam(defaultValue = "createdDate") String param,
                                         @RequestParam(defaultValue = "desc") String sort) throws ParseException {
        FeedBackResponse feedBackResponse  = new FeedBackResponse();
        List<FeedBack> feedBackList = sentimentService.getFeedBackList(startDate,endDate,location,words,pageNo,limit,param,sort);
        feedBackResponse.setFeedBackList(feedBackList);
        feedBackResponse.setTotal_count(sentimentService.getTotalFeedBack().size());
        return feedBackResponse;


//        return sentimentService.getFeedBackList().stream()
//                .filter(feedBack -> {
//                    if(startDate == null && endDate!=null){
//                        return feedBack.getCreatedDate().before(endDate);
//                    }
//                    if(startDate != null && endDate ==null){
//                        return feedBack.getCreatedDate().after(startDate);
//                    }
//                    if(startDate!=null && endDate!=null){
//                        return feedBack.getCreatedDate().after(startDate) && feedBack.getCreatedDate().before(endDate);
//                    }
//                    return true;
//                })
//                .filter(feedBack -> {
//                    if(location!=null){
//                        return feedBack.getLocation().equals(location);
//                    }
//                    return true;
//                })
//                .filter(feedBack -> {
//                    if(words!=null){
//                        return feedBack.getDescription().contains(words);
//                    }
//                    return true;
//                })
//                .sorted(Comparator.comparing(FeedBack::getCreatedDate).reversed())
//                .collect(Collectors.toList());

    }

    @DeleteMapping("/deleteFeedBack/{feedbackId}")
    public void deleteFeedBackById(@PathVariable String feedbackId){
        sentimentService.deleteFeedBack(feedbackId);
    }

}
