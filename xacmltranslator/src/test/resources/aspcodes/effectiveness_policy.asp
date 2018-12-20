
%
% This program checks the effectiveness of the policies.
%
%


no_subset_policy(PID1, PID2) :-
					policy(PN1, PID1, PSN, _),
					policy(PN2, PID2, PSN, _),
					PN1 != PN2,
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policy(PN1, PID1, PSN, _, _, ActReq, EnvReq, ResReq, SubReq),
					not match_policy(PN2, PID2, PSN, _, _, ActReq, EnvReq, ResReq, SubReq).

subset_policy(PID1, PID2) :-
					policy(PN1, PID1, PSN, _),
					policy(PN2, PID2, PSN, _),
					PN1 != PN2,
					not no_subset_policy(PID1, PID2).

overlap_policy(PID1, PID2) :-
					overlap_policy(PID2, PID1).
overlap_policy(PID1, PID2) :-
					policy(PN1, PID1, PSN, _),
					policy(PN2, PID2, PSN, _),
					PN1 != PN2,
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policy(PN1, PID1, PSN, _, _, ActReq, EnvReq, ResReq, SubReq),
					match_policy(PN2, PID2, PSN, _, _, ActReq, EnvReq, ResReq, SubReq).

% inter-policy anomalies among policies within a policy set
% policy PID1 is shadowed by policy PID2
simple_shadow_anomaly_policy(PID1, PID2) :-
					policy(PN1, PID1, PSN, ALG1),
					policy(PN2, PID2, PSN, ALG2),
					PID2 < PID1,
					ALG1 != ALG2,
					subset_policy(PID1, PID2).

% policy PID1 is redundant by policy PID2
simple_redundancy_anomaly_policy(PID1, PID2) :-
					policy(PN1, PID1, PSN, ALG1),
					policy(PN2, PID2, PSN, ALG2),
					PID2 < PID1,
					ALG1 == ALG2,
					subset_policy(PID1, PID2).

% correlation anomaly: a) different effects; b) overlapping; and c) no one is subset of other;
correlation_anomaly_policy(PID1, PID2) :-
					policy(PN1, PID1, PSN, ALG1),
					policy(PN2, PID2, PSN, ALG2),
					PID2 < PID1,
					ALG1 != ALG2,
					overlap_policy(PID1, PID2),
					not subset_policy(PID1, PID2),
					not subset_policy(PID2, PID1).

% generalization anomaly: R2 is a generalization of R1 if they have different effects and R1 is subset of R2.
simple_generalization_anomaly_policy(PID1, PID2) :-
					policy(PN1, PID1, PSN, ALG1),
					policy(PN2, PID2, PSN, ALG2),
					PID2 > PID1,
					ALG1 != ALG2,
					subset_policy(PID1, PID2).

% a policy is useless if there is no request matched by the policy.
useful_policy(PN) :-
					policy(PN, PID, PSN, _),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policy(PN, PID, PSN, _, _, ActReq, EnvReq, ResReq, SubReq).
useless_policy(PN) :-
					policy(PN, PID, PSN, _),
					not useful_policy(PN).

% a policy is reachable, if there is a request matched by this policy
reachable_policy(PN) :-
					policy(PN, _, _, _),
					rule(RN, _, PN, _),
					reachable_rule(RN).

unreachable_policy(PN) :-
					policy(PN, PID, PSN, _),
					not reachable_policy(PN).


% manage the output
#show simple_shadow_anomaly_policy/2.
#show simple_redundancy_anomaly_policy/2.
#show correlation_anomaly_policy/2.
#show simple_generalization_anomaly_policy/2.
#show useful_policy/1.
#show useless_policy/1.
#show reachable_policy/1.
#show unreachable_policy/1.






