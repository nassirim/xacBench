
%
% This is the match program for version 3 including the match for target elements
%

#script (python)
def match_str_func(str1, str2, any_str):
    if (str1 == str2) or (str2 == any_str):
        return 1
    else:
        return 0
#end.


match_anyof_req(RN, AnyID, ActReq, EnvReq, ResReq, SubReq) :-
					request(ActReq, EnvReq, ResReq, SubReq),
					anyof(RN, AnyID, ActAnyof, EnvAnyof, ResAnyof, SubAnyof),
					@match_str_func(ActReq, ActAnyof, any) == 1,
					@match_str_func(EnvReq, EnvAnyof, any) == 1,
					@match_str_func(ResReq, ResAnyof, any) == 1,
					@match_str_func(SubReq, SubAnyof, any) == 1.

no_match_anyof_req(RN, ActReq, EnvReq, ResReq, SubReq) :-
					request(ActReq, EnvReq, ResReq, SubReq),
					anyof(RN, AnyID, _, _, _, _),
					not match_anyof_req(RN, AnyID, ActReq, EnvReq, ResReq, SubReq).

match_target(TN, ActReq, EnvReq, ResReq, SubReq) :-
					request(ActReq, EnvReq, ResReq, SubReq),
					target(TN).

match_target(TN, ActReq, EnvReq, ResReq, SubReq) :-
					request(ActReq, EnvReq, ResReq, SubReq),
					anyof(TN, _, _, _, _, _),
					not no_match_anyof_req(TN, ActReq, EnvReq, ResReq, SubReq).


