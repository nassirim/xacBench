
%
% This program checks the effectiveness of the policy sets.
%
%


% isomorphism: two programs (policysets) are isomorphic if they provide same decisions for each query.
no_isomorphic_program(PSN1, PSN2) :-
					policy_set(PSN1, PSID1, _, ALG),
					policy_set(PSN2, PSID2, _, ALG),
					PSID1 != PSID2,
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policyset(PSN1, _, _, _, _, E1, ActReq, EnvReq, ResReq, SubReq),
					match_policyset(PSN2, _, _, _, _, E2, ActReq, EnvReq, ResReq, SubReq),
					E1 != E2.
no_isomorphic_program(PSN1, PSN2) :-
					policy_set(PSN1, PSID1, _, ALG),
					policy_set(PSN2, PSID2, _, ALG),
					PSID1 != PSID2,
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policyset(PSN1, _, _, _, _, _, ActReq, EnvReq, ResReq, SubReq),
					not match_policyset(PSN2, _, _, _, _, _, ActReq, EnvReq, ResReq, SubReq).
no_isomorphic_program(PSN1, PSN2) :-
					no_isomorphic_program(PSN2, PSN1).

isomorphic_program(PSN1, PSN2) :-
					policy_set(PSN1, PSID1, _, ALG),
					policy_set(PSN2, PSID2, _, ALG),
					PSID1 != PSID2,
					not no_isomorphic_program(PSN1, PSN2).
isomorphic_program(PSN1, PSN2) :-
					isomorphic_program(PSN2, PSN1).


% manage the output
#show isomorphic_program/2.






