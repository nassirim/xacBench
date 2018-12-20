
effect(permit;deny;indeterminate).
comb_alg(deny_overrides;permit_overrides;first_applicable;only_one_applicable).
comb_alg(do;po;fa;ooa).
bool_expr(true).

% r1
actions(r1, change).
environments(r1).
resources(r1, codes).
subjects(r1, designer).
subjects(r1, tester).
condition(r1, true).
rule(r1, 1, p1, deny).

% r2
actions(r2, read).
actions(r2, change).
environments(r2).
resources(r2, reports).
resources(r2, codes).
subjects(r2, designer).
subjects(r2, developer).
condition(r2, working_hours).
rule(r2, 2, p1, permit).

% r3
actions(r3, change).
environments(r3).
resources(r3, reports).
resources(r3, codes).
subjects(r3, designer).
subjects(r3, developer).
condition(r3, lunch_time).
rule(r3, 3, p1, deny).

% r4
actions(r4, change).
environments(r4).
resources(r4, reports).
subjects(r4).
condition(r4, true).
rule(r4, 4, p2, deny).

% r5
actions(r5, change).
environments(r5).
resources(r5, reports).
resources(r5, codes).
subjects(r5, manager).
subjects(r5, designer).
condition(r5, true).
rule(r5, 5, p2, permit).

% r6
actions(r6, change).
environments(r6, env1).
resources(r6, reports).
subjects(r6, developer).
condition(r6, lunch_time).
rule(r6, 6, p3, deny).

% r7
actions(r7).
environments(r7).
resources(r7).
subjects(r7, val_de_grace).
subjects(r7, europe).
subjects(r7, physician).
subjects(r7, clinical_researcher).
subjects(r7, radiologist).
condition(r7, true).
rule(r7, 7, p4, deny).

% r8
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









