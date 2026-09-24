-- Um voto por CPF em cada pauta, garantido pelo banco mesmo com requisições simultâneas
create unique index if not exists uk_voto_pauta_cpf on voto_sessao_pauta (id_pauta, cpf);
