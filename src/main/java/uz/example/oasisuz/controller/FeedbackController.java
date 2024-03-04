    package uz.example.oasisuz.controller;

    import lombok.RequiredArgsConstructor;
    import org.springframework.validation.annotation.Validated;
    import org.springframework.web.bind.annotation.*;
    import uz.example.oasisuz.dto.FeedbackDTO;
    import uz.example.oasisuz.service.FeedbackService;

    import javax.validation.Valid;

    @RestController
    @RequiredArgsConstructor
    @RequestMapping("api/feedback/")
    @Validated
    public class FeedbackController {

        private final FeedbackService feedbackService;
        @PostMapping("send/{id}")
        public void submitFeedback(@Valid @RequestBody FeedbackDTO feedbackDTO, @PathVariable("id") Integer id){
            feedbackService.submitFeedback(feedbackDTO,id);
        }
    }
