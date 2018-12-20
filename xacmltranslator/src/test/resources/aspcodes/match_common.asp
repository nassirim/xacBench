%
% This is the common match program for both version 2 and 3
%

bool_expr(true).
bool_expr(true, ActReq, EnvReq, ResReq, SubReq) :-
					request(ActReq, EnvReq, ResReq, SubReq).
bool_expr(is_subject(X), ActReq, EnvReq, ResReq, SubReq) :-
					condition(_, is_subject(X)),
					request(ActReq, EnvReq, ResReq, SubReq),
					X == SubReq.
bool_expr(is_subject_resource(X,Y), ActReq, EnvReq, ResReq, SubReq) :- 
					condition(_, is_subject_resource(X, Y)),
					request(ActReq, EnvReq, ResReq, SubReq),
					X == SubReq,
					Y == ResReq.

match_rule(RN, RID, PN, E, ActReq, EnvReq, ResReq, SubReq) :-
					rule(RN, RID, PN, E),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_target(RN, ActReq, EnvReq, ResReq, SubReq),
					condition(RN, B),
					bool_expr(B, ActReq, EnvReq, ResReq, SubReq).

match_policy(PN, PID, PS, RN, E, ActReq, EnvReq, ResReq, SubReq) :-
					policy(PN, PID, PS, ALG),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_target(PN, ActReq, EnvReq, ResReq, SubReq),
					match_policy_alg(PN, RN, ALG, E, ActReq, EnvReq, ResReq, SubReq).

match_policyset(PSN, PSID, PPS, CN, RN, E, ActReq, EnvReq, ResReq, SubReq) :-
					policy_set(PSN, PSID, PPS, ALG),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_target(PSN, ActReq, EnvReq, ResReq, SubReq),
					match_policyset_child(CN, _, PSN, RN, E, ActReq, EnvReq, ResReq, SubReq),
					match_policyset_alg(PS, CN, RN, ALG, E, ActReq, EnvReq, ResReq, SubReq).
match_policyset(PSN, PSID, PPS, 0, 0, indeterminate, ActReq, EnvReq, ResReq, SubReq) :-
					policy_set(PSN, PSID, PPS, ALG),
					request(ActReq, EnvReq, ResReq, SubReq),
					not match_policyset(PSN, PSID, PPS, _, _, permit, ActReq, EnvReq, ResReq, SubReq),
					not match_policyset(PSN, PSID, PPS, _, _, deny, ActReq, EnvReq, ResReq, SubReq).

match_program(PSRoot, CN, RN, E, ActReq, EnvReq, ResReq, SubReq) :-
					PSRoot = ps0,
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policyset(PSRoot, _, _, CN, RN, E, ActReq, EnvReq, ResReq, SubReq).

match_policyset_child(CN, CID, CPN, RN, E, ActReq, EnvReq, ResReq, SubReq) :-
					policy_set(CPN, _, _, _),
					policy(CN, CID, CPN, _),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policy(CN, CID, CPN, RN, E, ActReq, EnvReq, ResReq, SubReq).
match_policyset_child(CN, CID, CPN, RN, E, ActReq, EnvReq, ResReq, SubReq) :-
					policy_set(CPN, _, _, _),
					policy_set(CN, CID, CPN, _),
					CN != CPN,
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policyset(CN, CID, CPN, _, RN, E, ActReq, EnvReq, ResReq, SubReq).

% rule combining algorithm: permit overrides
match_policy_alg(PN, RN, permit_overrides, permit, ActReq, EnvReq, ResReq, SubReq) :-
					policy(PN, _, _, permit_overrides),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN, _, PN, permit, ActReq, EnvReq, ResReq, SubReq).
match_policy_alg(PN, RN, permit_overrides, deny, ActReq, EnvReq, ResReq, SubReq) :-
					policy(PN, _, _, permit_overrides),
					request(ActReq, EnvReq, ResReq, SubReq),
					not match_rule(_, _, PN, permit, ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN, _, PN, deny, ActReq, EnvReq, ResReq, SubReq).

% policy combining algorithm: permit overrides
match_policyset_alg(PS, CN, RN, permit_overrides, permit, ActReq, EnvReq, ResReq, SubReq) :-
					policy_set(PS, _, _, permit_overrides),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policyset_child(CN, _, PS, RN, permit, ActReq, EnvReq, ResReq, SubReq).
match_policyset_alg(PS, CN, RN, permit_overrides, deny, ActReq, EnvReq, ResReq, SubReq) :-
					policy_set(PS, _, _, permit_overrides),
					request(ActReq, EnvReq, ResReq, SubReq),
					not match_policyset_child(_, _, PS, _, permit, ActReq, EnvReq, ResReq, SubReq),
					match_policyset_child(CN, _, PS, RN, deny, ActReq, EnvReq, ResReq, SubReq).

% rule combining algorithm: deny overrides
match_policy_alg(PN, RN, deny_overrides, deny, ActReq, EnvReq, ResReq, SubReq) :-
					policy(PN, _, _, deny_overrides),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN, _, PN, deny, ActReq, EnvReq, ResReq, SubReq).
match_policy_alg(PN, RN, deny_overrides, permit, ActReq, EnvReq, ResReq, SubReq) :-
					policy(PN, _, _, deny_overrides),
					request(ActReq, EnvReq, ResReq, SubReq),
					not match_rule(_, _, PN, deny, ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN, _, PN, permit, ActReq, EnvReq, ResReq, SubReq).

% policy combining algorithm: deny overrides
match_policyset_alg(PS, CN, RN, deny_overrides, deny, ActReq, EnvReq, ResReq, SubReq) :-
					policy_set(PS, _, _, deny_overrides),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policyset_child(CN, _, PS, RN, deny, ActReq, EnvReq, ResReq, SubReq).
match_policyset_alg(PS, CN, RN, deny_overrides, permit, ActReq, EnvReq, ResReq, SubReq) :-
					policy_set(PS, _, _, deny_overrides),
					request(ActReq, EnvReq, ResReq, SubReq),
					not match_policyset_child(_, _, PS, _, deny, ActReq, EnvReq, ResReq, SubReq),
					match_policyset_child(CN, _, PS, RN, permit, ActReq, EnvReq, ResReq, SubReq).

% rule combining algorithm: first applicable
dom_match_rule(RN, RID1, PN, E, ActReq, EnvReq, ResReq, SubReq):-
					request(ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN, RID1, PN, E, ActReq, EnvReq, ResReq, SubReq),
					match_rule(_, RID2, PN, _, ActReq, EnvReq, ResReq, SubReq),
					RID2<RID1.
match_policy_alg(PN, RN, first_applicable, E, ActReq, EnvReq, ResReq, SubReq) :-
					policy(PN, _, _, first_applicable),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN, RID, PN, E, ActReq, EnvReq, ResReq, SubReq),
					not dom_match_rule(_, RID, PN, E, ActReq, EnvReq, ResReq, SubReq).

% policy combining algorithm: first applicable
dom_match_child(CN, CID1, PS, E, ActReq, EnvReq, ResReq, SubReq):-
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policyset_child(CN, CID1, PS, _, E, ActReq, EnvReq, ResReq, SubReq),
					match_policyset_child(_, CID2, PS, _, E2, ActReq, EnvReq, ResReq, SubReq),
					E != indeterminate,
					E2 != indeterminate,
					CID2<CID1.
match_policyset_alg(PS, CN, RN, first_applicable, E, ActReq, EnvReq, ResReq, SubReq) :-
					policy_set(PS, _, _, first_applicable),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policyset_child(CN, CID, PS, RN, E, ActReq, EnvReq, ResReq, SubReq),
					E != indeterminate,
					not dom_match_child(CN, CID, PS, E, ActReq, EnvReq, ResReq, SubReq).

% rule combining algorithm: only one applicable
match_policy_alg(PN, RN, only_one_applicable, E, ActReq, EnvReq, ResReq, SubReq) :-
					policy(PN, _, _, only_one_applicable),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_rule(RN, _, PN, E, ActReq, EnvReq, ResReq, SubReq),
					1{match_rule(_, _, PN, _, ActReq, EnvReq, ResReq, SubReq)}1.

% policy combining algorithm: only one applicable
match_policyset_alg(PS, CN, RN, only_one_applicable, E, ActReq, EnvReq, ResReq, SubReq) :-
					policy_set(PS, _, _, only_one_applicable),
					request(ActReq, EnvReq, ResReq, SubReq),
					match_policyset_child(CN, _, PS, RN, E, ActReq, EnvReq, ResReq, SubReq),
					E != indeterminate,
					1{match_policyset_child(_, _, PS, _, E2, ActReq, EnvReq, ResReq, SubReq) : E2 != indeterminate}1.



