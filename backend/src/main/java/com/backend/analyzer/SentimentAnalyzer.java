package com.backend.analyzer;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class SentimentAnalyzer {
    private final StanfordCoreNLP pipeline;

    public SentimentAnalyzer() {
        // Set up the properties
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");

        // Create the Stanford CoreNLP pipeline
        pipeline = new StanfordCoreNLP(props);
    }

    public double getSentimentScore(String text) {
        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        double sentimentScore = 0.0;
        int numSentences = 0;
        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            double score;
            switch (sentiment) {
                case "Very negative":
                    score = 0.0;
                    break;
                case "Negative":
                    score = 2.5;
                    break;
                case "Neutral":
                    score = 5.0;
                    break;
                case "Positive":
                    score = 7.5;
                    break;
                case "Very positive":
                    score = 10.0;
                    break;
                default:
                    score = 5.0;
            }
            sentimentScore += score;
            numSentences++;
        }

        if (numSentences > 0) {
            sentimentScore /= numSentences;
        }

        return sentimentScore;
    }
}
