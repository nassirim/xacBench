
effect(permit;deny;indeterminate).
comb_alg(deny_overrides;permit_overrides;first_applicable;only_one_applicable).
comb_alg(do;po;fa;ooa).
bool_expr(true).

anyof(r1, 1, read, any, patient_record, any).
%match_anyof_req(r1, 1, ActReq, EnvReq, ResReq, SubReq) :-
%					request(ActReq, EnvReq, ResReq, SubReq),
%					ResReq == patient_record,
%					ActReq == read.
%
anyof(r1, 1, write, any, patient_record, any).
anyof(r1, 1, read, any, radiography, any).
target(r1) :- anyof(r1, 1, _, _, _, _).
condition(r1, is_subject(physician)).
rule(r1, 1, p1, permit).

anyof(r2, 1, any, radiography, any).
target(r2) :- anyof(r2, 1, _, _, _, _).
condition(r2, is_subject(radiologist)).
rule(r2, 2, p1, permit).

target(r3).
condition(r3, is_subject(administrator)).
rule(r3, 3, p1, permit).

anyof(r4, 1, read, any, medical_file, any).
target(r4) :- anyof(r4, 1, _, _, _, _).
condition(r4, is_subject_resource(clinical_researcher,personal)).
rule(r4, 4, p1, permit).

anyof(r5, 1, write, any, patient_record, any).
target(r5) :- anyof(r5, 1, _, _, _, _).
condition(r5, is_subject(administrator)).
rule(r5, 5, p2, deny).

anyof(r6, 1, read, any, patient_record, any).
anyof(r6, 1, read, any, medical_file, any).
target(r6) :- anyof(r6, 1, _, _, _, _).
condition(r6, is_subject(europe)).
rule(r6, 6, p3, deny).

anyof(r7, 1, any, any, any, val_de_grace).
anyof(r7, 1, any, any, any, europe).
anyof(r7, 1, any, any, any, physician).
anyof(r7, 1, any, any, any, clinical_researcher).
anyof(r7, 1, any, any, any, radiologist).
target(r7) :- anyof(r7, 1, _, _, _, _).
condition(r7, true).
rule(r7, 7, p4, deny).

target(r8).
condition(r8, true).
rule(r8, 8, p5, deny).

target(p1).
policy(p1, 1, ps1, deny_overrides).

target(p2).
policy(p2, 2, ps1, permit_overrides).

target(p3).
policy(p3, 3, ps1, permit_overrides).

target(p4).
policy(p4, 4, ps1, deny_overrides).

target(p5).
policy(p5, 5, ps2, permit_overrides).

target(ps1).
policy_set(ps1, 1, ps0, first_applicable).

target(ps2).
policy_set(ps2, 2, ps0, first_applicable).

target(ps0).
policy_set(ps0, 0, ps0, first_applicable).









