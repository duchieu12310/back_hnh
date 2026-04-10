package com.hnh.controller.client;


// TODO: TẠM THỜI COMMENT - ĐĂNG KÝ NHẬN KM
/*@RestController
@RequestMapping("/client-api/newsletter")
@AllArgsConstructor
@CrossOrigin(AppConstants.FRONTEND_HOST)
public class ClientNewsletterController {

    private NewsletterSubscriptionRepository newsletterSubscriptionRepository;
    private ClientNewsletterSubscriptionMapper newsletterSubscriptionMapper;
    private EmailSenderService emailSenderService;

    @PostMapping("/subscribe")
    public ResponseEntity<ClientNewsletterSubscriptionResponse> subscribe(
            @Valid @RequestBody ClientNewsletterSubscriptionRequest request
    ) {
        String email = request.getEmail().toLowerCase().trim();
        
        // Check if email already exists
        Optional<NewsletterSubscription> existingSubscription = newsletterSubscriptionRepository.findByEmail(email);
        
        if (existingSubscription.isPresent()) {
            NewsletterSubscription subscription = existingSubscription.get();
            // If already subscribed and active, return conflict
            if (subscription.getStatus() == 1) {
                throw new ConflictException("Email này đã được đăng ký nhận tin");
            } else {
                // If unsubscribed, reactivate
                subscription.setStatus(1);
                subscription = newsletterSubscriptionRepository.save(subscription);
                
                // Send confirmation email
                Map<String, Object> emailAttributes = Map.of("frontendHost", AppConstants.FRONTEND_HOST);
                emailSenderService.sendNewsletterConfirmation(email, emailAttributes);
                
                return ResponseEntity.status(HttpStatus.OK)
                        .body(newsletterSubscriptionMapper.entityToResponse(subscription));
            }
        } else {
            // Create new subscription
            NewsletterSubscription entity = newsletterSubscriptionMapper.requestToEntity(request);
            entity = newsletterSubscriptionRepository.save(entity);
            
            // Send confirmation email
            Map<String, Object> emailAttributes = Map.of("frontendHost", AppConstants.FRONTEND_HOST);
            emailSenderService.sendNewsletterConfirmation(email, emailAttributes);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(newsletterSubscriptionMapper.entityToResponse(entity));
        }
    }
}*/


