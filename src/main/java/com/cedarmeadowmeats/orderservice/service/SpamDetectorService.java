package com.cedarmeadowmeats.orderservice.service;

import com.cedarmeadowmeats.orderservice.model.Submission;
import org.springframework.stereotype.Service;

@Service
public class SpamDetectorService {

      public static boolean isSpam(Submission submission) {
          if (submission == null) {
              return false;
          }

          String name = submission.getName();
          String comments = submission.getComments();

          if (name == null || comments == null) {
              return false;
          }

          boolean nameValid = name.length() >= 15 && !name.contains(" ");
          boolean commentsValid = comments.length() >= 15 && !comments.contains(" ");

          return nameValid && commentsValid;
      }
}
