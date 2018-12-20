
%
% This program checks the intra-policy properties related to an anchor rule specified by predicate base_rule.
% Here is the list of properties:
%			subset relation between two rules within a policy
%			overlap relation between two rules within a policy
%			simple shadow anomaly of the anchor rule with other rules within the policy
%			simple redundant anomaly of the anchor rule with other rules within the policy
%			correlation anomaly of the anchor rule with other rules within the policy
%			generalization anomaly of the anchor rule with other rules within the policy
%			total redundancy of the anchor rule with other rules within the policy
%			usefullness and uselessness of the anchor rule with other rules within the policy
%
%


#const base_rule_name = r7.
base_rule(base_rule_name).

no_subset_rule(RID1, RID2) :-
					rule(RN1, RID1, PN, _),
					rule(RN2, RID2, PN, _),
					base_rule(RN1),
					RN1 != RN2,
					request(ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN1, RID1, PN, _, ActReq, EnvReq, ResReq, SubReq),
					not match_rule(RN2, RID2, PN, _, ActReq, EnvReq, ResReq, SubReq).

subset_rule(RID1, RID2) :-
					rule(RN1, RID1, PN, _),
					rule(RN2, RID2, PN, _),
					base_rule(RN1),
					RN1 != RN2,
					not no_subset_rule(RID1, RID2).

overlap_rule(RID1, RID2) :-
					overlap_rule(RID2, RID1).
overlap_rule(RID1, RID2) :-
					rule(RN1, RID1, PN, _),
					rule(RN2, RID2, PN, _),
					base_rule(RN1),
					RN1 != RN2,
					request(ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN1, RID1, PN, _, ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN2, RID2, PN, _, ActReq, EnvReq, ResReq, SubReq).

% policy level anomalies among rules within a policy
% rule RID1 is shadowed by rule RID2
simple_shadow_anomaly(RID1, RID2) :-
					rule(RN1, RID1, PN, RE1),
					rule(RN2, RID2, PN, RE2),
					base_rule(RN1),
					RID2 < RID1,
					RE1 != RE2,
					subset_rule(RID1, RID2).

% rule RID1 is redundant by rule RID2
simple_redundancy_anomaly(RID1, RID2) :-
					rule(RN1, RID1, PN, RE1),
					rule(RN2, RID2, PN, RE2),
					base_rule(RN1),
					RID2 < RID1,
					RE1 == RE2,
					subset_rule(RID1, RID2).

% correlation anomaly: a) different effects; b) overlapping; and c) no one is subset of other;
correlation_anomaly(RID1, RID2) :-
					rule(RN1, RID1, PN, RE1),
					rule(RN2, RID2, PN, RE2),
					base_rule(RN1),
					RID2 < RID1,
					RE1 != RE2,
					overlap_rule(RID1, RID2),
					not subset_rule(RID1, RID2),
					not subset_rule(RID2, RID1).

% generalization anomaly: R2 is a generalization of R1 if they have different effects and R1 is subset of R2.
simple_generalization_anomaly(RID1, RID2) :-
					rule(RN1, RID1, PN, RE1),
					rule(RN2, RID2, PN, RE2),
					base_rule(RN1),
					RID2 > RID1,
					RE1 != RE2,
					subset_rule(RID1, RID2).

% total redundancy: a rule is totaly redundant if a subset of other rules cover this rule
match_by_others(RN, ActReq, EnvReq, ResReq, SubReq) :-
					rule(RN, RID, PN, _),
					base_rule(RN),
					rule(RN2, RID2, PN, _),
					RID > RID2,
					request(ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN, RID, PN, _, ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN2, RID2, PN, _, ActReq, EnvReq, ResReq, SubReq).
no_total_redundancy(RN) :-
					rule(RN, _, _, _),
					base_rule(RN),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN, RID, PN, _, ActReq, EnvReq, ResReq, SubReq),
					not match_by_others(RN, ActReq, EnvReq, ResReq, SubReq).
total_redundancy(RN) :-
					rule(RN, _, _, _),
					base_rule(RN),
					not no_total_redundancy(RN).

% a rule is useless if there is no request matched by the rule.
useful_rule(RN) :-
					rule(RN, RID, P, E),
					base_rule(RN),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN, RID, P, E, ActReq, EnvReq, ResReq, SubReq).
useless_rule(RN) :-
					rule(RN, _, _, _),
					base_rule(RN),
					not useful_rule(RN).

% a rule is reachable, if there is a request matched by this rule
reachable_rule(RN) :-
					rule(RN, _, _, _),
					base_rule(RN),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_program(_, _, RN, _, ActReq, EnvReq, ResReq, SubReq).

unreachable_rule(RN) :-
					rule(RN, _, _, _),
					base_rule(RN),
					not reachable_rule(RN).



% manage the output
%#show overlap_rule/2.
%#show subset_rule/2.
#show simple_shadow_anomaly/2.
#show simple_redundancy_anomaly/2.
#show correlation_anomaly/2.
#show simple_generalization_anomaly/2.
#show useful_rule/1.
#show useless_rule/1.
#show total_redundancy/1.
#show reachable_rule/1.
#show unreachable_rule/1.






