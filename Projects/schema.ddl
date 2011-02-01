alter table project_workers drop foreign key FKCC3BE58FE832C155
alter table project_workers drop foreign key FKCC3BE58FDCFF0B79
alter table projects drop foreign key FKC479187A3D65C7A8
drop table if exists project_workers
drop table if exists projects
drop table if exists workers
create table project_workers (project integer not null, worker integer)
create table projects (id integer not null auto_increment, executor varchar(255) not null, client varchar(255) not null, name varchar(255) not null, manager integer not null, comment varchar(255), primary key (id))
create table workers (id integer not null auto_increment, name varchar(255) not null, email varchar(255) not null, primary key (id))
alter table project_workers add index FKCC3BE58FE832C155 (project), add constraint FKCC3BE58FE832C155 foreign key (project) references projects (id)
alter table project_workers add index FKCC3BE58FDCFF0B79 (worker), add constraint FKCC3BE58FDCFF0B79 foreign key (worker) references workers (id)
alter table projects add index FKC479187A3D65C7A8 (manager), add constraint FKC479187A3D65C7A8 foreign key (manager) references workers (id)
