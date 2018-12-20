
%
% This program checks the properties between two XACML programs including refinement and isomorphism.
%

#const policy_set_name1 = ps1.
#const policy_set_name2 = ps2.
root_policy_sets(policy_set_name1;policy_set_name2).

% policy refinement: policy p1 refines policy p2 if using p1 automatically fulfills p2. 
no_refine_program(PSN1, PSN2, ActReq, EnvReq, ResReq, SubReq, E1, E2) :-
					policy_set(PSN1, PSID1, _, ALG),
					policy_set(PSN2, PSID2, _, ALG),
					root_policy_sets(PSN1),
					root_policy_sets(PSN2),
					PSID1 != PSID2,
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policyset(PSN1, _, _, _, _, E1, ActReq, EnvReq, ResReq, SubReq),
					match_policyset(PSN2, _, _, _, _, E2, ActReq, EnvReq, ResReq, SubReq),
					E1 != E2.
1{no_refine_program_evidence(PSN1, PSN2, ActReq, EnvReq, ResReq, SubReq, E1, E2) : no_refine_program(PSN1, PSN2, ActReq, EnvReq, ResReq, SubReq, E1, E2)}1 :-
					no_refine_program(PSN1, PSN2, _, _, _, _, _, _).
refine_program(PSN1, PSN2) :-
					policy_set(PSN1, PSID1, _, ALG),
					policy_set(PSN2, PSID2, _, ALG),
					root_policy_sets(PSN1),
					root_policy_sets(PSN2),
					PSID1 != PSID2,
					not no_refine_program(PSN1, PSN2, _, _, _, _, _, _).

% isomorphism: two programs (policysets) are isomorphic if they provide same decisions for each query.
isomorphic_program(PSN1, PSN2) :-
					policy_set(PSN1, PSID1, _, ALG),
					policy_set(PSN2, PSID2, _, ALG),
					root_policy_sets(PSN1),
					root_policy_sets(PSN2),
					PSID1 != PSID2,
					refine_program(PSN1, PSN2),
					refine_program(PSN2, PSN1).



%#show no_refine_program/8.
#show no_refine_program_evidence/8.
#show refine_program/2.
#show isomorphic_program/2.


