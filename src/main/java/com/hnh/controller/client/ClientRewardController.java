package com.hnh.controller.client;


// TODO: TẠM THỜI COMMENT - FLOW ĐIỂM THƯỞNG
/*
 * @RestController
 * 
 * @RequestMapping("/client-api/rewards")
 * 
 * @AllArgsConstructor
 * 
 * @CrossOrigin(AppConstants.FRONTEND_HOST)
 * public class ClientRewardController {
 * 
 * private RewardLogRepository rewardLogRepository;
 * private ClientRewardLogMapper clientRewardLogMapper;
 * 
 * @GetMapping
 * public ResponseEntity<ClientRewardResponse> getReward(Authentication
 * authentication) {
 * String username = authentication.getName();
 * 
 * int totalScore = rewardLogRepository.sumScoreByUsername(username);
 * List<ClientRewardLogResponse> logs = clientRewardLogMapper
 * .entityToResponse(rewardLogRepository
 * .findByUserUsername(username)
 * .stream()
 * .sorted(Comparator.comparing(RewardLog::getId).reversed())
 * .collect(Collectors.toList()));
 * 
 * ClientRewardResponse clientWishResponse = new ClientRewardResponse();
 * clientWishResponse.setRewardTotalScore(totalScore);
 * clientWishResponse.setRewardLogs(logs);
 * 
 * return ResponseEntity.status(HttpStatus.OK).body(clientWishResponse);
 * }
 * 
 * }
 */

