
% a policy is complete if it matches all possible requests.
incomplete(ActReq, EnvReq, ResReq, SubReq) :-
					request(ActReq, EnvReq, ResReq, SubReq),
					not match_program(_, _, _, permit, ActReq, EnvReq, ResReq, SubReq),
					not match_program(_, _, _, deny, ActReq, EnvReq, ResReq, SubReq).
1{incomplete_evidence(ActReq, EnvReq, ResReq, SubReq) : incomplete(ActReq, EnvReq, ResReq, SubReq)}1 :-
					incomplete(_, _, _, _).
complete :- not incomplete(_, _, _, _).



#show incomplete/4.
#show incomplete_evidence/4.
#show complete/0.



