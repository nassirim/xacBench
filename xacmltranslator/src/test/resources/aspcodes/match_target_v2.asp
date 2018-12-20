
%
% This is the match program for version 2 including the match for target elements
%


match_target(TN, ActReq, EnvReq, ResReq, SubReq) :-
					request(ActReq, EnvReq, ResReq, SubReq),
					target(TN).

match_target(TN, ActReq, EnvReq, ResReq, SubReq) :-
					request(ActReq, EnvReq, ResReq, SubReq),
					match_actions(TN, ActReq),
					match_environments(TN, EnvReq),
					match_resources(TN, ResReq),
					match_subjects(TN, SubReq).

match_actions(TN, ActReq) :-
					request(ActReq, _, _, _),
					actions(TN).
match_actions(TN, ActReq) :-
					request(ActReq, _, _, _),
					actions(TN, ActReq).
match_environments(TN, EnvReq) :-
					request(_, EnvReq, _, _),
					environments(TN).
match_environments(TN, EnvReq) :-
					request(_, EnvReq, _, _),
					environments(TN, EnvReq).
match_resources(TN, ResReq) :-
					request(_, _, ResReq, _),
					resources(TN).
match_resources(TN, ResReq) :-
					request(_, _, ResReq, _),
					resources(TN, ResReq).
match_subjects(TN, SubReq) :-
					request(_, _, _, SubReq),
					subjects(TN).
match_subjects(TN, SubReq) :-
					request(_, _, _, SubReq),
					subjects(TN, SubReq).










