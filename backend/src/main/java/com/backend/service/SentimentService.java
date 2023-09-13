package com.backend.service;

import com.backend.analyzer.SentimentAnalyzer;
import com.backend.dto.FeedBack;
import com.backend.repository.SentimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SentimentService {

    private SentimentRepository sentimentRepository;
    private final SentimentAnalyzer sentimentAnalyzer;

    @Autowired
    public SentimentService(SentimentRepository sentimentRepository, SentimentAnalyzer sentimentAnalyzer){
        this.sentimentRepository = sentimentRepository;
        this.sentimentAnalyzer = sentimentAnalyzer;
    }

    public FeedBack saveFeedBack(FeedBack feedBack){
        feedBack.setSentimentScore(sentimentAnalyzer.getSentimentScore(feedBack.getDescription()));
        return sentimentRepository.save(feedBack);
    }

    public List<FeedBack> getTotalFeedBack(){
        return sentimentRepository.findAll();
    }

    public List<FeedBack> getFeedBackList( String startDateStr, String endDateStr, String location,
                                           String words, int page,int size,String sortBy,String order) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(order),sortBy));
        Page<FeedBack> pagingFeedBack = sentimentRepository.findAll(pageRequest);














        Date  startDate = null;
        Date endDate = null;
        if(startDateStr!=null){
            startDate = dateFormat.parse(startDateStr);
        }
        if(endDateStr!=null){
            endDate = dateFormat.parse(endDateStr);
        }
        Date finalEndDate = endDate;
        Date finalStartDate = startDate;
        return pagingFeedBack.getContent().stream()
                .filter(feedBack -> {
                    if(startDateStr == null && endDateStr != null){
                        return feedBack.getCreatedDate().before(finalEndDate);
                    }
                    if(startDateStr != null && endDateStr == null){
                        return feedBack.getCreatedDate().after(finalStartDate);
                    }
                    if(startDateStr != null && endDateStr != null){
                        return feedBack.getCreatedDate().after(finalStartDate) && feedBack.getCreatedDate().before(finalEndDate);
                    }
                    return true;
                })
                .filter(feedBack -> {
                    if(location != null){
                        return feedBack.getLocation().equalsIgnoreCase(location);
                    }
                    return true;
                })
                .filter(feedBack -> {
                    if(words != null){
                        return feedBack.getDescription().contains(words);
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    public void deleteFeedBack(String feedbackID){
        sentimentRepository.deleteById(feedbackID);
    }


//    public List<FeedBack> getFilteredList(Date startDate, Date endDate, String location, String wordText){
//        return sentimentRepository.findAll().stream()
//                .filter(feedBack -> feedBack.getCreatedDate().after(startDate) && feedBack.getCreatedDate().before(endDate))
//                .filter(feedBack -> feedBack.getLocation().equals(location))
//                .filter(feedBack -> feedBack.getDescription().contains(wordText))
//                .collect(Collectors.toList());
//    }
//
//    public List<FeedBack> filteredOnDateRange(Date startDate, Date endDate){
//        return sentimentRepository.findAll().stream()
//                .filter(feedBack -> feedBack.getCreatedDate().after(startDate) && feedBack.getCreatedDate().before(endDate))
//                .collect(Collectors.toList());
//    }
//
//    public List<FeedBack> filteredOnLocation(String location){
//        return sentimentRepository.findAll().stream().filter(feedBack -> feedBack.getLocation().equals(location))
//                .collect(Collectors.toList());
//    }
//
//    public List<FeedBack> filteredOnWord(String wordText){
//        return sentimentRepository.findAll().stream().filter(feedBack -> feedBack.getDescription().contains(wordText))
//                .collect(Collectors.toList());
//    }
}
