
%
% This program checks the effectiveness of the policy sets.
%
%


% a policy set is useless if there is no request matched by the policy set.
useful_policyset(PSN) :-
					policy_set(PSN, _, _, _),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policyset(PSN, _, _, _, _, _, ActReq, EnvReq, ResReq, SubReq).
useless_policyset(PSN) :-
					policy_set(PSN, _, _, _),
					not useful_policyset(PSN).

% a policy set is reachable, if there is a request matched by this policy set
reachable_policyset(PSN) :-
					policy_set(PSN, _, _, _),
					policy(PN, _, PSN, _),
					reachable_policy(PN).

reachable_policyset(PSN) :-
					policy_set(PSN, _, _, _),
					policy_set(CN, _, PSN, _),
					reachable_policyset(CN).

unreachable_policyset(PSN) :-
					policy_set(PSN, _, _, _),
					not reachable_policyset(PSN).

% manage the output
#show useful_policyset/1.
#show useless_policyset/1.
#show reachable_policyset/1.
#show unreachable_policyset/1.






