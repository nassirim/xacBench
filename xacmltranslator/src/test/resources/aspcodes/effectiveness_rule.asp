
%
% This program checks the effectiveness of the rules.
%
%


no_subset_rule(RID1, RID2) :-
					rule(RN1, RID1, PN, _),
					rule(RN2, RID2, PN, _),
					RN1 != RN2,
					request(ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN1, RID1, PN, _, ActReq, EnvReq, ResReq, SubReq),
					not match_rule(RN2, RID2, PN, _, ActReq, EnvReq, ResReq, SubReq).

subset_rule(RID1, RID2) :-
					rule(RN1, RID1, PN, _),
					rule(RN2, RID2, PN, _),
					RN1 != RN2,
					not no_subset_rule(RID1, RID2).

overlap_rule(RID1, RID2) :-
					overlap_rule(RID2, RID1).
overlap_rule(RID1, RID2) :-
					rule(RN1, RID1, PN, _),
					rule(RN2, RID2, PN, _),
					RN1 != RN2,
					request(ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN1, RID1, PN, _, ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN2, RID2, PN, _, ActReq, EnvReq, ResReq, SubReq).

% policy level (intra-policy) anomalies among rules within a policy
% rule RID1 is shadowed by rule RID2
simple_shadow_anomaly_rule(RID1, RID2) :-
					rule(RN1, RID1, PN, RE1),
					rule(RN2, RID2, PN, RE2),
					RID2 < RID1,
					RE1 != RE2,
					subset_rule(RID1, RID2).

% rule RID1 is redundant by rule RID2
simple_redundancy_anomaly_rule(RID1, RID2) :-
					rule(RN1, RID1, PN, RE1),
					rule(RN2, RID2, PN, RE2),
					RID2 < RID1,
					RE1 == RE2,
					subset_rule(RID1, RID2).

% correlation anomaly: a) different effects; b) overlapping; and c) no one is subset of other;
correlation_anomaly_rule(RID1, RID2) :-
					rule(RN1, RID1, PN, RE1),
					rule(RN2, RID2, PN, RE2),
					RID2 < RID1,
					RE1 != RE2,
					overlap_rule(RID1, RID2),
					not subset_rule(RID1, RID2),
					not subset_rule(RID2, RID1).

% generalization anomaly: R2 is a generalization of R1 if they have different effects and R1 is subset of R2.
simple_generalization_anomaly_rule(RID1, RID2) :-
					rule(RN1, RID1, PN, RE1),
					rule(RN2, RID2, PN, RE2),
					RID2 > RID1,
					RE1 != RE2,
					subset_rule(RID1, RID2).

% a rule is useless if there is no request matched by the rule.
useful_rule(RN) :-
					rule(RN, RID, P, E),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN, RID, P, E, ActReq, EnvReq, ResReq, SubReq).
useless_rule(RN) :-
					rule(RN, _, _, _),
					not useful_rule(RN).

% a rule is reachable, if there is a request matched by this rule
reachable_rule(RN) :-
					rule(RN, _, _, _),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_program(_, _, RN, _, ActReq, EnvReq, ResReq, SubReq).

unreachable_rule(RN) :-
					rule(RN, _, _, _),
					not reachable_rule(RN).



% manage the output
#show simple_shadow_anomaly_rule/2.
#show simple_redundancy_anomaly_rule/2.
#show correlation_anomaly_rule/2.
#show simple_generalization_anomaly_rule/2.
#show useful_rule/1.
#show useless_rule/1.
#show reachable_rule/1.
#show unreachable_rule/1.







