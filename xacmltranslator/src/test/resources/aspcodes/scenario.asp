

action_domain(read;write).
environment_domain(env1).
resource_domain(patient_record;radiography;medical_file;personal).
subject_domain(administrator;physician;radiologist;clinical_researcher;val_de_grace;europe).

request(ActReq, EnvReq, ResReq, SubReq) :-
					ActReq == write,
					EnvReq == env1,
					resource_domain(ResReq),
					subject_domain(SubReq).


scenarios(ActReq, EnvReq, ResReq, SubReq) :-
					request(ActReq, EnvReq, ResReq, SubReq),
					match_program(_, _, _, deny, ActReq, EnvReq, ResReq, SubReq).


#show scenarios/4.









