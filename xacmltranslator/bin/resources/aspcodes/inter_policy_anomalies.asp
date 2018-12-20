
%
% This program checks the inter-policy properties related to an anchor policy specified by predicate base_policy.
% Here is the list of properties:
%			subset relation between two policies within a policy set
%			overlap relation between two policies within a policy set
%			simple shadow anomaly of the anchor policy with other rules within the policy set
%			simple redundant anomaly of the anchor policy with other rules within the policy set
%			correlation anomaly of the anchor policy with other rules within the policy set
%			generalization anomaly of the anchor policy with other rules within the policy set
%			total redundancy of the anchor policy with other policies within the policy set
%			usefullness and uselessness of the anchor policy with other policies within the policy set
%
%


#const base_policy_name = p3.
base_policy(base_policy_name).

no_subset_policy(PID1, PID2) :-
					policy(PN1, PID1, PSN, _),
					policy(PN2, PID2, PSN, _),
					base_policy(PN1),
					PN1 != PN2,
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policy(PN1, PID1, PSN, _, _, ActReq, EnvReq, ResReq, SubReq),
					not match_policy(PN2, PID2, PSN, _, _, ActReq, EnvReq, ResReq, SubReq).

subset_policy(PID1, PID2) :-
					policy(PN1, PID1, PSN, _),
					policy(PN2, PID2, PSN, _),
					base_policy(PN1),
					PN1 != PN2,
					not no_subset_policy(PID1, PID2).

overlap_policy(PID1, PID2) :-
					overlap_policy(PID2, PID1).
overlap_policy(PID1, PID2) :-
					policy(PN1, PID1, PSN, _),
					policy(PN2, PID2, PSN, _),
					base_policy(PN1),
					PN1 != PN2,
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policy(PN1, PID1, PSN, _, _, ActReq, EnvReq, ResReq, SubReq),
					match_policy(PN2, PID2, PSN, _, _, ActReq, EnvReq, ResReq, SubReq).

% total redundancy: a policy is totaly redundant if a subset of other policies cover this policy
match_by_others(PN, ActReq, EnvReq, ResReq, SubReq) :-
					policy(PN, PID, PSN, _),
					base_policy(PN),
					policy(PN2, PID2, PSN, _),
					PID > PID2,
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policy(PN, PID, PSN, _, _, ActReq, EnvReq, ResReq, SubReq),
					match_policy(PN2, PID2, PSN, _, _, ActReq, EnvReq, ResReq, SubReq).
no_total_redundancy(PN) :-
					policy(PN, PID, PSN, _),
					base_policy(PN),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policy(PN, PID, PSN, _, _, ActReq, EnvReq, ResReq, SubReq),
					not match_by_others(PN, ActReq, EnvReq, ResReq, SubReq).
total_redundancy(PN) :-
					policy(PN, PID, PSN, _),
					base_policy(PN),
					not no_total_redundancy(PN).

% a policy is useless if there is no request matched by the policy.
useful_policy(PN) :-
					policy(PN, PID, PSN, _),
					base_policy(PN),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policy(PN, PID, PSN, _, _, ActReq, EnvReq, ResReq, SubReq).
useless_policy(PN) :-
					policy(PN, PID, PSN, _),
					base_policy(PN),
					not useful_policy(PN).

% a rule is reachable, if there is a request matched by this rule
reachable_policy(PN) :-
					policy(PN, PID, PSN, _),
					base_policy(PN),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_program(_, PN, _, _, ActReq, EnvReq, ResReq, SubReq).

unreachable_policy(PN) :-
					policy(PN, PID, PSN, _),
					base_policy(PN),
					not reachable_policy(PN).



% manage the output
%#show overlap_policy/2.
%#show subset_policy/2.
#show useful_policy/1.
#show useless_policy/1.
#show total_redundancy/1.
#show reachable_policy/1.
#show unreachable_policy/1.






