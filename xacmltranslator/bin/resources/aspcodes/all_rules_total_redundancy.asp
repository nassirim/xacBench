
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

#show total_redundancy/1.


