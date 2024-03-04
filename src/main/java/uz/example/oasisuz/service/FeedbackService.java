package uz.example.oasisuz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.example.oasisuz.dto.FeedbackDTO;
import uz.example.oasisuz.entity.Feedback;
import uz.example.oasisuz.repository.FeedbackRepository;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private FeedbackRepository feedbackRepository;

    public void submitFeedback(FeedbackDTO feedbackDTO, Integer userId) {
        if (feedbackDTO.getMessage() == null) {
            // to do throw exception
            return;
        }
        Feedback feedback = new Feedback();
        feedback.setFullName(feedbackDTO.getFullName());
        feedback.setPhoneNumber(feedbackDTO.getPhoneNumber());
        feedback.setMessage(feedbackDTO.getMessage());
        feedback.setUserId(userId);
        feedbackRepository.save(feedback);

    }

}
