
action_domain(read;write).
environment_domain(env1).
resource_domain(patient_record;radiography;medical_file;personal).
subject_domain(administrator;physician;radiologist;clinical_researcher;val_de_grace;europe).

%subject(X) :- subject_domain(X).
%resource(X) :- resource_domain(X).
%action(X) :- action_domain(X).

request(ActReq, EnvReq, ResReq, SubReq) :- action_domain(ActReq), environment_domain(EnvReq), resource_domain(ResReq), subject_domain(SubReq).

%#show subject/1.
%#show resource/1.
%#show action/1.
%#show request/3.

