
no_subset_rule(RID1, RID2) :- rule(RN1, RID1, _, _),
					rule(RN2, RID2, _, _),
					RN1 != RN2,
					request(ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN1, _, _, _, ActReq, EnvReq, ResReq, SubReq),
					not match_rule(RN2, _, _, _, ActReq, EnvReq, ResReq, SubReq).

subset_rule(RID1, RID2) :- rule(RN1, RID1, _, _),
					rule(RN2, RID2, _, _),
					RN1 != RN2,
					not no_subset_rule(RID1, RID2).

overlap_rule(RID1, RID2) :- overlap_rule(RID2, RID1).
overlap_rule(RID1, RID2) :- rule(RN1, RID1, _, _),
					rule(RN2, RID2, _, _),
					RN1 != RN2,
					request(ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN1, _, _, _, ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN2, _, _, _, ActReq, EnvReq, ResReq, SubReq).

% policy level anomalies among rules within a policy
% rule RID1 is shadowed by rule RID2
simple_shadow_anomaly(RID1, RID2) :- rule(RN1, RID1, _, RE1),
					rule(RN2, RID2, _, RE2),
					RID2 < RID1,
					RE1 != RE2,
					subset_rule(RID1, RID2).

% rule RID1 is redundant by rule RID2
simple_redundancy_anomaly(RID1, RID2) :- rule(RN1, RID1, _, RE1),
					rule(RN2, RID2, _, RE2),
					RID2 < RID1,
					RE1 == RE2,
					subset_rule(RID1, RID2).

% correlation anomaly: a) different effects; b) overlapping; and c) no one is subset of other;
correlation_anomaly(RID1, RID2) :- rule(RN1, RID1, _, RE1),
					rule(RN2, RID2, _, RE2),
					RID2 < RID1,
					RE1 != RE2,
					overlap_rule(RID1, RID2),
					not subset_rule(RID1, RID2),
					not subset_rule(RID2, RID1).

% generalization anomaly: R2 is a generalization of R1 if they have different effects and R1 is subset of R2.
simple_generalization_anomaly(RID1, RID2) :- rule(RN1, RID1, _, RE1),
					rule(RN2, RID2, _, RE2),
					RID2 > RID1,
					RE1 != RE2,
					subset_rule(RID1, RID2).

% a rule is useless if there is no request matched by the rule.
useful_rule(RN) :- rule(RN, RID, P, E),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN, RID, P, E, ActReq, EnvReq, ResReq, SubReq).
useless_rule(RN) :- rule(RN, _, _, _),
					not useful_rule(RN).

% a policy is complete if it matches all possible requests.
incomplete(ActReq, EnvReq, ResReq, SubReq) :-
					request(ActReq, EnvReq, ResReq, SubReq),
					not match_program(_, _, _, permit, ActReq, EnvReq, ResReq, SubReq),
					not match_program(_, _, _, deny, ActReq, EnvReq, ResReq, SubReq).
1{incomplete_evidence(ActReq, EnvReq, ResReq, SubReq) : incomplete(ActReq, EnvReq, ResReq, SubReq)}1 :-
					incomplete(_, _, _, _).
complete :- not incomplete(_, _, _, _).

% total redundancy: a rule is totaly redundant if a subset of other rules cover this rule
match_by_others(RN, ActReq, EnvReq, ResReq, SubReq) :-
					rule(RN, RID, _, _),
					rule(RN2, RID2, _, _),
					RID > RID2,
					request(ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN, _, _, _, ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN2, _, _, _, ActReq, EnvReq, ResReq, SubReq).
no_total_redundancy(RN) :-
					rule(RN, _, _, _),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN, _, _, _, ActReq, EnvReq, ResReq, SubReq),
					not match_by_others(RN, ActReq, EnvReq, ResReq, SubReq).
total_redundancy(RN) :-
					rule(RN, _, _, _),
					not no_total_redundancy(RN).

% policy refinement: policy p1 refines policy p2 if using p1 automatically fulfills p2. 
no_refine_program(PSN1, PSN2, ActReq, EnvReq, ResReq, SubReq, E1, E2) :-
					policy_set(PSN1, PSID1, _, ALG),
					policy_set(PSN2, PSID2, _, ALG),
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
					PSID1 != PSID2,
					not no_refine_program(PSN1, PSN2, _, _, _, _, _, _).

% isomorphism: two programs (policysets) are isomorphic if they provide same decisions for each query.
isomorphic_program(PSN1, PSN2) :-
					policy_set(PSN1, PSID1, _, ALG),
					policy_set(PSN2, PSID2, _, ALG),
					PSID1 != PSID2,
					refine_program(PSN1, PSN2),
					refine_program(PSN2, PSN1).



% manage the output
%#show overlap_rule/2.
%#show subset_rule/2.
#show simple_shadow_anomaly/2.
#show simple_redundancy_anomaly/2.
#show correlation_anomaly/2.
#show simple_generalization_anomaly/2.
#show useful_rule/1.
#show useless_rule/1.
#show incomplete/4.
#show incomplete_evidence/4.
#show complete/0.
#show total_redundancy/1.
#show no_refine_program/8.
#show no_refine_program_evidence/8.
#show refine_program/2.
#show isomorphic_program/2.






