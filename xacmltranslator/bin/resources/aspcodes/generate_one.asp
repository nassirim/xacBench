

action_domain(read;write).
environment_domain(env1).
resource_domain(patient_record;radiography;medical_file;personal).
subject_domain(administrator;physician;radiologist;clinical_researcher;val_de_grace;europe).

1{action(X) : action_domain(X)}1.
1{environment(X) : environment_domain(X)}1.
1{subject(X) : subject_domain(X)}1.
1{resource(X) : resource_domain(X)}1.

#show action/1.
#show environment/1.
#show resource/1.
#show subject/1.


